
package vip.wqs.common.pojo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import vip.wqs.common.util.CommonTraceIdUtil;

import java.io.Serial;
import java.io.Serializable;

/**
 * 对Ajax请求返回Json格式数据的简易封装
 *
 * @author jetox
 * @date 2022/8/15 16:08
 **/
public class CommonResult<T> implements Serializable{
    @Serial
    private static final long serialVersionUID = 1L;
    public static final int CODE_SUCCESS = 200;
    public static final int CODE_ERROR = 500;

    @Schema(description = "状态码")
    private int code;

    /**
     *  获取msg
     */
    @Getter
    @Schema(description = "提示语")
    private String msg;

    /**
     *  获取data
     */
    @Getter
    @Schema(description = "返回数据")
    private T data;

    @Schema(description = "跟踪ID")
    private String traceId;

    public CommonResult() {
    }

    public CommonResult(int code, String msg, T data) {
        this.setCode(code);
        this.setMsg(msg);
        this.setData(data);
        this.setTraceId(CommonTraceIdUtil.getTraceId());
    }

    /**
     * 获取code
     * @return code
     */
    public Integer getCode() {
        return this.code;
    }

    /**
     * 获取traceId
     * @return traceId
     */
    public String getTraceId() {
        return this.traceId;
    }

    /**
     * 给code赋值，连缀风格
     * @param code code
     * @return 对象自身
     */
    public CommonResult<T> setCode(int code) {
        this.code = code;
        return this;
    }

    /**
     * 给msg赋值，连缀风格
     * @param msg msg
     * @return 对象自身
     */
    public CommonResult<T> setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    /**
     * 给data赋值，连缀风格
     * @param data data
     * @return 对象自身
     */
    public CommonResult<T> setData(T data) {
        this.data = data;
        return this;
    }

    /**
     * 给traceId赋值，连缀风格
     * @param traceId traceId
     * @return 对象自身
     */
    public CommonResult<T> setTraceId(String traceId) {
        this.traceId = traceId;
        return this;
    }


    // ============================  构建  ==================================

    // 构建成功
    public static <T> CommonResult<T> ok() {
        return new CommonResult<>(CODE_SUCCESS, "操作成功", null);
    }
    public static <T> CommonResult<T> ok(String msg) {
        return new CommonResult<>(CODE_SUCCESS, msg, null);
    }
    public static <T> CommonResult<T> code(int code) {
        return new CommonResult<>(code, null, null);
    }
    public static <T> CommonResult<T> data(T data) {
        return new CommonResult<>(CODE_SUCCESS, "操作成功", data);
    }

    // 构建失败
    public static <T> CommonResult<T> error() {
        return new CommonResult<>(CODE_ERROR, "服务器异常", null);
    }
    public static <T> CommonResult<T> error(String msg) {
        return new CommonResult<>(CODE_ERROR, msg, null);
    }

    // 构建指定状态码
    public static <T> CommonResult<T> get(int code, String msg, T data) {
        return new CommonResult<>(code, msg, data);
    }

    /*
     * toString()
     */
    @Override
    public String toString() {
        return "{"
                + "\"code\": " + this.getCode()
                + ", \"msg\": \"" + this.getMsg() + "\""
                + ", \"data\": " + (this.getData() != null ? "\"" + this.getData() + "\"" : "null")
                + "}";
    }
}
