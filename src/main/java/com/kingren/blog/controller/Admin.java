package com.kingren.blog.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kingren.blog.pojo.User;
import com.kingren.blog.service.AdminService;
import com.kingren.blog.service.UserService;
import com.kingren.blog.utils.MD5Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/admin")
public class Admin {

    @Autowired
    AdminService adminService;

    @Autowired
    UserService userService;

    @GetMapping("/login")
    public String loginAdmin(){

        return "admin/login";
    }


    /**
     * 管理员登录
     * @param username
     * @param password
     * @param
     * @return
     */
    @PostMapping("/login")
    public String loginSuccess(@RequestParam(value = "username")String username,
                               @RequestParam(value = "password")String password,
                               Model model,
                               HttpSession session){

        List<com.kingren.blog.pojo.Admin> list = adminService.list();
        for (com.kingren.blog.pojo.Admin admin : list) {
            if(username.equals(admin.getAdmin()) && MD5Utils.getMD5(password).equals(admin.getPassword())){
                session.setAttribute("adminid",admin.getId());
                return "redirect:/admin/user_table";
            }
        }
        model.addAttribute("msg","用户名和密码错误,请重新登录");
        return "admin/login";
    }

    /**
     * 管理员信息修改
     * @return
     */
    @GetMapping("/updateadmin")
    public String updateadmin(){
        return "admin/updateadmin";
    }
    @PostMapping("/updateadmin")
    public String updateadminPost(com.kingren.blog.pojo.Admin admin, HttpSession session,Model model){
        Integer adminid = (Integer) session.getAttribute("adminid");
        com.kingren.blog.pojo.Admin byId = adminService.getById(adminid);
        admin.setPassword(MD5Utils.getMD5(admin.getPassword()));
        byId = admin;
        byId.setId(adminid);
        adminService.saveOrUpdate(byId);
        model.addAttribute("msg","密码成功修改，请重新的登录!");
        return "admin/login";
    }


    /**
     * 展示用户
     * @param model
     * @return
     */
    @GetMapping("/user_table")
    public String user_table(Model model){
        Page<User> userPage = new Page<User>();
        Page<User> page = userService.page(userPage, null);
        model.addAttribute("page",page);
        return "admin/user_table";
    }

    /**
     * 删除用户
     */
    @GetMapping("/deleteuser/{id}")
    public String deleteUser(@PathVariable(value = "id") Integer id){
        userService.removeById(id);

        return "redirect:/admin/user_table";
    }

    /**
     * 修改用户（跳转）
     * @param id
     * @param session
     * @return
     */
    @GetMapping("/updateuser/{id}")
    public String updataUser(@PathVariable(value = "id") Integer id,
                             HttpSession session){
        session.setAttribute("id",id);
        return "admin/update";
    }

    /**
     * 修改用户
     * @param user
     * @param
     * @param session
     * @return
     * @throws IOException
     */
    @PostMapping("/updateuser")
    public String updateUser(User user,
                             HttpSession session)  {
        //获取日期
        Calendar instance = Calendar.getInstance();
        Integer id =(Integer) session.getAttribute("id");
        User byId = userService.getById(id);
        user.setPassword(MD5Utils.getMD5(user.getPassword()));
        byId=user;
        byId.setId(id);
        byId.setUpdateTime(instance.getTime());  //现在时间
        userService.saveOrUpdate(byId);
        return "redirect:/admin/user_table";
    }

    /**
     * 添加用户
     * @return
     */
    @GetMapping("/add")
    public String add(){
        return "admin/add";
    }
    @PostMapping("/add")
    public String addUser(User user)  {
        //获取日期
        Calendar instance = Calendar.getInstance();
        User user1 = new User();

        user.setPassword(MD5Utils.getMD5(user.getPassword()));

        user1 = user;
        user1.setCreateTime(instance.getTime());
        boolean save = userService.save(user1);

        return "redirect:/admin/user_table";

    }
}
