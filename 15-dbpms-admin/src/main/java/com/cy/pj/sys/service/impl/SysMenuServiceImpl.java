package com.cy.pj.sys.service.impl;

import com.cy.pj.common.pojo.Node;
import com.cy.pj.sys.dao.SysMenuDao;
import com.cy.pj.sys.dao.SysRoleMenuDao;
import com.cy.pj.sys.dao.SysUserRoleDao;
import com.cy.pj.sys.pojo.SysMenu;
import com.cy.pj.sys.pojo.SysUserMenu;
import com.cy.pj.sys.service.SysMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class SysMenuServiceImpl implements SysMenuService {
    @Autowired
    private SysMenuDao sysMenuDao;

    @Autowired
    private SysUserRoleDao sysUserRoleDao;

    @Autowired
    private SysRoleMenuDao sysRoleMenuDao;
    @Override
    public List<SysUserMenu> findUserMenusByUserId(Integer userId) {
        //1.基于用户id查找用户角色id
        List<Integer> roleIds=sysUserRoleDao.findRoleIdsByUserId(userId);
        //2.基于用户角色id查找菜单id
        List<Integer> menuIds=sysRoleMenuDao.findMenuIdsByRoleIds(roleIds);
        //3.基于菜单id获取菜单信息
        return sysMenuDao.findMenusByIds(menuIds);
    }

    @Override
    public int updateObject(SysMenu entity) {
        if(entity==null)
            throw new IllegalArgumentException("保存对象不能为空");
        if(entity.getName()==null||"".equals(entity.getName()))
            throw new IllegalArgumentException("菜单名不允许为空");
        //........
        int rows=sysMenuDao.updateObject(entity);
        return rows;
    }
    @Override
    public int saveObject(SysMenu entity) {
        if(entity==null)
            throw new IllegalArgumentException("保存对象不能为空");
        if(entity.getName()==null||"".equals(entity.getName()))
            throw new IllegalArgumentException("菜单名不允许为空");
        //........
        int rows=sysMenuDao.insertObject(entity);
        return rows;
    }

    public List<Node> findZtreeMenuNodes(){
        return sysMenuDao.findZtreeMenuNodes();
    }
    @Override
    public List<Map<String, Object>> findObjects() {
        //......
        return sysMenuDao.findObjects();
    }
}
