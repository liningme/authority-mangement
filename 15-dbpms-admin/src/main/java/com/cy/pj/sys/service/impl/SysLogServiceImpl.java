package com.cy.pj.sys.service.impl;

import com.cy.pj.common.pojo.PageObject;
import com.cy.pj.sys.dao.SysLogDao;
import com.cy.pj.sys.pojo.SysLog;
import com.cy.pj.sys.service.SysLogService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
public class SysLogServiceImpl implements SysLogService {
    @Autowired
    private SysLogDao sysLogDao;

    //@Async描述的方法为一个切入点方法，此方法会运行在一个新的线程中，
    @Async //这个线程来自与springboot提供的线程池
    @Transactional(propagation = Propagation.REQUIRES_NEW)//描述的方法运行在独立事务中
    @Override
    public void saveObject(SysLog entity) {
        String tName=Thread.currentThread().getName();
        System.out.println("SysLogServiceImpl.saveObject.thread.name="+tName);
        //模拟耗时操作
        try{Thread.sleep(5000);}catch (Exception e){}
        sysLogDao.insertObject(entity);
    }

    @Override
    public PageObject<SysLog> findPageObjects(String username, Integer pageCurrent) {
        //1.参数校验
        if(pageCurrent==null||pageCurrent<1)
            throw new IllegalArgumentException("页码值不正确");
        //2.启动分页配置(底层会启动一个mybatis拦截器，拦截sql会话)
        int pageSize=3;
        Page<SysLog> page= PageHelper.startPage(pageCurrent, pageSize);//底层会计算起始位置
        //3.执行分页查询(底层会在查询时为sql语句添加limit 语句)
        List<SysLog> records=sysLogDao.findPageObjects(username);
        return new PageObject<>((int)page.getTotal(), records, pageSize, pageCurrent);
    }
}
