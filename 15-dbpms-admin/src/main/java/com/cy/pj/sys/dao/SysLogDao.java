package com.cy.pj.sys.dao;

import com.cy.pj.sys.pojo.SysLog;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SysLogDao {

    List<SysLog> findPageObjects(String username);
    int insertObject(SysLog entity);
}
