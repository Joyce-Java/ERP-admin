package cn.tedu.codeadmin.ex;


import cn.tedu.codeadmin.web.ServiceCode;

public class ServiceException extends RuntimeException{

    private ServiceCode serviceCode;

    /**
     * 無參構造方法,聲明異常時傳入參數
     *
     * @param serviceCode 傳入錯誤參數
     * @param message     傳入錯誤訊息提示
     */

    public ServiceException(ServiceCode serviceCode,String message) {
        super(message);
        this.serviceCode = serviceCode;
    }

    public ServiceCode getServiceCode(){
        return serviceCode;
    }
}
