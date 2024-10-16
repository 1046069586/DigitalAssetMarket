package com.example.back_end.service.impl;

import cn.hutool.core.io.FileUtil;
import com.example.back_end.entity.Files;
import com.example.back_end.mapper.FileMapper;
import com.example.back_end.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

    @Value("${files.upload.path}")
    private String pathname;

    @Autowired
    private FileMapper fileMapper;

    @Override
    public Files upload(MultipartFile file) throws IOException {
        //创建所需参数
        long size = file.getSize() / 1024;
        String originalFilename = file.getOriginalFilename();
        String type = FileUtil.extName(originalFilename);
        String uuid = UUID.randomUUID().toString();
        String fileName = uuid + "." + type;
        String url = "http://192.168.233.128:9090/files/" + fileName;

        /// 将传来的文件保存到本地指定的文件夹
        File uploadFile = new File(pathname + fileName );
        file.transferTo(uploadFile);

        /// 将文件信息返回前端与表单信息一起保存至数据库，主要是url
        return new Files(url, type, size);


    }

    @Override
    public void download(String uuid, HttpServletResponse response) throws IOException {
        File uploadFile = new File(pathname + uuid);
        ServletOutputStream os = response.getOutputStream();
//        response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(uuid, "UTF-8"));
//        response.setContentType("application/octet-stream");
        os.write(FileUtil.readBytes(uploadFile));
        os.flush();
        os.close();
    }
}
