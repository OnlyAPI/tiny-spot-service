package com.yifan.admin.api.utils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Locale;

/**
 * 鉴权工具
 *
 */
public class XunFeiAuthUtils {

    /**
     * 日期格式化
     */
    public final static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
    public final static String preStr = "host: %s\n" + "date: %s\n" + "GET %s HTTP/1.1";


    /**
     * 鉴权方法
     *
     * @param hostUrl   地址
     * @param apiKey    apikey
     * @param apiSecret apiSecret
     * @return 鉴权信息
     * @throws MalformedURLException    e
     * @throws InvalidKeyException      e
     * @throws NoSuchAlgorithmException e
     */
    public static String getAuthUrl(String hostUrl, String apiKey, String apiSecret) throws MalformedURLException, InvalidKeyException, NoSuchAlgorithmException {
        URL url = new URL(hostUrl);
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("GMT"));
        String date = now.format(dateTimeFormatter);
        // SHA256加密
        Mac mac = Mac.getInstance("hmacsha256");
        SecretKeySpec spec = new SecretKeySpec(apiSecret.getBytes(StandardCharsets.UTF_8), "hmacsha256");
        mac.init(spec);
        byte[] hexDigits = mac.doFinal(String.format(preStr, url.getHost(), date, url.getPath()).getBytes(StandardCharsets.UTF_8));
        // Base64加密
        String sha = Base64.getEncoder().encodeToString(hexDigits);
        // 拼接
        String authorization = String.format("api_key=\"%s\", algorithm=\"%s\", headers=\"%s\", signature=\"%s\"", apiKey, "hmac-sha256", "host date request-line", sha);
        // 拼接地址
        StringBuffer httpUrl = new StringBuffer("https://")
                .append(url.getHost()).append(url.getPath())
                .append("?")
                .append("authorization=").append(Base64.getEncoder().encodeToString(authorization.getBytes(StandardCharsets.UTF_8)))
                .append("&")
                .append("date=").append(date)
                .append("&")
                .append("host=").append(url.getHost());
        return httpUrl.toString();
    }

}
