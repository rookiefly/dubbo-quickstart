package com.rookiefly.quickstart.dubbo.aop;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Aspect
@Component
@Slf4j
public class LogAspect {

    /**
     * 用来记录请求进入的时间，防止多线程时出错，这里用了ThreadLocal
     */
    private ThreadLocal<Long> startTime = new ThreadLocal<>();

    /**
     * 定义切入点，controller下面的所有类的所有公有方法，这里需要更改成自己项目的
     */
    @Pointcut("execution(public * com.rookiefly.quickstart.dubbo.controller..*.*(..))")
    public void requestLog() {
    }

    /**
     * 方法之前执行，日志打印请求信息
     *
     * @param joinPoint joinPoint
     */
    @Before("requestLog()")
    public void doBefore(JoinPoint joinPoint) {
        startTime.set(System.currentTimeMillis());
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();
        /**
         * 打印当前的请求路径
         */
        log.info("RequestMapping:[{}]", request.getRequestURI());

        //这里是从token中获取用户信息，打印当前的访问用户，代码不通用
/*        String token = request.getHeader(JwtUtils.TOKEN_HEADER);
        if (token != null && token.startsWith(JwtUtils.TOKEN_PREFIX)) {
            token = token.replace(JwtUtils.TOKEN_PREFIX, "");
            String username = JwtUtils.getUsername(token);
            log.info("Current User is:[{}]",username);
        }*/

        log.info("RequestParam:{}", JSON.toJSONString(joinPoint.getArgs()));
    }

    /**
     * 方法返回之前执行，记录返回值以及方法消耗时间
     *
     * @param response 返回值
     */
    @AfterReturning(returning = "response", pointcut = "requestLog()")
    public void doAfterRunning(Object response) {
        /**
         * 记录响应信息
         */

        log.info("Response:[{}]", JSON.toJSONString(response));

        /**
         * 记录请求耗时
         */
        log.info("Request spend times : [{}ms]", System.currentTimeMillis() - startTime.get());
    }

    @AfterThrowing(pointcut = "requestLog()", throwing = "error")
    public void afterThrowingAdvice(JoinPoint jp, Throwable error) {
        /**
         * 记录响应信息
         */

        log.info("Response:[{}]", JSON.toJSONString(error));

        /**
         * 记录请求耗时
         */
        log.info("Request spend times : [{}ms]", System.currentTimeMillis() - startTime.get());
    }

}