package com.fish.birdProducted.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fish.birdProducted.domain.dto.RolePermissionDTO;
import com.fish.birdProducted.domain.entity.Role;
import com.fish.birdProducted.domain.entity.RolePermission;
import com.fish.birdProducted.domain.response.ResponseResult;
import com.fish.birdProducted.domain.vo.RoleAllVO;
import com.fish.birdProducted.mapper.RoleMapper;
import com.fish.birdProducted.mapper.RolePermissionMapper;
import com.fish.birdProducted.service.RolePermissionService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * (RolePermission)表服务实现类
 *
 * @author fish
 * @since 2023-10-13 15:02:40
 */
@Service("rolePermissionService")
public class RolePermissionServiceImpl extends ServiceImpl<RolePermissionMapper, RolePermission> implements RolePermissionService {

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private RolePermissionMapper rolePermissionMapper;

    @Override
    public List<RoleAllVO> selectRoleByPermissionId(Long permissionId, String roleName, String roleKey, Integer type) {
        LambdaQueryWrapper<RolePermission> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RolePermission::getPermissionId, permissionId);
        List<Long> roleIds;
        if (type == 0) {
            // 没有使用该权限的角色
            roleIds = rolePermissionMapper.selectList(wrapper).stream().map(RolePermission::getRoleId).toList();
        } else {
            // 查询存在权限的角色id
            roleIds = rolePermissionMapper.selectList(wrapper).stream().map(RolePermission::getRoleId).toList();
            // 查询没有该权限的角色
            roleIds = roleMapper.selectList(new LambdaQueryWrapper<Role>().notIn(!roleIds.isEmpty(), Role::getId, roleIds))
                    .stream().map(Role::getId).toList();
        }

        if (!roleIds.isEmpty()) {
            LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();
            List<Role> roleList = new ArrayList<>();
            // 查询角色信息
            roleIds.forEach(roleId -> {
                if (Objects.nonNull(roleName)) queryWrapper.like(Role::getRoleName, roleName);
                if (Objects.nonNull(roleKey)) queryWrapper.and(a -> a.like(Role::getRoleKey, roleKey));
                queryWrapper.eq(Role::getId, roleId);
                roleList.addAll(roleMapper.selectList(queryWrapper));
                queryWrapper.clear();
            });
            return roleList.stream().map(role -> role.asViewObject(RoleAllVO.class)).toList();
        }
        return null;
    }

    @Transactional
    @Override
    public ResponseResult<Void> addRolePermission(RolePermissionDTO rolePermissionDTO) {
        List<Long> roleIds = rolePermissionDTO.getRoleId();
        List<Long> permissionIds = rolePermissionDTO.getPermissionId();
        LambdaQueryWrapper<RolePermission> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(RolePermission::getPermissionId, permissionIds)
                .in(RolePermission::getRoleId, roleIds);
        // 如有，先删除
        if (rolePermissionMapper.selectCount(wrapper) > 0) rolePermissionMapper.delete(wrapper);

        List<RolePermission> rolePermissions = new ArrayList<>();
        roleIds.forEach(roleId -> {
            permissionIds.forEach(permissionId -> {
                rolePermissions.add(RolePermission.builder().roleId(roleId).permissionId(permissionId).build());
            });
        });
        if (saveBatch(rolePermissions)) return ResponseResult.success();

        return ResponseResult.failure();
    }

    @Override
    public ResponseResult<Void> deleteRolePermission(RolePermissionDTO rolePermissionDTO) {
        int isDelete = rolePermissionMapper.delete(new LambdaQueryWrapper<RolePermission>().in(RolePermission::getPermissionId, rolePermissionDTO.getPermissionId()).in(RolePermission::getRoleId, rolePermissionDTO.getRoleId()));
        if (isDelete > 0) {
            return ResponseResult.success();
        }
        return ResponseResult.failure();
    }
}
