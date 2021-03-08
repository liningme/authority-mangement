package com.cy.pj.sys.controller;

import com.cy.pj.common.pojo.JsonResult;
import com.cy.pj.common.pojo.PageObject;
import com.cy.pj.sys.pojo.SysUser;
import com.cy.pj.sys.pojo.SysUserDept;
import com.cy.pj.sys.service.SysUserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/")
public class SysUserController {
    @Autowired
    private SysUserService sysUserService;//应用了AOP以后，这个service此时指向的对象是谁？

    @RequestMapping("doUpdatePassword")
    public JsonResult doUpdatePassword(String pwd,String newPwd,String cfgPwd){
        sysUserService.updatePassword(pwd,newPwd,cfgPwd);
        return new JsonResult("update ok");
    }

    @RequestMapping("/doLogin")
    public JsonResult doLogin(String username,String password,boolean isRememberMe){
        //将用户名和密码封装到令牌对象中
        UsernamePasswordToken token=
                new UsernamePasswordToken(username, password);
        Subject subject=SecurityUtils.getSubject();
        //设置记住我
        token.setRememberMe(isRememberMe);
        //提交用户信息进行登录
        subject.login(token);
        return new JsonResult("login ok");
    }

    @GetMapping("doFindObjectById")
    public JsonResult doFindById(Integer id){
        return new JsonResult(sysUserService.findById(id));
    }
    @PostMapping("doSaveObject")
    public JsonResult doSaveObject(SysUser entity,Integer[]roleIds){
        sysUserService.saveObject(entity, roleIds);
        return new JsonResult("save ok");
    }

    @PostMapping("doUpdateObject")
    public JsonResult doUpdateObject(SysUser entity,Integer[]roleIds){
        sysUserService.updateObject(entity, roleIds);
        return new JsonResult("update ok");
    }
    @PostMapping("doValidById")
    public JsonResult doValidById(Integer id,Integer valid){
        //long t1=System.currentTimeMillis();
        sysUserService.validById(id,valid);
        //long t2=System.currentTimeMillis();
        //long time=t2-t1;
        return new JsonResult("update ok");
    }

    @GetMapping("doFindPageObjects")
    public JsonResult doFindPageObjects(String username,Integer pageCurrent){
        //long t1=System.currentTimeMillis();
        PageObject<SysUserDept> pageObject=
        sysUserService.findPageObjects(username, pageCurrent);
        //long t2=System.currentTimeMillis();
        //long time=t2-t1;
        return new JsonResult(pageObject);
    }
}
