package com.pan1.service;

import com.pan1.entity.UserFile;

import java.util.List;
import java.util.Map;

public interface UserFileService {

    List<UserFile> queryUserFileList();

    List<UserFile> queryUserFileByEmail(String email);

    UserFile queryUserFileById(String id);

    int addUserFile(UserFile userFile);

    int deleteUserFile(String id);

    double queryMemoryByGroupname(String groupname);

    int addUserFileGroup(String groupname, String fileid, double memory);

    String queryfileidFileById(String fileid);

    int updateGroupMemory(Map map);

    int deleteUserFileinfo(String fileid);

    List<UserFile> queryPublicFileByUsername(String username);

    List<UserFile> queryPrivateFileByUsername(String username);

    int updateStatus(Map map);

    String queryfileidBygroupname(String username, String filename);
}
