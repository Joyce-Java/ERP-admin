package cn.tedu.codeadmin.pojo.vo;


import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class AdminListItemVO implements Serializable {
    /**
     * 數據id
     */
    private Long id;
    /**
     * 用戶名
     */
    private String username;
    /**
     * 暱稱
     */
    private String nickname;
    /**
     * 頭像
     */
    private String avatar;
    /**
     * 手機號碼
     */
    private String phone;
    /**
     * 信箱
     */
    private String email;
    /**
     * 描述
     */
    private String description;
    /**
     * 是否啟用,1=啟用 2=不啟用
     */
    private Integer enable;
    /**
     * 登入IP地址
     */
    private String lastLoginIp;
    /**
     * 最後登入時間
     */
    private Integer loginCount;

    private LocalDateTime gmtLastLogin;
}
