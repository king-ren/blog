package com.kingren.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kingren.blog.mapper.PictureMapper;
import com.kingren.blog.pojo.Picture;
import com.kingren.blog.service.PictureService;
import org.springframework.stereotype.Service;

@Service
public class PictureServiceImpl extends ServiceImpl<PictureMapper, Picture> implements PictureService {
}
