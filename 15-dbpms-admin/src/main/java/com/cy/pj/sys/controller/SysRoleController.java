package com.cy.pj.sys.controller;

import com.cy.pj.common.pojo.JsonResult;
import com.cy.pj.sys.pojo.SysRole;
import com.cy.pj.sys.service.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/role/")
public class SysRoleController {
    @Autowired
    private SysRoleService sysRoleService;

    @GetMapping("doFindRoles")
    public JsonResult doFindObjects(){
        return new JsonResult(sysRoleService.findObjects());
    }

    @GetMapping("/doFindObjectById")
    public JsonResult doFindById(Integer id){
        return new JsonResult(sysRoleService.findById(id));
    }

    @PostMapping("doUpdateObject")
    public JsonResult doUpdateObject(SysRole entity,Integer[] menuIds){
        sysRoleService.updateObject(entity, menuIds);
        return new JsonResult("update ok");
    }
    @PostMapping("doSaveObject")
    public JsonResult doSaveObject(SysRole entity,Integer[] menuIds){
        sysRoleService.saveObject(entity, menuIds);
        return new JsonResult("save ok");
    }

    @GetMapping("doFindPageObjects")
    public JsonResult doFindPageObjects(String name,Integer pageCurrent){
        return new JsonResult(sysRoleService.findPageObjects(name,pageCurrent));
    }

}
