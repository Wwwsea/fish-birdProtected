package com.fish.birdProducted.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fish.birdProducted.domain.dto.RolePermissionDTO;
import com.fish.birdProducted.domain.entity.RolePermission;
import com.fish.birdProducted.domain.response.ResponseResult;
import com.fish.birdProducted.domain.vo.RoleAllVO;

import java.util.List;


/**
 * (RolePermission)表服务接口
 *
 * @author fish
 * @since 2023-10-13 15:02:40
 */
public interface RolePermissionService extends IService<RolePermission> {

    /**
     * 所有使用该权限的角色
     *
     * @param permissionId 权限id
     * @param roleName 角色名称
     * @param roleKey 角色字符
     * @param type 角色类型,0：该角色的使用用户 1：该角色的未使用用户
     * @return 角色列表
     */
    List<RoleAllVO> selectRoleByPermissionId(Long permissionId, String roleName, String roleKey,Integer type);

    /**
     * 给多个角色添加某个权限
     * @param rolePermissionDTO 角色权限数据
     * @return 是否成功
     */
    ResponseResult<Void> addRolePermission(RolePermissionDTO rolePermissionDTO);

    /**
     * 批量或单个取消授权
     * @param rolePermissionDTO 角色权限数据
     * @return 是否成功
     */
    ResponseResult<Void> deleteRolePermission(RolePermissionDTO rolePermissionDTO);
}
