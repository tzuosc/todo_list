//package org.example.todo_list.security;
//
//import io.jsonwebtoken.*;
//import io.jsonwebtoken.io.Decoders;
//import io.jsonwebtoken.io.Encoders;
//import io.jsonwebtoken.security.Keys;
//import lombok.NoArgsConstructor;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.stereotype.Component;
//
//import javax.crypto.SecretKey;
//import java.time.Instant;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//
//@Component
//@NoArgsConstructor
//public class JwtUtils {
//    // 通过 Base64 编码的密钥（推荐环境变量注入）
//    @Value("${jwt.secret}")
//    private String base64Secret;
//
//    @Value("${jwt.expiration}")
//    private long expirationSeconds;
//
//    // 缓存 SecretKey 避免重复计算
//    private SecretKey secretKey;
//
//    // 初始化时生成 SecretKey
/// /    public JwtUtils() {
/// /        // 密钥生成逻辑将在 setBase64Secret() 中处理
/// /    }
//
//    public static String generateSecureKey() {
//        return Encoders.BASE64.encode(Keys.secretKeyFor(SignatureAlgorithm.HS512).getEncoded());
//    }
//
//    @Value("${jwt.secret}")
//    public void setBase64Secret(String base64Secret) {
//        this.base64Secret = base64Secret;
//        // 将 Base64 字符串解码为字节数组并生成安全密钥
//        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(base64Secret));
//    }
//
//    public String generateToken(UserDetails userDetails) {
//        Map<String, Object> claims = new HashMap<>();
//        claims.put("roles", userDetails.getAuthorities());
//        return Jwts.builder()
//                .claims(claims) // 显式设置 claims 避免字段覆盖
//                .subject(userDetails.getUsername())
//                .issuedAt(Date.from(Instant.now()))
//                .expiration(Date.from(Instant.now().plusSeconds(expirationSeconds)))
//                .signWith(secretKey, Jwts.SIG.HS512) // 指定算法和密钥
//                .compact();
//    }
//
//    public Claims parseToken(String token) {
//        try {
//            return Jwts.parser()
//                    .verifyWith(secretKey)
//                    .build()
//                    .parseSignedClaims(token)
//                    .getPayload();
//        } catch (ExpiredJwtException e) {
//            throw new SecurityException("Token 已过期", e);
//        } catch (MalformedJwtException e) {
//            throw new SecurityException("Token 格式错误", e);
//        } catch (JwtException e) {
//            throw new SecurityException("Token 验证失败", e);
//        }
//    }
//}
