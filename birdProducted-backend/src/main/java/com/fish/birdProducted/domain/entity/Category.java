package com.fish.birdProducted.domain.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fish.birdProducted.domain.BaseData;


/**
 * (Category)表实体类
 *
 * @author fish
 * @since 2023-10-15 02:29:13
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_category")
public class Category implements BaseData {
    //分类id
    private Long id;
    //分类名
    private String categoryName;
    //创建时间
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    //更新时间
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
    //是否删除（0：未删除，1：已删除）
    private Integer isDeleted;
}

