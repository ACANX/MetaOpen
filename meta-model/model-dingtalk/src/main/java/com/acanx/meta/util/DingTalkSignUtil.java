package com.acanx.meta.util;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.util.Base64;

/**
 * DingTalkSignUtil
 */
public class DingTalkSignUtil {

    private static final String UTF8 = "UTF-8";

    /**
     *
     * @param secret      私钥
     * @param timestamp   时间戳
     * @return            签名结果
     * @throws Exception  异常
     */
    public static String generateSign(String secret, Long timestamp) throws Exception {
        String stringToSign = timestamp + "\n" + secret;
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(secret.getBytes(UTF8), "HmacSHA256"));
        byte[] signData = mac.doFinal(stringToSign.getBytes(UTF8));
        String sign = URLEncoder.encode(Base64.getEncoder().encodeToString(signData), UTF8);
        return sign;
    }

}
