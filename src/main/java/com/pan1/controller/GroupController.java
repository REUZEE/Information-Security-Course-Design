package com.pan1.controller;

import com.pan1.em.Result;
import com.pan1.service.serviceImpl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin(origins = "*",allowCredentials="true",allowedHeaders = "",methods = {RequestMethod.POST})
public class GroupController {

    @Autowired
    UserServiceImpl userService;

    @RequestMapping(value = "/group", method = RequestMethod.POST)
    public String group(HttpServletRequest request) {
        String username = request.getParameter("username");
        String group = userService.queryUserByUsername(username).getGroup();
        return group;
    }

    @RequestMapping(value = "/group/create", method = RequestMethod.POST)
    public Result groupCreate(HttpServletRequest request) {
        String username = request.getParameter("username");
        String groupname = request.getParameter("groupname");

        String group = userService.queryUserByUsername(username).getGroup();
        if (group.equals(groupname))
            return Result.USER_IN_GROUP;

        if (userService.queryGroupByGroup(groupname) != null)
            return Result.GROUP_EXISTS;

        userService.updateGroup(username, groupname);
        return Result.SUCCESS;
    }

    @RequestMapping(value = "/group/join", method = RequestMethod.POST)
    public Result groupJoin(HttpServletRequest request) {
        String username = request.getParameter("username");
        String groupname = request.getParameter("groupname");

        String group = userService.queryUserByUsername(username).getGroup();
        if (group.equals(groupname))
            return Result.USER_IN_GROUP;

        if (userService.queryGroupByGroup(groupname) == null)
            return Result.GROUP_ERROR;

        userService.updateGroup(username, groupname);
        return Result.SUCCESS;
    }

    @RequestMapping(value = "/group/change", method = RequestMethod.POST)
    public Result groupChange(HttpServletRequest request) {
        String username = request.getParameter("username");
        String groupname = request.getParameter("groupname");

        userService.updateGroup(username, groupname);
        return Result.SUCCESS;
    }

    @RequestMapping(value = "/group/quit", method = RequestMethod.POST)
    public Result groupQuit(HttpServletRequest request) {
        String username = request.getParameter("username");
        String groupname = request.getParameter("groupname");

        String group = userService.queryUserByUsername(username).getGroup();
        if (group == null)
            return Result.USER_ARENT_IN_GROUP;

        userService.updateGroup(username, "");
        return Result.SUCCESS;
    }
}
