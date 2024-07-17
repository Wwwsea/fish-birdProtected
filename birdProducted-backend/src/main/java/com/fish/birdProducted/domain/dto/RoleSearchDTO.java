package com.fish.birdProducted.domain.dto;

import lombok.Data;

import java.util.Date;

/**
 * @author fish
 * <p>
 * 创建时间：2024/2/4 10:18
 */
@Data
public class RoleSearchDTO {
    // 角色名称
    private String roleName;
    //角色字符
    private String roleKey;
    // 状态（0：正常，1：停用）
    private Integer status;
    //创建时间，开始时间
    private Date createTimeStart;
    //创建时间，结束时间
    private Date createTimeEnd;
}
