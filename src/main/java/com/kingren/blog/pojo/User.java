package com.kingren.blog.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_user")
public class User {
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;

    private String nickname;
    private String username;
    private String password;
    private String email;
    private String avatar; //头像
    private String type;

//    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createTime;
//    @JsonFormat(pattern="yyyy-MM-dd HH:mm")
    private Date updateTime;



//    private List<Blog> blogs = new ArrayList<>();
}
