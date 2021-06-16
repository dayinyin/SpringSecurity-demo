 package com.demo.filter;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;
import org.thymeleaf.util.StringUtils;

import com.demo.pojo.RespBean;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 自定义springsecrity拦截器，这是验证码Filter
 * @author admin
 * @date 2021/06/16
 */
@Component
public class VerifyCodeFilter extends GenericFilterBean {
    
    /**
     * 拦截登录url
     */
    private String filterUrl="/login";

    /* (non-Javadoc)
     * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
     */
    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
        throws IOException, ServletException {
        // TODO Auto-generated method stub
        HttpServletRequest request = (HttpServletRequest)req;
        HttpServletResponse response =(HttpServletResponse)resp;
        //输入的验证码
        String tryCode = request.getParameter("tryCode");
        //获取系统生成验证码
        String rightCode = (String) request.getSession().getAttribute("rightCode");
        if("POST".equals(request.getMethod())&&filterUrl.equals(request.getServletPath())) {
            if(StringUtils.isEmpty(tryCode)) {
                /**
                 * throw异常和输出json二选一，页面接受json解析
                 */
//              RespBean error = RespBean.error("验证码不能为空");
//              response.setContentType("application/json;charset=utf-8");
//              PrintWriter out = response.getWriter();
//              out.write(new ObjectMapper().writeValueAsString(error));
//              out.flush();
//              out.close();
              throw new AuthenticationServiceException("验证码不能为空!");
            }
            if(!rightCode.equals(tryCode)) {
                /**
                 * throw异常和输出json二选一，页面接受json解析
                 */
//              RespBean error = RespBean.error("验证码错误");
//              response.setContentType("application/json;charset=utf-8");
//              PrintWriter out = response.getWriter();
//              out.write(new ObjectMapper().writeValueAsString(error));
//              out.flush();
//              out.close();
              throw new AuthenticationServiceException("验证码错误!");
            }
        }
        chain.doFilter(request, response);
    }

}
