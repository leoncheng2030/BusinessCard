
package vip.wqs.dev.modular.file.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import vip.wqs.common.mapper.CommonDeleteAbsoluteMapper;
import vip.wqs.dev.modular.file.entity.DevFile;

/**
 * 文件Mapper接口
 *
 * @author jetox
 * @date 2022/2/23 18:40
 **/
public interface DevFileMapper extends CommonDeleteAbsoluteMapper<DevFile> {

    /**
     * 按ID查询未删除文件（仅限签名下载使用）
     *
     * <p>download 直链被 &lt;img&gt;/&lt;a-image&gt;/新窗口等场景直接访问，
     * 接口以 URL 签名（sign）作为文件访问凭证。</p>
     *
     * @param id 主键
     * @return 文件
     */
    @Select("SELECT * FROM DEV_FILE WHERE ID = #{id} AND DELETE_FLAG = 'NOT_DELETE'")
    DevFile selectNotDeletedById(@Param("id") String id);
}
