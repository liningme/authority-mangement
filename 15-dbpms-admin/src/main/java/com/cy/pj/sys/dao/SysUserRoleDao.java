package com.cy.pj.sys.dao;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SysUserRoleDao {


    /**
     * 基于用户id查找用户对应的角色id
     * @param userId
     * @return
     */
    @Select("select role_id from sys_user_roles where user_id=#{userId}")
    List<Integer> findRoleIdsByUserId(Integer userId);

    int insertObjects(Integer userId,Integer[]roleIds);

    @Delete("delete from sys_user_roles where user_id=#{userId}")
    int deleteObjectsByUserId(Integer userId);
}
