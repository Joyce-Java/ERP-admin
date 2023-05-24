package cn.tedu.codeadmin.pojo.vo;

import lombok.Data;

import java.util.List;

@Data
public class AdminLoginInfoVO {
    /**
     * 數據id
     */
    private Long id;
    /**
     * 用戶名
     */
    private String username;
    /**
     * 密碼(密文)
     */
    private String password;
    /**
     * 是否啟用 1=啟用,0=未啟用
     */
    private Integer enable;
    /**
     * 管理員權限列表
     */
    private List<String> permissions;
}
