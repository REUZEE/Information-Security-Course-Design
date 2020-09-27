package com.pan1.controller;

import com.pan1.entity.User;
import com.pan1.service.serviceImpl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin(origins = "*",allowCredentials="true",allowedHeaders = "",methods = {RequestMethod.POST})
public class Controller {

    @Autowired
    UserServiceImpl userService;

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public String test(@RequestParam("file") MultipartFile file) {
        System.out.println("===============================================");
        System.out.println(file);
        return "ok";
    }

    @RequestMapping(value = "/getMemory", method = RequestMethod.POST)
    public String[] getMemory(HttpServletRequest request) {

        String username = request.getParameter("username");
        User user = userService.queryUserByUsername(username);
        //String kind = user.getKind();
        /*
        if (kind.equals(Group.GENERAL.getName()))
            kind = "200";
        else
            kind = "400";

         */
        String memory = String.valueOf(user.getMemory());

        return new String[]{"200", memory};
    }

}
