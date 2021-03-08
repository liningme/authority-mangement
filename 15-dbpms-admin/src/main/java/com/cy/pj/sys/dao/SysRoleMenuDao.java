package com.cy.pj.sys.dao;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SysRoleMenuDao {
    List<Integer> findMenuIdsByRoleIds(List<Integer> roleIds);
    /**
     * 基于角色id删除角色和菜单关系数据
     * @param roleId
     * @return
     */
    @Delete("delete from sys_role_menus where role_id=#{roleId}")
    int deleteObjectsByRoleId(Integer roleId);

    List<Integer> findMenuIdsByRoleId(Integer roleId);
    /**
     * 保存角色和菜单关系数据
     * @param roleId
     * @param menuIds
     * @return
     */
    int insertObjects(Integer roleId,Integer[] menuIds);
}
