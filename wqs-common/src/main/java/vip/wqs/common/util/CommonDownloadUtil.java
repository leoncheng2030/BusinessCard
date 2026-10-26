
package vip.wqs.common.util;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.URLUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * 文件下载工具类，使用本类前，对参数校验的异常使用CommonResponseUtil.renderError()方法进行渲染
 *
 * @author jetox
 * @date 2020/8/5 21:45
 */
@Slf4j
public class CommonDownloadUtil {

    /**
     * 下载文件
     *
     * @param file     要下载的文件
     * @param response 响应
     * @author jetox
     * @date 2020/8/5 21:46
     */
    public static void download(File file, HttpServletResponse response) {
        download(file.getName(), FileUtil.readBytes(file), response);
    }

    /**
     * 下载文件
     *
     * @author jetox
     * @date 2022/7/31 10:57
     */
    public static void download(String fileName, byte[] fileBytes, HttpServletResponse response) {
        try {
            response.setHeader("Content-Disposition", "attachment;filename=" + URLUtil.encode(fileName));
            response.addHeader("Content-Length", "" + fileBytes.length);
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
            response.setContentType("application/octet-stream;charset=UTF-8");
            IoUtil.write(response.getOutputStream(), true, fileBytes);
        } catch (IOException e) {
            log.error(">>> 文件下载异常：", e);
        }
    }

    /**
     * 图片内联渲染（downloadPath 直链被 <img>/<a-image> 引用时需 inline 输出，
     * 否则浏览器按附件下载不会渲染图片，导致列表/回显图片空白）
     *
     * @param suffix   图片后缀（png/jpg/jpeg/gif/bmp/webp），用于设置 Content-Type
     * @author wqs
     * @date 2026/9/4 15:00
     */
    public static void renderImageInline(String fileName, String suffix, byte[] fileBytes, HttpServletResponse response) {
        try {
            response.setHeader("Content-Disposition", "inline;filename=" + URLUtil.encode(fileName));
            response.addHeader("Content-Length", "" + fileBytes.length);
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
            response.setContentType(IMAGE_CONTENT_TYPE_MAP.getOrDefault(suffix, "image/png") + ";charset=UTF-8");
            IoUtil.write(response.getOutputStream(), true, fileBytes);
        } catch (IOException e) {
            log.error(">>> 图片渲染异常：", e);
        }
    }

    /** 图片后缀与 Content-Type 映射 */
    private static final Map<String, String> IMAGE_CONTENT_TYPE_MAP = Map.of(
            "png", "image/png",
            "jpg", "image/jpeg",
            "jpeg", "image/jpeg",
            "gif", "image/gif",
            "bmp", "image/bmp",
            "webp", "image/webp"
    );
}
