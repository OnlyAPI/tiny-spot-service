package com.yifan.admin.api.model.baidu;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

/**
 * @author TaiYi
 * @ClassName
 * @date 2023/8/4 15:37
 */
@Data
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AccessTokenResponse {

    private String refreshToken;

    private Long expiresIn;

    private String sessionToken;

    private String accessToken;

    private String scope;

    private String error;

    private String errorDescription;

}
