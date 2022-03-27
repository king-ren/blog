package com.kingren.blog;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kingren.blog.pojo.Blog;
import com.kingren.blog.service.BlogService;
import com.kingren.blog.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;

@SpringBootTest
@Slf4j
class BlogApplicationTests {

    @Autowired
    BlogService blogService;
    @Autowired
    UserService userService;

    @Test
    void contextLoads() {
        QueryWrapper<Blog> blogQueryWrapper = new QueryWrapper<>();
        QueryWrapper<Blog> like = blogQueryWrapper.eq("published", true);
        Page<Blog> blogPage = new Page<Blog>();
        Page<Blog> page = blogService.page(blogPage, like);
        List<Blog> records = page.getRecords();
        log.info("records是{}",records.size());

    }


}
