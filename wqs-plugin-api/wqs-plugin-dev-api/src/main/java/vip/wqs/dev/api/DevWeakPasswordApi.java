
package vip.wqs.dev.api;

import java.util.List;

/**
 * 弱密码库APi接口
 *
 * @author jetox
 * @date 2022/6/17 10:37
 **/
public interface DevWeakPasswordApi {

    /**
     * 获取弱密码库列表
     *
     * @author jetox
     * @date 2022/6/17 11:11
     **/
    List<String> weakPasswordList();
}
