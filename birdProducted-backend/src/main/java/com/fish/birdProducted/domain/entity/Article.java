package com.fish.birdProducted.domain.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fish.birdProducted.domain.BaseData;

import java.util.Date;


/**
 * (Article)表实体类
 *
 * @author fish
 * @since 2024-2-15 02:38:48
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@TableName("t_article")
public class Article implements BaseData {
    //文章id
    private Long id;
    //作者id
    private Long userId;
    //分类id
    private Long categoryId;
    //文章缩略图
    private String articleCover;
    //文章标题
    private String articleTitle;
    //文章内容
    private String articleContent;
    //类型 (1现存 2濒危 3灭绝)
    private Integer articleType;
    //是否置顶 (0否 1是）
    private Integer isTop;
    //文章状态 (1公开 2私密 3草稿)
    private Integer status;
    //访问量
    private Long visitCount;
    //文章创建时间
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    //文章更新时间
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
    //是否删除（0：未删除，1：已删除）
    private Integer isDeleted;
    // 灭绝时间

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date extinctionDate;
}

