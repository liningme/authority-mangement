# 权限管理子系统

#### 介绍
[模块] 部门、用户、菜单、角色、日志等管理模块
[完成]
1、使用SSM框架完成对部门、用户、菜单、角色的增删改查。
2、使用AspectJ实现AOP异步记录日志，记录用户的操作，并将其存储到数据库。
3、使用Apache Shiro框架实现用户身份认证，权限授权、加密、会话管理等功能。

#### 软件架构
 SpringBoot + Spring MVC + MyBatis + MySQL +Thymeleaf + Ajax+ jQuery + Shiro+zTree


#### 业务实现
##### Shiro 框架认证业务实现？ -- 判断用户身份的合法性。
~~~~
1)客户端提交用户名和密码。
2)在服务端的Controller对象中获取用户名和密码？
3)将用户名和密码封装到token对象，然后交给subject对象。
4)Subject对象将token提交给谁securityManager对象
5)SecurityManager对象调用realm基于客户端提交的用户信息，查询数据库中对应的用户
信息并进行封装，将封装信息返回给SecurityManager对象。
6)SecurityManager对象基于客户端提交的信息以及数据查询到信息进行用户身份认证
7)认证不通过要回到登录页面进行重新认证。
~~~~

##### Shiro 框架授权业务实现？ -- 访问指定的资源（底层授权通过aop实现）
~~~~
1)在SpringShiroConfig中配置advisor对象？(发现切入点并应用通知方法)
2)使用@RequiresPermissions注解对需要授权访问的方法进行描述(定义切入点，对需要授权访问的资源进行标识)
3)在ShiroUserRealm中获取认证用户的权限信息并返回给SecurityManager对象。
4)SecurityManager对象判断用户的资源访问权限中是否包含@RequiresPermissions中指定的权限信息，假如包含则进行访问授权，不包含则抛出异常。
~~~~



##### Shiro 缓存的实现？ -- 提高授权效率，降低数据库的访问压力
~~~~
1)添加缓存管理器(CacheManager)配置
2)将缓存管理器对象注入给SecurityManager对象
~~~~



##### Shiro 记住用户登录状态？     
~~~~
1)在Shiro的配置类中，添加RememberMeManager对象的配置，并且会为此对象注入一个Cookie对象。
2)将RememberMeManager对象注入给SecurityManager对象(因为此对象负责实现用户认证。认证以后可以通过此对象获取RememberMeManager，进而拿到Cookie对象，将用户信息在Cookie中做一个记录)
3)在ShiroFilterFactoryBean的配置中，修改认证方式为user方式。
4)在SysUserController类的doLogin方法中接收记住我的状态，并将此状态存储到token对象，然后将token再提交给securitymanager。
5)在客户端login.html中，提交用户登录信息时，同时也要提交记住我这个状态值。
~~~~






##### Shiro 会话Session？     
~~~~
1)配置SessionManager对象 （会话时长，禁用url重写）
2)将SessionManager注入给SecurityManager对象
~~~~



#### 使用说明

1.  xxxx
2.  xxxx
3.  xxxx

#### 参与贡献

1.  Fork 本仓库
2.  新建 Feat_xxx 分支
3.  提交代码
4.  新建 Pull Request

