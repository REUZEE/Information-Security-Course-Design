package com.pan1.controller;

import com.pan1.em.Result;
import com.pan1.entity.UserFile;
import com.pan1.service.serviceImpl.UserFileServiceImpl;
import com.pan1.service.serviceImpl.UserServiceImpl;
import com.pan1.utils.GetFileSize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*",allowCredentials="true",allowedHeaders = "")
public class FileController {

    private static final String FILEPATH = "/home/reuze/Documents/file/";

    @Autowired
    UserFileServiceImpl userFileService;

    @Autowired
    UserServiceImpl userService;

    @Autowired
    RedisTemplate redisTemplate;

    // return the public filename of user
    @RequestMapping(value = "/file/list/group", method = RequestMethod.POST)
    public List<UserFile> handleGroupFileList(HttpServletRequest request) {

        String username = request.getParameter("username");

        return userFileService.queryPublicFileByUsername(username);
    }

    @RequestMapping(value = "/file/list", method = RequestMethod.POST)
    public List<List<UserFile>> handleFileList(HttpServletRequest request) {

        List<List<UserFile>> res = new ArrayList<>();

        String username = request.getParameter("username");

        res.add(userFileService.queryPrivateFileByUsername(username));
        res.add(userFileService.queryPublicFileByUsername(username));
        return res;
    }

