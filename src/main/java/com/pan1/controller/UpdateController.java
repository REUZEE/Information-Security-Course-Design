package com.pan1.controller;

import com.pan1.em.Result;
import com.pan1.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@CrossOrigin(origins = "*",allowCredentials="true",allowedHeaders = "",methods = {RequestMethod.POST})
public class UpdateController {

    @Autowired
    UserService userService;

    @Autowired
    RedisTemplate redisTemplate;

    @RequestMapping(value = "/user/update/username", method = RequestMethod.POST)
    public Result updateUsername(HttpServletRequest request, HttpSession session) {
        String oldUsername = (String) session.getAttribute("username");
        String username = request.getParameter("username");

        userService.updateUsername(oldUsername, username);

        return Result.SUCCESS;
    }


    @RequestMapping(value = "/user/update/password", method = RequestMethod.POST)
    public Result updatePassword(HttpServletRequest request, HttpSession session) {
        String email = request.getParameter("email");
        String authCode = request.getParameter("authCode");
        String password = request.getParameter("password");
        System.out.println("pre: " + userService.queryUserByEmail(email).getPassword());

        ValueOperations valueOperations = redisTemplate.opsForValue();
        String verificationCode = (String) valueOperations.get(email);

        if (verificationCode == null)
            return Result.EMAIL_ERROR;

        if (!verificationCode.equals(authCode))
            return Result.AUTHCODE_ERROR;

        userService.updatePassword(email, password);
        System.out.println("now: " + password);
        return Result.SUCCESS;
    }
}
