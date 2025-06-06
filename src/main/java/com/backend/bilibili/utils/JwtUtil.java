package com.backend.bilibili.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

@Component
public class JwtUtil {

    // Token的有效期设置为14天
    public static final long JWT_TTL = 60 * 60 * 1000L * 24 * 14;  // 有效期14天
    // 签名的私钥，生产环境中请使用更安全的方式存储
    public static final String JWT_KEY = "JSDFSDFSDFASJDHASDASDdfa32dJHASFDA67765asda123";

    // 生成一个UUID，作为token的唯一标识
    public static String getUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    // 创建JWT，设置subject为用户的唯一标识（通常是用户名或用户ID）
    public static String createJWT(String subject) {
        // 调用getJwtBuilder来构造JWT
        JwtBuilder builder = getJwtBuilder(subject, null, getUUID());
        // 返回生成的JWT
        return builder.compact();
    }

    // 生成JWT的Builder
    private static JwtBuilder getJwtBuilder(String subject, Long ttlMillis, String uuid) {
        // 设置签名算法为HS256
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        // 获取密钥
        SecretKey secretKey = generalKey();
        // 当前时间
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        // 如果没有设置有效期，则使用默认的JWT_TTL
        if (ttlMillis == null) {
            ttlMillis = JwtUtil.JWT_TTL;
        }
        // 计算过期时间
        long expMillis = nowMillis + ttlMillis;
        Date expDate = new Date(expMillis);

        // 构造JWT
        return Jwts.builder()
                .setId(uuid)  // 设置UUID
                .setSubject(subject)  // 设置用户信息
                .setIssuer("sg")  // 设置发布者
                .setIssuedAt(now)  // 设置发行时间
                .signWith(signatureAlgorithm, secretKey)  // 设置签名算法
                .setExpiration(expDate);  // 设置过期时间
    }

    // 生成密钥（HS256）
    public static SecretKey generalKey() {
        // 将JWT_KEY进行Base64解码
        byte[] encodeKey = Base64.getDecoder().decode(JwtUtil.JWT_KEY);
        // 返回SecretKey
        return new SecretKeySpec(encodeKey, 0, encodeKey.length, "HmacSHA256");
    }

    // 解析JWT
    public static Claims parseJWT(String jwt) throws Exception {
        SecretKey secretKey = generalKey();
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(jwt)
                .getBody();
    }

    // 打印前端和后端的Token，用于调试
    public static void printTokenInfo(String token) {
        System.out.println("收到的前端Token：" + token);  // 打印前端传来的Token
        try {
            Claims claims = parseJWT(token);
            System.out.println("解析后的Token信息：" + claims);  // 打印解析后的Token内容
        } catch (Exception e) {
            System.out.println("Token解析失败：" + e.getMessage());
        }
    }

    public static String getUsername(String token) throws Exception {
        SecretKey secretKey = generalKey();
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }


}
