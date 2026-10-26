
package vip.wqs.sys.api;

/**
 * 按钮API
 *
 * @author jetox
 * @date 2022/11/1 13:45
 **/
public interface SysButtonApi {

    /**
     * 代码生成按钮插入
     *
     * @author jetox
     * @date 2022/11/1 13:48
     **/
    void addForGenButton(String menuId, String className, String functionName);

    /**
     * 代码生成按钮插入（支持不同生成类型）
     *
     * @author jetox
     * @date 2022/11/1 13:48
     **/
    void addForGenButton(String menuId, String className, String functionName, String genType, String subFunctionName);
}
