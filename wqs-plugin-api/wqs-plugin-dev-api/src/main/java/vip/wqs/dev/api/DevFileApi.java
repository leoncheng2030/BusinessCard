
package vip.wqs.dev.api;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 文件API接口，可参考vip.wqs.dev.core.util.file包下的工具类扩展更多需要的方法
 *
 * @author jetox
 * @date 2022/6/22 15:21
 **/
public interface DevFileApi {

    /**
     * 动态上传文件返回id（使用系统配置的默认文件引擎）
     *
     * @author jetox
     * @date 2021/10/13 14:01
     **/
    String uploadDynamicReturnId(MultipartFile file);

    /**
     * 动态上传文件返回url（使用系统配置的默认文件引擎）
     *
     * @author jetox
     * @date 2021/10/13 14:01
     **/
    String uploadDynamicReturnUrl(MultipartFile file);

    /* =========本地文件========= */

    /**
     * 上传文件返回Url
     *
     * @param file 文件
     * @author jetox
     * @date 2022/6/22 17:44
     **/
    String storageFileWithReturnUrlLocal(MultipartFile file);

    /**
     * 上传文件返回Id
     *
     * @param file 文件
     * @author jetox
     * @date 2022/6/22 17:44
     **/
    String storageFileWithReturnIdLocal(MultipartFile file);

    /* =========阿里云文件========= */

    /**
     * 上传文件返回Url
     *
     * @param file 文件
     * @author jetox
     * @date 2022/6/22 17:44
     **/
    String storageFileWithReturnUrlAliyun(MultipartFile file);

    /**
     * 上传文件返回Id
     *
     * @param file 文件
     * @author jetox
     * @date 2022/6/22 17:44
     **/
    String storageFileWithReturnIdAliyun(MultipartFile file);

    /* =========腾讯云件========= */

    /**
     * 上传文件返回Url
     *
     * @param file 文件
     * @author jetox
     * @date 2022/6/22 17:44
     **/
    String storageFileWithReturnUrlTencent(MultipartFile file);

    /**
     * 上传文件返回Id
     *
     * @param file 文件
     * @author jetox
     * @date 2022/6/22 17:44
     **/
    String storageFileWithReturnIdTencent(MultipartFile file);

    /* =========MINIO件========= */

    /**
     * 上传文件返回Url
     *
     * @param file 文件
     * @author jetox
     * @date 2022/6/22 17:44
     **/
    String storageFileWithReturnUrlMinio(MultipartFile file);

    /**
     * 上传文件返回Id
     *
     * @param file 文件
     * @author jetox
     * @date 2022/6/22 17:44
     **/
    String storageFileWithReturnIdMinio(MultipartFile file);

    /* =========FTP文件========= */

    /**
     * 上传文件返回Url
     *
     * @param file 文件
     * @author lilang
     * @date 2026/06/30
     **/
    String storageFileWithReturnUrlFtp(MultipartFile file);

    /**
     * 上传文件返回Id
     *
     * @param file 文件
     * @author lilang
     * @date 2026/06/30
     **/
    String storageFileWithReturnIdFtp(MultipartFile file);

    /**
     * 通过文件id查询文件详情
     *
     * @author chengchuanyao
     * @date 2024/7/26 16:10
     */
    JSONObject getFileInfoById(String id);


    /**
     * 根据文件id物理删除文件
     *
     * @author jetox
     * @date 2022/8/4 10:36
     **/
    void deleteAbsoluteById(String id);

    /**
     * 根据文件ids获得文件列表
     *
     * @author yubaoshan
     * @date 2025/8/25 21:18
     */
    JSONArray getFileListByIds(List<String> ids);

    /**
     * 根据配置的存储类型，返回不同的URL
     *
     * @author yubaoshan
     * @date 2025/8/25 21:18
     */
    String storageFileWithReturnUrlOss(MultipartFile file);
}
