
package vip.wqs.sys.modular.user.service;

import cn.hutool.json.JSONObject;
import vip.wqs.sys.modular.user.param.SysUserTransferExecuteParam;
import vip.wqs.sys.modular.user.param.SysUserTransferResourceDetailParam;
import vip.wqs.sys.modular.user.param.SysUserTransferResourceListParam;
import vip.wqs.sys.modular.user.result.SysUserTransferResourceResult;

import java.util.List;

/**
 * 权限转移Service接口
 *
 * @author yubaoshan
 * @date 2026/05/13
 */
public interface SysUserTransferService {

    /**
     * 获取用户可转移资源列表
     */
    List<SysUserTransferResourceResult> resourceList(SysUserTransferResourceListParam param);

    /**
     * 获取资源明细列表
     */
    List<JSONObject> resourceDetail(SysUserTransferResourceDetailParam param);

    /**
     * 执行转移
     */
    void execute(SysUserTransferExecuteParam param);
}
