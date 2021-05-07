package com.example.demo.service.impl;

import com.example.demo.common.ResultDto;
import com.example.demo.dao.HogwartsTestcaseMapper;
import com.example.demo.dto.TokenDto;
import com.example.demo.dto.testcase.AddTestcaseDto;
import com.example.demo.entity.HogwartsTestcase;
import com.example.demo.service.TestcaseService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TestcaseServiceImpl implements TestcaseService {
    @Autowired
    HogwartsTestcaseMapper testcaseMapper;
    @Override
    public ResultDto<HogwartsTestcase> save(TokenDto userInfo, AddTestcaseDto addTestcaseDto) {
        // 1、声明一个HogwartsTestcase 将 addTestcaseDto的值赋给实例对象
        HogwartsTestcase hogwartsTestcase = new HogwartsTestcase();
        BeanUtils.copyProperties(addTestcaseDto,hogwartsTestcase);
        // 2、设置其他非空信息
        // 设置创建人ID
        hogwartsTestcase.setId(userInfo.getUserID());
        // 设置创建和修改时间时间
        hogwartsTestcase.setCreateTime(new Date());
        hogwartsTestcase.setUpdateTime(new Date());
        hogwartsTestcase.setDelFlag(0);
        // 3、将数据存入数据库
        testcaseMapper.insert(hogwartsTestcase);
        return ResultDto.success("添加成功",hogwartsTestcase);
    }
}
