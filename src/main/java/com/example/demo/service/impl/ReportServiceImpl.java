package com.example.demo.service.impl;

import com.example.demo.common.Constants;
import com.example.demo.common.ResultDto;
import com.example.demo.dao.HogwartsTestJenkinsMapper;
import com.example.demo.dao.HogwartsTestTaskMapper;
import com.example.demo.dto.TokenDto;
import com.example.demo.dto.report.ReportDto;
import com.example.demo.entity.HogwartsTestJenkins;
import com.example.demo.entity.HogwartsTestTask;
import com.example.demo.service.ReportService;
import com.example.demo.utils.CustomStrUtils;
import com.example.demo.utils.ReportUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * author: JJJJ
 * date:2021/5/13 14:59
 * Description: TODO
 */
@Service
public class ReportServiceImpl implements ReportService {
    @Autowired
    HogwartsTestTaskMapper taskMapper;

    @Autowired
    HogwartsTestJenkinsMapper jenkinsMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultDto<ReportDto> getReport(TokenDto userInfo, Integer taskId) {
        // 查询本地测试任务
        HogwartsTestTask hogwartsTestTask = new HogwartsTestTask();
        hogwartsTestTask.setId(taskId);
        hogwartsTestTask.setCreateUserId(userInfo.getUserID());
        HogwartsTestTask resultTask = taskMapper.selectOne(hogwartsTestTask);
        if (resultTask == null)
            return ResultDto.fail("任务不存在或者已经失效");
        String buildUrl = resultTask.getBuildUrl();
        if (CustomStrUtils.isEmpty(buildUrl))
            return ResultDto.fail("任务构建地址为空");
        if (resultTask.getTestJenkinsId() == null)
            return ResultDto.fail("任务构建地址为空");
        // 先查询测试任务状态查看是否已经执行完成
        if (!resultTask.getStatus().equals(Constants.STATUS_THREE)){
            return ResultDto.fail("该任务还未执行");
        }
        HogwartsTestJenkins queryJenkins = new HogwartsTestJenkins();
        queryJenkins.setId(resultTask.getTestJenkinsId());
        HogwartsTestJenkins resultJenkins = jenkinsMapper.selectOne(queryJenkins);
        // 获取测试报告路径
        String reportUrl = ReportUtils.getReportUrl(buildUrl, resultJenkins, 1);
        ReportDto reportDto = new ReportDto();
        reportDto.setReportUrl(reportUrl);
        reportDto.setTaskId(taskId);
        return ResultDto.success("成功",reportDto);
    }
}
