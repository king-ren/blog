package com.kingren.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kingren.blog.mapper.TypeMapper;
import com.kingren.blog.pojo.Type;
import com.kingren.blog.service.TypeService;
import org.springframework.stereotype.Service;

@Service
public class TypeServiceImpl extends ServiceImpl<TypeMapper, Type> implements TypeService {
}
