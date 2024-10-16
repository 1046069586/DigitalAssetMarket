package com.example.back_end.controller;

import com.example.back_end.config.Result;
import com.example.back_end.service.OrderService;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/{username}/{id}")
    Result buy(@PathVariable String username, @PathVariable String id) throws IOException{
        orderService.buy(username, id);
        return new Result("200", "success", null);
    }
}
