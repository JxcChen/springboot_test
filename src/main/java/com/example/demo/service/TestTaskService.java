package com.example.demo.service;

import com.example.demo.common.PageTableRequest;
import com.example.demo.common.PageTableResponse;
import com.example.demo.common.ResultDto;
import com.example.demo.dto.TokenDto;
import com.example.demo.dto.task.QueryCaseCountOrStatusDto;
import com.example.demo.dto.task.QueryHogwartsTestTaskListDto;
import com.example.demo.dto.task.RequestInfoDto;
import com.example.demo.dto.task.TestTaskDto;
import com.example.demo.entity.HogwartsTestTask;

import java.io.IOException;

/**
 * @author: JJJJ
 * @date:2021/5/11 10:52
 * @Description: TODO
 */
public interface TestTaskService {
    ResultDto<HogwartsTestTask> getTestTaskById(Integer userID, Integer testTaskId);

    ResultDto<HogwartsTestTask> save(TestTaskDto testTaskDto, Integer taskTypeOne);

    ResultDto<HogwartsTestTask> update(HogwartsTestTask hogwartsTestTask);

    ResultDto<PageTableResponse<HogwartsTestTask>> list(PageTableRequest<QueryHogwartsTestTaskListDto> pageTableRequest);

    ResultDto delete(Integer userID, Integer taskId);

    ResultDto startTask(TokenDto userInfo, HogwartsTestTask hogwartsTestTask, RequestInfoDto requestInfoDto) throws IOException, Exception;

    ResultDto changeStatus(HogwartsTestTask hogwartsTestTask);

    ResultDto<Integer> getCaseCount(QueryCaseCountOrStatusDto queryCaseCountOrStatusDto);

    ResultDto<Integer> getTaskStatus(QueryCaseCountOrStatusDto queryTaskStatusDto);
}
