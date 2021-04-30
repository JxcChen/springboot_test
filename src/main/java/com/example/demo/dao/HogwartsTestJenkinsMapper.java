package com.example.demo.dao;

import com.example.demo.common.MySqlExtensionMapper;
import com.example.demo.dto.jenkins.QueryJenkinsListDto;
import com.example.demo.entity.HogwartsTestJenkins;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HogwartsTestJenkinsMapper extends MySqlExtensionMapper<HogwartsTestJenkins> {
    /**
     * 查询数据条数
     * @param params 查询条件参数
     * @return
     */
    int count(@Param("params") QueryJenkinsListDto params);

    /**
     * 分页展示
     * @param pageNum 页数
     * @param pageSize 每页显示条数
     * @param params 查询条件参数
     * @return
     */
    List<HogwartsTestJenkins> list(@Param("pageNum") int pageNum, @Param("pageSize") int pageSize, @Param("params") QueryJenkinsListDto params);

    /**
     * 根据创建者ID查询所有jenkins列表
     * @param createUserId 创建者ID
     * @return
     */
    List<HogwartsTestJenkins> selectJenkinsListByCreateUserId(@Param("createUserId") int createUserId);

    /**
     * 将其余DefaultJenkinsFlag设置成0
     * @param createUserId 创建者ID
     * @param jenkinsId JenkinsId
     */
    void changeDefaultFlag(Integer createUserId,Integer jenkinsId);

    /**
     * 查找删除默认jenkins后ID最小的jenkins
     * @param createUserId
     * @return
     */
    HogwartsTestJenkins selectMinIdJenkins(@Param("createUserId") Integer createUserId);
}