package com.granicus.grails.plugins.bugsnag;

import javax.servlet.FilterChain;
import org.springframework.web.filter.OncePerRequestFilter;

import java.util.Map;
import java.util.ArrayList;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.beans.BeansException;
import java.io.IOException;
import javax.servlet.ServletException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

public class BugsnagFilter extends OncePerRequestFilter implements InitializingBean, ApplicationContextAware {

    final static Logger log = Logger.getLogger(BugsnagFilter.class.getName());
    private ApplicationContext applicationContext;

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException
    {
        if( log.isTraceEnabled() ){ log.trace("setApplicationContext()"); }
        this.applicationContext = applicationContext;
    }

    @Override
    public void afterPropertiesSet() throws javax.servlet.ServletException
    {
      super.afterPropertiesSet();

      if( log.isTraceEnabled() ){ log.trace("afterPropertiesSet()"); }
    }

    @Override
    protected void initFilterBean() {
    }

    @Override
    protected void doFilterInternal( HttpServletRequest request, 
            HttpServletResponse response, 
            FilterChain chain) throws IOException, ServletException {
      if( log.isTraceEnabled() ){ log.trace("doFilterInteral()"); }
      chain.doFilter(request, response);
    }

}
