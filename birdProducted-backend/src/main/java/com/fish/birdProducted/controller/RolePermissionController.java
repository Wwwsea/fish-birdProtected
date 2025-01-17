package com.fish.birdProducted.controller;

import com.fish.birdProducted.domain.dto.RolePermissionDTO;
import com.fish.birdProducted.domain.response.ResponseResult;
import com.fish.birdProducted.domain.vo.RoleAllVO;
import com.fish.birdProducted.service.RolePermissionService;
import com.fish.birdProducted.utils.ControllerUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.fish.birdProducted.annotation.AccessLimit;
import com.fish.birdProducted.annotation.LogAnnotation;
import com.fish.birdProducted.constants.LogConst;

import java.util.List;

/**
 * @author fish
 * <p>
 * 创建时间：2024/2/7 14:53
 */
@RestController
@Tag(name = "用户角色相关接口")
@RequestMapping("role/permission")
public class RolePermissionController {

    @Resource
    private RolePermissionService rolePermissionService;

    @PreAuthorize("hasAnyAuthority('system:permission:role:list')")
    @Parameters({
            @Parameter(name = "permissionId", description = "权限id"),
            @Parameter(name = "roleName", description = "角色名称"),
            @Parameter(name = "roleKey", description = "角色键")
    })
    @AccessLimit(seconds = 60, maxCount = 30)
    @Operation(summary = "查询权限的角色列表")
    @GetMapping("/role/list")
    public ResponseResult<List<RoleAllVO>> selectPermissionIdRole(
            @NotNull(message = "角色id不能为空") Long permissionId,
            @RequestParam(required = false, name = "roleName") String roleName,
            @RequestParam(required = false, name = "roleKey") String roleKey
    ) {
        return ControllerUtils.messageHandler(() -> rolePermissionService.selectRoleByPermissionId(permissionId,roleName,roleKey,0));
    }

    @PreAuthorize("hasAnyAuthority('system:permission:role:not:list')")
    @Parameters({
            @Parameter(name = "permissionId", description = "权限id"),
            @Parameter(name = "roleName", description = "角色名称"),
            @Parameter(name = "roleKey", description = "角色键")
    })
    @AccessLimit(seconds = 60, maxCount = 30)
    @Operation(summary = "查询没有该权限的角色列表")
    @GetMapping("/not/role/list")
    public ResponseResult<List<RoleAllVO>> selectPermissionNotRole(
            @NotNull(message = "角色id不能为空") Long permissionId,
            @RequestParam(required = false, name = "roleName") String roleName,
            @RequestParam(required = false, name = "roleKey") String roleKey
    ) {
        return ControllerUtils.messageHandler(() -> rolePermissionService.selectRoleByPermissionId(permissionId,roleName,roleKey,1));
    }

    @Operation(summary = "添加角色权限关系")
    @PreAuthorize("hasAnyAuthority('system:permission:role:add')")
    @AccessLimit(seconds = 60, maxCount = 30)
    @Parameters({
            @Parameter(name = "rolePermissionDTO", description = "添加的数据")
    })
    @LogAnnotation(module="角色权限",operation= LogConst.GRANT)
    @PostMapping("/add")
    public ResponseResult<Void> addRolePermission(@Valid @RequestBody RolePermissionDTO rolePermissionDTO) {
        return rolePermissionService.addRolePermission(rolePermissionDTO);
    }

    @Operation(summary = "删除角色权限关系")
    @PreAuthorize("hasAnyAuthority('system:permission:role:delete')")
    @AccessLimit(seconds = 60, maxCount = 30)
    @Parameters({
            @Parameter(name = "rolePermissionDTO", description = "删除的所需数据")
    })
    @LogAnnotation(module="角色权限",operation= LogConst.DELETE)
    @DeleteMapping("/delete")
    public ResponseResult<Void> deleteRolePermission(@Valid @RequestBody RolePermissionDTO rolePermissionDTO) {
        return rolePermissionService.deleteRolePermission(rolePermissionDTO);
    }

}
