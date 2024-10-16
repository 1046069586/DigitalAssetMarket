package com.example.back_end.service;

import com.example.back_end.entity.Files;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface FileService {

    Files upload(MultipartFile file) throws IOException;

    void download(String uuid, HttpServletResponse response) throws IOException;
}
