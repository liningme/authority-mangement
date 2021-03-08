package com.cy.pj.sys.service.impl;

import com.cy.pj.common.annotation.RequiredLog;
import com.cy.pj.common.exception.ServiceException;
import com.cy.pj.common.pojo.PageObject;
import com.cy.pj.sys.dao.SysRoleMenuDao;
import com.cy.pj.sys.dao.SysUserDao;
import com.cy.pj.sys.dao.SysUserRoleDao;
import com.cy.pj.sys.pojo.SysUser;
import com.cy.pj.sys.pojo.SysUserDept;
import com.cy.pj.sys.pojo.SysUserMenu;
import com.cy.pj.sys.service.SysUserService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.micrometer.core.instrument.util.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
@Transactional(readOnly = false,
                rollbackFor = Throwable.class,
                isolation= Isolation.READ_COMMITTED)
@Service
public class SysUserServiceImpl implements SysUserService {
    @Autowired
    private SysUserDao sysUserDao;
    @Autowired
    private SysUserRoleDao sysUserRoleDao;



    @Override
    public int updatePassword(String sourcePassword, String newPassword, String cfgPassword) {
        //1.校验参数有效性
        if(StringUtils.isEmpty(sourcePassword))
            throw new IllegalArgumentException("原密码不能为空");
        if(StringUtils.isEmpty(newPassword))
            throw new IllegalArgumentException("新密码不能为空");
        if(!newPassword.equals(cfgPassword))
            throw new IllegalArgumentException("两次输入的新密码不一致");
        //判断原密码是否正确
        SysUser user=(SysUser) SecurityUtils.getSubject().getPrincipal();
        String sourceSalt=user.getSalt();
        SimpleHash sh=new SimpleHash("MD5",sourcePassword,sourceSalt,1);
        if(!user.getPassword().equals(sh.toHex()))
            throw new IllegalArgumentException("原密码不正确");
        //对新密码进行加密
        String newSalt=UUID.randomUUID().toString();
        sh=new SimpleHash("MD5",newPassword,newSalt,1);
        String newHashedPassword=sh.toHex();
        //2.更新密码
        int rows=sysUserDao.updatePassword(newHashedPassword,newSalt,user.getUsername());
        return rows;
    }

    /**所有业务查询方法，建议事务的readOnly属性值为true*/
    @Transactional(readOnly = true)//只读事务，多线程可以共享读
    @Override
    public Map<String, Object> findById(Integer id) {
        //1.参数校验
        //2.查询用户以及用户对应的部门信息
        SysUserDept user=sysUserDao.findById(id);
        if(user==null)
            throw new ServiceException("用户可能已经不存在");
        //3.查询用户对应的角色id
        List<Integer> roleIds=sysUserRoleDao.findRoleIdsByUserId(id);
        //4.封装结果并返回
        Map<String,Object> map=new HashMap<>();
        map.put("user", user);
        map.put("roleIds", roleIds);
        return map;
    }

    @Override
    public int updateObject(SysUser entity, Integer[] roleIds) {
        //1.参数校验
        if(entity==null)
            throw new IllegalArgumentException("保存对象不能为空");
        if(StringUtils.isEmpty(entity.getUsername()))
            throw new IllegalArgumentException("用户名不能为空");
        if(roleIds==null||roleIds.length==0)
            throw new IllegalArgumentException("必须要为用户分配角色");
        //.....
        //2.更新用户自身信息
        int rows=sysUserDao.updateObject(entity);
        if(rows==0)
            throw new ServiceException("用户可能已经不存在");
        //3.更新用户和角色关系数据
        sysUserRoleDao.deleteObjectsByUserId(entity.getId());
        sysUserRoleDao.insertObjects(entity.getId(),roleIds);
        return rows;
    }
    @Transactional //此注解描述的方法为切入点方法
    @Override
    public int saveObject(SysUser entity, Integer[] roleIds) {

        //1.参数校验
        if(entity==null)
            throw new IllegalArgumentException("保存对象不能为空");
        if(StringUtils.isEmpty(entity.getUsername()))
            throw new IllegalArgumentException("用户名不能为空");
        if(StringUtils.isEmpty(entity.getPassword()))
            throw new IllegalArgumentException("密码不能为空");
        if(roleIds==null||roleIds.length==0)
            throw new IllegalArgumentException("必须要为用户分配角色");
        //.....
        //2.保存用户自身信息
        //2.1对密码进行加密
        String salt= UUID.randomUUID().toString();//通过UUID获取一个随机字符串，作为加密盐
        SimpleHash sh=new SimpleHash("MD5", entity.getPassword(), salt, 1);
        String hashedPassword=sh.toHex();
        //2.2存储加密结果
        entity.setSalt(salt);//为什么还要存储盐值(登录时还要基于这个盐值进行加密)
        entity.setPassword(hashedPassword);
        //2.3持久化内存中的用户对象
        int rows=sysUserDao.insertObject(entity);
        //3.保存用户和角色关系数据
        sysUserRoleDao.insertObjects(entity.getId(),null);
        return rows;
    }

    /**
     *   @RequiresPermissions 注解描述的方法为授权访问切入点方法，表示
     * 访问这个方法需要授权.这个授权的动作由shiro框架中的securityManager
     * 对象来实现，请问这个对象如何对用户进行授权？第一，要获取用户有什么权限，
     * 第二，要获取访问这个方法需要什么权限(RequiresPermissions内部value属性
     * 的值) 第三，检测用户拥有的这些权限中是否包含执行此方法需要的权限，假如
     * 包含则授权。
     * @param id
     * @param valid
     * @return
     */
    @Transactional //此注解描述的方法为事务切入点方法
    @RequiresPermissions(value="sys:user:update")//述的方法为授权访问切入点方法
    @RequiredLog //此注解描述的方法为日志切入点方法
    @Override
    public int validById(Integer id, Integer valid) {
        ///...............
        //1.参数校验
        if(id==null||id<1)
            throw new IllegalArgumentException("id值无效");
        if(valid==null||valid!=0&&valid!=1)
            throw new IllegalArgumentException("状态值不正确");
        //2.修改状态并校验结果
        int rows=sysUserDao.validById(id,valid,"admin");//这里admin代表登录用户
        if(rows==0)
            throw new ServiceException("记录可能已经不存在");
        return rows;
    }

    @RequiredLog(operation = "分页查询用户信息") //此注解描述的方法为日志记录切入点方法
    @Override
    public PageObject<SysUserDept> findPageObjects(String username, Integer pageCurrent) {
        String tName=Thread.currentThread().getName();
        System.out.println("SysUserServiceImpl.findPageObjects.thread.name="+tName);
        //1.参数校验
        if(pageCurrent==null||pageCurrent<1)
            throw new IllegalArgumentException("页码值不正确");
        //2.启动分页配置(底层会启动一个mybatis拦截器，拦截sql会话)
        int pageSize=3;
        Page<SysUserDept> page=PageHelper.startPage(pageCurrent, pageSize);//底层会计算起始位置
        //3.执行分页查询(底层会在查询时为sql语句添加limit 语句)
        List<SysUserDept> records=sysUserDao.findPageObjects(username);
        return new PageObject<>((int)page.getTotal(), records, pageSize, pageCurrent);
    }
}
