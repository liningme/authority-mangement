package com.cy.pj.sys.service.realm;

import com.cy.pj.sys.dao.SysMenuDao;
import com.cy.pj.sys.dao.SysRoleMenuDao;
import com.cy.pj.sys.dao.SysUserDao;
import com.cy.pj.sys.dao.SysUserRoleDao;
import com.cy.pj.sys.pojo.SysUser;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 定义realm类型继承授权AuthorizingRealm类型，
 * 假如只做认证可以直接继承认证AuthenticatingRealm即可.
 *
 * Shiro框架中Realm对象可以理解为用户获取认证数据信息和授权数据信息的一个对象。
 */
@Service
public class ShiroUserRealm extends AuthorizingRealm {//AuthorizingRealm 继承了 AuthenticatingRealm

    @Autowired
    private SysUserDao sysUserDao;
    @Autowired
    private SysUserRoleDao sysUserRoleDao;
    @Autowired
    private SysRoleMenuDao sysRoleMenuDao;
    @Autowired
    private SysMenuDao sysMenuDao;
    /**
     *此方法用于获取用户的权限信息并进行封装。
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(
            PrincipalCollection principalCollection) {
        //1.获取登录用户
        SysUser user=(SysUser)principalCollection.getPrimaryPrincipal();
        //2.获取登录用户的角色id (从用户角色关系表)
        List<Integer> roleIds=sysUserRoleDao.findRoleIdsByUserId(user.getId());
        if(roleIds==null||roleIds.size()==0)throw new AuthorizationException();
        //3.获取角色对应的菜单id(用角色菜单关系表)
        List<Integer> menuIds=sysRoleMenuDao.findMenuIdsByRoleIds(roleIds);
        if(menuIds==null||menuIds.size()==0)throw new AuthorizationException();
        //4.获取菜单id对应授权标识(permisssion)-从菜单表
        List<String> permissions=sysMenuDao.findPermissions(menuIds);
        if(permissions==null||permissions.size()==0)throw new AuthorizationException();
        //5.封装数据并返回，交给securityManager对象
        SimpleAuthorizationInfo info=new SimpleAuthorizationInfo();
        Set<String> setPermissions=new HashSet<>();
        for(String per:permissions){
            if(per!=null&&!"".equals(per)){
                setPermissions.add(per);
            }
        }
        System.out.println("setPermissions="+setPermissions);
        info.setStringPermissions(setPermissions);
        return info;
    }

    /**
     * 此方法获取用户认证信息并进行封装
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(
            AuthenticationToken authenticationToken) throws AuthenticationException {
        //1.获取登录时输入的用户名
        UsernamePasswordToken upToken= (UsernamePasswordToken) authenticationToken;
        String username=upToken.getUsername();
        //2.基于用户名查找数据库中的用户信息
        SysUser user=sysUserDao.findUserByUsername(username);
        //3.校验用户是否存在
        if(user==null)
            throw new UnknownAccountException();
        //4.校验用户是否已被禁用
        if(user.getValid()==0) throw new LockedAccountException();
        //5.封装用户信息并返回，将信息交给SecurityManager进行认证
        ByteSource credentialSalt=ByteSource.Util.bytes(user.getSalt());
        SimpleAuthenticationInfo info=
                new SimpleAuthenticationInfo(user,//principal用户身份
                        user.getPassword(),//hashedCredentials已加密的密码
                        credentialSalt,//credentialSalt 加密盐
                        getName());//realmName
        return info;//此对象最终会交给securityManager
    }

    /***
     * SecurityManager比对密码时需要调用此方法获取加密算法相关信息
     * @return
     */
    @Override
    public CredentialsMatcher getCredentialsMatcher() {
        HashedCredentialsMatcher credentialsMatcher=
                new HashedCredentialsMatcher("MD5");
        credentialsMatcher.setHashIterations(1);
        return credentialsMatcher;
    }

}

//login form->controller-->subject.login(token)-->securityManager-->realm-->dao