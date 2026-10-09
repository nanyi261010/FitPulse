package com.example.fitpulse.Exception;

import lombok.Getter;

//自定义业务异常类
//在日常开发中，代码报错通常分两种：
//系统级崩溃：比如数据库突然断网、代码写错导致 NullPointerException（空指针异常）。这些不是用户的错，属于不可预期的事故。
//业务级阻断：比如用户打卡时长写了个负数、查询一个根本不存在的训练 ID、或者没权限删除别人的记录。
//简单来说，设定这个类的目的就是减少代码，将异常丢到这个类中来判断是用户业务不合规还是服务器崩了,
//抛出一个异常中断后续代码
@Getter
//继承运行时异常
public class BizException extends RuntimeException{
    //业务错误码，一旦被设立不可被篡改，增强安全性
    private final Integer code;

    public BizException(Integer code ,String message) {
        super(message);
        this.code = code;
    }
    /**
     * 常用重载：默认使用 400 业务错误码
     */
    public BizException(String message) {
        super(message);
        this.code=400;
    }
}
