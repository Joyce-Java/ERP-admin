package cn.tedu.codeadmin.service.impl;

import cn.tedu.codeadmin.ex.ServiceException;
import cn.tedu.codeadmin.mapper.AdminMapper;
import cn.tedu.codeadmin.mapper.RoleMapper;
import cn.tedu.codeadmin.pojo.dto.AdminLoginDTO;
import cn.tedu.codeadmin.pojo.entity.Admin;
import cn.tedu.codeadmin.pojo.dto.AdminAddNewDTO;
import cn.tedu.codeadmin.pojo.entity.AdminRole;
import cn.tedu.codeadmin.pojo.vo.AdminListItemVO;
import cn.tedu.codeadmin.pojo.vo.AdminStandardItemVO;
import cn.tedu.codeadmin.security.AdminDetails;
import cn.tedu.codeadmin.service.IAdminService;
import cn.tedu.codeadmin.web.ServiceCode;
import com.alibaba.fastjson.JSON;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class AdminServiceImpl implements IAdminService {
    @Autowired
    AdminMapper adminMapper;
    @Autowired
    RoleMapper adminRoleMapper;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationManager authenticationManager;
    @Value("${jwt.secret-key}")
    String secretKey;
    @Value("${jwt.duration-in-minute}")
    Long durationInMinute;

    @Override
    public String login(AdminLoginDTO adminLoginDTO) {
        log.debug("开始处理【管理员登录】的业务，参数：{}", adminLoginDTO);
        // TODO 调用AuthenticationManager对象的authenticate()方法处理认证
        Authentication authentication = new UsernamePasswordAuthenticationToken(adminLoginDTO.getUsername(),
                adminLoginDTO.getPassword());

        // 調用此方法可知道用戶名以及密碼是否匹配正確，若不正確則拋異常 不會執行以下方法。若執行至以下方法則認為登入成功
        Authentication authenticateResult = authenticationManager.authenticate(authentication);
        log.debug("執行認證成功,返回的對象:{}", authenticateResult);

        Object principal = authenticateResult.getPrincipal();
        log.debug("認證結果中的Principal數據類型:{}", principal.getClass().getName());
        log.debug("認證結果中的Principal數據:{}", principal);
        AdminDetails adminDetails = (AdminDetails) principal;

        log.debug("執行認證成功，即將向客戶端生成並響應JWT數據");
        Map<String, Object> claims = new HashMap<>();
        // 向JWT封裝id
        claims.put("id", adminDetails.getId());
        claims.put("username", adminDetails.getUsername()); // 向JWT中封裝username
        claims.put("authorities", JSON.toJSONString(adminDetails.getAuthorities()));// 向JWT中封装權限

        Date expirationDate = new Date(System.currentTimeMillis() + durationInMinute * 60 * 1000);

        String jwt = Jwts.builder().setHeaderParam("alg", "HS256").setHeaderParam("typ", "JWT").setClaims(claims)
                .setExpiration(expirationDate).signWith(SignatureAlgorithm.HS256, secretKey).compact();
        log.debug("返回JWT數據");
        return jwt;
    }

    public void insert(AdminAddNewDTO adminAddNewDTO){
        log.debug("即將檢查選擇的角色是否合法");

        Long[] roleIds = adminAddNewDTO.getRoleIds();
        for (int i = 0; i < roleIds.length; i++) {
            if (roleIds[i] == 1) {
                String message = "添加管理員失敗,非法訪問";
                log.warn(message);
                throw new ServiceException(ServiceCode.ERR_INSERT, message);
            }
        }

        log.debug("開始處理檢查業務");

        if (adminMapper.countByUsername(adminAddNewDTO.getUsername()) != 0) {
            log.debug("開始處理檢查業務");
            throw new ServiceException(ServiceCode.ERR_CONFLICT, "添加失敗,用戶名已存在");
        }

        if (adminMapper.countByEmail(adminAddNewDTO.getEmail()) != 0) {
            throw new ServiceException(ServiceCode.ERR_CONFLICT, "添加失敗,信箱已存在");
        }

        if (adminMapper.countByPhone(adminAddNewDTO.getPhone()) != 0) {
            log.debug("測試手機號碼");

            throw new ServiceException(ServiceCode.ERR_CONFLICT, "添加失敗,手機號碼已存在");
        }

        Admin admin = new Admin();
        BeanUtils.copyProperties(adminAddNewDTO, admin);
        // TODO 從Admin對象中取出密碼,進行加密處理,並將密文封裝回Admin對象中
        String rawPassword = admin.getPassword();
        String encode = passwordEncoder.encode(rawPassword);
        admin.setPassword(encode);

        admin.setLoginCount(0);

        log.debug("數據準備完成,即將插入【{}】", admin);
        int rows = adminMapper.insert(admin);
        if (rows != 1) {
            String message = "添加管理員失敗,服務器繁忙,請稍後再次嘗試";
            log.warn(message);
            throw new ServiceException(ServiceCode.ERR_INSERT, message);
        }

        log.debug("插入管理員數據成功");

        List<AdminRole> adminRoleList = new ArrayList<>();
        for (int i = 0; i < roleIds.length; i++) {
            AdminRole adminRole = new AdminRole();
            adminRole.setAdminId(admin.getId());
            adminRole.setRoleId(roleIds[i]);
            adminRoleList.add(adminRole);
        }
        rows = adminRoleMapper.insertBatch(adminRoleList);
        if (rows != roleIds.length) {
            String message = "添加管理員失敗,服務器繁忙,請稍後再次嘗試";
            log.warn(message);
            throw new ServiceException(ServiceCode.ERR_INSERT, message);
        }
    }
    @Override
    public void deleteById(Long id) {
        if (id == 1) {
            String message = "刪除管理員失敗,嘗試訪問的數據不存在!";
            log.warn(message);
            throw new ServiceException(ServiceCode.ERR_NOT_FOUND, message);
        }
        AdminStandardItemVO standardById = adminMapper.getStandardById(id);
        if (standardById == null) {
            log.debug("測試id" + id);
            throw new ServiceException(ServiceCode.ERR_CONFLICT, "刪除的數據不存在");
        }
        log.debug("準備刪除數據,參數:【{}】", id);
        int rows = adminMapper.deleteById(id);
        if (rows != 1) {
            String message = "刪除管理員失敗,服務器繁忙,請稍後再次嘗試";
            log.warn(message);
            throw new ServiceException(ServiceCode.ERR_DELETE, message);
        }
        log.debug("刪除數據完成");
        int rows1 = adminRoleMapper.deleteByAdminId(id);
        if (rows1 < 1) {
            String message = "刪除管理員失敗,服務器繁忙,請稍後再次嘗試";
            log.warn(message);
            throw new ServiceException(ServiceCode.ERR_DELETE, message);
        }
    }

    @Override
    public void setEnable(Long id) {
        updateEnableById(id, 1);


    }

    @Override
    public void setDisable(Long id) {
        updateEnableById(id, 0);
    }

    @Override
    public List<AdminListItemVO> list() {

        List<AdminListItemVO> list = adminMapper.list();

        Iterator<AdminListItemVO> iterator = list.iterator();
        while (iterator.hasNext()) {
            AdminListItemVO item = iterator.next();
            if (item.getId() == 1) {
                iterator.remove();
                break;
            }

        }
        return list;
    }

    @Override
    public AdminStandardItemVO selectById(Long id) {
        return adminMapper.getStandardById(id);
    }

    private void updateEnableById(Long id, Integer enable) {


        String[] tips = {"禁用", "啟用"};
        log.debug("開始處理【{}管理員】的業務,參數:{}", tips[enable], id);

        if (id == 1) {
            String message = tips[enable] + "管理員失敗,嘗試訪問的數據不存在!";
            log.warn(message);
            throw new ServiceException(ServiceCode.ERR_NOT_FOUND, message);
        }


        AdminStandardItemVO result = adminMapper.getStandardById(id);

        if (result == null) {
            String message = tips[enable] + "管理員失敗,嘗試訪問的數據不存在";
            throw new ServiceException(ServiceCode.ERR_NOT_FOUND, message);
        }

        //判斷狀態是否衝突(當前已經是目標狀態)
        if (result.getEnable() == enable) {
            String message = tips[enable] + "管理員失敗,當前的管理員帳號已經處於" + tips[enable] + "狀態";
            throw new ServiceException(ServiceCode.ERR_NOT_FOUND, message);
        }

        Admin admin = new Admin();
        admin.setId(id);
        admin.setEnable(enable);

        int rows = adminMapper.updateById(admin);
        if (rows != 1) {
            String message = tips[enable] + "管理員失敗,服務器繁忙,請稍後再次嘗試";
            log.warn(message);
            throw new ServiceException(ServiceCode.ERR_UPDATE, message);
        }
    }
}
