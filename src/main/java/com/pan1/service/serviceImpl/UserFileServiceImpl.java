package com.pan1.service.serviceImpl;

import com.pan1.dao.UserFileMapper;
import com.pan1.dao.UserMapper;
import com.pan1.entity.UserFile;
import com.pan1.service.UserFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class UserFileServiceImpl implements UserFileService {

    @Autowired
    UserFileMapper userFileMapper;

    @Autowired
    UserMapper userMapper;

    @Override
    public List<UserFile> queryUserFileList() {
        return userFileMapper.queryUserFileList();
    }

    @Override
    public List<UserFile> queryUserFileByEmail(String email) {
        return userFileMapper.queryUserFileByEmail(email);
    }

    @Override
    public UserFile queryUserFileById(String id) {
        return userFileMapper.queryUserFileById(id);
    }

    @Override
    public int addUserFile(UserFile userFile) {
        return userFileMapper.addUserFile(userFile);
    }

    @Override
    public int deleteUserFile(String id) {
        return userFileMapper.deleteUserFile(id);
    }

    @Override
    public double queryMemoryByGroupname(String groupname) {
        return userFileMapper.queryMemoryByGroupname(groupname);
    }

    @Override
    public int addUserFileGroup(String groupname, String fileid, double memory) {
        return userFileMapper.addUserFileGroup(groupname, fileid, memory);
    }

    @Override
    public String queryfileidFileById(String fileid) {
        return userFileMapper.queryfileidFileById(fileid);
    }

    @Override
    public int updateGroupMemory(Map map) {
        return userFileMapper.updateGroupMemory(map);
    }

    @Override
    public int deleteUserFileinfo(String fileid) {
        return userFileMapper.deleteUserFileinfo(fileid);
    }

    @Override
    public List<UserFile> queryPublicFileByUsername(String username) {

        ArrayList<UserFile> res = new ArrayList<>();

        String groupName = userMapper.queryUserByUsername(username).getGroup();
        List<String> fileId = userMapper.queryFileidByGroupname(groupName);
        for (String id: fileId)
            res.add(userFileMapper.queryUserFileById(id));
        return res;
    }

    @Override
    public List<UserFile> queryPrivateFileByUsername(String username) {

        ArrayList<UserFile> res = new ArrayList<>();
        List<UserFile> files = userFileMapper.queryUserFileByEmail(userMapper.queryUserByUsername(username).getEmail());

        for (UserFile file : files)
            if (file.getStatus() == 0 || file.getStatus() == 2)
                res.add(file);
        return res;
    }

    @Override
    public int updateStatus(Map map) {
        return userFileMapper.updateStatus(map);
    }

    @Override
    public String queryfileidBygroupname(String groupname, String filename) {

        List<String> files = userFileMapper.queryfileidBygroupname(groupname);
        for (String file : files) {
            String[] temp = file.split(filename);
            if (!temp[0].equals(filename))
                return file;
        }
        return null;
    }
}
