package cn.tedu.codeadmin.web;

import cn.tedu.codeadmin.ex.ServiceException;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class JsonResult<T> implements Serializable {
    @ApiModelProperty("業務狀態碼")
    private Integer state;
    @ApiModelProperty("操作失敗時提示文本")
    private String message;
    private T data;

    public static JsonResult<Void> ok(){
        return ok(null);
    }

    public static <T> JsonResult ok(T data){
        JsonResult jsonResult = new JsonResult();
        jsonResult.state = ServiceCode.OK.getValue();
        jsonResult.data = data;
        return jsonResult;
    }

    public static JsonResult<Void> fail(ServiceException e) {
        return fail(e.getServiceCode(), e.getMessage());
    }

    public static JsonResult<Void> fail(ServiceCode serviceCode,String message){
        JsonResult jsonResult = new JsonResult();
        jsonResult.state = serviceCode.getValue();
        jsonResult.message=message;
        return jsonResult;
    }
}
