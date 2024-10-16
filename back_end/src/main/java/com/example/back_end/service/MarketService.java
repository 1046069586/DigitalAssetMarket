package com.example.back_end.service;

import com.example.back_end.entity.CreateForm;

import java.util.List;

public interface MarketService {

    List<CreateForm> selectAll();
}
