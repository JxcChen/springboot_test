package com.example.demo.dao;

import com.example.demo.common.MySqlExtensionMapper;
import com.example.demo.dto.LoginUserDto;
import com.example.demo.entity.HogwartsTestUser;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HogwartsTestUserMapper extends MySqlExtensionMapper<HogwartsTestUser> {
    /**
     * 通过用户名模糊查询
     * @param userName 用户名
     * @return List<HogwartsTestUser>
     */
    List<HogwartsTestUser> getUserByName(@Param("userName") String userName);

    /**
     * 通过用户名进行精确查询
     * @param userName 登录用户名
     * @return HogwartsTestUser
     */
    HogwartsTestUser selectOneByUserName(@Param("userName") String userName);
}