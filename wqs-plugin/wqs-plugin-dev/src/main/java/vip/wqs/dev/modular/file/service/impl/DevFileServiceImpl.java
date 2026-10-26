
package vip.wqs.dev.modular.file.service.impl;

import cn.hutool.core.collection.CollStreamUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.digest.HMac;
import cn.hutool.crypto.digest.HmacAlgorithm;
import cn.hutool.http.HttpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import vip.wqs.common.exception.CommonException;
import vip.wqs.common.page.CommonPageRequest;
import vip.wqs.common.prop.CommonProperties;
import vip.wqs.common.util.CommonDownloadUtil;
import vip.wqs.common.util.CommonResponseUtil;
import vip.wqs.dev.modular.file.entity.DevFile;
import vip.wqs.dev.modular.file.enums.DevFileEngineTypeEnum;
import vip.wqs.dev.modular.file.mapper.DevFileMapper;
import vip.wqs.dev.modular.file.param.DevFileDownloadParam;
import vip.wqs.dev.modular.file.param.DevFileIdParam;
import vip.wqs.dev.modular.file.param.DevFileListParam;
import vip.wqs.dev.modular.file.param.DevFilePageParam;
import vip.wqs.dev.modular.file.param.DevFileUrlListParam;
import vip.wqs.dev.modular.file.service.DevFileService;
import vip.wqs.dev.modular.file.util.*;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Set;

/**
 * 文件Service接口实现类
 *
 * @author jetox
 * @date 2022/2/23 18:43
 **/
@Slf4j
@Service
public class DevFileServiceImpl extends ServiceImpl<DevFileMapper, DevFile> implements DevFileService {

    @Resource
    private CommonProperties commonProperties;

    /**
     * 历史默认签名密钥：签名密钥已升级为配置项（wqs.config.common.file-download-sign-secret），
     * 配置缺失时回落到本值，保证存量已签发下载链接（downloadPath 落库直链）仍可校验通过
     */
    private static final String LEGACY_FILE_DOWNLOAD_SIGN_SECRET = "wqs_file_download_secret_key_2025";

    /**
     * 获取文件下载签名密钥（配置优先，缺省回落历史默认值以兼容存量签名链接）
     *
     * <p>注：轮换密钥会使存量已签发链接全部失效（downloadPath 落库直链无法重新签发），
     * 仅在密钥泄露等必要场景下轮换。</p>
     **/
    private String getFileDownloadSignSecret() {
        return StrUtil.isNotEmpty(commonProperties.getFileDownloadSignSecret())
                ? commonProperties.getFileDownloadSignSecret() : LEGACY_FILE_DOWNLOAD_SIGN_SECRET;
    }

    /** 图片文件后缀（下载时以内联方式渲染输出，供前端直链展示） */
    private static final Set<String> IMAGE_SUFFIX_SET = Set.of("jpg", "jpeg", "png", "gif", "bmp", "webp");

    @Override
    public String uploadReturnId(String engine, MultipartFile file) {
        return this.storageFile(engine, file, true,  true);
    }

    @Override
    public String uploadReturnIdWithValidation(String engine, MultipartFile file, List<String> allowedExtensions) {
        validateFileExtension(file, allowedExtensions);
        return this.storageFile(engine, file, true, true);
    }

    @Override
    public String uploadReturnUrl(String engine, MultipartFile file) {
        return this.storageFile(engine, file, false, false);
    }

    @Override
    public String uploadReturnUrlWithValidation(String engine, MultipartFile file, List<String> allowedExtensions) {
        validateFileExtension(file, allowedExtensions);
        return this.storageFile(engine, file, false, false);
    }

    @Override
    public Page<DevFile> page(DevFilePageParam devFilePageParam) {
        QueryWrapper<DevFile> queryWrapper = new QueryWrapper<DevFile>().checkSqlInjection();
        if(ObjectUtil.isNotEmpty(devFilePageParam.getEngine())) {
            queryWrapper.lambda().eq(DevFile::getEngine, devFilePageParam.getEngine());
        }
        if(ObjectUtil.isNotEmpty(devFilePageParam.getSearchKey())) {
            queryWrapper.lambda().like(DevFile::getName, devFilePageParam.getSearchKey());
        }
        return this.page(CommonPageRequest.defaultPage(), queryWrapper);
    }

