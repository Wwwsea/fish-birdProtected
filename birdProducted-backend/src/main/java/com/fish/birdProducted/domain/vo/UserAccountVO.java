package com.fish.birdProducted.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import com.fish.birdProducted.domain.BaseData;

import java.util.List;


/**
 * @author fish
 * <p>
 * 创建时间：2023/10/14 16:31
 * 用户账户VO
 */
@Data
@Schema(name = "UserAccountVO", description = "前台用户账户VO")
public class UserAccountVO implements BaseData {
    //用户昵称
    @Schema(description = "用户昵称")
    private String nickname;
    //用户名
    @Schema(description = "用户名")
    private String username;
    @Schema(description = "用户类型")
    //用户注册方式(1邮箱/姓名 2Gitee 3Github)
    private Integer registerType;
    //用户头像
    @Schema(description = "用户头像")
    private String avatar;
    //个人简介
    @Schema(description = "个人简介")
    private String intro;
    //用户邮箱
    @Schema(description = "用户邮箱")
    private String email;
    // 账号角色
    @Schema(description = "用户角色")
    private List<String> roles;
    // 账号权限
    @Schema(description = "用户权限")
    private List<String> permissions;
}
