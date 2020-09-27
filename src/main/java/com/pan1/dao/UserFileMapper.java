package com.pan1.dao;

import com.pan1.entity.UserFile;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface UserFileMapper {

    List<UserFile> queryUserFileList();

    List<UserFile> queryUserFileByEmail(String email);

    List<UserFile> queryUserFileByGroupname(String groupname);

    UserFile queryUserFileById(String id);

    String queryfileidFileById(String fileid);

    double queryMemoryByGroupname(String groupname);

    int addUserFile(UserFile userFile);

    int addUserFileGroup(String groupname, String fileid, double memory);

    int updateGroupMemory(Map map);

    int deleteUserFile(String id);

    int deleteUserFileinfo(String fileid);

    int updateStatus(Map map);

    List<String> queryfileidBygroupname(String groupname);

}
