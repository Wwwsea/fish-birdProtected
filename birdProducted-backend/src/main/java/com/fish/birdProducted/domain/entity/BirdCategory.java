package com.fish.birdProducted.domain.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fish.birdProducted.domain.BaseData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author:fish
 * @date: 2024/3/15-14:42
 * @content:
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("b_category")
public class BirdCategory implements BaseData {
    //分类id
    private Long id;
    //分类名
    @TableField(value = "category_name")
    private String categoryName;
    //创建时间
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    //更新时间
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
    //是否删除（0：未删除，1：已删除）
    private Integer isDeleted;
    // 所属生物分支
    @TableField(value = "biology_branch")
    private String biologyBranch;
    // 所属父id
    @TableField(value = "parent_id")
    private int parentId;
}
