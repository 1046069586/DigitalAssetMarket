package com.example.back_end.controller;


import com.example.back_end.config.Result;
import com.example.back_end.entity.CreateForm;
import com.example.back_end.service.MarketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/markets")
public class MarketController {

    @Autowired
    private MarketService marketService;

    @GetMapping
    public Result selectAll() {
        List<CreateForm> marketList = marketService.selectAll();
        return(Result.success(marketList));
    }
}
