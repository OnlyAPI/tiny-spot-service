package com.yifan.admin.api.model.dto;

import com.yifan.admin.api.model.node.SysMenuNode;
import lombok.Data;

import java.util.List;

/**
 * @author TaiYi
 * @ClassName
 * @date 2023/2/14 11:13
 */
@Data
public class LoginDTO {

    private String token;

    private List<SysMenuNode> menus;

}
