package cn.tedu.codeadmin.service;

import cn.tedu.codeadmin.pojo.dto.AdminAddNewDTO;
import cn.tedu.codeadmin.pojo.vo.RoleListItemVO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 管理員的service層
 */
@Transactional
public interface IRoleService {

    /**
     * 查詢管理員數據列表
     * @return 返回管理員數據
     */
    List<RoleListItemVO> list();
}
