package com.example.back_end.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface FileMapper {

    @Insert("insert into files (url, type, size) values (#{url}, #{type}, #{size})")
    void upload(@Param("url")String url, @Param("type")String type, @Param("size")long size);
}
