package com.example.demo.controller;

import com.example.demo.common.ResultDto;
import com.example.demo.common.TokenDb;
import com.example.demo.common.UserConstants;
import com.example.demo.dto.TokenDto;
import com.example.demo.dto.report.ReportDto;
import com.example.demo.service.ReportService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * author: JJJJ
 * date:2021/5/13 14:45
 * Description: 测试报告controller
 */
@RestController
@Slf4j
@Api(tags = "测试报告controller")
@RequestMapping("spring/report")
public class ReportController {

    @Autowired
    TokenDb tokenDb;

    @Autowired
    ReportService reportService;

    /**
     *
     * @param request http请求
     * @param taskId 测试任务id
     */
    @ApiOperation(value = "获取测试报告")
    @GetMapping("/{taskId}")
    public ResultDto<ReportDto> getReport(HttpServletRequest request, @PathVariable Integer taskId){
        if (taskId == null)
            return ResultDto.fail("请选择你需要查看结果的任务");
        log.info("获取id为: "+taskId+"的任务测试报告");
        String token = request.getHeader(UserConstants.LOGIN_TOKEN);
        TokenDto userInfo = tokenDb.getUserInfo(token);
        ResultDto<ReportDto> resultDto = reportService.getReport(userInfo,taskId);
        return resultDto;
    }
}
