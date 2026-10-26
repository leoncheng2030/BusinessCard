
package vip.wqs.client;

import cn.hutool.json.JSONObject;

import java.util.List;

/**
 * 用户Api
 *
 * @author jetox
 * @date 2022/6/6 11:33
 **/
public interface ClientUserApi {

    /**
     * 根据用户id获取用户对象，没有则返回null
     *
     * @author jetox
     * @date 2022/6/20 18:19
     **/
    JSONObject getUserByIdWithoutException(String userId);

    /**
     * 根据用户id获取用户对象列表，没有的则为空，都没有则返回空集合
     *
     * @author jetox
     * @date 2022/6/20 18:19
     **/
    List<JSONObject> getUserListByIdListWithoutException(List<String> userIdList);

    /**
     * 根据用户id获取用户对象，没有则抛出异常
     *
     * @author jetox
     * @date 2022/6/20 18:19
     **/
    JSONObject getUserByIdWithException(String userId);

    /**
     * 根据用户id获取用户对象列表，只要有一个没有则抛出异常
     *
     * @author jetox
     * @date 2022/6/20 18:19
     **/
    List<JSONObject> getUserListByIdWithException(List<String> userIdList);

    /**
     * 获取用户列表（排除当前用户）
     *
     * @author chengchuanyao
     * @date 2024/7/19 9:54
     */
    List<JSONObject> listUserWithoutCurrent();

    /**
     * 获取用户扩展信息，没有则创建
     *
     * @author jetox
     * @date 2022/7/8 9:26
     **/
    JSONObject getOrCreateClientUserExt(String userId);

    /**
     * C端用户选择器，返回id和name列表
     *
     * @author yubaoshan
     * @date 2026/3/31
     **/
    List<JSONObject> userSelector(String searchKey);
}
