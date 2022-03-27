package com.kingren.blog.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kingren.blog.pojo.*;
import com.kingren.blog.service.*;
import com.sun.org.apache.xpath.internal.operations.Mod;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/blog")
public class BlogController {
    @Autowired
    BlogService blogService;

    @Autowired
    TypeService typeService;

    @Autowired
    UserService userService;

    @Autowired
    TagService tagService;

    @Autowired
    PictureService pictureService;

    /**
     * 博客详情页
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/detail/{id}")
    public String blogPage(@PathVariable(value = "id",required = false) Integer id,
            Model model,HttpSession session){

        Blog byId = blogService.getById(id);
        Object visitor = session.getAttribute("visitor");
        model.addAttribute("blog",byId);
        if(visitor != null){
            byId.setViews(byId.getViews()+1);
            blogService.saveOrUpdate(byId);
        }
        User byId1 = userService.getById(byId.getUserId());
        model.addAttribute("user",byId1);


        QueryWrapper<Type> typeQueryWrapper = new QueryWrapper<>();
        QueryWrapper<Type> typeId = typeQueryWrapper.eq("id", byId.getTypeId());
        Type type = typeService.getOne(typeId);
        model.addAttribute("type",type);

        return "blog/blog";
    }

    /**
     * 跳转到音乐页面
     * @return
     */
    @GetMapping("/musicpage")
    public String musicPage(){
        return "blog/music.html";
    }


    /**
     * 跳转到类型界面
     * @param model
     * @param session
     * @return
     */
    @GetMapping("/typepage")
    public String typePage(Model model,HttpSession session){
        List<Type> typeList = typeService.list();
        model.addAttribute("typelist",typeList);
         return "blog/types";
    }

    /**
     * 分类博客
     * @param typeId
     * @param session
     * @param model
     * @return
     */
    @GetMapping("/searchtype/{id}")
    public String searchType(@PathVariable(value = "id") Integer typeId,
                             HttpSession session,Model model){
        QueryWrapper<Blog> blogQueryWrapper = new QueryWrapper<>();
        QueryWrapper<Blog> typeId1 = blogQueryWrapper.eq("type_id", typeId).eq("published",true);
        Page<Blog> blogPage = new Page<>(1,4);
        Page<Blog> page = blogService.page(blogPage, typeId1);
        model.addAttribute("blog",page);
        return "blog/search";
    }

    /**
     * 博客模糊查询
     * @param title
     * @param session
     * @param model
     * @return
     */
    @PostMapping("/searchblog")
    public String searchBlog(@RequestParam(value = "title") String  title,
                             HttpSession session,Model model){
        QueryWrapper<Blog> blogQueryWrapper = new QueryWrapper<>();
        QueryWrapper<Blog> typeId1 = blogQueryWrapper.eq("published",true).like("title",title);
        Page<Blog> blogPage = new Page<>(1,4);
        Page<Blog> page = blogService.page(blogPage, typeId1);
        model.addAttribute("blog",page);
        return "blog/search";
    }

    /**
     * 跳转到友联
     * @return
     */
    @GetMapping("/friend")
    public String friend(){
        return "blog/friends";
    }

    /**
     * 跳转到时间轴
     * @return
     */
    @GetMapping("/timeaxis")
    public String timeAxis(Model model){

        List<Tag> list = tagService.list();
        Collections.reverse(list);
        model.addAttribute("timeAxis",list);
        return "blog/archives.html";
    }

    /**
     * 跳转到photo
     * @return
     */
    @GetMapping("/photo")
    public String photo(Model model){
        List<Picture> list = pictureService.list();
        Collections.reverse(list);
        model.addAttribute("photos",list);
        return "blog/picture";
    }
}
