package com.example.demo.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.common.*;
import com.example.demo.dao.HogwartsTestJenkinsMapper;
import com.example.demo.dao.HogwartsTestTaskMapper;
import com.example.demo.dao.HogwartsTestUserMapper;
import com.example.demo.dao.HogwartsTestcaseMapper;
import com.example.demo.dto.TokenDto;
import com.example.demo.dto.jenkins.OperateJenkinsDto;
import com.example.demo.dto.task.*;
import com.example.demo.entity.HogwartsTestJenkins;
import com.example.demo.entity.HogwartsTestTask;
import com.example.demo.entity.HogwartsTestUser;
import com.example.demo.entity.HogwartsTestcase;
import com.example.demo.service.TestTaskService;
import com.example.demo.utils.CustomStrUtils;
import com.example.demo.utils.TestCommandUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * author: JJJJ
 * date:2021/5/11 10:52
 * Description: TODO
 */
@Service
@Slf4j
public class TestTaskServiceImpl implements TestTaskService {
    @Autowired
    HogwartsTestcaseMapper testcaseMapper;

    @Autowired
    HogwartsTestJenkinsMapper jenkinsMapper;

    @Autowired
    HogwartsTestTaskMapper testTaskMapper;

    @Autowired
    HogwartsTestUserMapper userMapper;
    @Autowired
    JenkinsClient jenkinsClient;

    /**
     * 添加测试任务
     * @param testTaskDto 测试任务信息
     * @param taskTypeOne 测试任务类型
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultDto<HogwartsTestTask> save(TestTaskDto testTaskDto, Integer taskTypeOne) {
        // 先判断本地是否存在jenkins
        AddHogwartsTestTaskDto addHogwartsTestTaskDto = testTaskDto.getAddHogwartsTestTaskDto();
        Integer jenkinsId = addHogwartsTestTaskDto.getTestJenkinsId();
        Integer createUserId = addHogwartsTestTaskDto.getCreateUserId();
        HogwartsTestJenkins queryJenkins = new HogwartsTestJenkins();
        queryJenkins.setId(jenkinsId);
        queryJenkins.setCreateUserId(createUserId);
        HogwartsTestJenkins hogwartsTestJenkins = jenkinsMapper.selectOne(queryJenkins);
        if (hogwartsTestJenkins == null)
            return ResultDto.fail("所选择的jenkins不存在");
        // 获取测试用例列表
        // 需要先将id集合转化成id字符串
        String idStr = CustomStrUtils.list2String(testTaskDto.getTestTaskIdList());
        // 获取测试用例列表
        List<HogwartsTestcase> hogwartsTestcaseList = testcaseMapper.selectByIds(idStr);
        // 声明一个容器存放命令
        StringBuilder testCommand = new StringBuilder();
        // 调用方法对测试命令进行拼接
        TestCommandUtils.spliceCommand(testCommand,hogwartsTestJenkins,hogwartsTestcaseList);

        // 分装所有测试任务数据
        HogwartsTestTask hogwartsTestTask = new HogwartsTestTask();
        hogwartsTestTask.setCreateUserId(createUserId);
        hogwartsTestTask.setTaskType(taskTypeOne);
        hogwartsTestTask.setTestCommand(testCommand.toString());
        hogwartsTestTask.setName(addHogwartsTestTaskDto.getName());
        hogwartsTestTask.setRemark(addHogwartsTestTaskDto.getRemark());
        hogwartsTestTask.setTestJenkinsId(jenkinsId);
        hogwartsTestTask.setStatus(Constants.STATUS_ONE);
        hogwartsTestTask.setCreateTime(new Date());
        hogwartsTestTask.setUpdateTime(new Date());
        hogwartsTestTask.setCaseCount(hogwartsTestcaseList.size());


        // 数据落库
        testTaskMapper.insert(hogwartsTestTask);

        return ResultDto.success("添加成功",hogwartsTestTask);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultDto<HogwartsTestTask> update(HogwartsTestTask hogwartsTestTask) {
        HogwartsTestTask queryHogwartsTestTask = new HogwartsTestTask();

        queryHogwartsTestTask.setId(hogwartsTestTask.getId());
        queryHogwartsTestTask.setCreateUserId(hogwartsTestTask.getCreateUserId());

        HogwartsTestTask result = testTaskMapper.selectOne(queryHogwartsTestTask);

        //如果为空，则提示，也可以直接返回成功
        if (Objects.isNull(result)) {
            return ResultDto.fail("未查到测试任务信息");
        }

        result.setUpdateTime(new Date());
        result.setName(hogwartsTestTask.getName());
        result.setRemark(hogwartsTestTask.getRemark());

        testTaskMapper.updateByPrimaryKeySelective(result);

        return ResultDto.success("成功",result);
    }

    /**
     * 获取单个测试任务
     * @param userID 用户id
     * @param testTaskId 测试任务id
     */
    @Override
    public ResultDto<HogwartsTestTask> getTestTaskById(Integer userID, Integer testTaskId) {
        HogwartsTestTask queryTestTask = new HogwartsTestTask();
        queryTestTask.setCreateUserId(userID);
        queryTestTask.setId(testTaskId);
        HogwartsTestTask hogwartsTestTask = testTaskMapper.selectOne(queryTestTask);
        if (hogwartsTestTask == null)
            return ResultDto.fail("所查询的测试任务不存在");

        return ResultDto.success("success",hogwartsTestTask);
    }

