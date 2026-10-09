package com.example.fitpulse.Exception;

import com.example.fitpulse.Common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j//自动生成log对象，用于打印错误日志
@RestControllerAdvice//声明这是一个涌入restful控制器的全局拦截类，自动监听全乡航母所有的controller
public class GlobalExceptionHandler {
    @ExceptionHandler(BizException.class)//只要是bizexception抛出的就要进这个方法
    public Result<Void> handleBizException(BizException e){
        log.warn("业务规则拦截:{}",e.getMessage());
        return Result.error(e.getCode(),e.getMessage());

    }
    /**
     * 2. 拦截 JSR-303 参数校验失败异常 (@Valid 报错时由 Spring 抛出)
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<Void> handleValidationException(MethodArgumentNotValidException e) {
        // 取出我们在 @NotBlank、@NotNull 里定义的具体的 message 提示
        FieldError fieldError = e.getBindingResult().getFieldError();
        String errorMsg = (fieldError != null) ? fieldError.getDefaultMessage() : "请求参数校验不通过";
        log.warn("参数校验未通过: {}", errorMsg);
        return Result.error(400, errorMsg);
    }
    /**
     * 3. 兜底拦截所有不可预期的系统异常 (如空指针、数据库断联、内存溢出等)
     */
    @ExceptionHandler(Exception.class)
    public Result<Void> handleSystemException(Exception e) {
        // 打印详细的堆栈信息到控制台或日志文件，供开发人员排查定位 Bug
        log.error("系统发生未知异常: ", e);
        // 对外绝不暴露真实错误堆栈，给用户一个体面的提示
        return Result.error(500, "服务器开小差了，请稍后再试");
    }

}
