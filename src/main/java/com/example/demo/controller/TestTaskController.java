package com.example.demo.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.common.*;

import com.example.demo.dto.TokenDto;
import com.example.demo.dto.UpdateTaskStatusDto;
import com.example.demo.dto.task.*;
import com.example.demo.entity.HogwartsTestTask;
import com.example.demo.service.TestTaskService;
import com.example.demo.utils.CustomStrUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * author: JJJJ
 * date:2021/5/11 10:49
 * Description: TODO
 */
@RequestMapping(value = "spring/testTask")
@Api(tags = "TestTask Api")
@RestController
@Slf4j
public class TestTaskController {

    @Autowired
    TestTaskService testTaskService;

    @Autowired
    TokenDb tokenDb;

    @ApiOperation(value = "添加测试任务")
    @PostMapping("addTestTask")
    public ResultDto<HogwartsTestTask> addTestTask(HttpServletRequest request, @RequestBody TestTaskDto testTaskDto){
        // 进行必要信息非空判断
        if (testTaskDto == null)
            return ResultDto.fail("测试任务相关信息不能为空");
        AddHogwartsTestTaskDto addHogwartsTestTaskDto = testTaskDto.getAddHogwartsTestTaskDto();
        if (addHogwartsTestTaskDto == null)
            return ResultDto.fail("添加的测试任务信息不能为空");
        String taskName = addHogwartsTestTaskDto.getName();
        if (taskName == null || taskName.equals(""))
            return ResultDto.fail("测试任务名称不能为空");
        if (testTaskDto.getTestTaskIdList() == null || testTaskDto.getTestTaskIdList().size()==0)
            return ResultDto.fail("请选择需要执行的测试用例");
        // 获取token
        log.info("添加名字为: "+taskName+"的测试任务");
        String token = request.getHeader(UserConstants.LOGIN_TOKEN);
        TokenDto userInfo = tokenDb.getUserInfo(token);
        // 设置任务创建者id
        addHogwartsTestTaskDto.setCreateUserId(userInfo.getUserID());
        // 判断是否选择jenkins
        if (addHogwartsTestTaskDto.getTestJenkinsId() == null)
            addHogwartsTestTaskDto.setTestJenkinsId(userInfo.getDefaultJenkinsId());
        testTaskDto.setAddHogwartsTestTaskDto(addHogwartsTestTaskDto);
        return testTaskService.save(testTaskDto, Constants.TASK_TYPE_ONE);
    }

    @ApiOperation(value = "删除测试任务")
    @DeleteMapping("deleteTestTask")
    public ResultDto delete(HttpServletRequest request,@RequestParam Integer taskId){
        // 进行非空校验
        if (taskId == null)
            return ResultDto.fail("请选择要删除的task");
        log.info("删除id为"+taskId+"的测试任务 ");
        String token = request.getHeader(UserConstants.LOGIN_TOKEN);
        Integer userID = tokenDb.getUserInfo(token).getUserID();
        return testTaskService.delete(userID,taskId);
    }

    @ApiOperation(value = "修改测试任务")
    @PutMapping("updateTestTask")
    public ResultDto<HogwartsTestTask> update(HttpServletRequest request, @RequestBody UpdateHogwartsTestTaskDto updateHogwartsTestTaskDto){

        log.info("修改测试任务-入参= "+ JSONObject.toJSONString(updateHogwartsTestTaskDto));

        if(Objects.isNull(updateHogwartsTestTaskDto)){
            return ResultDto.fail("测试任务信息不能为空");
        }

        Integer taskId = updateHogwartsTestTaskDto.getId();
        String name = updateHogwartsTestTaskDto.getName();

        if(Objects.isNull(taskId)){
            return ResultDto.fail("任务id不能为空");
        }


        if(name == null || name.equals("")){
            return ResultDto.fail("任务名称不能为空");
        }

        HogwartsTestTask hogwartsTestTask = new HogwartsTestTask();
        BeanUtils.copyProperties(updateHogwartsTestTaskDto,hogwartsTestTask);

        TokenDto tokenDto = tokenDb.getUserInfo(request.getHeader(UserConstants.LOGIN_TOKEN));
        hogwartsTestTask.setCreateUserId(tokenDto.getUserID());

        ResultDto<HogwartsTestTask> resultDto = testTaskService.update(hogwartsTestTask);
        return resultDto;
    }


