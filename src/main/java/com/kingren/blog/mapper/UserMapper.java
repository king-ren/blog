package com.kingren.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kingren.blog.pojo.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
