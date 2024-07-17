package com.fish.birdProducted.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fish.birdProducted.domain.dto.UserRegisterDTO;
import com.fish.birdProducted.domain.dto.UserResetConfirmDTO;
import com.fish.birdProducted.domain.dto.UserResetPasswordDTO;
import com.fish.birdProducted.domain.dto.UserSearchDTO;
import org.springframework.security.core.userdetails.UserDetailsService;
import com.fish.birdProducted.domain.entity.User;
import com.fish.birdProducted.domain.response.ResponseResult;
import com.fish.birdProducted.domain.vo.UserAccountVO;
import com.fish.birdProducted.domain.vo.UserDetailsVO;
import com.fish.birdProducted.domain.vo.UserListVO;

import java.util.List;


/**
 * (User)表服务接口
 *
 * @author fish
 * @since 2024-2-10 19:33:43
 */
public interface UserService extends IService<User>, UserDetailsService {
    /**
     * 根据用户名或者密码查询用户
     *
     * @param text 用户名或者邮箱
     * @return 用户信息
     */
    User findAccountByNameOrEmail(String text);

    /**
     * 根据用户id查询用户信息
     *
     * @param id 用户id
     * @return 用户信息
     */
    UserAccountVO findAccountById(Long id);

    /**
     * 用户登录状态
     * @param id 用户id
     */
    void userLoginStatus(Long id);

    /**
     * 用户注册
     * @param userRegisterDTO 参数
     * @return 是否成功
     */
    ResponseResult<Void> userRegister(UserRegisterDTO userRegisterDTO);

    /**
     * 用户重置密码，步骤一
     * @param userResetDTO 参数
     * @return 是否成功
     */
    ResponseResult<Void> userResetConfirm(UserResetConfirmDTO userResetDTO);

    /**
     * 重置密码，已经确认邮箱
     * @param userResetDTO 参数
     * @return 是否成功
     */
    ResponseResult<Void> userResetPassword(UserResetPasswordDTO userResetDTO);

    /**
     * 获取所有的用户
     * @param userSearchDTO 查询条件
     * @return 用户列表
     */
    List<UserListVO> getUserOrSearch(UserSearchDTO userSearchDTO);

    /**
     * 修改用户状态
     * @param id 用户id
     * @param status 状态
     * @return 是否成功
     */
    ResponseResult<Void> updateStatus(Long id, Integer status);

    /**
     * 查看用户详情
     * @param id 用户id
     * @return 用户信息
     */
    UserDetailsVO findUserDetailsById(Long id);

    /**
     * 删除用户
     * @param ids 用户id
     * @return 是否成功
     */
    ResponseResult<Void> deleteUser(List<Long> ids);
}
