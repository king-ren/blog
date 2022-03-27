package com.kingren.blog.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kingren.blog.mapper.BlogMaapper;
import com.kingren.blog.pojo.*;
import com.kingren.blog.service.*;
import com.kingren.blog.utils.MD5Utils;
import com.kingren.blog.utils.MarkdownUtils;
import com.sun.org.apache.xpath.internal.operations.Mod;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    TypeService typeService;

    @Autowired
    FirendLinnkService firendLinnkService;

    @Autowired
    BlogService blogService;

    @Autowired
    TagService tagService;
    @Autowired
    PictureService pictureService;

//=================================================userService==================================



    /**
     * 用户登录
     * @return
     */
    @GetMapping("/login")
    public String login(Model model,HttpSession session){
        Object msg = session.getAttribute("msg");
        model.addAttribute("msg",msg);
        session.removeAttribute("msg");
        return "user/login";
    }
    @PostMapping("/login")
    public String loginUser(@RequestParam(value = "username")String username,
                            @RequestParam(value = "password") String  password,
                            HttpSession session,
                            RedirectAttributes ra){
        List<User> list = userService.list();
//        log.info("list集合{}",list);
//        log.info("username{}",username);
//        log.info("password{}",password);
        for (User user1 : list) {
            if(username.equals(user1.getUsername()) && MD5Utils.getMD5(password).equals(user1.getPassword())){
                session.setAttribute("user",user1);
                session.setAttribute("userid",user1.getId());
                return "redirect:/user/blogpage";
            }
        }
        session.setAttribute("msg","用户名或密码错误！");
        return "redirect:/user/login";
    }

    /**
     * 跳转到修改自己（用户）信息页面
     * @return
     */
    @GetMapping("/updateuserpage/{id}")
    public String updateUserMySelfPage(@PathVariable(value = "id")Integer id,
                                       HttpSession session,Model model){
        session.setAttribute("updateuserid",id);
        User byId = userService.getById(id);
        model.addAttribute("byId",byId);
        return "user/update";
    }

    @PostMapping("/updateuserpage")
    public String updateUser(User user,HttpSession session,Model model,RedirectAttributes ra){
        Integer id =(Integer) session.getAttribute("updateuserid");
        user.setId(id);
        user.setPassword(MD5Utils.getMD5(user.getPassword()));
        Calendar instance = Calendar.getInstance();
        user.setUpdateTime(instance.getTime());
        userService.saveOrUpdate(user);
        session.removeAttribute("user");
        session.removeAttribute("updateuserid");
        session.setAttribute("msg","用户已修改信息，请重新登录！");
        return "redirect:/user/login";
    }

    /**
     * 注销
     * @return
     */
    @GetMapping("/logout")
    public String logout(HttpSession session){

        session.removeAttribute("user");
        session.setAttribute("msg","session移除，请重新登录");
        return "redirect:/user/login";
    }
    //================================================typeService====================================



    /**
     * 显示标签（type）列表
     * @param pn
     * @param model
     * @return
     */
    @GetMapping("/typepage")
    public String typePage(@RequestParam(value = "pn",defaultValue = "1")Integer pn,
                           Model model,
                           HttpSession session){
        User user = (User) session.getAttribute("user");
        QueryWrapper<Type> typeQueryWrapper = new QueryWrapper<>();
        QueryWrapper<Type> user_id = typeQueryWrapper.eq("user_id", user.getId());
        Page<Type> typePage = new Page<Type>(pn,5);
        Page<Type> page = typeService.page(typePage, user_id);
        Object msg = session.getAttribute("msg");
        model.addAttribute("msg",msg);
        model.addAttribute("page",page);
        model.addAttribute("user",user);
        session.removeAttribute("msg");
        session.removeAttribute("id");
        session.removeAttribute("pn");
        return "user/types";
    }

    /**
     * 新增type页面跳转
     * @return
     */
    @GetMapping("/addtype")
    public String addTypePage(HttpSession session,Model model){
        User user =(User) session.getAttribute("user");
        model.addAttribute("user",user);
        return "user/types-input";
    }

    /**
     * 新增type内容
     * @return
     */
    @PostMapping("/addtype")
    public String addType(Type type,HttpSession session,Model model)  {
        User user =(User) session.getAttribute("user");
        model.addAttribute("user",user);
        QueryWrapper<Type> typeQueryWrapper = new QueryWrapper<>();
        QueryWrapper<Type> user_id = typeQueryWrapper.eq("user_id", user.getId());
        List<Type> list = typeService.list(user_id);
//        log.info("type类型是：{}",type.getName());
        for (Type type1 : list) {
            if (type1.getName().equals(type.getName()) && type != null){
//                log.info("type1类型是：{}",type1.getName());
                session.setAttribute("msg","提示：操作失败！");
                return "redirect:/user/typepage";
            }
        }
        type.setUserId(user.getId());
        boolean save = typeService.save(type);
        session.setAttribute("msg","提示：操作成功！");
        return "redirect:/user/typepage";
    }

    /**
     * 删除type
     * @param id
     * @param pn
     * @param session
     * @param ra
     * @return
     */
    @GetMapping("/deletetype/{id}")
    public String deleteType(@PathVariable(value = "id")Integer id,
                             @RequestParam(value = "pn")Integer pn,
                             HttpSession session,
                             Model model,
                             RedirectAttributes ra){
        User user =(User) session.getAttribute("user");
        model.addAttribute("user",user);
        boolean b = typeService.removeById(id);
        ra.addAttribute("pn",pn);
        if(b){
            session.setAttribute("msg","提示：操作成功！");
            return "redirect:/user/typepage";
        }else {
            session.setAttribute("msg","提示：操作失败！");
            return "redirect:/user/typepage";
        }
    }

    /**
     * 修改类型
     * @param id
     * @param pn
     * @param session
     * @return
     */
    @GetMapping("/updatetype/{id}")
    public String updateTypePage(@PathVariable(value = "id")Integer id,
                                 @RequestParam(value = "pn") Integer pn,
                                 HttpSession session,
                                 Model model){
        User user =(User) session.getAttribute("user");
        model.addAttribute("user",user);
        Type byId = typeService.getById(id);
        model.addAttribute("byId",byId);
        session.setAttribute("id",id);
        session.setAttribute("pn",pn);
        return "user/types-update";
    }
    @PostMapping("/updatetype")
    public String updateType(@RequestParam(value = "name")String name, HttpSession session,Model model,RedirectAttributes ra){
        User user =(User) session.getAttribute("user");
        model.addAttribute("user",user);
        Integer id = (Integer) session.getAttribute("id");
        Integer pn = (Integer) session.getAttribute("pn");

        List<Type> list = typeService.list();
        for (Type type : list) {
            if(type.getName().equals(name)){
                session.setAttribute("msg","提示：修改失败！");
                ra.addAttribute("pn",pn);
                return "redirect:/user/typepage";
            }
        }

        Type type = new Type();
        type.setUserId(user.getId());
        type.setId(id);
        type.setName(name);
        typeService.saveOrUpdate(type);
        session.setAttribute("msg","提示：修改成功！");
        ra.addAttribute("pn",pn);
        return "redirect:/user/typepage";
    }


    //=============================================================firendLinnkService=====================

    /**
     * 友联首页
     * @param pn
     * @param model
     * @param session
     * @return
     */
    @GetMapping("/firendlinkpage")
    public String firendLinkPage(@RequestParam(value = "pn",defaultValue = "1")Integer pn,
                           Model model,
                           HttpSession session){

        User user =(User) session.getAttribute("user");
        model.addAttribute("user",user);
        Page<FirendLink> firendLinkPage = new Page<>(pn, 5);
        Page<FirendLink> page = firendLinnkService.page(firendLinkPage, null);
        Object msg = session.getAttribute("msg");
        model.addAttribute("msg",msg);
        model.addAttribute("page",page);
        session.removeAttribute("msg");
        session.removeAttribute("id");
        session.removeAttribute("pn");
        return "user/friendlinks";
    }

    /**
     * 添加友联
     * @return
     */
    @GetMapping("/addfirendlink")
    public String addFirendLinkPage(Model model,HttpSession session){
        User user =(User) session.getAttribute("user");
        model.addAttribute("user",user);
        model.addAttribute("operation","add");
        return "user/friendlinks-input";
    }


    @PostMapping("/addfirendlink")
    public String addFirendLink(FirendLink firendLink,HttpSession session,Model model)  {
        User user =(User) session.getAttribute("user");
        model.addAttribute("user",user);
        List<FirendLink> list = firendLinnkService.list();
        Calendar instance = Calendar.getInstance();
        for (FirendLink firendLink1 : list) {
            if (firendLink.getName().equals(firendLink1.getName()) && firendLink != null){
                session.setAttribute("msg","提示：添加失败！");
                return "redirect:/user/firendlinkpage";
            }
        }
//        log.info("进入了addFirendLink");
        firendLink.setCreateTime(instance.getTime());
        firendLinnkService.save(firendLink);
        session.setAttribute("msg","提示：添加成功！");

        return "redirect:/user/firendlinkpage";
    }

    /**
     * 删除友联
     * @param id
     * @param pn
     * @param session
     * @param ra
     * @return
     */
    @GetMapping("/deletefirendlink/{id}")
    public String deleteFirend(@PathVariable(value = "id")Integer id,
                             @RequestParam(value = "pn")Integer pn,
                             HttpSession session,
                             RedirectAttributes ra,
                               Model model){
        User user =(User) session.getAttribute("user");
        model.addAttribute("user",user);
        boolean b = firendLinnkService.removeById(id);
        ra.addAttribute("pn",pn);
        if(b){
            session.setAttribute("msg","提示：删除成功！");
            return "redirect:/user/firendlinkpage";
        }else {
            session.setAttribute("msg","提示：删除失败！");
            return "redirect:/user/firendlinkpage";
        }
    }

    /**
     * 修改友联
     * @param id
     * @param pn
     * @param session
     * @return
     */
    @GetMapping("/updatefirendpage/{id}")
    public String updateFirendPage(@PathVariable(value = "id")Integer id,
                                 @RequestParam(value = "pn") Integer pn,
                                 HttpSession session,
                                    Model model){
        User user =(User) session.getAttribute("user");
        model.addAttribute("user",user);
        session.setAttribute("id",id);
        session.setAttribute("pn",pn);
        FirendLink byId = firendLinnkService.getById(id);
        model.addAttribute("operation","update");
        model.addAttribute("byId",byId);
        return "user/friendlinks-input";
    }
    @PostMapping("/updatefirend")
    public String updateType(FirendLink firendLink, HttpSession session,Model model,RedirectAttributes ra){
        User user =(User) session.getAttribute("user");
        model.addAttribute("user",user);
        Integer id = (Integer) session.getAttribute("id");
        Integer pn = (Integer) session.getAttribute("pn");
        List<FirendLink> list = firendLinnkService.list();
        for (FirendLink link : list) {
            if(link.getName().equals(firendLink.getName())){
                session.setAttribute("msg","提示：修改失败！");
                ra.addAttribute("pn",pn);
                return "redirect:/user/firendlinkpage";
            }
        }
        Calendar instance = Calendar.getInstance();
        firendLink.setCreateTime(instance.getTime());
        firendLink.setId(id);
        firendLinnkService.saveOrUpdate(firendLink);
        session.setAttribute("msg","提示：修改成功！");
        ra.addAttribute("pn",pn);
        return "redirect:/user/firendlinkpage";
    }

    //===============================Blog-=======================

    @GetMapping("/blogpage")
    public String blogPage(@RequestParam(value = "pn",defaultValue = "1")Integer pn,
                           @RequestParam(value = "title",defaultValue = "1") String title,
                           Model model,
                           HttpSession session){
        User user =(User) session.getAttribute("user");
        model.addAttribute("user",user);
//        log.info("title的值：{}", title);
        if(!(title.equals("1"))) {
            QueryWrapper<Blog> blogQueryWrapper = new QueryWrapper<>();
            QueryWrapper<Blog> title1 = blogQueryWrapper.like("title",title).eq("user_id",user.getId());
//            Blog title1 = query.eq("title", title).one();
            Page<Blog> blogPage = new Page<>(pn, 5);
            Page<Blog> page = blogService.page(blogPage, title1);
            List<Blog> records = page.getRecords();
//            log.info("page是：{}",page);
            if(records.size() == 0){
                model.addAttribute("msg", "查询失败！请返回查询");
                model.addAttribute("page", page);
            }else {
                model.addAttribute("page", page);

                model.addAttribute("msg", "查询成功！");
//                log.info("blog不空");
            }

        }else {
            QueryWrapper<Blog> blogQueryWrapper = new QueryWrapper<>();
            QueryWrapper<Blog> user_id = blogQueryWrapper.eq("user_id", user.getId());
            Page<Blog> blogPage = new Page<>(pn, 5);
            Page<Blog> page = blogService.page(blogPage, user_id);
            List<Blog> records = page.getRecords();
//            log.info("blog空");
            Object msg = session.getAttribute("msg");
            model.addAttribute("msg", msg);
            model.addAttribute("page", page);
        }
        session.removeAttribute("msg");
        session.removeAttribute("id");
        session.removeAttribute("pn");
        return "user/blogs";
    }

    /**
     * 添加博客
     * @param model
     * @return
     */
    @GetMapping("/addblog")
    public String addBlogPage(Model model,HttpSession session){
        User user =(User) session.getAttribute("user");
        model.addAttribute("user",user);
        QueryWrapper<Type> typeQueryWrapper = new QueryWrapper<>();
        QueryWrapper<Type> user_id = typeQueryWrapper.eq("user_id", user.getId());
        List<Type> list = typeService.list(user_id);
        model.addAttribute("operation","add");
        model.addAttribute("type",list);
        return "user/blogs-input";
    }
    @PostMapping("/addblog")
    public String addBlog(Blog blog,HttpSession session,Model model){
        User user =(User) session.getAttribute("user");
        model.addAttribute("user",user);
        QueryWrapper<Blog> blogQueryWrapper = new QueryWrapper<>();
        QueryWrapper<Blog> user_id = blogQueryWrapper.eq("user_id", user.getId());
        List<Blog> list = blogService.list(user_id);

        String title = blog.getTitle();
        for (Blog blog1 : list) {
            if(blog1.getTitle().equals(title)){
                session.setAttribute("msg","提示：添加失败！");
                return "redirect:/user/blogpage";
            }
        }
        Calendar instance = Calendar.getInstance();
        Integer userid = (Integer) session.getAttribute("userid");
        blog.setContent(MarkdownUtils.markdownToHtml(blog.getContent()));
        blog.setUserId(userid);
        blog.setCreateTime(instance.getTime());
        blogService.save(blog);
        session.setAttribute("msg","提示：添加成功！");
        return "redirect:/user/blogpage";
    }

    /**
     * 删除blog
     * @param id
     * @param pn
     * @param session
     * @param ra
     * @return
     */
    @GetMapping("/delete/{id}")
    public String deleteBlog(@PathVariable(value = "id")Integer id,
                             @RequestParam("pn")Integer pn,
                             HttpSession session,
                             RedirectAttributes ra,
                             Model model){
        User user =(User) session.getAttribute("user");
        model.addAttribute("user",user);
        blogService.removeById(id);
        model.addAttribute("operation","update");
        ra.addAttribute("pn",pn);
        session.setAttribute("msg","删除成功！");
        return "redirect:/user/blogpage";
    }

    @GetMapping("/updateblogpage/{id}")
    public String updateBlogPage(@PathVariable(value = "id")Integer id,
                                 @RequestParam(value = "pn")Integer pn,
                                 HttpSession session,
                                 Model model){
        User user =(User) session.getAttribute("user");
        model.addAttribute("user",user);
        session.setAttribute("id",id);
        session.setAttribute("pn",pn);
        Blog byId = blogService.getById(id);
        model.addAttribute("byId",byId);
        QueryWrapper<Type> typeQueryWrapper = new QueryWrapper<>();
        QueryWrapper<Type> user_id = typeQueryWrapper.eq("user_id", user.getId());
        List<Type> list = typeService.list(user_id);
        model.addAttribute("type",list);
        return "user/blogs-input";
    }
    @PostMapping("/updateblog")
    public String updateBlog(Blog blog,HttpSession session,Model model,RedirectAttributes ra){
        User user =(User) session.getAttribute("user");
        model.addAttribute("user",user);
        model.addAttribute("operation","update");
        Integer id = (Integer) session.getAttribute("id");
        Integer pn = (Integer) session.getAttribute("pn");
        ra.addAttribute("pn",pn);
        Calendar instance = Calendar.getInstance();
        blog.setContent(MarkdownUtils.markdownToHtml(blog.getContent()));
        blog.setUpdateTime(instance.getTime());
        blog.setId(id);
//        log.info("id是：{}",id);
        session.setAttribute("msg","修改成功");
        blogService.saveOrUpdate(blog);
        return "redirect:/user/blogpage";
    }


    //=========================================标签tag(时间轴)===================================================



    /**
     * 显示标签（tag）列表
     * @param pn
     * @param model
     * @return
     */
    @GetMapping("/tagpage")
    public String tagPage(@RequestParam(value = "pn",defaultValue = "1")Integer pn,
                           Model model,
                           HttpSession session){
        User user =(User) session.getAttribute("user");
        model.addAttribute("user",user);
        QueryWrapper<Tag> tagQueryWrapper = new QueryWrapper<>();
        QueryWrapper<Tag> user_id = tagQueryWrapper.eq("user_id", user.getId());
        Page<Tag> tagPage = new Page<>(pn, 5);
        Page<Tag> page = tagService.page(tagPage, user_id);
        Object msg = session.getAttribute("msg");
        model.addAttribute("msg",msg);
        model.addAttribute("page",page);
        session.removeAttribute("msg");
        session.removeAttribute("id");
        session.removeAttribute("pn");
        return "user/tags";
    }

    /**
     * 新增tag页面跳转
     * @return
     */
    @GetMapping("/addtag")
    public String addTagPage(Model model,HttpSession session){
        User user =(User) session.getAttribute("user");
        model.addAttribute("user",user);
        model.addAttribute("operation","add");
        return "user/tags-input";
    }

    /**
     * tag
     * @return
     */
    @PostMapping("/addtag")
    public String addTag(Tag tag,HttpSession session,Model model)  {
        User user =(User) session.getAttribute("user");
        model.addAttribute("user",user);
        QueryWrapper<Tag> tagQueryWrapper = new QueryWrapper<>();
        QueryWrapper<Tag> user_id = tagQueryWrapper.eq("user_id", user.getId());
        List<Tag> list = tagService.list(user_id);
        for (Tag tag1 : list) {
            if (tag1.getName().equals(tag.getName()) && tag != null){
//                log.info("type1类型是：{}",tag.getName());
                session.setAttribute("msg","提示：添加失败！");
                return "redirect:/user/tagpage";
            }
        }
        Calendar instance = Calendar.getInstance();
        tag.setCreateTime(instance.getTime());
        tag.setUserId(user.getId());

        tagService.save(tag);
        session.setAttribute("msg","提示：添加成功！");
        return "redirect:/user/tagpage";
    }

    /**
     * tag
     * @param id
     * @param pn
     * @param session
     * @param ra
     * @return
     */
    @GetMapping("/deletetag/{id}")
    public String deleteTag(@PathVariable(value = "id")Integer id,
                             @RequestParam(value = "pn")Integer pn,
                             HttpSession session,
                             Model model,
                             RedirectAttributes ra){
        User user =(User) session.getAttribute("user");
        model.addAttribute("user",user);
        boolean b = tagService.removeById(id);
        ra.addAttribute("pn",pn);
        if(b){
            session.setAttribute("msg","提示：删除成功！");
            return "redirect:/user/tagpage";
        }else {
            session.setAttribute("msg","提示：删除失败！");
            return "redirect:/user/tagpage";
        }
    }

    /**
     * 修改tag
     * @param id
     * @param pn
     * @param session
     * @return
     */
    @GetMapping("/updatetag/{id}")
    public String updateTypeTag(@PathVariable(value = "id")Integer id,
                                 @RequestParam(value = "pn") Integer pn,
                                 HttpSession session,
                                 Model model){
        User user =(User) session.getAttribute("user");
        model.addAttribute("user",user);
        Tag byId = tagService.getById(id);
        model.addAttribute("byId",byId);
        model.addAttribute("operation","update");
        session.setAttribute("id",id);
        session.setAttribute("pn",pn);
        return "user/tags-input";
    }
    @PostMapping("/updatetag")
    public String updateTag(Tag tag,HttpSession session,Model model,RedirectAttributes ra){
        User user =(User) session.getAttribute("user");
        model.addAttribute("user",user);
        Integer id = (Integer) session.getAttribute("id");
        Integer pn = (Integer) session.getAttribute("pn");
        Calendar instance = Calendar.getInstance();
        tag.setCreateTime(instance.getTime());
        tag.setId(id);
        tag.setUserId(user.getId());
        tagService.saveOrUpdate(tag);

        session.setAttribute("msg","提示：修改成功！");
        ra.addAttribute("pn",pn);
        return "redirect:/user/tagpage";
    }

    //=============================================图片墙=======================

    /**
     * 显示图片
     * @return
     */
    @GetMapping("/picture")
    public String picture(@RequestParam(value = "pn",defaultValue = "1")Integer pn,
                          Model model,
                          HttpSession session){
        User user =(User) session.getAttribute("user");
        model.addAttribute("user",user);
        Page<Picture> picturePage = new Page<>(pn, 5);
        Page<Picture> page = pictureService.page(picturePage, null);

        Object msg = session.getAttribute("msg");
        model.addAttribute("msg",msg);
        model.addAttribute("page",page);
        session.removeAttribute("msg");
        session.removeAttribute("id");
        session.removeAttribute("pn");
        return "user/pictures";
    }

    /**
     * 添加图片
     * @param model
     * @return
     */
    @GetMapping("/addpicture")
    public String addpicturePage(Model model,HttpSession session){
        User user =(User) session.getAttribute("user");
        model.addAttribute("user",user);
        model.addAttribute("operation","add");
        return "user/pictures-input";
    }

    @PostMapping("/addpicture")
    public String addpicture(Picture picture,HttpSession session,Model model){
        User user =(User) session.getAttribute("user");
        model.addAttribute("user",user);

        pictureService.saveOrUpdate(picture);
        session.setAttribute("msg","提示：添加成功！");
        return "redirect:/user/picture";
    }

    /**
     * 删除picture
     * @param id
     * @param pn
     * @param session
     * @param ra
     * @return
     */
    @GetMapping("/deletepicture/{id}")
    public String deletePictureById(@PathVariable(value = "id")Integer id,
                                    @RequestParam(value = "pn")Integer pn,
                                    HttpSession session,
                                    Model model,
                                    RedirectAttributes ra) {
        User user =(User) session.getAttribute("user");
        model.addAttribute("user",user);
        boolean b = pictureService.removeById(id);
        ra.addAttribute("pn", pn);
        if (b) {
            session.setAttribute("msg", "提示：删除成功！");
            return "redirect:/user/picture";
        } else {
            session.setAttribute("msg", "提示：删除失败！");
            return "redirect:/user/picture";
        }
    }

    @GetMapping("/updatepicture/{id}")
    public String updatePicturePage(@PathVariable(value = "id")Integer id,
                                @RequestParam(value = "pn") Integer pn,
                                HttpSession session,
                                Model model){
        User user =(User) session.getAttribute("user");
        model.addAttribute("user",user);
        Picture byId = pictureService.getById(id);
        model.addAttribute("byId",byId);
        model.addAttribute("operation","update");
        session.setAttribute("id",id);
        session.setAttribute("pn",pn);
        return "user/pictures-input";
    }
    @PostMapping("/updatepicture")
    public String updatePicture(Picture picture,HttpSession session,Model model,RedirectAttributes ra){
        User user =(User) session.getAttribute("user");
        model.addAttribute("user",user);
        Integer id = (Integer) session.getAttribute("id");
        Integer pn = (Integer) session.getAttribute("pn");
        picture.setId(id);
        pictureService.saveOrUpdate(picture);
        session.setAttribute("msg","提示：修改成功！");
        ra.addAttribute("pn",pn);
        return "redirect:/user/picture";
    }
}

