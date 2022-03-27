package com.kingren.blog.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_picture")
public class Picture {
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    private String name;
    private String pictureTime;  //时间地点
    private String pictureAddress;  //图片地址
    private String description;  //描述

}
