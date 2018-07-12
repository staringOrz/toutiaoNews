package com.xijian.toutiao.configuration;

import com.xijian.toutiao.intercepter.LoginRequiredIntercepter;
import com.xijian.toutiao.intercepter.PassportIntercepter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Component
public class toutianWebConfiguration implements WebMvcConfigurer {
    @Autowired
    PassportIntercepter passportIntercepter;

    @Autowired
    LoginRequiredIntercepter loginRequiredIntercepter;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(passportIntercepter);
        registry.addInterceptor(loginRequiredIntercepter).addPathPatterns("/setting*");
    }
}
