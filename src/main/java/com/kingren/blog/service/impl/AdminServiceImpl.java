package com.kingren.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kingren.blog.mapper.AdminMapper;
import com.kingren.blog.pojo.Admin;
import com.kingren.blog.service.AdminService;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService {
}
