package com.kingren.blog.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_type")
public class Type {
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    private String name;
    private Integer userId;

//    private List<Blog> blogs = new ArrayList<>();
}
