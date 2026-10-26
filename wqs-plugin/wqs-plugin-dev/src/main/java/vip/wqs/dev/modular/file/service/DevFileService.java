
package vip.wqs.dev.modular.file.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;
import vip.wqs.dev.modular.file.entity.DevFile;
import vip.wqs.dev.modular.file.param.DevFileDownloadParam;
import vip.wqs.dev.modular.file.param.DevFileIdParam;
import vip.wqs.dev.modular.file.param.DevFileListParam;
import vip.wqs.dev.modular.file.param.DevFilePageParam;
import vip.wqs.dev.modular.file.param.DevFileUrlListParam;

import java.io.IOException;
import java.util.List;

/**
 * 文件Service接口
 *
 * @author jetox
 * @date 2022/2/23 18:27
 **/
public interface DevFileService extends IService<DevFile> {

    /**
     * MultipartFile文件上传，返回文件id
     *
     * @author jetox
     * @date 2022/4/22 15:53
     **/
    String uploadReturnId(String engine, MultipartFile file);

    /**
     * MultipartFile文件上传，返回文件id（带后缀校验）
     *
     * @author jetox
     * @date 2025/6/26 15:53
     **/
    String uploadReturnIdWithValidation(String engine, MultipartFile file, List<String> allowedExtensions);

    /**
     * MultipartFile文件上传，返回文件Url
     *
     * @author jetox
     * @date 2022/4/22 15:53
     **/
    String uploadReturnUrl(String engine, MultipartFile file);

    /**
     * MultipartFile文件上传，返回文件Url（带后缀校验）
     *
     * @author jetox
     * @date 2025/6/26 15:53
     **/
    String uploadReturnUrlWithValidation(String engine, MultipartFile file, List<String> allowedExtensions);

    /**
     * 文件分页列表接口
     *
     * @author jetox
     * @date 2022/6/21 15:44
     **/
    Page<DevFile> page(DevFilePageParam devFilePageParam);

    /**
     * 文件列表接口
     *
     * @author jetox
     * @date 2022/6/21 15:44
     **/
    List<DevFile> list(DevFileListParam devFileListParam);

    /**
     * 下载文件
     *
     * @author jetox
     * @date 2022/6/21 15:44
     **/
    void download(DevFileDownloadParam devFileDownloadParam, HttpServletResponse response) throws IOException;


    /**
     * 授权下载文件
     *
     * @author jetox
     * @date 2022/6/21 15:44
     **/
    void authDownload(DevFileIdParam devFileIdParam, HttpServletResponse response) throws IOException;


    /**
     * 删除文件
     *
     * @author jetox
     * @date 2022/8/4 10:36
     **/
    void delete(List<DevFileIdParam> devFileIdParamList);

    /**
     * 物理删除文件
     *
     * @author jetox
     * @date 2022/8/4 10:36
     **/
    void deleteAbsolute(DevFileIdParam devFileIdParam);

    /**
     * 获取文件详情
     *
     * @author jetox
     * @date 2022/4/24 21:18
     */
    DevFile detail(DevFileIdParam devFileIdParam);

    /**
     * 根据文件url集合获取文件集合
     *
     * @author yubaoshan
     * @date 2024/6/9 23:52
     **/
    List<DevFile> getFileListByUrlList(DevFileUrlListParam devFileUrlListParam);

    /**
     * 获取文件详情
     *
     * @author jetox
     * @date 2022/4/24 21:18
     */
    DevFile queryEntity(String id);

    /**
     * 根据文件ids获得文件列表
     *
     * @author yubaoshan
     * @date 2025/8/25 21:18
     */
    List<DevFile> getFileListByIds(List<String> ids);
}
