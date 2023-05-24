package cn.tedu.codeadmin.mapper;

import cn.tedu.codeadmin.pojo.entity.Admin;
import cn.tedu.codeadmin.pojo.vo.AdminListItemVO;
import cn.tedu.codeadmin.pojo.vo.AdminLoginInfoVO;
import cn.tedu.codeadmin.pojo.vo.AdminStandardItemVO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminMapper {
    /**
     * 插入管理員數據
     * @param admin 管理員數據
     * @return 返回受影響的行數
     */
    int insert(Admin admin);

    /**
     * 根據id刪除管理員數據
     * @param id 管理員id
     * @return 返回受影響的行數
     */
    int deleteById(Long id);

    /**
     * 根據id修改參數
     * @param admin 要修改的參數對象
     * @return 返回受影響的行數
     */
    int updateById(Admin admin);

    /**
     * 根據用戶名統計管理員的數量
     * @param username 用戶名
     * @return 匹配用戶名的管理員數據
     */
    int countByUsername(String username);

    /**
     * 根據手機號碼統計管理員的數量
     * @param phone 手機號碼
     * @return 匹配手機號碼的管理員數據
     */
    int countByPhone(String phone);

    /**
     * 根據信箱計管理員的數量
     * @param email 電子信箱
     * @return 匹配電子信箱的管理員數據
     */
    int countByEmail(String email);

    /**
     * 根據查詢管理員詳情
     * @param id 管理員id
     * @return 匹配的管理員詳情,如果沒有匹配的數據,則返回null
     */
    AdminStandardItemVO getStandardById(Long id);

    /**
     * 查詢管理員列表
     * @return 返回管理員列表參數
     */
    List<AdminListItemVO> list();

    /**
     *
     * @param username
     * @return
     */
    AdminLoginInfoVO getLoginInfoByUsername(String username);

}
