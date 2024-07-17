package com.fish.birdProducted.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fish.birdProducted.domain.dto.RoleDTO;
import com.fish.birdProducted.domain.dto.RoleSearchDTO;
import com.fish.birdProducted.domain.entity.Role;
import com.fish.birdProducted.domain.response.ResponseResult;
import com.fish.birdProducted.domain.vo.RoleAllVO;
import com.fish.birdProducted.domain.vo.RoleByIdVO;
import com.fish.birdProducted.domain.vo.RoleVO;

import java.util.List;


/**
 * (Role)表服务接口
 *
 * @author fish
 * @since 2024-2-13 15:02:40
 */
public interface RoleService extends IService<Role> {
    /**
     * 查询所有的角色
     * @return 所有角色
     */
    ResponseResult<List<RoleVO>> selectAll();

    /**
     * 查询所有角色的所有信息
     * @return 所有角色的所有信息（与第一个返回的信息不同）
     */
    List<RoleAllVO> selectRole(RoleSearchDTO roleSearchDTO);

    /**
     * 根据角色id修改角色状态
     * @param id 角色id
     * @param status 角色状态
     * @return 是否成功
     */
    ResponseResult<Void> updateStatus(Long id, Integer status);


    /**
     * 修改时信息回滚
     * @param id 角色id
     * @return 回滚的信息
     */
    ResponseResult<RoleByIdVO> selectRoleById(Long id);

    /**
     * 修改或添加角色信息
     *
     * @param roleDTO 角色信息
     * @return 是否成功
     */
    ResponseResult<Void> updateOrInsertRole(RoleDTO roleDTO);

    /**
     * 删除角色
     * @param ids 角色ids
     * @return 是否成功
     */
    ResponseResult<Void> deleteRole(List<Long> ids);

}
