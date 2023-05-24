package cn.tedu.codeadmin.pojo.entity;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 管理員實體類
 *  @author Joyce
 *  @version 0.0.1
 */
@Data
public class Admin implements Serializable {
    /**
     * 管理員編號
     */
    private Long id;
    /**
     * 管理員名稱
     */
    private String username;
    /**
     * 管理員密碼(密文)
     */
    private String password;
    /**
     * 管理員暱稱
     */
    private String nickname;
    /**
     * 頭像URL
     */
    private String avatar;
    /**
     * 手機號碼
     */
    private String phone;
    /**
     * 電子郵件
     */
    private String email;
    /**
     * 簡介
     */
    private String description;
    /**
     *是否啟用 1=啟用,0=未啟用
     */
    private Integer enable;
    /**
     *最後登入IP地址
     */
    private String lastLoginIp;
    /**
     * 累計登入次數
     */
    private Integer loginCount;
    /**
     *  最後登入時間
     */
    private LocalDateTime gmtLastLogin;
    /**
     *  數據創建時間
     */
    private LocalDateTime gmtCreate;
    /**
     *  數據最後修改時間
     */
    private LocalDateTime gmtModified;
}
