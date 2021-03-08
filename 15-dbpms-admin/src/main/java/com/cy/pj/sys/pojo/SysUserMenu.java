package com.cy.pj.sys.pojo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**用户菜单对象*/
@Data
public class SysUserMenu implements Serializable {
    private static final long serialVersionUID = 2152789115079901409L;
    //一级菜单信息
    private Integer id;
    private String name;
    private String url;
    //二级菜单信息
    private List<SysUserMenu> childs;
}
