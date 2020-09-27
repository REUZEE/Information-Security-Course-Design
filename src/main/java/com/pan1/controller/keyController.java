package com.pan1.controller;

import com.pan1.em.Result;
import com.pan1.service.serviceImpl.UserFileServiceImpl;
import com.pan1.service.serviceImpl.UserServiceImpl;
import com.pan1.utils.AES;
import com.pan1.utils.RSA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.security.PrivateKey;
import java.util.concurrent.TimeUnit;

@RestController
@CrossOrigin(origins = "*",allowCredentials="true",allowedHeaders = "",methods = {RequestMethod.POST})
public class keyController {

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    UserServiceImpl userService;

    @Autowired
    UserFileServiceImpl userFileService;

    @RequestMapping(value = "/key/rsa", method = RequestMethod.POST)
    public String rsa(HttpServletRequest request) {
        return RSA.getPublicKey(RSA.keyPair);
    }

    @RequestMapping(value = "/key/upload/aes", method = RequestMethod.POST)
    public Result receiveAes(HttpServletRequest request) throws Exception {

        System.out.println("====================================/key/upload/aes======================================");

        String username = request.getParameter("username");
        String encryAESKey = request.getParameter("encryAESKey");
        String encryAESIv = request.getParameter("encryAESIv");

        System.out.println("                       =========before decrypto========");
        PrivateKey privateKey = RSA.keyPair.getPrivate();
        byte[] AESKey = RSA.base642Byte(encryAESKey);
        byte[] AESIv = RSA.base642Byte(encryAESIv);

        System.out.println("byte[] AESIv: " + new String(AESIv));
        System.out.println("byte[] AESKey: " + new String(AESKey));

        AESKey = RSA.privateDecrypt(AESKey, privateKey);
        AESIv = RSA.privateDecrypt(AESIv, privateKey);

        System.out.println("                       =========after decrypto========");

        System.out.println("AESIv: " + new String(AESIv));
        System.out.println("AESKey: " + new String(AESKey));

        ValueOperations valueOperations = redisTemplate.opsForValue();
        valueOperations.set(username + "key", AESKey);
        valueOperations.set(username + "iv", AESIv);
        redisTemplate.expire(username + "key",60, TimeUnit.SECONDS);
        redisTemplate.expire(username + "iv", 60, TimeUnit.SECONDS);

        return Result.SUCCESS;
    }

    @RequestMapping(value = "/key/download/aes", method = RequestMethod.POST)
    public String[] sendAes(HttpServletRequest request) throws Exception {

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

        System.out.println("===================================/key/download/aes=====================================");

        String encryAESKey = request.getParameter("encryAESKey");
        String id = email + filename;

        // get from database
        String key = userFileService.queryUserFileById(id).getKey();
        String iv = userFileService.queryUserFileById(id).getIv();
        System.out.println("                       =========before encrypto========");
        System.out.println("encryAESKey: " + encryAESKey);
        System.out.println("byte[] AESKey: " + new String(key));
        System.out.println("byte[] AESIv: " + new String(iv));

        PrivateKey privateKey = RSA.keyPair.getPrivate();
        byte[] AESKey = RSA.base642Byte(encryAESKey);

        AESKey = RSA.privateDecrypt(AESKey, privateKey);

        System.out.println("                       =========after encrypto========");


        System.out.println("encryAESKey: " + new String(AESKey));
        key = AES.aesEncrypt(key, new String(AESKey));
        iv = AES.aesEncrypt(iv, new String(AESKey));
        System.out.println("byte[] AESKey: " + key);
        System.out.println("byte[] AESIv: " + iv);
        return new String[]{key, iv};

    }
}
