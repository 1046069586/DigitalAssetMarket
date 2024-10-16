package com.example.back_end.service;

import com.example.back_end.entity.CreateForm;
import com.example.back_end.entity.User;

import java.util.List;

public interface UserService {

    void register(User user);

    boolean login(User user);

    List<CreateForm> profile(String type, String username);
}
