package com.cy.pj.sys.service.aspect;

import com.cy.pj.common.annotation.RequiredLog;
import com.cy.pj.common.util.IPUtils;
import com.cy.pj.sys.pojo.SysLog;
import com.cy.pj.sys.pojo.SysUser;
import com.cy.pj.sys.service.SysLogService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Date;

/**
 * @Aspect 注解描述的类型为spring中的一个切面类型，
 * 此类型的对象我们称之为切面对象.这样的对象中的封装
 * 是扩展业务逻辑(服务增益)的实现，它通常会包含两部
 * 分内容的定义(切入点和通知)
 *
 * 1)切入点:切入拓展业务的点的集合
 * 2)通知：封装的是拓展业务逻辑的实现(日志记录，缓存，权限，事务，异步，。。。。)
 *
 */
@Slf4j
@Aspect
@Component
public class SysLogAspect {//FAQ？此类型的对象谁来调用？代理对象
    /**
     * @Pointcut 注解用于描述方法，定义切入点。
     * bean(spring容器中bean的名字)为切入点表达式中的一种定义切入点的方式
     * 在当前切入点的定义中，表示名字为sysUserServiceImpl的bean对象中，所有方法的集合
     * 为拓展业务的切入点。也就是说sysUserServiceImpl这个bean对象中的任意一个方法执行
     * 时，都要进行功能拓展(例如记录日志)。
     */
    //@Pointcut("bean(sysUserServiceImpl)")
    //@annotation方式的切入点表达式定义(表示由此注解描述的方法为切入点方法)
    @Pointcut("@annotation(com.cy.pj.common.annotation.RequiredLog)")
    public void doLog(){}//方法内部不需要写任何代码，只是用于承载注解@PointCut


    /**
     * @Around 注解描述的方法为一个业务拓展方法，此方法在aop应用中通常称之为环绕通知.
     * 此注解内部value属性的值为切入点，这些切入点方法执行时，会执行@Around注解描述
     * 的方法。在方法内部进行拓展业务逻辑的实现。
     * @param joinPoint 表示连接点，@Around注解描述的方法参数必须为ProceedingJoinPoint类型，
     *                  这个连接点对应的是切入点集合中某个正在执行的目标方法
     * @return 目标方法的返回值，@Around注解描述的方法返回值必须为object类型。
     * @throws Throwable 在执行业务的过程中可能出现的异常
     */
    @Around("doLog()")
    public Object doAround(ProceedingJoinPoint joinPoint)throws Throwable{
        long t1=System.currentTimeMillis();
        log.info("start {}",t1);
        try {
            Object result = joinPoint.proceed();//执行目标业务方法
            long t2 = System.currentTimeMillis();
            log.info("end {}", t2);
            //保存用户正常行为日志(谁在什么时间，执行了什么操作，访问了什么方法，传递了什么参数，执行时长是多少)
            saveUserLog(joinPoint,t2-t1);//--获取日志，调用日志service，调用日志dao，将数据写入到数据库
            return result;//这个值返回给谁，代理对象
        }catch (Throwable e){
            log.error("error {}",e.getMessage());
            throw e;
        }
    }
    @Autowired
    private SysLogService sysLogService;
    private void saveUserLog(ProceedingJoinPoint joinPoint,long time) throws NoSuchMethodException, JsonProcessingException {
        //1.获取用户行为日志
        //1.1获取访问用户的ip地址
        String ip= IPUtils.getIpAddr();
        //1.2获取登录用户的用户名
        SysUser user=(SysUser) SecurityUtils.getSubject().getPrincipal();
        String username=user.getUsername();//将来会用户登录用户名
        //1.3获取目标方法对象
        //获取目标对象类型(反射入口)
        Class<?> targetClass=joinPoint.getTarget().getClass();
        //获取目标对象类中的方法
        MethodSignature ms= (MethodSignature) joinPoint.getSignature();
        Method targetMethod=targetClass.getDeclaredMethod(ms.getName(), ms.getParameterTypes());
        //1.4获取方法上的注解,进而获取注解中operation属性的值。
        RequiredLog requiredLog=targetMethod.getAnnotation(RequiredLog.class);
        String operation=requiredLog.operation();
        //1.5获取方法信息(方法所在类的类全名+方法名)
        String method=targetClass.getName()+"."+targetMethod.getName();//类全名+方法名
        //1.6 获取调用方法时传入的参数信息(假如参数可以转换为json格式，尽量转换为json格式)
        String params=new ObjectMapper().writeValueAsString(joinPoint.getArgs());
        //2.封装用户行为日志
        SysLog log=new SysLog();
        log.setIp(ip);
        log.setUsername(username);
        log.setOperation(operation);
        log.setMethod(method);
        log.setParams(params);
        log.setTime(time);
        log.setCreatedTime(new Date());
        //3.将日志写入到数据库
//        new Thread(){//线程对象的创建即耗时，又占用内存(一般一个线程1M内存)
//            @Override
//            public void run() {
//                sysLogService.saveObject(log);
//            }
//        }.start();

         sysLogService.saveObject(log);
    }
}
