package cn.tedu.codeadmin.mapper;

import cn.tedu.codeadmin.pojo.entity.AdminRole;
import cn.tedu.codeadmin.pojo.vo.RoleListItemVO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleMapper {
    /**
     * 查詢管理員列表
     * @return 返回管理員列表參數
     */
    List<RoleListItemVO> list();
    int insertBatch(List<AdminRole> adminRoleList);

    /**
     * 根據管理員Id刪除權限數據
     * @param adminId 管理員Id
     * @return 返回受影響的行數
     */
    int deleteByAdminId(Long adminId);

}
