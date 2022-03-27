package com.kingren.blog.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_comment")
public class Comment {
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    private String nicename;
    private String email;
    private String content;
    private String avatar;  //头像
    private Date createTime;


//    private Blog blog;
//    private List<Comment> replyComments = new ArrayList<>();
//    private Comment parentComments;
}
