package com.yifan.admin.api.utils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

/**
 * 字节模型签名工具
 */

public class SkyLarkSignUtils {
    private static final String SERVICE = "ml_maas";
    private static final String REGION = "cn-beijing";
    private static final String METHOD = "POST";
    private static final String CONTENT_TYPE = "application/json";

    public static Map<String, String> getSkylarkSign(String host,
                                                     String path,
                                                     String ak,
                                                     String sk,
                                                     byte[] body) throws Exception {
        final String endpoint = extractEndpoint(host);

        if (body == null) {
            body = new byte[0];
        }
        String xContentSha256 = hashSHA256(body);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        String xDate = sdf.format(new Date());
        String shortXDate = xDate.substring(0, 8);
        final String signHeader = "host;x-date;x-content-sha256;content-type";
        String canonicalStringBuilder = METHOD + "\n" + path + "\n" + "\n" +
                "host:" + endpoint + "\n" +
                "x-date:" + xDate + "\n" +
                "x-content-sha256:" + xContentSha256 + "\n" +
                "content-type:" + CONTENT_TYPE + "\n" +
                "\n" +
                signHeader + "\n" +
                xContentSha256;
        String hashcanonicalString = hashSHA256(canonicalStringBuilder.getBytes());
        final String credentialScope = shortXDate + "/" + REGION + "/" + SERVICE + "/request";
        String signString = "HMAC-SHA256" + "\n" + xDate + "\n" + credentialScope + "\n" + hashcanonicalString;
        byte[] signKey = genSigningSecretKeyV4(sk, shortXDate, REGION, SERVICE);
        final String signature = byteArrayToHexString(hmacSHA256(signKey, signString));

        final String authorization = "HMAC-SHA256" +
                " Credential=" + ak + "/" + credentialScope +
                ", SignedHeaders=" + signHeader +
                ", Signature=" + signature;

        Map<String, String> signMap = new HashMap<>();
        signMap.put("Content-Type", CONTENT_TYPE);
        signMap.put("Host", endpoint);
        signMap.put("X-Date", xDate);
        signMap.put("X-Content-Sha256", xContentSha256);
        signMap.put("Authorization", authorization);
        return signMap;
    }

    private static String hashSHA256(byte[] content) throws Exception {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            return byteArrayToHexString(md.digest(content));
        } catch (Exception e) {
            throw new Exception(
                    "Unable to compute hash while signing request: "
                            + e.getMessage(), e);
        }
    }

    private static byte[] hmacSHA256(byte[] key, String content) throws Exception {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(key, "HmacSHA256"));
            return mac.doFinal(content.getBytes());
        } catch (Exception e) {
            throw new Exception(
                    "Unable to calculate a request signature: "
                            + e.getMessage(), e);
        }
    }

    private static byte[] genSigningSecretKeyV4(String secretKey, String date, String region, String service) throws Exception {
        byte[] kDate = hmacSHA256((secretKey).getBytes(), date);
        byte[] kRegion = hmacSHA256(kDate, region);
        byte[] kService = hmacSHA256(kRegion, service);
        return hmacSHA256(kService, "request");
    }

    private static String extractEndpoint(String host) {
        int startIndex = host.indexOf("//") + 2; // 找到"//"后面的第二个"/"的索引
        return host.substring(startIndex); // 提取域名
    }

    public static String byteArrayToHexString(byte[] byteArray) {
        char[] hexChars = "0123456789abcdef".toCharArray();
        StringBuilder hexString = new StringBuilder();
        for (byte b : byteArray) {
            hexString.append(hexChars[(b >> 4) & 0x0F]);
            hexString.append(hexChars[b & 0x0F]);
        }
        return hexString.toString();
    }

}
