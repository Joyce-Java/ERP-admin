package cn.tedu.codeadmin.controller;

import cn.tedu.codeadmin.pojo.dto.AdminAddNewDTO;
import cn.tedu.codeadmin.service.IAdminService;
import cn.tedu.codeadmin.service.IRoleService;
import cn.tedu.codeadmin.web.JsonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/roles")
public class RoleController {
    @Autowired
    IRoleService iRoleService;

    public RoleController() {
      log.debug("創建控制器類=RoleController");
    }

    @GetMapping("")
    public JsonResult list(){
        log.debug("開始處理【查詢角色列表】的請求");
        return JsonResult.ok(iRoleService.list());
    }

}