    /**
     * 查询测试任务信息列表
     * @param pageTableRequest 分页查询请求数据
     */
    @Override
    public ResultDto<PageTableResponse<HogwartsTestTask>> list(PageTableRequest<QueryHogwartsTestTaskListDto> pageTableRequest) {
        QueryHogwartsTestTaskListDto params = pageTableRequest.getParam();
        Integer pageNum = pageTableRequest.getPageNum();
        Integer pageSize = pageTableRequest.getPageSize();

        //总数
        Integer recordsTotal = testTaskMapper.count(params);

        //分页查询数据
        List<HogwartsTestTask> hogwartsTestJenkinsList = testTaskMapper.list(params, (pageNum - 1) * pageSize, pageSize);

        PageTableResponse<HogwartsTestTask> hogwartsTestJenkinsPageTableResponse = new PageTableResponse<>();
        hogwartsTestJenkinsPageTableResponse.setRecordsTotal(recordsTotal);
        hogwartsTestJenkinsPageTableResponse.setData(hogwartsTestJenkinsList);

        return ResultDto.success("成功", hogwartsTestJenkinsPageTableResponse);
    }

    /**
     * 根据任务id删除任务
     * @param userID 当前用户id
     * @param taskId 测试任务id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultDto delete(Integer userID, Integer taskId) {
        // 查询数据库中是否存在相应的task
        HogwartsTestTask queryTestTask = new HogwartsTestTask();
        queryTestTask.setId(taskId);
        HogwartsTestTask hogwartsTestTask = testTaskMapper.selectOne(queryTestTask);
        if (hogwartsTestTask == null)
            return ResultDto.fail("该测试任务不存在或者已被删除");
        if (!hogwartsTestTask.getCreateUserId().equals(userID))
            return ResultDto.fail("当前用户无权限删除该任务");
        testTaskMapper.deleteByIds(taskId.toString());
        return ResultDto.success("删除成功");
    }

    /**
     * 开始执行测试任务
     * @param userInfo 当前登录用户信息
     * @param hogwartsTestTask 测试任务信息
     * @param requestInfoDto 请求jenkins 数据传输对象
     */
    @Override
    public ResultDto startTask(TokenDto userInfo, HogwartsTestTask hogwartsTestTask, RequestInfoDto requestInfoDto) throws Exception {
        // 判断任务是否存在
        HogwartsTestTask resultTestTask = testTaskMapper.selectOne(hogwartsTestTask);
        if (resultTestTask == null)
            return ResultDto.fail("所选择的任务不存在");
        // 获取jenkins
        Integer jenkinsId = resultTestTask.getTestJenkinsId();
        // 判断jenkins是否存在
        HogwartsTestJenkins queryTestJenkins = new HogwartsTestJenkins();
        queryTestJenkins.setId(jenkinsId);
        queryTestJenkins.setCreateUserId(resultTestTask.getCreateUserId());
        HogwartsTestJenkins resultJenkins = jenkinsMapper.selectOne(queryTestJenkins);
        if (resultJenkins == null){
            // 如果任务中没有指定jenkins  直接调用默认jenkins
            HogwartsTestUser hogwartsTestUser = new HogwartsTestUser();
            hogwartsTestUser.setId(hogwartsTestTask.getCreateUserId());
            HogwartsTestUser resultUser = userMapper.selectOne(hogwartsTestUser);
            Integer defaultJenkinsId = resultUser.getDefaultJenkinsId();
            queryTestJenkins.setId(defaultJenkinsId);
            resultJenkins = jenkinsMapper.selectOne(queryTestJenkins);
            if (resultJenkins == null)
                return ResultDto.fail("所要调用jenkins不存在");
        }
        // 获取测试任务中测试命令
        String runTestCommand = hogwartsTestTask.getTestCommand();
        if (CustomStrUtils.isEmpty(runTestCommand)){
            runTestCommand = resultTestTask.getTestCommand();
            if (CustomStrUtils.isEmpty(runTestCommand))
                return ResultDto.fail("测试命令不能为空");
        }

        // 更新任务状态  改成执行中 2
        resultTestTask.setStatus(Constants.STATUS_TWO);
        testTaskMapper.updateByPrimaryKeySelective(resultTestTask);

        // 新声明一个新的命令储存容器  需要执行测试的后续命令
        StringBuilder stbTestCommand = new StringBuilder();
        stbTestCommand.append(runTestCommand);
        stbTestCommand.append("\n");

        // 拼接改变任务状态的命令
        StringBuilder updateStatusCommand = TestCommandUtils.getUpdateStatusCommand(requestInfoDto,resultTestTask);
        // jenkins构建参数封装
        HashMap<String,String> jenkinsBuildCommand = new HashMap<>();
        // 设置请求域和端口
        jenkinsBuildCommand.put("aitestBaseUrl",requestInfoDto.getBaseUrl());
        // 设置请求token
        jenkinsBuildCommand.put("token",requestInfoDto.getToken());
        // 设置运行命令
        jenkinsBuildCommand.put("testCommand",runTestCommand);
        // 设置修改任务状态的命令
        jenkinsBuildCommand.put("updateStatusData",updateStatusCommand.toString());

        log.info("=====执行测试Job的构建参数组装====：" +JSONObject.toJSONString(jenkinsBuildCommand));
        log.info("=====执行测试Job的修改任务状态的数据组装====：" +updateStatusCommand);

        // 将构建参数传给jenkins进行构建
        OperateJenkinsDto operateJenkinsDto = new OperateJenkinsDto();
        operateJenkinsDto.setTokenDto(userInfo);
        operateJenkinsDto.setHogwartsTestJenkins(resultJenkins);
        operateJenkinsDto.setParams(jenkinsBuildCommand);

        // 调用jenkinsClient 对jenkins进行构建
        ResultDto resultDto = jenkinsClient.operateJenkins(operateJenkinsDto);
        // 调用jenkinsClient进行构建
        return resultDto;
    }


