package com.fish.birdProducted.domain.vo;

import lombok.Data;

import java.util.List;

/**
 * @author fish
 * <p>
 * 创建时间：2024/2/1 16:18
 */
@Data
public class RoleByIdVO {
    //角色id
    private Long id;
    // 角色名称
    private String roleName;
    //角色字符
    private String roleKey;
    // 顺序
    private Long orderNum;
    // 备注
    private String remark;
    // 状态（0：正常，1：停用）
    private Integer status;
    // 角色拥有的菜单权限
    private List<Long> menuIds;
}
