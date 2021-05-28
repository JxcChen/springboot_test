package com.example.demo.dao;

import com.example.demo.common.MySqlExtensionMapper;
import com.example.demo.dto.task.QueryCaseCountOrStatusDto;
import com.example.demo.dto.task.QueryHogwartsTestTaskListDto;
import com.example.demo.entity.HogwartsTestTask;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HogwartsTestTaskMapper extends MySqlExtensionMapper<HogwartsTestTask> {

    Integer count(@Param("param") QueryHogwartsTestTaskListDto param);

    List<HogwartsTestTask> list(@Param("param") QueryHogwartsTestTaskListDto param,@Param("pageNum") int pageNum,@Param("pageSize") Integer pageSize);

    Integer selectCaseCountById(@Param("param") QueryCaseCountOrStatusDto queryCaseCountOrStatusDto);

    Integer selectTaskStatusById(@Param("param") QueryCaseCountOrStatusDto queryCaseCountOrStatusDto);
}