package com.example.back_end.config.Jwt;

import cn.hutool.core.date.DateUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import java.util.Date;

public class JwtGenerator {
    /**
     * 根据用户名，密码创建jwt token
     * 用于登录验证后，创建token，并返回前端。之后用户发送请求，token作为验证信息。
     */

    public static String genToken(String username, String password){
        return JWT.create()
                  .withAudience(username)
                  .withExpiresAt(DateUtil.offsetHour(new Date(), 2))
                  .sign(Algorithm.HMAC256(password));
    }
}
