package com.github.plexpt.lolicon.lolicon.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

/**
 * <p>
 *
 * </p>
 *
 * @author plexpt
 * @since 2022-05-24
 */
@Data
@TableName("t_setu")
public class Setu {


    @TableId(value = "pid", type = IdType.INPUT)
    private Long pid;

    private Long p;

    private Long uid;

    private String title;

    private String author;

    private String r18;

    private Long width;

    private Long height;

    private String tags;

    private String ext;

    private String uploadDate;

    private String urls;

    Integer dl;


}
