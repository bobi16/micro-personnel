package com.bobiko.core.domain;

import java.io.Serializable;

/**
 * @Author: bobiko
 * @CreateTime: 2025-03-25
 * @Description: 统一响应实体类
 */
public class Result<T> implements Serializable
{
    /**
     * 返回状态码
     */
    private Integer code;
    /**
     * 返回消息
     */
    private String message;
    /**
     * 返回数据
     */
    private T data;

    public Result(){

    }

    public Result(Integer code,String message,T data){
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static Result<String> success(){
        return new Result<>(1, "success", null);
    }

    public static <T> Result<T> success(T data){
        return new Result<>(1, "success", data);
    }

    public static <T> Result<T> error(String message){
        return new Result<>(0, message, null);
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
}
