 package com.demo.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.demo.pojo.MyUser;

/**
 * @author admin
 * @date 2021/06/15
 */
@Mapper
public interface UserMapper extends BaseMapper<MyUser>{

}
