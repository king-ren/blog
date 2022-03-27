package com.kingren.blog.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Slf4j
public class AdminLoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        Object adminid = session.getAttribute("adminid");
        //打印日志
        String requestURI = request.getRequestURI();
        log.info("拦截请求路径{}",requestURI);
        if(adminid != null){
            return true;
        }else {
            request.setAttribute("msg","请先登录");
            request.getRequestDispatcher("/admin/login").forward(request,response);
            return false;
        }
    }
}
