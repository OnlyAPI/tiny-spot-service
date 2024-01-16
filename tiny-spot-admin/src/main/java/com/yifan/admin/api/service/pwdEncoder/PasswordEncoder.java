package com.yifan.admin.api.service.pwdEncoder;

/**
 * @author TaiYi
 * @ClassName
 * @date 2023/2/13 18:51
 */
public interface PasswordEncoder {

    /**
     * 明文密码编码
     * @param rawPassword 明文密码
     * @param salt 盐值：目前使用用户名
     * @return
     */
    String encode(String rawPassword, String salt);

    /**
     * 验证密码配置
     * @param rawPassword：明文密码
     * @param encodedPassword：密文密码
     * @param salt 盐值：目前使用用户名
     * @return
     */
    boolean matches(String rawPassword, String encodedPassword, String salt);

}
