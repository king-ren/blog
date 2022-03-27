package com.kingren.blog.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kingren.blog.pojo.Blog;
import com.kingren.blog.pojo.User;
import com.kingren.blog.service.BlogService;
import com.kingren.blog.service.UserService;
import com.kingren.blog.utils.RandomUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.List;
import java.util.Random;

@Slf4j
@Controller
public class IndexController {

    @Autowired
    BlogService blogService;

    @Autowired
    UserService userService;

//    /**
//     * 欢迎页
//     * @return
//     */
    @GetMapping("/shouye")
    public String index(HttpSession session){
        session.setAttribute("visitor", RandomUtils.getRandomString());
        return "redirect:/main";
    }

    /**
     * 跳转到主页
     * @return
     */
    @GetMapping("/main")
    public String mainPage(@RequestParam(value = "pn", defaultValue = "1")Integer pn,
                           Model model,
                           HttpSession session){
        //推荐

        QueryWrapper<Blog> blogQueryWrapper = new QueryWrapper<>();
        QueryWrapper<Blog> eq = blogQueryWrapper.eq("published", true).eq("recommend", true);
        Page<Blog> blogPage = new Page<Blog>(pn,4);
        Page<Blog> page = blogService.page(blogPage, eq);
        List<Blog> records = page.getRecords();
        //集合反转
        Collections.reverse(records);
        model.addAttribute("page",records);



        //最新
        QueryWrapper<Blog> blogQueryWrapper1 = new QueryWrapper<>();
        QueryWrapper<Blog> published = blogQueryWrapper1.eq("published", true);
        Page<Blog> blogPage1 = new Page<>(pn,4);
        Page<Blog> page1 = blogService.page(blogPage1,published);
        model.addAttribute("newPage",page1);
        List<Blog> records1 = page1.getRecords();


//        log.info("Size:{}",page1.getRecords().size());

//        User byId1 = userService.getById(byId.getUserId());
        return "main";
    }

}
