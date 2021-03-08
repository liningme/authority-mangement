package com.cy.pj.sys.dao;

import com.cy.pj.common.pojo.Node;
import com.cy.pj.sys.pojo.SysMenu;
import com.cy.pj.sys.pojo.SysUserMenu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface SysMenuDao {

       /**基于用户id找到对应的菜单*/
       List<SysUserMenu> findMenusByIds(List<Integer> menuIds);
       List<String> findPermissions(List<Integer> menuIds);
       int insertObject(SysMenu menu);
       int updateObject(SysMenu menu);
       @Select("select id,name,parentId from sys_menus")
       public List<Node> findZtreeMenuNodes();

       List<Map<String,Object>> findObjects();
}
