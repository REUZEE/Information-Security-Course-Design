package com.pan1.dao;

import com.pan1.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface UserMapper {

    List<User> queryUserList();

    User queryUserByUsername(String username);

    User queryUserByEmail(String email);

    String queryGroupByGroupname(String groupname);

    List<String> queryFileidByGroupname(String groupname);

    int addUser(User user);

    int deleteUser(String username);

    int updateUsername(String oldUsername, String username);

    int updatePassword(String email, String password);

    int updateMemory(String username, double memory);

    int updateGroup(String username, String group);

    String queryGroupByGroup(String group);

}
