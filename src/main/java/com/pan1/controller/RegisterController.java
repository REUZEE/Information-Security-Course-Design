package com.pan1.controller;

import com.pan1.em.Result;
import com.pan1.entity.User;
import com.pan1.service.serviceImpl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@CrossOrigin(origins = "*",allowCredentials="true",allowedHeaders = "",methods = {RequestMethod.POST})
public class RegisterController {

    @Autowired
    UserServiceImpl userService;

    @Autowired
    RedisTemplate redisTemplate;

    @RequestMapping(value = "/user/register/authCode", method = RequestMethod.POST)
    public Result email(HttpServletRequest request, HttpServletResponse response) {

        //email send
        String email = request.getParameter("email");
        System.out.println(email);

        if (userService.queryUserByEmail(email) != null)
            return Result.USERID_EXISTS;

        userService.sendAuthCode(email);

        return Result.SUCCESS;
    }


    @RequestMapping(value = "/user/register", method = RequestMethod.POST)
    public Result register(HttpServletRequest request) {

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String email = request.getParameter("email");
        String authCode = request.getParameter("authCode");
        String group = "";

        ValueOperations valueOperations = redisTemplate.opsForValue();
        String verificationCode = (String) valueOperations.get(email);

        if (verificationCode == null)
            return Result.EMAIL_ERROR;

        if (!verificationCode.equals(authCode))
            return Result.AUTHCODE_ERROR;

        if (userService.queryUserByUsername(username) != null)
            return Result.USERNAME_EXISTS;

        redisTemplate.delete(email);
        User user = new User();
        user.setGroup(group);
        user.setPassword(password);
        user.setEmail(email);
        user.setUsername(username);
        user.setMemory(200);
        userService.addUser(user);

        System.out.println(user);
        return Result.SUCCESS;

    }

    @RequestMapping(value = "/user/delete", method = RequestMethod.POST)
    public Result delete(HttpServletRequest request) {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        User user = userService.queryUserByUsername(username);

        if (user == null)
            return Result.USER_ERROR;

        if (user.getPassword() != password)
            return Result.PASSWORD_ERROR;

        userService.deleteUser(username);
        return Result.SUCCESS;
    }

}
