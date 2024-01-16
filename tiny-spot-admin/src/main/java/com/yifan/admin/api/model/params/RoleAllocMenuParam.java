package com.yifan.admin.api.model.params;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 角色分配菜单参数
 * @author TaiYi
 * @ClassName
 * @date 2023/2/16 15:09
 */
@Data
public class RoleAllocMenuParam {

    @NotNull(message = "invalid args [roleId]")
    private Long roleId;

    @NotEmpty(message = "invalid args [menuList]")
    private List<Long> menuList;

}
