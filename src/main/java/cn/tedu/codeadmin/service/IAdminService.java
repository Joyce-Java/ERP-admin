package cn.tedu.codeadmin.service;

import cn.tedu.codeadmin.pojo.dto.AdminAddNewDTO;
import cn.tedu.codeadmin.pojo.dto.AdminLoginDTO;
import cn.tedu.codeadmin.pojo.vo.AdminListItemVO;
import cn.tedu.codeadmin.pojo.vo.AdminStandardItemVO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 管理員的service層
 */
@Transactional
public interface IAdminService {

    /**
     * 管理員登入
     * @param adminLoginDTO
     * @return 成功登入的JWT數據
     */
    String login(AdminLoginDTO adminLoginDTO);

    /**
     * 插入管理員
     * @param adminAddNewDTO 管理員參數封裝的對象
     */
    void insert(AdminAddNewDTO adminAddNewDTO);
    /**
     * 刪除管理員
     * @param id 管理員的id
     */
    void deleteById(Long id);



    void setEnable(Long id);

    void setDisable(Long id);



    /**
     * 管理員列表
     * @return
     */
    List<AdminListItemVO> list();

    AdminStandardItemVO selectById(Long id);
}
