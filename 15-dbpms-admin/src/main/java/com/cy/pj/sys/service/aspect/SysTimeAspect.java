package com.cy.pj.sys.service.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
/**通过此案例演示aop中各种通知类型的执行时间点，
 * 实际项目中并不会将所有通知方法都写到一个切面类中。
 * 具体写哪个个通知方法要结合具体业务*/
@Aspect
@Component
public class SysTimeAspect {

    @Pointcut("bean(sysUserServiceImpl)")
    public void doTime(){}

    @Before("doTime()")
    public void doBefore(){//业务方法执行之前执行
        System.out.println("@Before");
    }
    @After("doTime()")
    public void doAfter(){// @After在业务方法结束时执行(不管是正常还是异常都会执行)
        System.out.println("@After");
    }
    @AfterReturning("doTime()")
    public void doAfterReturning(){//@AfterReturning在业务方法正常结束执行
        System.out.println("@AfterReturning");
    }
    @AfterThrowing("doTime()")
    public void doAfterThrowing(){//@AfterThrowing在业务方法异常结束时执行
        System.out.println("@AfterThrowing");
    }
    /**@Around注解描述的方法优先级最高*/
    @Around("doTime()")
    public Object doAround(ProceedingJoinPoint joinPoint)throws Throwable{
        System.out.println("@Around.before");
        try {
            Object result = joinPoint.proceed();//执行目标方法
            System.out.println("@Around.afterReturning");
            return result;
        }catch (Throwable e){
            e.printStackTrace();
            System.out.println("@Around.afterThrowing");
            throw e;
        }finally{
            System.out.println("@Around.After...");
        }
    }
}
