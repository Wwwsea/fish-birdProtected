package com.fish.birdProducted.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * (UserRole)表实体类
 *
 * @author fish
 * @since 2024-3-17 16:33:52
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("sys_user_role")
public class UserRole {
    //主键
    private Long id;
    //用户id
    private Long userId;
    //角色id
    private Long roleId;
}

