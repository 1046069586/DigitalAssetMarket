package com.example.back_end.service.impl;

import com.example.back_end.config.Fabric;
import com.example.back_end.entity.CreateForm;
import com.example.back_end.entity.User;
import com.example.back_end.mapper.UserMapper;
import com.example.back_end.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private Fabric fabric;

    @Override
    public void register(User user) {
        userMapper.register(user.getUsername(), user.getPassword());
        try {
            fabric.register(user.getUsername());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean login(User user) {
        String username = user.getUsername();
        String key = userMapper.login(username);
        return key.equals(user.getPassword());
    }

    @Override
    public List<CreateForm> profile(String type, String username) {
        if (type.equals("collect") ) {
            return userMapper.collectProfile(username);

        }else {
            return userMapper.createProfile(username);
        }
    }
}
