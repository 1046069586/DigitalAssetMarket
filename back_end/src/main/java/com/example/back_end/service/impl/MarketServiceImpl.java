package com.example.back_end.service.impl;

import com.example.back_end.entity.CreateForm;
import com.example.back_end.mapper.MarketMapper;
import com.example.back_end.service.MarketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MarketServiceImpl implements MarketService {

    @Autowired
    private MarketMapper marketMapper;

    @Override
    public List<CreateForm> selectAll() {
        List<CreateForm> marketList = marketMapper.selectAll();
        return marketList;
    }
}
