package com.cy.pj.sys.controller;

import com.cy.pj.common.pojo.JsonResult;
import com.cy.pj.sys.pojo.SysMenu;
import com.cy.pj.sys.service.SysMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SysMenuController {
    @Autowired
    private SysMenuService sysMenuService;

    @PostMapping("/menu/doUpdateObject")
    public JsonResult doUpateObject(SysMenu entity){
        sysMenuService.updateObject(entity);
        return new JsonResult("update ok");
    }
    @PostMapping("/menu/doSaveObject")
    public JsonResult doSaveObject(SysMenu entity){
        sysMenuService.saveObject(entity);
        return new JsonResult("save ok");
    }

    @CrossOrigin
    @GetMapping("/menu/doFindZtreeMenuNodes")
    public JsonResult doFindZtreeMenuNodes(){
        return new JsonResult(sysMenuService.findZtreeMenuNodes());
    }

    @CrossOrigin
    @GetMapping("/menu/doFindObjects")
    public JsonResult doFindObjects() throws InterruptedException {
        //Thread.sleep(5000);
        return new JsonResult(sysMenuService.findObjects());
    }
}
