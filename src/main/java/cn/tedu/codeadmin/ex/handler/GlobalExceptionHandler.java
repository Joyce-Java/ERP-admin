package cn.tedu.codeadmin.ex.handler;

import cn.tedu.codeadmin.ex.ServiceException;
import cn.tedu.codeadmin.web.JsonResult;
import cn.tedu.codeadmin.web.ServiceCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.util.Set;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    public GlobalExceptionHandler() {
        log.debug("創建全局異常處理器物件:GlobalExceptionHandler");
    }
    @Autowired
    private CustomAccessDeniedHandler accessDeniedHandler;

    @ExceptionHandler
    public JsonResult<Void> handleBindException(BindException e) {
        //以下2行代碼,如果有多種錯誤時,將隨機獲取其中一種錯誤的信息,並響應
        log.debug("捕獲到BindException:{}" + e.getMessage());
        String message = e.getFieldError().getDefaultMessage();
        return JsonResult.fail(ServiceCode.ERR_BAD_REQUEST, message);

    }

    @ExceptionHandler
    public JsonResult<Void>  handleConstraintViolationException(ConstraintViolationException e) {
        log.debug("捕获到ConstraintViolationException：{}", e.getMessage());
        StringBuilder stringBuilder = new StringBuilder();
        Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();
        for (ConstraintViolation<?> constraintViolation : constraintViolations) {
            stringBuilder.append(constraintViolation.getMessage());
        }

        return JsonResult.fail(ServiceCode.ERR_BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler
    public String handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        log.debug("捕獲到HttpRequestMethodNotSupportedException：{}", e.getMessage());
        return "非法訪問";

    }

    @ExceptionHandler
    public JsonResult<Void>  handleServiceException(ServiceException e) {
        log.debug("捕獲到ServiceException:{}", e.getMessage());
        return JsonResult.fail(e);
    }

    @ExceptionHandler
    public void handleAccessDeniedException(AccessDeniedException e, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        accessDeniedHandler.handle(request, response, e);
    }

    @ExceptionHandler
    public String handleThrowable(Throwable e) {
        log.debug("捕獲到Throwable：{}", e.getMessage());
        e.printStackTrace(); // 强烈建议
        return "服務器運行過程中出現未知錯誤，請聯繫系統管理員！";
    }
}
