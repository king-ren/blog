package com.kingren.blog.config;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.kingren.blog.interceptor.AdminLoginInterceptor;
import com.kingren.blog.interceptor.UserInterceptor;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.context.logging.LoggingApplicationListener;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.SessionCookieConfig;
import javax.servlet.SessionTrackingMode;
import java.util.Collections;

@Configuration
public class BlogConfig  extends SpringBootServletInitializer implements WebMvcConfigurer{
    /**
     * 注入admin超级管理员拦截器
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AdminLoginInterceptor()).addPathPatterns("/admin/**").excludePathPatterns("/css/**","/font/**","/font-awesome/**","/images/**","/js/**","/layui/**","/lib/**","/admin/login");
        registry.addInterceptor(new UserInterceptor()).addPathPatterns("/user/**").excludePathPatterns("/css/**","/font/**","/font-awesome/**","/images/**","/js/**","/layui/**","/lib/**","/user/login");

    }
    @Bean
    public MybatisPlusInterceptor paginationInterceptor(){
        MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
        //设置请求的页面大于最大页后操作， true调回到首页，false 继续请求默认false
        // paginationInterceptor.setoverfLow(false);
        //设置最大单页限制数量，默认50日条，-1不受限制
        // paginationInterceptor.setLimit(500 ) ;
        //开启count 的join优化，只针对部分left join

        //这是分页拦截器
        PaginationInnerInterceptor interceptor = new PaginationInnerInterceptor();
        interceptor.setOverflow(true);
        interceptor.setMaxLimit(20L);
        mybatisPlusInterceptor.addInnerInterceptor(interceptor);
        return mybatisPlusInterceptor;
    }

}
