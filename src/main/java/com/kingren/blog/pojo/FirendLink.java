package com.kingren.blog.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_firendLink")
public class FirendLink {
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    private String name;
    private String blogAddress;
    private String photoAddress;
    private Date createTime;

}
