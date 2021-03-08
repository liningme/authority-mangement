package com.cy.pj.sys.service.impl;

import com.cy.pj.common.exception.ServiceException;
import com.cy.pj.common.pojo.CheckBox;
import com.cy.pj.common.pojo.PageObject;
import com.cy.pj.sys.dao.SysRoleDao;
import com.cy.pj.sys.dao.SysRoleMenuDao;
import com.cy.pj.sys.pojo.SysRole;
import com.cy.pj.sys.pojo.SysRoleMenu;
import com.cy.pj.sys.service.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysRoleServiceImpl implements SysRoleService {
    @Autowired
    private SysRoleDao sysRoleDao;

    @Autowired
    private SysRoleMenuDao sysRoleMenuDao;

    @Override
    public List<CheckBox> findObjects() {
        return sysRoleDao.findObjects();
    }

    //方案2：数据层进行表关联查询(left join)
    @Override
    public SysRoleMenu findById(Integer id) {
        //1.参数校验
        //2.基于id查询角色以及角色对应的菜单id
        SysRoleMenu roleMenu=sysRoleDao.findById(id);
        return roleMenu;
    }

    @Override
    public int updateObject(SysRole entity, Integer[] menuIds) {
        //1.参数校验
        //2.更新角色自身信息
        int rows=sysRoleDao.updateObject(entity);
        if(rows==0)
            throw new ServiceException("记录可能已经不存在");
        //3.更新角色菜单关系数据
        //3.1先删除原有关系数据
        sysRoleMenuDao.deleteObjectsByRoleId(entity.getId());
        //3.2添加新的角色菜单关系数据
        sysRoleMenuDao.insertObjects(entity.getId(),menuIds);
        return rows;
    }
    //方案1：业务层发起多次单表查询(一张表一张的查询)
//    @Override
//    public SysRoleMenu findById(Integer id) {
//        //1.参数校验
//        //2.基于id查询角色自身信息
//        SysRoleMenu roleMenu=sysRoleDao.findById(id);
//        //3.基于id查询角色对应的菜单id
//        List<Integer> menuIds=sysRoleMenuDao.findMenuIdsByRoleId(id);
//        roleMenu.setMenuIds(menuIds);
//        return roleMenu;
//    }

    @Override
    public int saveObject(SysRole entity, Integer[] menuIds) {
        //1.参数校验(暂时忽略)
        //2.保存角色自身信息
        int rows=sysRoleDao.insertObject(entity);
        //3.保存角色菜单关系数据
        sysRoleMenuDao.insertObjects(entity.getId(),menuIds);
        return rows;
    }

    @Override
    public PageObject<SysRole> findPageObjects(String name, Integer pageCurrent) {
        //1.参数校验
        if(pageCurrent==null||pageCurrent<1)
            throw new IllegalArgumentException("页码值不正确，页码值应该>=1");
        //2.查询总记录数并校验
        int rowCount=sysRoleDao.getRowCount(name);
        if(rowCount==0)
            throw new ServiceException("没有找到对应记录");
        //3.查询当前页记录
        int pageSize=2;//页面大小，暂时可以写成固定值，后续可以从页面获取
        int startIndex=(pageCurrent-1)*pageSize;//分页查询时的起始位置(计算得出)
        List<SysRole> records= sysRoleDao.findPageObjects(name,startIndex,pageSize);
        //4.封装查询结果
//        PageObject<SysRole> po=new PageObject<>();
//        po.setRowCount(rowCount);
//        po.setRecords(records);
//        po.setPageSize(pageSize);
//        po.setPageCurrent(pageCurrent);
//        int pageCount=rowCount/pageSize;
//        if(rowCount%pageSize!=0){
//            pageCount++;
//        }
//        po.setPageCount(pageCount);
//        return po;
        return new PageObject<>(rowCount, records, pageSize, pageCurrent);
    }
}
