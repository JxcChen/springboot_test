package com.example.demo.service;

import com.example.demo.common.ResultDto;
import com.example.demo.dto.TokenDto;
import com.example.demo.dto.testcase.AddTestcaseDto;
import com.example.demo.entity.HogwartsTestcase;

public interface TestcaseService {
    ResultDto<HogwartsTestcase> save(TokenDto userInfo, AddTestcaseDto addTestcaseDto);
}