    // 0: private; 1: public; 2: private & public
    @Transactional(propagation = Propagation.REQUIRED)
    @RequestMapping(value = "/file/upload", method = RequestMethod.POST)
    public String handleFileUpload(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        if (!file.isEmpty()) {
            try {

                String username = request.getParameter("username");
                String filename  =request.getParameter("filename");
                String group = request.getParameter("space");
                int status = 0;
                double size = GetFileSize.cal(file.getSize());
                String email = userService.queryUserByUsername(username).getEmail();
                String fileid = email + filename;
                double memory = 200;
                ValueOperations valueOperations = redisTemplate.opsForValue();
                String key = (String) valueOperations.get(username + "key");
                String iv = (String) valueOperations.get(username + "iv");

                UserFile userFile = userFileService.queryUserFileById(fileid);
                if (userFile != null) { //exist

                    if ((!group.equals("") && group.length() > 0) && (userFileService.queryfileidFileById(fileid) == null)) { // to public
                        if (userService.queryGroupByGroupname(group) == null)
                            memory = 200;
                        else
                            memory = userFileService.queryMemoryByGroupname(group);
                        if (memory - size < 0)
                            return Result.MEMORY_OVERFLOW.toString();
                        userFileService.addUserFileGroup(group, fileid, memory - size);

                        Map map = new HashMap();
                        map.put("memory", memory - size);
                        map.put("groupname", group);
                        userFileService.updateGroupMemory(map);
                    }

                    status = userFile.getStatus();

                    if (status == 1 && (!group.equals("") && group.length() > 0))
                        return Result.SUCCESS.toString();

                    // public to private
                    if (status == 1 && (group.equals(""))) {

                        memory = userService.queryUserByUsername(username).getMemory();
                        userService.updateMemory(username, memory - size);

                    }

                    Map map = new HashMap();
                    map.put("id", fileid);
                    map.put("status", 2);
                    userFileService.updateStatus(map);
                    return String.valueOf(size);
                }

                else {
                    if (!group.equals("") && group.length() > 0) { // to public
                        if (userService.queryGroupByGroupname(group) == null) {
                            if (memory - size < 0)
                                return Result.MEMORY_OVERFLOW.toString();
                        }
                        else {
                                memory = userFileService.queryMemoryByGroupname(group);
                                if (memory - size < 0)
                                    return Result.MEMORY_OVERFLOW.toString();
                            }

                        userFileService.addUserFileGroup(group, fileid, memory - size);
                        userFileService.addUserFile(new UserFile(fileid, email, size, filename, 1, key, iv));
                        // group update

                        Map map = new HashMap();
                        map.put("memory", memory - size);
                        map.put("groupname", group);
                        userFileService.updateGroupMemory(map);
                    }
                    else { // to private
                        memory = userService.queryUserByUsername(username).getMemory();
                        if (memory - size < 0)
                            return Result.MEMORY_OVERFLOW.toString();
                        userFileService.addUserFile(new UserFile(fileid, email, size, filename, 0, key, iv));
                        userService.updateMemory(username, memory - size);

                    }
                    // upload to disk
                    String path = FILEPATH + email;
                    if (!new File(path).exists())
                        new File(path).mkdir();

                    path = FILEPATH + email + "/" + filename;
                    BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(new File(path)));
                    System.out.println(path);
                    out.write(file.getBytes());
                    out.flush();
                    out.close();

                }

                return String.valueOf(size);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return "上传失败," + e.getMessage();
            } catch (IOException e) {
                e.printStackTrace();
                return "上传失败," + e.getMessage();
            }


        } else {
            return "上传失败，因为文件是空的.";
        }
    }

    @RequestMapping(value = "/file/download", method = RequestMethod.POST)
    public byte[] handleFileDownload(HttpServletRequest request, HttpServletResponse response) {

        String username = request.getParameter("username");
        String filename = request.getParameter("filename");
        String group = request.getParameter("space");
        String email;

        if (!group.equals("")) {
            String fileid = userFileService.queryfileidBygroupname(group, filename);
            email = userFileService.queryUserFileById(fileid).getEmail();
        }
        else
            email = userService.queryUserByUsername(username).getEmail();

        response.setContentType("arraybuffer");

        String path = FILEPATH + email + "/" + filename;
        byte[] buffer = null;
        try {
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            FileInputStream fis = new FileInputStream(path);
            byte[] b = new byte[1024];
            int length;
            while ((length = fis.read(b)) != -1) {
                output.write(b, 0, length);
            }
            buffer = output.toByteArray();
            output.close();
            fis.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return buffer;

    }

    @Transactional(propagation = Propagation.REQUIRED)
    @RequestMapping(value = "/file/delete", method = RequestMethod.POST)
    public Result handleFileDelete(HttpServletRequest request) {

        String username = request.getParameter("username");
        String filename = request.getParameter("filename");
        String group = request.getParameter("space");
        String email;

        if (!group.equals("")) {
            String fileid = userFileService.queryfileidBygroupname(group, filename);
            email = userFileService.queryUserFileById(fileid).getEmail();
        }
        else
            email = userService.queryUserByUsername(username).getEmail();

        String id = email + filename;

        System.out.println("/file/delete/group: " + group);

        if (!group.equals("") && group.length() > 0) { // delete public space file
            double size = userFileService.queryUserFileById(id).getSize();
            double memory = userFileService.queryMemoryByGroupname(group);
            memory += size;
            if (memory > 200.0)
                memory = 200.0;
            Map map = new HashMap();
            map.put("memory", memory);
            map.put("groupname", group);
            userFileService.updateGroupMemory(map);
            userFileService.deleteUserFileinfo(id);

            if (userFileService.queryUserFileById(id).getStatus() == 2) {
                int status = 0;
                System.out.println("==================================================================");
                System.out.println("status: " + status);
                map = new HashMap();
                map.put("id", id);
                map.put("status", status);
                userFileService.updateStatus(map);
                return Result.SUCCESS;
            }
            else
                userFileService.deleteUserFile(id);
        }
        else {
            double size = userFileService.queryUserFileById(id).getSize();
            double memory = userService.queryUserByUsername(username).getMemory();
            memory += size;
            if (memory > 200)
                memory = 200;
            int status = userFileService.queryUserFileById(id).getStatus();
            userService.updateMemory(username, memory);
            //userFileService.deleteUserFile(id);

            if (status == 2) {
                status = 1;
                System.out.println("==================================================================");
                System.out.println("status: " + status);
                Map map = new HashMap();
                map.put("id", id);
                map.put("status", status);
                userFileService.updateStatus(map);
                return Result.SUCCESS;
            }
            else
                userFileService.deleteUserFile(id);
        }

        String path = FILEPATH + email + "/" + filename;
        new File(path).delete();

        System.out.println(path);
        System.out.println("==============delete============");
        return Result.SUCCESS;
    }
}