    @ApiOperation(value = "列表查询")
    @GetMapping("/list")
    public ResultDto<PageTableResponse<HogwartsTestTask>> list(HttpServletRequest request, PageTableRequest<QueryHogwartsTestTaskListDto> pageTableRequest){

        log.info("任务列表查询-入参= "+ JSONObject.toJSONString(pageTableRequest));

        if(Objects.isNull(pageTableRequest)){
            return ResultDto.success("列表查询参数不能为空");
        }
        // 获取token
        TokenDto tokenDto = tokenDb.getUserInfo(request.getHeader(UserConstants.LOGIN_TOKEN));
        QueryHogwartsTestTaskListDto params = pageTableRequest.getParam();

        if(Objects.isNull(params)){
            params = new QueryHogwartsTestTaskListDto();
        }
        // 设置创建者id
        params.setCreateUserId(tokenDto.getUserID());
        pageTableRequest.setParam(params);
        // 调用service 获取分页列表响应
        ResultDto<PageTableResponse<HogwartsTestTask>> responseResultDto = testTaskService.list(pageTableRequest);
        return responseResultDto;
    }

    @ApiOperation(value = "获取单个测试任务")
    @GetMapping("/{testTaskId}")
    public ResultDto<HogwartsTestTask> getTestTaskById(HttpServletRequest request,@PathVariable Integer testTaskId){
        // 进行非空判断
        if (testTaskId == null)
            return ResultDto.fail("测试任务ID不得为空");
        log.info("查找id为: "+testTaskId+"的测试任务");
        // 获取当前用户id
        String token = request.getHeader(UserConstants.LOGIN_TOKEN);
        Integer userID = tokenDb.getUserInfo(token).getUserID();
        return testTaskService.getTestTaskById(userID,testTaskId);
    }

    @ApiOperation(value = "开始测试", notes = "开始测试-说明", httpMethod = "POST", response = ResultDto.class)
    @PostMapping("startTest")
    public ResultDto startTest(HttpServletRequest request, @RequestBody StartTestTaskDto startTestTaskDto){
        // 进行数据非空判断
        if (startTestTaskDto == null)
            return ResultDto.fail("请选择你要执行的测试任务");
        Integer taskId = startTestTaskDto.getTaskId();
        if (taskId == null)
            return ResultDto.fail("请选择你要执行的测试任务");
        log.info("开始执行id为:"+taskId+"测试任务");
        // 获取token
        String token = request.getHeader(UserConstants.LOGIN_TOKEN);
        TokenDto userInfo = tokenDb.getUserInfo(token);
        // 封装测试任务对象
        HogwartsTestTask hogwartsTestTask = new HogwartsTestTask();
        hogwartsTestTask.setId(taskId);
        hogwartsTestTask.setCreateUserId(userInfo.getUserID());
        // 获取请求base_url
        StringBuffer url = request.getRequestURL();
        String baseUrl = CustomStrUtils.getBaseUrl(url.toString());
        // 封装requestInfo
        RequestInfoDto requestInfoDto = new RequestInfoDto();
        requestInfoDto.setBaseUrl(baseUrl);
        requestInfoDto.setRequestUrl(baseUrl);
        requestInfoDto.setToken(token);
        // service层执行任务
        try {
            return testTaskService.startTask(userInfo,hogwartsTestTask,requestInfoDto);
        } catch (Exception e) {
            String tips = "创建jenkins_job中存在错误:" + e.getMessage();
            throw new ServiceException(tips);
        }


    }

    @ApiOperation(value = "修改测试状态", httpMethod = "POST", response = ResultDto.class)
    @PutMapping("changeStatus")
    public ResultDto changeStatus(HttpServletRequest request, @RequestBody UpdateTaskStatusDto updateTaskStatusDto){
        // 进行非空判断
        if (updateTaskStatusDto == null)
            return ResultDto.fail("更新状态数据不能为空");
        Integer taskId = updateTaskStatusDto.getTaskId();
        if (taskId == null)
            return ResultDto.fail("请选择你需要修改的测试任务");
        Integer status = updateTaskStatusDto.getStatus();
        if (status == null)
            return ResultDto.fail("请选择测试任务改变后的状态");
        String buildUrl = updateTaskStatusDto.getBuildUrl();
        if (CustomStrUtils.isEmpty(buildUrl))
            return ResultDto.fail("构建路径不能为空");
        log.info("对id为:"+taskId+"的测试任务状态进行修改 修改后状态:"+status);
        // 获取token
        String token = request.getHeader(UserConstants.LOGIN_TOKEN);
        Integer userID = tokenDb.getUserInfo(token).getUserID();
        HogwartsTestTask hogwartsTestTask = new HogwartsTestTask();
        hogwartsTestTask.setStatus(status);
        hogwartsTestTask.setId(taskId);
        hogwartsTestTask.setCreateUserId(userID);
        hogwartsTestTask.setBuildUrl(buildUrl);
        // 调用service进行状态修改
        return testTaskService.changeStatus(hogwartsTestTask);
    }

}
