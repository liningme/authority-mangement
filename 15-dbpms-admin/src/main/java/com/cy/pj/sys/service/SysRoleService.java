package com.cy.pj.sys.service;

import com.cy.pj.common.pojo.CheckBox;
import com.cy.pj.common.pojo.PageObject;
import com.cy.pj.sys.pojo.SysRole;
import com.cy.pj.sys.pojo.SysRoleMenu;

import java.util.List;

/**
 * 此接口中定义角色业务规则
 */
public interface SysRoleService {
    List<CheckBox> findObjects();
    /**
     * 基于角色id查询角色以及角色对应的菜单id
     * @param id
     * @return
     */
    SysRoleMenu findById(Integer id);
    /**
     * 更新角色自身信息以及角色和菜单的关系数据
     * @param entity
     * @param menuIds
     * @return
     */
    int updateObject(SysRole entity,Integer[]menuIds);
    /**
     * 保存角色自身信息以及角色和菜单的关系数据
     * @param entity
     * @param menuIds
     * @return
     */
    int saveObject(SysRole entity,Integer[]menuIds);
    /**
     * 查询角色总记录数以及当前页要呈现的记录，并计算总页数然后封装数据并返回
     * @param name 角色名
     * @param pageCurrent 当前页码值
     * @return 封装了当前也数据以及分析信息的一个对象
     */
     PageObject<SysRole> findPageObjects(String name,Integer pageCurrent);
}
