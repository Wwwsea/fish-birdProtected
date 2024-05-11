package com.fish.birdProducted.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fish.birdProducted.domain.entity.RoleMenu;
import com.fish.birdProducted.mapper.RoleMenuMapper;
import com.fish.birdProducted.service.RoleMenuService;
import org.springframework.stereotype.Service;

/**
 * (RoleMenu)表服务实现类
 *
 * @author fish
 * @since 2023-11-28 10:23:17
 */
@Service("roleMenuService")
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuMapper, RoleMenu> implements RoleMenuService {

}
