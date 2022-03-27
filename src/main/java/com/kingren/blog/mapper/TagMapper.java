package com.kingren.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kingren.blog.pojo.Tag;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TagMapper extends BaseMapper<Tag> {
}
