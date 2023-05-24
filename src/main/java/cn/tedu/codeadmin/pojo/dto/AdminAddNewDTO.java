package cn.tedu.codeadmin.pojo.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class AdminAddNewDTO implements Serializable {
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

    private Long[] roleIds;

}
