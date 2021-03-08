package com.cy.pj.sys.common.config;

import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.cache.MemoryConstrainedCacheManager;
import org.apache.shiro.mgt.RememberMeManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;

/**
 * @Configuration 注解描述的类，为springboot工程中的配置类，此类
 * 的实例由spring创建和管理。
 */
@Configuration
public class SpringShiroConfig {

    @Bean
    public SessionManager sessionManager(){
        DefaultWebSessionManager sessionManager=new DefaultWebSessionManager();
        sessionManager.setGlobalSessionTimeout(2*1800000L);//默认为30分钟
        //不重写url
        sessionManager.setSessionIdUrlRewritingEnabled(false);
        return sessionManager;
    }
    /**记住我对象配置*/
    @Bean
    public RememberMeManager rememberMeManager(){
        CookieRememberMeManager rememberMeManager=new CookieRememberMeManager();
        //构建Cookie对象，此对象负责存储用户状态信息，并将状态信息写到客户端
        SimpleCookie cookie=new SimpleCookie("rememberMe");
        cookie.setMaxAge(7*24*60*60);//设置cookie的的生命周期
        rememberMeManager.setCookie(cookie);
        return rememberMeManager;
    }

    /**配置shiro缓存管理器，减少授权时，频繁访问数据库查询用户权限信息的过程。*/
    @Bean
    public CacheManager shiroCacheManager(){
        return new MemoryConstrainedCacheManager();
    }
    /**
     * 配置SecurityManager，此对象是Shiro框架的核心，负责认证和授权等业务实现。
     * 当由spring框架整合一个第三方的bean对象时，这个类型不是我们自己写的，
     * 我们无法在类上直接使用类似@Component的注解进行描述,那么如何去整合
     * 这样的bean的呢？
     * 解决方案：自己在spring中的配置类(@Configuration)中定义方法,
     * 在方法内部构建对象实例，并且由@Bean注解对方法进行描述即可.(记住，这是规则),
     * 这个注解描述的方法其返回值会交给spring管理，其bean的名字默认为方法名，
     * 当然也可以通过@Bean注解对应bean的名字进行定义(例如@Bean("sManager")).
     */
    //站在java多态特性应用的角度分析，方法的返回值，参数列表类型能用抽象尽量使用抽象类型。
    @Bean //
    public SecurityManager securityManager(Realm realm,
                                           CacheManager cacheManager,
                                           RememberMeManager rememberMeManager,
                                           SessionManager sessionManager){
        //在web应用项目中，SecurityManager的具体实现建议使用DefaultWebSecurityManager对象。
        DefaultWebSecurityManager securityManager=
                      new DefaultWebSecurityManager();
        securityManager.setRealm(realm);
        securityManager.setCacheManager(cacheManager);
        securityManager.setRememberMeManager(rememberMeManager);
        securityManager.setSessionManager(sessionManager);
        return securityManager;
    }

    /**
     * 配置认证过滤规则,例如,哪些资源需要认证访问，哪些资源可以匿名访问。
     * 这个规则我们来定义，规则的检验在shiro框架中是借助大量过滤器(Filter)去实现的,
     * Shiro框架中提供了过滤器类型，但是基于其类型创建其过滤器实例需要通过滤器工厂，
     * 而我们这里配置的FactoryBean对象就是用于创建过滤器工厂的一个对象(spring框架中
     * 所有FactoryBean的作用都是用于创建过滤器工厂的)。
     * @return
     */
    @Bean
    //@Autowired 可以省略
    public ShiroFilterFactoryBean shiroFilterFactory(SecurityManager securityManager){
        System.out.println("==shiroFilterFactoryBean===");
        ShiroFilterFactoryBean filterFactoryBean=new ShiroFilterFactoryBean();
        //在map中存储规则，key为资源名，value为规则
        LinkedHashMap<String,String> map=new LinkedHashMap<>();
        map.put("/bower_components/**","anon");//anon表示匿名访问,Shiro框架定义的字符串
        map.put("/build/**","anon");
        map.put("/dist/**","anon");
        map.put("/plugins/**","anon");
        map.put("/user/doLogin", "anon");//放行登录操作，允许登录
        map.put("/doLogout", "logout");//当value为logout时，退出时会自动回到登录页面
        //除了以上资源，后续所有资源都要认证访问
        //map.put("/**", "authc");//authc表示需要认证
        map.put("/**", "user");//此方式的认证还可以从客户端的cookie中取用户信息
        filterFactoryBean.setFilterChainDefinitionMap(map);
        //如何判定你访问这个资源时是否已经认证过了呢？（要通过securityManager实现）
        filterFactoryBean.setSecurityManager(securityManager);
        //假如访问一个需要认证的资源，但这个用户还没有通过认证，我们要做什么？跳转到指定认证页面(例如登录页面)
        filterFactoryBean.setLoginUrl("/doLoginUI");
        return filterFactoryBean;
    }

    /**
     * 这里的advisor(顾问)负责找到类中使用此注解RequiresPermissions描述的方法，
     * 这些方法为授权访问的切入点方法，当在执行这些方时会由通知(Advice)对象
     * 调用SecurityManager对象完成权限检测及授权。
     * @param securityManager
     * @return
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager){
        AuthorizationAttributeSourceAdvisor advisor=new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }

}
