package com.example.back_end.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result {
    /**
     *统一返回前端的响应数据格式
     */

    private String code;
    private String msg;
    private Object data;

    public static Result success(Object data) {
        return new Result("", "", data);
    }

//    public static Result success(String msg) {
//        return new Result("", msg, null);
//    }
}