    @Override
    public List<DevFile> list(DevFileListParam devFileListParam) {
        QueryWrapper<DevFile> queryWrapper = new QueryWrapper<DevFile>().checkSqlInjection();
        if(ObjectUtil.isNotEmpty(devFileListParam.getEngine())) {
            queryWrapper.lambda().eq(DevFile::getEngine, devFileListParam.getEngine());
        }
        if(ObjectUtil.isNotEmpty(devFileListParam.getSearchKey())) {
            queryWrapper.lambda().like(DevFile::getName, devFileListParam.getSearchKey());
        }
        return this.list(queryWrapper);
    }

    @Override
    public void download(DevFileDownloadParam devFileDownloadParam, HttpServletResponse response) throws IOException {
        // 验证签名
        if (!verifyDownloadSign(devFileDownloadParam.getId(), devFileDownloadParam.getSign())) {
            CommonResponseUtil.renderError(response, "下载链接无效，请使用正确的下载地址");
            return;
        }
        DevFileIdParam devFileIdParam = new DevFileIdParam();
        devFileIdParam.setId(devFileDownloadParam.getId());
        // 签名下载直链免登录，以 URL 签名为访问凭证
        unifiedDownload(devFileIdParam, response, false);
    }

    @Override
    public void authDownload(DevFileIdParam devFileIdParam, HttpServletResponse response) throws IOException {
        // 授权下载为已登录请求，走常规查询
        unifiedDownload(devFileIdParam, response, true);
    }

