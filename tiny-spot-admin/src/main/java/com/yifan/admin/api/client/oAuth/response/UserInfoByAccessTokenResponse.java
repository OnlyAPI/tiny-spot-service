package com.yifan.admin.api.client.oAuth.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author TaiYi
 * @ClassName
 * @date 2023/6/15 14:07
 */
@Data
@ApiModel("通过AccessToken获取用户信息-响应")
public class UserInfoByAccessTokenResponse {

    @ApiModelProperty("uId")
    private Integer id;

    @ApiModelProperty("头像地址")
    private String avatar_url;

    @ApiModelProperty("登录名称")
    private String login;

    @ApiModelProperty("成员角色")
    private String member_role;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("")
    private String bio;

    @ApiModelProperty("博客地址")
    private String blog;

    @ApiModelProperty("创建日期")
    private String created_at;

    @ApiModelProperty("修改日期")
    private String updated_at;

    @ApiModelProperty("邮箱")
    private String email;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("")
    private String events_url;

    @ApiModelProperty("")
    private String followers;

    @ApiModelProperty("")
    private String followers_url;

    @ApiModelProperty("")
    private String following;

    @ApiModelProperty("")
    private String following_url;

    @ApiModelProperty("")
    private String gists_url;

    @ApiModelProperty("用户首页地址")
    private String html_url;

    @ApiModelProperty("用户组织地址")
    private String organizations_url;

    @ApiModelProperty("")
    private String public_gists;

    @ApiModelProperty("")
    private String public_repos;

    @ApiModelProperty("")
    private String received_events_url;

    @ApiModelProperty("")
    private String repos_url;

    @ApiModelProperty("")
    private String stared;

    @ApiModelProperty("")
    private String starred_url;

    @ApiModelProperty("")
    private String subscriptions_url;

    @ApiModelProperty("")
    private String type;

    @ApiModelProperty("")
    private String url;

    @ApiModelProperty("")
    private String watched;

    @ApiModelProperty("")
    private String weibo;
}
