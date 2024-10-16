package com.example.back_end.mapper;

import com.example.back_end.entity.CreateForm;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserMapper {

    @Insert("insert into user (username, password) values(#{username}, #{password})")
    void register(@Param("username") String username, @Param("password") String password);

    @Select("select password from user where username like #{username}")
    String login(@Param("username") String username);

    @Select("select name, description, externalLink from nfts where collectUser like #{username}")
    List<CreateForm> collectProfile(@Param("username") String username);

    @Select("select name, description, externalLink from nfts where createUser like #{username}")
    List<CreateForm> createProfile(@Param("username") String username);
}