    // 统一下载
    private void unifiedDownload(DevFileIdParam devFileIdParam, HttpServletResponse response, boolean isDownloadAuth) throws IOException{
        DevFile devFile;
        if(isDownloadAuth) {
            // 授权下载为已登录请求，走常规查询
            devFile = this.queryEntity(devFileIdParam.getId());
        } else {
            // 签名下载直链被 <img>/<a-image>/新窗口等场景直接访问，以URL签名为凭证按主键直查
            devFile = this.baseMapper.selectNotDeletedById(devFileIdParam.getId());
        }
        if(ObjectUtil.isEmpty(devFile)) {
            CommonResponseUtil.renderError(response, "文件不存在，id值为：" + devFileIdParam.getId());
            return;
        }
        if (BooleanUtil.isTrue(devFile.getIsDownloadAuth()) && !isDownloadAuth){
            CommonResponseUtil.renderError(response, "该文件需要授权下载，请使用授权接口authDownload进行下载！");
            return;
        }
        byte[] fileBytes;
        if(devFile.getEngine().equals(DevFileEngineTypeEnum.LOCAL.getValue())) {
            File file = FileUtil.file(devFile.getStoragePath());
            if(!FileUtil.exist(file)) {
                CommonResponseUtil.renderError(response, "找不到存储的文件，id值为：" + devFile.getId());
                return;
            }
            fileBytes = IoUtil.readBytes(FileUtil.getInputStream(file));
        } else if(devFile.getEngine().equals(DevFileEngineTypeEnum.FTP.getValue())) {
            fileBytes = DevFileFtpUtil.getFileBytes(devFile.getBucket(), devFile.getFileKey());
        } else {
            String storagePath = devFile.getStoragePath();
            fileBytes = HttpUtil.downloadBytes(storagePath);
        }
        // 图片文件以内联方式渲染输出（downloadPath 直链被前端 <img>/<a-image> 直接引用），
        // 非图片文件保持附件下载语义
        if(IMAGE_SUFFIX_SET.contains(StrUtil.emptyToDefault(devFile.getSuffix(), "").toLowerCase())) {
            CommonDownloadUtil.renderImageInline(devFile.getName(), devFile.getSuffix(), fileBytes, response);
        } else {
            CommonDownloadUtil.download(devFile.getName(), fileBytes, response);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(List<DevFileIdParam> devFileIdParamList) {
        this.removeByIds(CollStreamUtil.toList(devFileIdParamList, DevFileIdParam::getId));
    }

    @Override
    public void deleteAbsolute(DevFileIdParam devFileIdParam) {
        DevFile devFile = this.queryEntity(devFileIdParam.getId());
        try {
            // 存储引擎
            String engine = devFile.getEngine();
            // 存储桶名称
            String bucketName = devFile.getBucket();
            // 文件key
            String fileKey = devFile.getFileKey();
            // 根据存储引擎删除文件
            if (DevFileEngineTypeEnum.LOCAL.getValue().equals(engine)) {
                DevFileLocalUtil.deleteFile(bucketName, fileKey);
            } else if (DevFileEngineTypeEnum.ALIYUN.getValue().equals(engine)) {
                DevFileAliyunUtil.deleteFile(bucketName, fileKey);
            } else if (DevFileEngineTypeEnum.TENCENT.getValue().equals(engine)) {
                DevFileTencentUtil.deleteFile(bucketName, fileKey);
            } else if (DevFileEngineTypeEnum.MINIO.getValue().equals(engine)) {
                DevFileMinIoUtil.deleteFile(bucketName, fileKey);
            } else if (DevFileEngineTypeEnum.RUSTFS.getValue().equals(engine)) {
                DevFileRustFsUtil.deleteFile(bucketName, fileKey);
            } else if (DevFileEngineTypeEnum.FTP.getValue().equals(engine)) {
                DevFileFtpUtil.deleteFile(bucketName, fileKey);
            } else {
                log.error("未知存储引擎：{}", engine);
            }
        } catch (Exception e) {
            log.error("文件删除失败：{}，路径：{}", devFile.getName(), devFile.getStoragePath(), e);
            throw new CommonException("文件删除失败"); // 触发事务回滚
        }
        this.baseMapper.deleteAbsoluteById(devFile.getId());
    }

    /**
     * 存储文件
     *
     * @author jetox
     * @date 2022/6/16 16:24
     **/
    private String storageFile(String engine, MultipartFile file, boolean returnFileId, Boolean isDownloadAuth) {

        // 如果引擎为空，默认使用本地
        if(ObjectUtil.isEmpty(engine)) {
            engine = DevFileEngineTypeEnum.LOCAL.getValue();
        }

        // 生成id
        String fileId = IdWorker.getIdStr();

        // 存储桶名称
        String bucketName;

        String fileKey = genFileKey(fileId, file);

        // 定义存储的url，本地文件返回文件实际路径，其他引擎返回网络地址
        String storageUrl;

        // 根据引擎类型执行不同方法
        if(engine.equals(DevFileEngineTypeEnum.LOCAL.getValue())) {

            // 使用固定名称defaultBucketName
            bucketName = "defaultBucketName";
            storageUrl = DevFileLocalUtil.storageFileWithReturnUrl(bucketName, fileKey, file);
        } else if(engine.equals(DevFileEngineTypeEnum.ALIYUN.getValue())) {

            // 使用阿里云默认配置的bucketName
            bucketName = DevFileAliyunUtil.getDefaultBucketName();
            storageUrl = DevFileAliyunUtil.storageFileWithReturnUrl(bucketName, fileKey, file);
        } else if(engine.equals(DevFileEngineTypeEnum.TENCENT.getValue())) {

            // 使用腾讯云默认配置的bucketName
            bucketName = DevFileTencentUtil.getDefaultBucketName();
            storageUrl = DevFileTencentUtil.storageFileWithReturnUrl(bucketName, fileKey, file);
        } else if(engine.equals(DevFileEngineTypeEnum.MINIO.getValue())) {

            // 使用MINIO默认配置的bucketName
            bucketName = DevFileMinIoUtil.getDefaultBucketName();
            storageUrl = DevFileMinIoUtil.storageFileWithReturnUrl(bucketName, fileKey, file);
        } else if(engine.equals(DevFileEngineTypeEnum.RUSTFS.getValue())) {

            // 使用RUSTFS默认配置的bucketName
            bucketName = DevFileRustFsUtil.getDefaultBucketName();
            storageUrl = DevFileRustFsUtil.storageFileWithReturnUrl(bucketName, fileKey, file);
        } else if(engine.equals(DevFileEngineTypeEnum.FTP.getValue())) {

            // 使用FTP默认配置的bucketName
            bucketName = DevFileFtpUtil.getDefaultBucketName();
            storageUrl = DevFileFtpUtil.storageFileWithReturnUrl(bucketName, fileKey, file);
        } else {
            throw new CommonException("不支持的文件引擎：{}", engine);
        }

        // 将文件信息保存到数据库
        DevFile devFile = new DevFile();

        // 设置文件id
        devFile.setId(fileId);

        // 设置存储引擎类型
        devFile.setEngine(engine);
        devFile.setBucket(bucketName);
        devFile.setFileKey(fileKey);
        devFile.setName(file.getOriginalFilename());
        String suffix = ObjectUtil.isNotEmpty(file.getOriginalFilename())?StrUtil.subAfter(file.getOriginalFilename(),
                StrUtil.DOT, true):null;
        devFile.setSuffix(suffix);
        devFile.setSizeKb(Convert.toStr(NumberUtil.div(new BigDecimal(file.getSize()), BigDecimal.valueOf(1024))
                .setScale(0,  RoundingMode.HALF_UP )));
        devFile.setSizeInfo(FileUtil.readableFileSize(file.getSize()));
        devFile.setObjName(ObjectUtil.isNotEmpty(devFile.getSuffix())?fileId + StrUtil.DOT + devFile.getSuffix():null);
        // 如果是图片，则压缩生成缩略图
        if(ObjectUtil.isNotEmpty(suffix)) {
            if(isPic(suffix)) {
                try {
                    devFile.setThumbnail(ImgUtil.toBase64DataUri(ImgUtil.scale(ImgUtil.toImage(file.getBytes()),
                            100, 100, null), suffix));
                } catch (Exception ignored) {
                }
            }
        }
        // 存储路径
        devFile.setStoragePath(storageUrl);

        // 定义下载地址
        String downloadUrl;
        String apiUrl = commonProperties.getBackendUrl();
        if(ObjectUtil.isEmpty(apiUrl)) {
            throw new CommonException("后端域名地址未正确配置：wqs.config.common.backend-url为空");
        }
        if (BooleanUtil.isTrue(isDownloadAuth)){
            downloadUrl= apiUrl + "/dev/file/authDownload?id=" + fileId + "&token=";
        }else {
            // 公开文件使用带签名的 download 接口
            String sign = generateDownloadSign(fileId);
            downloadUrl= apiUrl + "/dev/file/download?id=" + fileId + "&sign=" + sign;
        }
        devFile.setDownloadPath(downloadUrl);

//        // 下载路径，注意：本地文件下载地址设置为下载接口地址 + 文件id
//        if(engine.equals(DevFileEngineTypeEnum.LOCAL.getValue())) {
//            String apiUrl = commonProperties.getBackendUrl();
//            if(ObjectUtil.isEmpty(apiUrl)) {
//                throw new CommonException("后端域名地址未正确配置：wqs.config.common.backend-url为空");
//            }
//            downloadUrl= apiUrl + "/dev/file/download?id=" + fileId;
//            devFile.setDownloadPath(downloadUrl);
//        } else {
//            // 阿里云、腾讯云、MINIO可以直接使用存储地址（公网）作为下载地址
//            downloadUrl= storageUrl;
//            devFile.setDownloadPath(devFile.getStoragePath());
//        }

        devFile.setIsDownloadAuth(isDownloadAuth);

        this.save(devFile);

        // 如果是返回id则返回文件id
        if(returnFileId) {
            return fileId;
        } else {
            // 否则返回下载地址
            return downloadUrl;
        }
    }

    /**
     * 生成文件的key，格式如 2021/10/11/1377109572375810050.docx
     *
     * @author jetox
     * @date 2022/4/22 15:58
     **/
    public String genFileKey(String fileId, MultipartFile file) {

        // 获取文件原始名称
        String originalFileName = file.getOriginalFilename();

        // 获取文件后缀
        String fileSuffix = FileUtil.getSuffix(originalFileName);

        // 生成文件的对象名称，格式如:1377109572375810050.docx
        String fileObjectName = fileId + StrUtil.DOT + fileSuffix;

        // 获取日期文件夹，格式如，2021/10/11/
        String dateFolderPath = DateUtil.thisYear() + StrUtil.SLASH +
                (DateUtil.thisMonth() + 1) + StrUtil.SLASH +
                DateUtil.thisDayOfMonth() + StrUtil.SLASH;

        // 返回
        return dateFolderPath + fileObjectName;
    }

    @Override
    public DevFile detail(DevFileIdParam devFileIdParam) {
        return this.queryEntity(devFileIdParam.getId());
    }

    @Override
    public List<DevFile> getFileListByUrlList(DevFileUrlListParam devFileUrlListParam) {
        LambdaQueryWrapper<DevFile> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 只查询部分字段
        lambdaQueryWrapper.select(DevFile::getId, DevFile::getSuffix, DevFile::getDownloadPath, DevFile::getName, DevFile::getThumbnail,
                        DevFile::getSizeKb, DevFile::getSizeInfo, DevFile::getObjName)
                .in(DevFile::getDownloadPath, devFileUrlListParam.getUrlList());
        return this.list(lambdaQueryWrapper);
    }

    @Override
    public DevFile queryEntity(String id) {
        DevFile devFile = this.getById(id);
        if(ObjectUtil.isEmpty(devFile)) {
            throw new CommonException("文件不存在，id值为：{}", id);
        }
        return devFile;
    }

    @Override
    public List<DevFile> getFileListByIds(List<String> ids) {
        return list(Wrappers.<DevFile>lambdaQuery().in(DevFile::getId,ids));
    }

    /**
     * 根据文件后缀判断是否图片
     *
     * @author jetox
     * @date 2020/7/6 15:31
     */
    private static boolean isPic(String fileSuffix) {
        fileSuffix = fileSuffix.toLowerCase();
        return ImgUtil.IMAGE_TYPE_GIF.equals(fileSuffix)
                || ImgUtil.IMAGE_TYPE_JPG.equals(fileSuffix)
                || ImgUtil.IMAGE_TYPE_JPEG.equals(fileSuffix)
                || ImgUtil.IMAGE_TYPE_BMP.equals(fileSuffix)
                || ImgUtil.IMAGE_TYPE_PNG.equals(fileSuffix)
                || ImgUtil.IMAGE_TYPE_PSD.equals(fileSuffix);
    }

    /**
     * 生成文件下载签名
     *
     * @param fileId 文件ID
     * @return 签名字符串
     */
    private String generateDownloadSign(String fileId) {
        HMac hmac = SecureUtil.hmac(HmacAlgorithm.HmacSHA256, getFileDownloadSignSecret());
        String sign = hmac.digestHex(fileId);
        return cn.hutool.core.codec.Base64.encode(sign);
    }

    /**
     * 验证文件下载签名
     *
     * @param fileId 文件ID
     * @param sign 签名字符串
     * @return 是否有效
     */
    public boolean verifyDownloadSign(String fileId, String sign) {
        if (StrUtil.isEmpty(sign)) {
            return false;
        }
        try {
            String decoded = cn.hutool.core.codec.Base64.decodeStr(sign);
            HMac hmac = SecureUtil.hmac(HmacAlgorithm.HmacSHA256, getFileDownloadSignSecret());
            String expectedSign = hmac.digestHex(fileId);
            return expectedSign.equals(decoded);
        } catch (Exception e) {
            log.error("验证文件下载签名异常，fileId: {}", fileId, e);
            return false;
        }
    }

    /**
     * 验证文件后缀名
     *
     * @param file 上传的文件
     * @param allowedExtensions 允许的后缀列表（不含点号，如：jpg, png）
     */
    private void validateFileExtension(MultipartFile file, List<String> allowedExtensions) {
        if (file == null || file.isEmpty()) {
            throw new CommonException("上传文件不能为空");
        }

        String originalFileName = file.getOriginalFilename();
        if (StrUtil.isEmpty(originalFileName)) {
            throw new CommonException("文件名不能为空");
        }

        // 获取文件后缀（小写）
        String fileSuffix = FileUtil.getSuffix(originalFileName).toLowerCase();
        if (StrUtil.isEmpty(fileSuffix)) {
            throw new CommonException("文件必须有后缀名");
        }

        // 校验后缀是否在白名单中
        if (CollUtil.isEmpty(allowedExtensions) || !allowedExtensions.contains(fileSuffix)) {
            throw new CommonException("不允许上传该文件类型，仅支持：{}",
                CollUtil.isEmpty(allowedExtensions) ? "无" : String.join("、", allowedExtensions));
        }
    }
}
