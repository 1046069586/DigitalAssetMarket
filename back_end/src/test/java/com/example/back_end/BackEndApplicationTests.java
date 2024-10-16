package com.example.back_end;

import java.io.File;
import java.io.FileWriter;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.back_end.config.TraceTest;
// import com.example.back_end.config.FabricTest;

@SpringBootTest
class BackEndApplicationTests {

    @Autowired
    private TraceTest test;

    // @Autowired
    // private FabricTest test2;

    @Test
    void register() throws Exception{
        test.register("appUser");
    }

    // @Test
    // void mint() throws Exception{
    //     test2.mint("aseet.com", "zyx");
    // }

    // @Test
    // void transfer() throws Exception{
    //     test2.transfer("1", "right222.com", "zml");
    // }

    @Test
    void dataGen() throws Exception{
        test.dataGen(5, 125);
    }

    @Test
    void NFRtxTrace() throws Exception{
        File f = new File("/home/zyx/Desktop/NFRsMarket/data.txt");
        f.createNewFile();
        FileWriter writer = new FileWriter(f, true);
        writer.write("NFR:  ");
        for(int i = 1; i <= 5; i++){
            Long time = test.NFRtxTrace(String.valueOf(i));
            writer.write(time.toString() + "\t");
        }
        writer.write("\n");
        writer.flush();
        writer.close();

        //test.NFRtxTrace("1");
    }

    @Test
    void ERCtxTrace() throws Exception{
        File f = new File("/home/zyx/Desktop/NFRsMarket/data.txt");
        f.createNewFile();
        FileWriter writer = new FileWriter(f, true);
        writer.write("ERC:  ");
        for(int i = 1; i <= 5; i++){
            Long time = test.ERCtxTrace(String.valueOf(i), "user125");
            writer.write(time.toString() + "\t");
        }
        writer.write("\n");
        writer.flush();
        writer.close();
        //test.ERCtxTrace("1", "user2");
    }

    @Test
    void blockHeight() throws Exception{
        test.blockHeight();
       
    }

    @Test
    void queryAll() throws Exception{
        test.queryAll();
       
    }

    @Test
    void qscc() throws Exception{
        test.qscc("43221705b5e93767391ef43881cadfc58de456298836c17dfd03d7f2c29f9468");
       
    }

}
