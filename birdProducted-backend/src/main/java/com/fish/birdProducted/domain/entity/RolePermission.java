package com.fish.birdProducted.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * (Permission)表实体类
 *
 * @author fish
 * @since 2024-2-13 15:02:40
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("sys_role_permission")
public class RolePermission {
    //关系表id
    private Integer id;
    //角色id
    @TableId
    private Long roleId;
    //权限id
    private Long permissionId;
}

