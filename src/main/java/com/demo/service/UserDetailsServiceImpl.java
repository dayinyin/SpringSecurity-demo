 package com.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.demo.mapper.UserMapper;
import com.demo.pojo.MyUser;

/**
 * @author admin
 * @date 2021/06/15
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService{
    
    @Autowired
    PasswordEncoder passwordEncoder;
    
    @Autowired
    UserMapper userMapper;
    

    /* (non-Javadoc)
     * @see org.springframework.security.core.userdetails.UserDetailsService#loadUserByUsername(java.lang.String)
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // TODO Auto-generated method stub
        QueryWrapper<MyUser> wrapper=new QueryWrapper<MyUser>();
        wrapper.eq("username", username);
        MyUser myUser= userMapper.selectOne(wrapper);
        String password = myUser.getPassword();
        User user=null;
        try {
            if(myUser.getRole()!=null&&myUser.getRole().equals("admin")) {
                List<GrantedAuthority> admin = AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_admin,ROLE_general");
                user = new User(myUser.getUsername(),password,admin);
            }else {
                List<GrantedAuthority> general = AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_general");
                user = new User(myUser.getUsername(),password,general);
            }
        } catch (Exception e) {
            // TODO: handle exception
        }

        return user;
    }

}
