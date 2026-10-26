
package vip.wqs.dev.modular.password.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import vip.wqs.dev.modular.password.entity.DevWeakPassword;
import vip.wqs.dev.modular.password.param.DevWeakPasswordAddParam;
import vip.wqs.dev.modular.password.param.DevWeakPasswordEditParam;
import vip.wqs.dev.modular.password.param.DevWeakPasswordIdParam;
import vip.wqs.dev.modular.password.param.DevWeakPasswordPageParam;

import java.util.List;

/**
 * 弱密码库Service接口
 *
 * @author yubaoshan
 * @date  2024/12/21 01:25
 **/
public interface DevWeakPasswordService extends IService<DevWeakPassword> {

    /**
     * 获取弱密码库分页
     *
     * @author jetox
     * @date 2022/4/24 20:08
     */
    Page<DevWeakPassword> page(DevWeakPasswordPageParam devWeakPasswordPageParam);

    /**
     * 添加弱密码库
     *
     * @author jetox
     * @date 2022/4/24 20:48
     */
    void add(DevWeakPasswordAddParam devWeakPasswordAddParam);

    /**
     * 编辑弱密码库
     *
     * @author jetox
     * @date 2022/4/24 21:13
     */
    void edit(DevWeakPasswordEditParam devWeakPasswordEditParam);

    /**
     * 删除弱密码库
     *
     * @author jetox
     * @date 2022/4/24 21:18
     */
    void delete(List<DevWeakPasswordIdParam> devWeakPasswordIdParamList);

    /**
     * 获取弱密码库详情
     *
     * @author jetox
     * @date 2022/4/24 21:18
     */
    DevWeakPassword detail(DevWeakPasswordIdParam devWeakPasswordIdParam);

    /**
     * 获取弱密码库详情
     *
     * @author jetox
     * @date 2022/7/25 19:42
     **/
    DevWeakPassword queryEntity(String id);

}
