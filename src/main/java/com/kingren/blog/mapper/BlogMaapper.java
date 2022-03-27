package com.kingren.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kingren.blog.pojo.Blog;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BlogMaapper extends BaseMapper<Blog> {
}
