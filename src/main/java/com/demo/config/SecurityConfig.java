 package com.demo.config;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import com.demo.filter.VerifyCodeFilter;
import com.demo.pojo.RespBean;
import com.demo.service.UserDetailsServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author admin
 * @date 2021/06/15
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    
    @Autowired
    VerifyCodeFilter verifyCodeFilter;

    @Autowired
    UserDetailsServiceImpl userDetailsServiceImpl;
    
    @Autowired
    DataSource datasource;
    
    /**
     * 记住我设置
     * @return
     */
    @Bean
    public PersistentTokenRepository persistentTokenRepository(){
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(datasource);
      //系统在启动的时候生成“记住我”的数据表（只能使用一次）,如果表存在第二次就会报错
//        tokenRepository.setCreateTableOnStartup(true);  
        return tokenRepository;
    }
    
    /**
     * 认证登录名和密码得
     */
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsServiceImpl).passwordEncoder(password());
    }
    
    @Bean
    PasswordEncoder password() {
        return new BCryptPasswordEncoder();
    }
    
    protected void configure(HttpSecurity http) throws Exception {
        http.addFilterBefore(verifyCodeFilter, UsernamePasswordAuthenticationFilter.class);
      /**
       *   
       //（/）表示主页请求，(permitAll())表全部拥有此授权
      http.authorizeRequests()
      .antMatchers("/").permitAll(); 
      */
        //url拦截认证  >所有请求都必须被认证 必须登录后才可以访问
        http.authorizeRequests()
                //设置不需要拦截的页面
                .antMatchers("/defaultKaptcha").permitAll()
                .antMatchers("/toLogin").permitAll()
                .antMatchers("/").permitAll()
                 //所有请求都必须被认真,必须登录后才能访问
//                .antMatchers("/toSuccess").hasRole("general");
                .anyRequest().authenticated();

      /*当没有权限时跳转到此登陆页(系统自带)*/
     /*http.formLogin();*/
     /*进入自带的登陆页*/
    http.formLogin().loginPage("/toLogin")
            .defaultSuccessUrl("/toSuccess")
            .usernameParameter("username")
            .passwordParameter("password")
            .loginProcessingUrl("/login")
            /**
             * 返回json信息，页面接受json解析
             */
//            .successHandler(new AuthenticationSuccessHandler(){
//                @Override
//                public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
//                    Authentication authentication) throws IOException, ServletException {
//                    // TODO Auto-generated method stub
//                    RespBean ok = RespBean.ok("登录成功", authentication.getPrincipal());
//                    response.setContentType("application/json;charset=utf-8");
//                    PrintWriter out = response.getWriter();
//                    out.write(new ObjectMapper().writeValueAsString(ok));
//                    out.flush();
//                    out.close();
//                }
//            })
//            .failureHandler(new AuthenticationFailureHandler() {
//                @Override
//                public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
//                    AuthenticationException exception) throws IOException, ServletException {
//                    // TODO Auto-generated method stub
//                    RespBean error = RespBean.error("登录失败");
//                    response.setContentType("application/json;charset=utf-8");
//                    PrintWriter out = response.getWriter();
//                    out.write(new ObjectMapper().writeValueAsString(error));
//                    out.flush();
//                    out.close();
//                }
//            })
            ;

     /*登陆注销操作
     * 1.在前面添加一个按钮链接th:href=@{/logout}
     * 注销后跳转到(/)链接页面
     * 2.http.logout().logoutSuccessUrl("/")
     * */
     http.csrf().disable();
     http.logout().logoutUrl("/logout").logoutSuccessUrl("/toLogin");

     /*使用自带的登陆页面所以remember me 要改变*/
      /* http.rememberMe();*/
      http.rememberMe().tokenRepository(persistentTokenRepository())
      .tokenValiditySeconds(3600)   //单位秒
      .userDetailsService(userDetailsServiceImpl)
      .rememberMeParameter("remeberme");  //自定义checkbox的name，网页
  }
    
    /**
     *处理Spring Security拦截静态资源问题 
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        //解决静态资源被拦截的问题
        web.ignoring().antMatchers("/lib/**");
        web.ignoring().antMatchers("/css/**");
        web.ignoring().antMatchers("/images/**");
    }
}
