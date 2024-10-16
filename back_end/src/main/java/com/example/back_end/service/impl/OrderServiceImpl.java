package com.example.back_end.service.impl;

import com.example.back_end.config.Fabric;
import com.example.back_end.mapper.OrderMapper;
import com.example.back_end.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.UUID;  


@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private Fabric fabric;

    @Value("${files.upload.path}")
    private String pathname;

    @Override
    public void buy(String username, String id) throws IOException{
        String uuid = UUID.randomUUID().toString();
        String fileName = uuid + ".txt";
        String rightURI = "http://192.168.233.128:9090/files/" + fileName;
        File f = new File(pathname + fileName);
        try {
            FileOutputStream fop = new FileOutputStream(f);
            OutputStreamWriter fw = new OutputStreamWriter(fop, "UTF-8");
            fw.append("assetID:" + id + "\n");
            fw.append("creator:" + username + "\n");
            fw.append("owner:" + username + "\n");
            fw.close();
            fop.close();
        } catch (Exception e){
            e.printStackTrace();
        }
        
        String transferID = fabric.transfer(id, rightURI, username);
        System.out.println(transferID);

        orderMapper.buy(username, id);
    }


    public void sell(SaleForm saleForm) throws IOException{
        String watermarkUrl = watermark.add(saleForm.getAssetUrl());
        txMapper.sell(saleForm.getNFRID(), saleForm.price(), saleForm.getrightUrl(), saleForm.getStart(), saleForm.getEnd(), watermarkUrl);
    }
    
}
