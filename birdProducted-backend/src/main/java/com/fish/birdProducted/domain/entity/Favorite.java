package com.fish.birdProducted.domain.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fish.birdProducted.domain.BaseData;

import java.util.Date;


/**
 * (Favorite)表实体类
 *
 * @author fish
 * @since 2024-2-18 14:12:23
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_favorite")
public class Favorite implements BaseData {
    //收藏id
    private Long id;
    //收藏的用户id
    private Long userId;
    //收藏类型(1,文章 2,留言板)
    private Integer type;
    //类型id
    private Long typeId;
    // 是否有效 (0否 1是)
    private Integer isCheck;
    //收藏时间
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
}

