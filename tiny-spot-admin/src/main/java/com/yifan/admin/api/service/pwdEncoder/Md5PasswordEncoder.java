package com.yifan.admin.api.service.pwdEncoder;

import com.yifan.admin.api.exception.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 * @author TaiYi
 * @ClassName
 * @date 2023/2/13 18:52
 */
@Slf4j
@Component
public class Md5PasswordEncoder implements PasswordEncoder {

    private static final String secret = "YJ_MALL_API";

    @Override
    public String encode(String rawPassword, String salt) {
        if (StringUtils.isBlank(rawPassword)) {
            throw new ApiException("rawPassword cannot be null");
        }
        //final String encodePwd = DigestUtils.md5Hex(rawPassword + secrte + salt).toUpperCase();
        return md5Hex(rawPassword, salt);
    }


    @Override
    public boolean matches(String rawPassword, String encodedPassword, String salt) {
        if (StringUtils.isBlank(rawPassword)) {
            throw new ApiException("rawPassword cannot be null");
        }
        if (StringUtils.isBlank(encodedPassword)) {
            log.error("[matches] Empty encoded password");
            return false;
        }
        return encodedPassword.equals(md5Hex(rawPassword, salt));
    }

    private String md5Hex(String rawPassword, String salt) {
        return DigestUtils.md5Hex(rawPassword + secret + salt).toUpperCase();
    }

    public static void main(String[] args) {
        String rawPassword = "asdf1234*#@&";
        String salt = "root";
        Md5PasswordEncoder encoder = new Md5PasswordEncoder();
        String encodedPwd = encoder.encode(rawPassword, salt);
        System.out.println(encodedPwd);
        System.out.println(encoder.matches(rawPassword, "asdf1234", salt));
    }
}
