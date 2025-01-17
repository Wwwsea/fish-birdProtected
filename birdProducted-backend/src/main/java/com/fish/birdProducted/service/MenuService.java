package com.fish.birdProducted.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fish.birdProducted.domain.dto.MenuDTO;
import com.fish.birdProducted.domain.entity.Menu;
import com.fish.birdProducted.domain.response.ResponseResult;
import com.fish.birdProducted.domain.vo.MenuByIdVO;
import com.fish.birdProducted.domain.vo.MenuVO;

import java.util.List;


/**
 * (Menu)表服务接口
 *
 * @author fish
 * @since 2024-3-17 22:15:22
 */
public interface MenuService extends IService<Menu> {

    /**
     * 获取菜单列表,0:系统菜单，1:菜单管理
     *
     * @return 菜单列表
     */
    ResponseResult<List<MenuVO>> getMenuList(Integer typeId, String username, Integer status);

    /**
     * 添加菜单
     *
     * @param menuDTO 菜单信息
     * @return 添加菜单的结果
     */
    ResponseResult<Void> addMenu(MenuDTO menuDTO);

    /**
     * 修改菜单，id回滚
     *
     * @param id 菜单id
     * @return 数据
     */
    ResponseResult<MenuByIdVO> selectUpdateMenu(Long id);

    /**
     * 修改菜单
     *
     * @param menuDTO 菜单信息
     * @return 是否成功
     */
    ResponseResult<Void> updateMenu(MenuDTO menuDTO);

    /**
     * 根据id删除菜单
     *
     * @param id 对应的id
     * @return 是否成功
     */
    ResponseResult<String> deleteMenu(Long id);

}
