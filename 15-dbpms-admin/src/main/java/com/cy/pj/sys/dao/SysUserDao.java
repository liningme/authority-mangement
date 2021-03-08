package com.cy.pj.sys.dao;

import com.cy.pj.sys.pojo.SysUser;
import com.cy.pj.sys.pojo.SysUserDept;
import com.cy.pj.sys.pojo.SysUserMenu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface SysUserDao {



    @Update("update sys_users set password=#{newPassword},salt=#{newSalt},modifiedTime=now() where username=#{username}")
    int updatePassword(String newPassword,String newSalt,String username);

    /**
     * 基于用户名查找数据库中的用户对象
     * @param username
     * @return
     */
    @Select("select * from sys_users where username = #{username}")
    SysUser findUserByUsername(String username);

    /**
     * 基于用户id获取用户以及用户对应的部门信息
     * @param id
     * @return
     */
    SysUserDept findById(Integer id);


    int updateObject(SysUser entity);
    int insertObject(SysUser entity);

    @Update("update sys_users set valid=#{valid},modifiedUser=#{modifiedUser},modifiedTime=now() where id=#{id}")
    int validById(Integer id,Integer valid,String modifiedUser);

    /**
     * 基于条件查询总记录数
     * @param username
     * @return
     */
    //int getRowCount(String username);
    /**
     * 基于条件查询当前页记录
     * @param username
     * @param startIndex
     * @param pageSize
     * @return
     */
    // List<SysUserDept> findPageObjects(String username,Integer startIndex,Integer pageSize);

     List<SysUserDept> findPageObjects(String username);

}
