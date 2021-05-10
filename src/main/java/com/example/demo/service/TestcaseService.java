package com.example.demo.service;

import com.example.demo.common.PageTableRequest;
import com.example.demo.common.PageTableResponse;
import com.example.demo.common.ResultDto;
import com.example.demo.dto.TokenDto;
import com.example.demo.dto.testcase.AddTestcaseDto;
import com.example.demo.dto.testcase.QueryTestcaseDto;
import com.example.demo.dto.testcase.UpdateTestcaseDto;
import com.example.demo.entity.HogwartsTestcase;

import java.util.List;

public interface TestcaseService {
    ResultDto<HogwartsTestcase> save(TokenDto userInfo, AddTestcaseDto addTestcaseDto);

    ResultDto<HogwartsTestcase> getCaseById(Integer userID, Integer caseId);

    ResultDto<HogwartsTestcase> updateCase(TokenDto userInfo, UpdateTestcaseDto updateTestcaseDto);

    ResultDto deleteCase(Integer userID, Integer caseId);

    ResultDto<PageTableResponse<HogwartsTestcase>> getUserCaseList(PageTableRequest<QueryTestcaseDto> pageTableRequest);

    String getCaseDataById(Integer userID, Integer caseId);
}
