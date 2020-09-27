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
import javax.servlet.http.HttpSession;

@RestController
@CrossOrigin(origins = "*",allowCredentials="true",allowedHeaders = "",methods = {RequestMethod.POST})
public class LoginController {

    @Autowired
    UserServiceImpl userService;

    @Autowired
    RedisTemplate redisTemplate;

    @RequestMapping(value = "/user/login/username", method = RequestMethod.POST)
    public Result loginUsername(HttpServletRequest request) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        User user = userService.queryUserByUsername(username);

        if (user == null)
            return Result.USER_ERROR;

        if (!user.getPassword().equals(password))
            return Result.PASSWORD_ERROR;

        System.out.println(username);
        return Result.SUCCESS;
    }

    @RequestMapping(value = "/user/login/authCode", method = RequestMethod.POST)
    public Result email(HttpServletRequest request, HttpServletResponse response) {

        //email send
        String email = request.getParameter("email");
        System.out.println(email);

        if (userService.queryUserByEmail(email) == null)
            return Result.USERID_ERROR;

        userService.sendAuthCode(email);
        return Result.SUCCESS;
    }

    @RequestMapping(value = "/user/login/email", method = RequestMethod.POST)
    public String[] loginEmail(HttpServletRequest request, HttpSession session) {
        String email = request.getParameter("email");
        String authCode = request.getParameter("authCode");

        User user = userService.queryUserByEmail(email);

        if (user == null)
            return new String[]{"606", ""};

        ValueOperations valueOperations = redisTemplate.opsForValue();

        String verificationCode = (String) valueOperations.get(email);
        if (verificationCode == null)
            return new String[]{"605", ""};

        if (!verificationCode.equals(authCode))
            return new String[]{"604", ""};
        redisTemplate.delete(email);

        return new String[]{"200", userService.queryUserByEmail(email).getUsername()};
    }
}
