package com.example.back_end.controller;

import com.example.back_end.config.Code;
import com.example.back_end.config.Jwt.JwtGenerator;
import com.example.back_end.config.Result;
import com.example.back_end.entity.CreateForm;
import com.example.back_end.entity.User;
import com.example.back_end.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public Result register(@RequestBody User user) {
        userService.register(user);
        return Result.success("注册成功");
    }

    @PostMapping("/login")
    public Result login(@RequestBody User user) {

        boolean success = userService.login(user);
        if (success) {
            String token = JwtGenerator.genToken(user.getUsername(), user.getPassword());
            user.setToken(token);
            return new Result(Code.CODE_SUCCESS, "success", user);
        } else {
            return new Result(Code.CODE_USER_ERROR, "fail", null);
        }

    }

    @GetMapping("/{type}/{username}")
    public Result profile(@PathVariable String type, @PathVariable String username) {
        List<CreateForm> profile = userService.profile(type, username);
        return Result.success(profile);
    }

}
