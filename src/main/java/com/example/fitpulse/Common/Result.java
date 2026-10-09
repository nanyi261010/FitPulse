package com.example.fitpulse.Common;

import lombok.Data;

@Data
public class Result<T> {
    //状态码
    private Integer code;
    //提示信息
    private String message;
    //核心数据
    private T data;
    private Result(){}
    //成功的静态方法（带数据返回，比如查询，分页列表）
    public static <T> Result<T> success(T data){
        Result<T> r=new Result<>();
        r.setCode(200);
        r.setMessage("success");
        r.setData(data);
        return r;
    }
    // 3. 成功的静态方法（无数据返回，比如删除成功、更新成功，data 传 null 即可）
    public static <T> Result<T> success() {
        return success(null);
    }
    // 4. 失败的静态方法（带自定义状态码和错误原因）
    public static <T> Result<T> error(Integer code,String message){
        Result<T> r=new Result<>();
        r.setCode(code);
        r.setMessage(message);
        r.setData(null);
        return  r;
    }// 5. 失败的快捷重载方法（默认 400 业务错误码）
    public static <T> Result<T> error(String message){
        return error(400,message);
    }
}
