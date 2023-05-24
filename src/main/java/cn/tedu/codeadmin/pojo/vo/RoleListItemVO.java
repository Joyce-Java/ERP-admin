package cn.tedu.codeadmin.pojo.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class RoleListItemVO implements Serializable {
    /**
     * 管理員id
     */
    private Long id;
    /**
     * 管理員名稱
     */
    private String name;
    /**
     * 管理員簡介
     */
    private String description;
    /**
     * 排序編號
     */
    private Integer sort;

}
