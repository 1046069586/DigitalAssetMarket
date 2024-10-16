package com.example.back_end.config.Jwt;

import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

@Component
public class JwtInterceptor implements HandlerInterceptor{
    /**
     * 拦截器拦截请求后，对token的逻辑处理
     * 拦截器功能实现的核心
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(!(handler instanceof HandlerMethod)){
            return true;
        }
        String token = request.getHeader("token");
        if(StrUtil.isBlank(token)){
//            response.setStatus(302);
//            response.setHeader("location", "http://localhost:3000/login");

//            throw new Exception();

            response.setContentType("application/json;charset=utf-8");
            PrintWriter writer = response.getWriter();
            writer.write("{\"code\":\"400\",\"msg\":null,\"data\":null}");
            writer.flush();
            writer.close();
        }
        return true;

    }
}
