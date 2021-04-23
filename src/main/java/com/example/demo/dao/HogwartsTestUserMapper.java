package com.example.demo.dao;

import com.example.demo.common.MySqlExtensionMapper;
import com.example.demo.entity.HogwartsTestUser;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HogwartsTestUserMapper extends MySqlExtensionMapper<HogwartsTestUser> {

    List<HogwartsTestUser> getUserByName(@Param("userName") String userName);
}