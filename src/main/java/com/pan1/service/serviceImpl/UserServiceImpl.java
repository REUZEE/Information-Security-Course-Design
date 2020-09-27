package com.pan1.service.serviceImpl;

import com.pan1.dao.UserMapper;
import com.pan1.entity.User;
import com.pan1.service.UserService;
import com.pan1.utils.AuthCode;
import com.pan1.utils.SendEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserMapper userMapper;

    @Autowired
    RedisTemplate redisTemplate;

    @Override
    public List<User> queryUserList() {
        return userMapper.queryUserList();
    }

    @Override
    public User queryUserByUsername(String username) {
        return userMapper.queryUserByUsername(username);
    }

    @Override
    public User queryUserByEmail(String email) {
        return userMapper.queryUserByEmail(email);
    }

    @Override
    public String queryGroupByGroupname(String groupname) {
        return userMapper.queryGroupByGroupname(groupname);
    }

    @Override
    public List<String> queryFileidByGroupname(String groupname) {
        return userMapper.queryFileidByGroupname(groupname);
    }

    @Override
    public String queryGroupByGroup(String group) {
        return userMapper.queryGroupByGroup(group);
    }

    @Override
    public int addUser(User user) {
        userMapper.addUser(user);
        return 1;
    }

    @Override
    public int deleteUser(String username) {
        userMapper.deleteUser(username);
        return 1;
    }

    @Override
    public int updateUsername(String oldUsername, String username) {
        userMapper.updateUsername(oldUsername, username);
        return 1;
    }

    @Override
    public int updatePassword(String email, String password) {
        return userMapper.updatePassword(email, password);
    }

    @Override
    public int updateGroup(String username, String group) {
        userMapper.updateGroup(username, group);
        return 1;
    }

    @Override
    public int updateMemory(String username, double memory) {
        userMapper.updateMemory(username, memory);
        return 1;
    }

    @Override
    public void sendAuthCode(String email) {

        String authCode = AuthCode.generate();
        SendEmail.send(email,
                "auth code: " + authCode + ". This is an automatic email. If you didn't operate, please ignore it",
                "auth code");

        System.out.println(email + ": " + authCode);

        ValueOperations valueOperations = redisTemplate.opsForValue();
        valueOperations.set(email, authCode);
        redisTemplate.expire(email,60, TimeUnit.SECONDS);


    }
}
