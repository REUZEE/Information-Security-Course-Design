package com.pan1.service;

import com.pan1.entity.User;

import java.util.List;

public interface UserService {

    List<User> queryUserList();

    User queryUserByUsername(String username);

    User queryUserByEmail(String email);

    String queryGroupByGroupname(String groupname);

    List<String> queryFileidByGroupname(String groupname);

    String queryGroupByGroup(String group);

    int addUser(User user);

    int deleteUser(String username);

    int updateUsername(String oldUsername, String username);

    int updatePassword(String email, String password);

    int updateGroup(String username, String group);

    int updateMemory(String username, double memory);

    void sendAuthCode(String email);
}
