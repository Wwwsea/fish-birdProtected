package com.fish.birdProducted.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fish.birdProducted.domain.BaseData;


/**
 * (RoleMenu)表实体类
 *
 * @author fish
 * @since 2024-3-28 10:23:15
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("sys_role_menu")
public class RoleMenu implements BaseData {
    //主键
    private Integer id;
    //角色id
    @TableId
    private Long roleId;
    //菜单id
    private Long menuId;

    public RoleMenu(Long roleId, Long menuId) {
        this.roleId = roleId;
        this.menuId = menuId;
    }
}

