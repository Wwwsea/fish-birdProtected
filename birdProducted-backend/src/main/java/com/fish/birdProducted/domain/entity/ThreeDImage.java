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
 * @date: 2024/3/29-17:18
 * @content:
 */

@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_threed_image")
public class ThreeDImage implements BaseData {

    private Long id;
    // 文章标签
    private String articleId;

    // 3D模型存放地址
    private String imageThreeD;
    //创建时间
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    //更新时间
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
    //是否删除（0：未删除，1：已删除）
    private Integer isDeleted;
}
