package com.example.back_end.mapper;

import com.example.back_end.entity.CreateForm;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface MarketMapper {


    @Select("select * from nfts")
    List<CreateForm> selectAll();
}
