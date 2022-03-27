package com.kingren.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kingren.blog.mapper.BlogMaapper;
import com.kingren.blog.pojo.Blog;
import com.kingren.blog.service.BlogService;
import org.springframework.stereotype.Service;

@Service
public class BlogServiceImpl extends ServiceImpl<BlogMaapper, Blog> implements BlogService {
}
