package com.cy.pj.sys.controller;

import com.cy.pj.sys.pojo.SysUser;
import com.cy.pj.sys.pojo.SysUserMenu;
import com.cy.pj.sys.service.SysMenuService;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * 通过这个Controller处理所有的页面请求
 */
@Controller
public class PageController {

    @GetMapping("/doLoginUI")
    public String doLoginUI(){
        return "login";
    }

//    @GetMapping("/menu/menu_list")
//    public String doMenuUI(){
//        return "sys/menu_list";
//    }

    //通过rest风格的url处理客户端的ui请求
    @GetMapping("/{module}/{moduleUI}")
    public String doModuleUI(@PathVariable String moduleUI){
        return "sys/"+moduleUI;
    }

    @Autowired
    private SysMenuService sysMenuService;
    /**返回首页页面*/
    @GetMapping("/doIndexUI")
    public String doIndexUI(Model model){
        //获取登录用户对象(底层从session获取)
        SysUser user=(SysUser)SecurityUtils.getSubject().getPrincipal();
        String loginUser=user.getUsername();//假设这是登录用户
        model.addAttribute("username", loginUser);

        List<SysUserMenu> userMenus=
        sysMenuService.findUserMenusByUserId(user.getId());
        model.addAttribute("userMenus", userMenus);
        System.out.println("userMenus="+userMenus);
        return "starter";
    }

    @GetMapping("/doPageUI")
    public String doPageUI(){
       return "common/page";
    }
}
