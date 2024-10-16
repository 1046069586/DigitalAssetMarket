package com.example.back_end.controller;

import com.example.back_end.config.Result;
import com.example.back_end.entity.CreateForm;
import com.example.back_end.service.CreateService;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/creates")
public class CreateController {

    @Autowired
    private CreateService createService;

    @PostMapping
    public void createNFT(@RequestBody CreateForm createForm) throws IOException {
        createService.createNFT(createForm);
    }
}
