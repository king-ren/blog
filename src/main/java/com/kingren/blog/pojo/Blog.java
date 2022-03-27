package com.kingren.blog.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_blog")
public class Blog {
   @TableId(value = "id",type = IdType.AUTO)
   private Integer id;
   private boolean  appreciation;
   private boolean  commentabled;
   private String content;
   private Date createTime;
   private String firstPicture;
   private String flag;   //原创，转载
   private boolean published;  //保存或发布
   private boolean recommend;   //推荐
   private boolean shareStatement;
   private String title;
   private Date updateTime;
   private Integer views;
   private Integer typeId;
   private Integer userId;
   private String  description;


//   private  Type type;
//   private List<Tag> tags = new ArrayList<>();
//   private User user;
//   private List<Comment> comments = new ArrayList<>();

}