    @Override
    public ResultDto changeStatus(HogwartsTestTask hogwartsTestTask) {
        // 先查询本地任务是否存在
        HogwartsTestTask queryTask = new HogwartsTestTask();
        queryTask.setId(hogwartsTestTask.getId());
        queryTask.setCreateUserId(hogwartsTestTask.getCreateUserId());
        HogwartsTestTask resultTask = testTaskMapper.selectOne(queryTask);
        if (resultTask == null)
            return ResultDto.fail("所需要修改的测任务不存在或已失效");

        // 查看已有任务状态 若已经是测试完成则不需要修改
        if (resultTask.getStatus().equals(Constants.STATUS_THREE))
            return ResultDto.fail("任务已执行完成无需在改变状态");
        // 查看需要修改的状态  只要3的状态才需要进行修改
        if (hogwartsTestTask.getStatus().equals(Constants.STATUS_THREE)){
            resultTask.setStatus(Constants.STATUS_THREE);
            resultTask.setBuildUrl(hogwartsTestTask.getBuildUrl());
            resultTask.setUpdateTime(new Date());
            testTaskMapper.updateByPrimaryKeySelective(resultTask);
        }
        return ResultDto.success("修改成功");

    }

    @Override
    public ResultDto<Integer> getCaseCount(QueryCaseCountDto queryCaseCountDto) {
        HogwartsTestTask queryTask = new HogwartsTestTask();
        queryTask.setId(queryCaseCountDto.getTaskId());
        queryTask.setCreateUserId(queryCaseCountDto.getCreateUserId());
        HogwartsTestTask hogwartsTestTask = testTaskMapper.selectOne(queryTask);
        if (hogwartsTestTask == null)
            return ResultDto.fail("查询的的任务不存在");
        Integer count = testTaskMapper.selectCaseCountById(queryCaseCountDto);

        return ResultDto.success("查询成功",count);
    }


}
