package com.yifan.admin.api.client.oAuth.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @description: TODO
 * @author: wyf
 * @date: 2023/7/3
 */
@ApiModel("通过OpenId和AccessToken获取用户信息-响应")
@Data
public class UserInfoByOpenIdAndAccTokenResponse {

    @ApiModelProperty("返回码")
    private int ret;

    @ApiModelProperty("信息")
    private String msg;

    @ApiModelProperty("用户在QQ空间的昵称。")
    private String nickname;

    @ApiModelProperty("大小为30×30像素的QQ空间头像URL。")
    private String figureurl;

    @ApiModelProperty("大小为50×50像素的QQ空间头像URL。")
    private String figureurl_1;

    @ApiModelProperty("大小为100×100像素的QQ空间头像URL。")
    private String figureurl_2;

    @ApiModelProperty("大小为40×40像素的QQ头像URL")
    private String figureurl_qq_1;

    @ApiModelProperty("大小为100×100像素的QQ头像URL")
    private String figureurl_qq_2;

    @ApiModelProperty("性别。 如果获取不到则默认返回-男")
    private String gender;
}
