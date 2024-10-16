package com.example.back_end.controller;

import com.example.back_end.config.Result;
import com.example.back_end.entity.Files;
import com.example.back_end.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/files")
public class FileController {

    @Autowired
    private FileService fileService;

    @PostMapping
    public Result upload(MultipartFile file) throws IOException {  //@RequestBody 此处似乎不需要注解就可以接收传来的文件，字节流
        Files files = fileService.upload(file);
        return (Result.success(files));
    }

    @GetMapping("/{uuid}")
    public void download(@PathVariable String uuid, HttpServletResponse response) throws IOException {
        fileService.download(uuid, response);
    }
}
