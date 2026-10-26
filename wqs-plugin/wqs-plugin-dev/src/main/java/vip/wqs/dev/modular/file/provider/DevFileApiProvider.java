
package vip.wqs.dev.modular.file.provider;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vip.wqs.dev.api.DevConfigApi;
import vip.wqs.dev.api.DevFileApi;
import vip.wqs.dev.modular.file.enums.DevFileEngineTypeEnum;
import vip.wqs.dev.modular.file.param.DevFileIdParam;
import vip.wqs.dev.modular.file.service.DevFileService;

import java.util.List;
import java.util.Optional;

/**
 * 文件API接口提供者
 *
 * @author jetox
 * @date 2022/6/22 15:32
 **/
@Service
public class DevFileApiProvider implements DevFileApi {

    /** 默认文件引擎 */
    private static final String SNOWY_SYS_DEFAULT_FILE_ENGINE_KEY = "SNOWY_SYS_DEFAULT_FILE_ENGINE";

    @Resource
    private DevConfigApi devConfigApi;

    @Resource
    private DevFileService devFileService;

    @Override
    public String uploadDynamicReturnId(MultipartFile file) {
        return devFileService.uploadReturnId(devConfigApi.getValueByKey(SNOWY_SYS_DEFAULT_FILE_ENGINE_KEY), file);
    }

    @Override
    public String uploadDynamicReturnUrl(MultipartFile file) {
        return devFileService.uploadReturnUrl(devConfigApi.getValueByKey(SNOWY_SYS_DEFAULT_FILE_ENGINE_KEY), file);
    }

    @Override
    public String storageFileWithReturnUrlLocal(MultipartFile file) {
        return devFileService.uploadReturnUrl(DevFileEngineTypeEnum.LOCAL.getValue(), file);
    }

    @Override
    public String storageFileWithReturnIdLocal(MultipartFile file) {
        return devFileService.uploadReturnId(DevFileEngineTypeEnum.LOCAL.getValue(), file);
    }

    @Override
    public String storageFileWithReturnUrlAliyun(MultipartFile file) {
        return devFileService.uploadReturnUrl(DevFileEngineTypeEnum.ALIYUN.getValue(), file);
    }

    @Override
    public String storageFileWithReturnIdAliyun(MultipartFile file) {
        return devFileService.uploadReturnId(DevFileEngineTypeEnum.ALIYUN.getValue(), file);
    }

    @Override
    public String storageFileWithReturnUrlTencent(MultipartFile file) {
        return devFileService.uploadReturnUrl(DevFileEngineTypeEnum.TENCENT.getValue(), file);
    }

    @Override
    public String storageFileWithReturnIdTencent(MultipartFile file) {
        return devFileService.uploadReturnId(DevFileEngineTypeEnum.TENCENT.getValue(), file);
    }

    @Override
    public String storageFileWithReturnUrlMinio(MultipartFile file) {
        return devFileService.uploadReturnUrl(DevFileEngineTypeEnum.MINIO.getValue(), file);
    }

    @Override
    public String storageFileWithReturnIdMinio(MultipartFile file) {
        return devFileService.uploadReturnId(DevFileEngineTypeEnum.MINIO.getValue(), file);
    }

    @Override
    public String storageFileWithReturnUrlFtp(MultipartFile file) {
        return devFileService.uploadReturnUrl(DevFileEngineTypeEnum.FTP.getValue(), file);
    }

    @Override
    public String storageFileWithReturnIdFtp(MultipartFile file) {
        return devFileService.uploadReturnId(DevFileEngineTypeEnum.FTP.getValue(), file);
    }

    @Override
    public JSONObject getFileInfoById(String id) {
        return Optional.ofNullable(devFileService.getById(id))
                .map(JSONUtil::parseObj)
                .orElse(new JSONObject());
    }

    @Override
    public void deleteAbsoluteById(String id) {
        DevFileIdParam devFileIdParam = new DevFileIdParam();
        devFileIdParam.setId(id);
        devFileService.deleteAbsolute(devFileIdParam);
    }

    @Override
    public JSONArray getFileListByIds(List<String> ids) {
        JSONArray jsonArray = new JSONArray();
        jsonArray.addAll(devFileService.getFileListByIds(ids));
        return jsonArray;
    }

    @Override
    public String storageFileWithReturnUrlOss(MultipartFile file) {
        // 获取系统配置的默认文件存储引擎
        String defaultEngine = devConfigApi.getValueByKey(SNOWY_SYS_DEFAULT_FILE_ENGINE_KEY);
        // 如果配置为空，默认使用本地存储
        if (ObjectUtil.isEmpty(defaultEngine)) {
            defaultEngine = DevFileEngineTypeEnum.LOCAL.getValue();
        }
        // 根据配置的引擎类型调用相应的存储方法
        return devFileService.uploadReturnUrl(defaultEngine, file);
    }
}
