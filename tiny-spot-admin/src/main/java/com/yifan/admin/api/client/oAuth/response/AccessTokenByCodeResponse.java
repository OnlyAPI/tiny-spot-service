package com.yifan.admin.api.client.oAuth.response;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @author TaiYi
 * @ClassName
 * @date 2023/6/15 12:40
 */
@Data
@ApiModel("获取AccessToken-响应")
public class AccessTokenByCodeResponse {

    private String access_token;

    private String token_type;

    private long expires_in;

    private String refresh_token;

    private String scope;

    private long created_at;
}
