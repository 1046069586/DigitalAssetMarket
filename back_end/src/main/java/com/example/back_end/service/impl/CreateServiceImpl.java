package com.example.back_end.service.impl;

import com.example.back_end.config.Fabric;
import com.example.back_end.entity.CreateForm;
import com.example.back_end.mapper.CreateMapper;
import com.example.back_end.service.CreateService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.UUID;  

@Service
public class CreateServiceImpl implements CreateService {

    @Autowired
    private CreateMapper createMapper;

    @Autowired
    private Fabric fabric;

    @Value("${files.upload.path}")
    private String pathname;

    @Override
    public void createNFT(CreateForm createForm) throws IOException{
        String assetID = fabric.mint(createForm.getUrl(), createForm.getCreateUser());

        String uuid = UUID.randomUUID().toString();
        String fileName = uuid + ".txt";
        String rightURI = "http://192.168.233.128:9090/files/" + fileName;
        File f = new File(pathname + fileName);
        try {
            FileOutputStream fop = new FileOutputStream(f);
            OutputStreamWriter fw = new OutputStreamWriter(fop, "UTF-8");
            fw.append("assetID:" + assetID + "\n");
            fw.append("creator:" + createForm.getCreateUser() + "\n");
            fw.append("owner:" + createForm.getCreateUser() + "\n");
            fw.close();
            fop.close();
        } catch (Exception e){
            e.printStackTrace();
        }

        String transferID = fabric.transfer(assetID, rightURI, createForm.getCreateUser());
        System.out.println(transferID);

        int success = createMapper.createNFT(assetID, createForm.getName(), createForm.getExternalLink(), createForm.getDescription(),
                createForm.getUrl(), rightURI, createForm.getType(), createForm.getSize(), createForm.getCreateUser());
        System.out.println(success);
        
    }
    

    public boolean mintNFR(MintForm mintForm) throws IOException{
        if(similarity.test(mintForm.getAssetUrl())){
            String NFRID = fabric.mint(mintForm.getAssetUrl(), mintForm.getUser());
            String assetID = fabric.transfer(assetID, mintForm.getRightUrl(), mintForm.getCreateUser());
            boolean flag1 = mintMapper.asset(assetID, mintForm.getName(), mintForm.getExternalLink(), mintForm.getDescription(),
            mintForm.getUrl(), mintForm.getUser());
            boolean flag2 = mintMapper.nfr(NFRID, assetID, mintForm.getRightUrl(), mintForm.getUser());
            if(flag1 && flag2){
                return true;
            }else{
                return false;
            }
        }else{
            return false;
        }
        

        
        
    }
}
