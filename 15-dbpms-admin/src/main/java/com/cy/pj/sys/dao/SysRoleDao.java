package com.cy.pj.sys.dao;

import com.cy.pj.common.pojo.CheckBox;
import com.cy.pj.sys.pojo.SysRole;
import com.cy.pj.sys.pojo.SysRoleMenu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 通过此接口对象操作角色表数据
 */
@Mapper
public interface SysRoleDao {
      /**
       * 查询角色id和名字，每个一行数据封装为一个Checkbox对象
       * @return
       */
      @Select("select id,name from sys_roles")
      List<CheckBox> findObjects();

      SysRoleMenu findById(Integer id);

      int updateObject(SysRole entity);
      int insertObject(SysRole entity);

      /**
       * 基于条件统计记录总数
       * @param name 查询条件
       * @return 总记录数
       */
      int getRowCount(String name);

      /**
       * 基于查询条件，查询当前页角色信息
       * @param name
       * @param startIndex
       * @param pageSize
       * @return
       */
      List<SysRole> findPageObjects(String name, Integer startIndex, Integer pageSize);
}
