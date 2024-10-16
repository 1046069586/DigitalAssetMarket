package com.example.back_end.entity;

import lombok.Data;

@Data
public class User {

    private String username;
    private String password;
    private String token;
}
