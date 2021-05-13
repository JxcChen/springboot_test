package com.example.demo.service;

import com.example.demo.common.ResultDto;
import com.example.demo.dto.TokenDto;
import com.example.demo.dto.report.ReportDto;

/**
 * @author: JJJJ
 * @date:2021/5/13 14:59
 * @Description: TODO
 */
public interface ReportService {
    ResultDto<ReportDto> getReport(TokenDto userInfo, Integer taskId);
}
