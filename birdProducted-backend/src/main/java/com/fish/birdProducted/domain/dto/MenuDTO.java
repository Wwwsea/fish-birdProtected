package com.fish.birdProducted.domain.dto;

import lombok.Data;
import com.fish.birdProducted.domain.BaseData;

import java.util.List;

/**
 * @author fish
 * <p>
 * 创建时间：2023/2/23 12:05
 */
@Data
public class MenuDTO implements BaseData {
    private Long id;
    private Long parentId;
    private String title;
    private List<Long> roleId;
    private Integer orderNum;
    private String icon;
    private Integer routerType;
    private String component;
    private String redirect;
    private String path;
    private String url;
    private String target;
    private Integer affix;
    private Integer keepAlive;
    private Integer hideInMenu;
    private Integer isDisable;
}
