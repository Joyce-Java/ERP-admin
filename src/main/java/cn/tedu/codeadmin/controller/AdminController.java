package cn.tedu.codeadmin.controller;

import cn.tedu.codeadmin.pojo.dto.AdminAddNewDTO;
import cn.tedu.codeadmin.pojo.dto.AdminLoginDTO;
import cn.tedu.codeadmin.pojo.vo.AdminListItemVO;
import cn.tedu.codeadmin.pojo.vo.AdminStandardItemVO;
import cn.tedu.codeadmin.service.IAdminService;
import cn.tedu.codeadmin.web.JsonResult;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/admins")
@Api(tags = "01. 管理員管理模塊")
public class AdminController {
    @Autowired
    IAdminService iAdminService;

    @ApiOperation("管理員登入")
    @PostMapping("/login")
    public JsonResult<String> login(AdminLoginDTO adminLoginDTO) {
        log.debug("开始处理【管理员登录】的请求，参数：{}", adminLoginDTO);
        // TODO 調用Service處理
        String jwt = iAdminService.login(adminLoginDTO);
        return JsonResult.ok(jwt);
    }

    @ApiOperation("添加管理員")
    @PostMapping("/add-new")
    @PreAuthorize("hasAuthority('/ams/admin/add-new')")

    public JsonResult<Void> insert(AdminAddNewDTO adminAddNewDTO){
        log.debug("開始處理【添加管理員】的請求");
        iAdminService.insert(adminAddNewDTO);
        return JsonResult.ok();
    }

    @ApiOperation("刪除管理員")
    @ApiImplicitParam(name="id" ,value = "管理員id",required = true,dataType = "long")
    @PostMapping("/{id:[0-9]+}/delete")
    @PreAuthorize("hasAuthority('/ams/admin/delete')")
    @ApiOperationSupport(order = 200)
    public JsonResult<Void> delete(@PathVariable Long id){
        log.debug("開始處理【刪除管理員的請求】,參數:{}",id);
        iAdminService.deleteById(id);
        return JsonResult.ok();
    }

    @ApiOperation("啟用管理員")
    @ApiImplicitParam(name="id" ,value = "管理員id",required = true,dataType = "long")
    @PostMapping("/{id:[0-9]+}/enable")
    @ApiOperationSupport(order = 300)
    public JsonResult<Void> setEnable(@PathVariable Long id){
        iAdminService.setEnable(id);
        return JsonResult.ok();
    }

    @ApiOperation("禁用管理員")
    @ApiImplicitParam(name="id" ,value = "管理員id",required = true,dataType = "long")
    @PostMapping("/{id:[0-9]+}/disable")
    @ApiOperationSupport(order = 311)
    public JsonResult<Void> setDisable(@PathVariable Long id){
        log.debug("開始處理【禁用管理員】的請求");
        iAdminService.setDisable(id);
        return JsonResult.ok();
    }

    @ApiOperation("管理員列表")
    @ApiOperationSupport(order = 420)
    @GetMapping("")
    @PreAuthorize("hasAuthority('/ams/admin/read')")
    public JsonResult<List<AdminListItemVO>> list(){
        log.debug("開始處理【查詢管理員列表】的請求");
        List<AdminListItemVO> list = iAdminService.list();
        return JsonResult.ok(list);
    }

    @ApiOperation("管理員資料")
    @ApiOperationSupport(order = 420)
    @PostMapping("/{id:[0-9]+}/selectById")
    public JsonResult<Void> selectById(@PathVariable Long id){
        log.debug("開始處理【查詢管理員列表】的請求");
        AdminStandardItemVO list = iAdminService.selectById(id);
        return JsonResult.ok(list);
    }

}
