package cn.z201.example.spring.aop.log.config.core;

import java.io.Serializable;

/**
 * 返回结果
 *
 * @author z201.coding@gmail.com
 */
public class AjaxResult<T> implements Serializable {

    static final long serialVersionUID = 3320624190109556619L;

    private Boolean success;

    private Integer code;

    private String message;

    private T data;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    private AjaxResult() {
    }

    public static <T> AjaxResult<T> success() {
        AjaxResult<T> result = new AjaxResult<>();
        result.setSuccess(Boolean.TRUE);
        result.setCode(SysConstant.ResultCode.SUCCESS);
        result.setMessage(null);
        return result;
    }

    public static <T> AjaxResult<T> success(String msg) {
        AjaxResult<T> result = new AjaxResult<>();
        result.setSuccess(Boolean.TRUE);
        result.setCode(SysConstant.ResultCode.SUCCESS);
        result.setMessage(msg);
        return result;
    }

    public static <T> AjaxResult<T> success(Integer status, String msg) {
        AjaxResult<T> result = new AjaxResult<>();
        result.setSuccess(Boolean.TRUE);
        result.setCode(status);
        result.setMessage(msg);
        return result;
    }

    public static <T> AjaxResult<T> success(T data) {
        AjaxResult<T> result = new AjaxResult<>();
        result.setSuccess(Boolean.TRUE);
        result.setCode(SysConstant.ResultCode.SUCCESS);
        result.setMessage(null);
        result.setData(data);
        return result;
    }

    public static <T> AjaxResult<T> success(String msg, T data) {
        AjaxResult<T> result = new AjaxResult<>();
        result.setSuccess(Boolean.TRUE);
        result.setCode(SysConstant.ResultCode.SUCCESS);
        result.setMessage(msg);
        result.setData(data);
        return result;
    }

    public static <T> AjaxResult<T> failure(String msg) {
        AjaxResult<T> result = new AjaxResult<>();
        result.setSuccess(Boolean.FALSE);
        result.setCode(SysConstant.ResultCode.FAILURE);
        result.setMessage(msg);
        return result;
    }

    public static <T> AjaxResult<T> failure(Integer code, String msg) {
        AjaxResult<T> result = new AjaxResult<>();
        result.setSuccess(Boolean.FALSE);
        result.setCode(code);
        result.setMessage(msg);
        return result;
    }

}
