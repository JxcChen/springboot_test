package com.example.demo.service.impl;

import com.example.demo.common.Constants;
import com.example.demo.common.PageTableRequest;
import com.example.demo.common.PageTableResponse;
import com.example.demo.common.ResultDto;
import com.example.demo.dao.HogwartsTestcaseMapper;
import com.example.demo.dto.TokenDto;
import com.example.demo.dto.testcase.AddTestcaseDto;
import com.example.demo.dto.testcase.QueryTestcaseDto;
import com.example.demo.dto.testcase.UpdateTestcaseDto;
import com.example.demo.entity.HogwartsTestcase;
import com.example.demo.service.TestcaseService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class TestcaseServiceImpl implements TestcaseService {
    @Autowired
    HogwartsTestcaseMapper testcaseMapper;

    /**
     * 新增测试用例
     * @param userInfo 当前登录用户信息
     * @param addTestcaseDto 需要添加的用例信息
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultDto<HogwartsTestcase> save(TokenDto userInfo, AddTestcaseDto addTestcaseDto) {
        // 判断测试用例名称是否已经存在
        HogwartsTestcase testcase = new HogwartsTestcase();
        testcase.setCaseName(addTestcaseDto.getCaseName());
        HogwartsTestcase isExist = testcaseMapper.selectOne(testcase);
        if (isExist != null)
            return ResultDto.fail("用例名称已经存在");
        // 1、声明一个HogwartsTestcase 将 addTestcaseDto的值赋给实例对象
        HogwartsTestcase hogwartsTestcase = new HogwartsTestcase();
        BeanUtils.copyProperties(addTestcaseDto,hogwartsTestcase);
        // 2、设置其他非空信息
        // 设置创建人ID
        hogwartsTestcase.setCreateUserId(userInfo.getUserID());
        // 设置创建和修改时间时间
        hogwartsTestcase.setCreateTime(new Date());
        hogwartsTestcase.setUpdateTime(new Date());
        hogwartsTestcase.setDelFlag(Constants.DEL_FLAG_ONE);
        // 3、将数据存入数据库
        testcaseMapper.insert(hogwartsTestcase);
        return ResultDto.success("添加成功",hogwartsTestcase);
    }



    /**
     * 修改用例信息
     * @param userInfo 当前用户信息
     * @param updateTestcaseDto 修改的测试用例信息
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultDto<HogwartsTestcase> updateCase(TokenDto userInfo, UpdateTestcaseDto updateTestcaseDto) {
        // 1 判断用例名称是否重复
        Integer userID = userInfo.getUserID();
        String caseName = updateTestcaseDto.getCaseName();
        Integer caseId = updateTestcaseDto.getCaseId();
        HogwartsTestcase isExist = testcaseMapper.selectOneCaseByName(caseId, caseName);
        if (isExist != null)
            return ResultDto.fail("用例名称已存在");
        // 2 查看是否有权限进行修改
        HogwartsTestcase hogwartsTestcase = new HogwartsTestcase();
        hogwartsTestcase.setCreateUserId(userID);
        hogwartsTestcase.setId(caseId);
        HogwartsTestcase existCase = testcaseMapper.selectOne(hogwartsTestcase);
        if (existCase == null)
            return ResultDto.fail("当前用户无权限对该用例修改");
        if (existCase.getDelFlag() == 0)
            return ResultDto.fail("该用例不存在或者已经被删除");
        // 3 获取创建时间
        BeanUtils.copyProperties(updateTestcaseDto,hogwartsTestcase);
        hogwartsTestcase.setCreateTime(existCase.getCreateTime());
        hogwartsTestcase.setUpdateTime(new Date());
        // 4 进行数据库修改
        testcaseMapper.updateByPrimaryKeySelective(hogwartsTestcase);
        return ResultDto.success("修改成功",hogwartsTestcase);
    }

    /**
     * 删除用例
     * @param userID 用户id
     * @param caseId 用例id
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultDto deleteCase(Integer userID, Integer caseId) {
        // 判断是否有权限进行删除
        HogwartsTestcase hogwartsTestcase = new HogwartsTestcase();
        hogwartsTestcase.setCreateUserId(userID);
        hogwartsTestcase.setId(caseId);

        HogwartsTestcase existCase = testcaseMapper.selectOne(hogwartsTestcase);
        if (existCase == null)
            return ResultDto.fail("当前用户没有权限删除该用例");
        if (existCase.getDelFlag() == 0)
            return ResultDto.fail("该用例不存在或者已经被删除");
        // 进行删除  修改删除标记
        hogwartsTestcase.setDelFlag(Constants.DEL_FLAG_ZERO);
        testcaseMapper.updateByPrimaryKeySelective(hogwartsTestcase);
        return ResultDto.success("删除成功");
    }

    /**
     * 获取用例列表
     * @param pageTableRequest 查询分页列表需要的信息
     * @return
     */
    @Override
    public ResultDto<PageTableResponse<HogwartsTestcase>> getUserCaseList(PageTableRequest<QueryTestcaseDto> pageTableRequest) {
        QueryTestcaseDto param = pageTableRequest.getParam();
        Integer pageNum = pageTableRequest.getPageNum();
        Integer pageSize = pageTableRequest.getPageSize();

        // 进行查询
        // 查询总数量
        Integer countCaseNum = testcaseMapper.countCaseNum(param);
        // 查询列表
        List<HogwartsTestcase> caseList = testcaseMapper.getCaseList(param, (pageNum - 1) * pageSize, pageSize);
        // 封装分页响应
        PageTableResponse<HogwartsTestcase> pageTableResponse = new PageTableResponse<HogwartsTestcase>();
        pageTableResponse.setRecordsTotal(countCaseNum);
        pageTableResponse.setData(caseList);
        return ResultDto.success("查询成功",pageTableResponse);
    }

    /**
     * 根据用户id和用例id查询用例
     * @param userID 用户id
     * @param caseId 用例id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultDto<HogwartsTestcase> getCaseById(Integer userID, Integer caseId) {
        HogwartsTestcase hogwartsTestcase = new HogwartsTestcase();
        hogwartsTestcase.setCreateUserId(userID);
        hogwartsTestcase.setId(caseId);
        // 进行查询
        HogwartsTestcase existCase = testcaseMapper.selectOne(hogwartsTestcase);
        if (existCase == null)
            return ResultDto.fail("该用例不存在");
        if (existCase.getDelFlag() == 0)
            return ResultDto.fail("该用例不存在或者已经被删除");
        return ResultDto.success("成功",existCase);
    }

    /**
     * 根据用户id和用例id查询用例数据  直接返回数据字符串
     * @param userID 用户id
     * @param caseId 用例id
     * @return
     */
    @Override
    public String getCaseDataById(Integer userID, Integer caseId) {
        HogwartsTestcase hogwartsTestcase = new HogwartsTestcase();
        hogwartsTestcase.setCreateUserId(userID);
        hogwartsTestcase.setId(caseId);
        // 进行查询
        HogwartsTestcase existCase = testcaseMapper.selectOne(hogwartsTestcase);
        if (existCase == null)
            return "该用例不存在";
        if (existCase.getDelFlag() == 0)
            return "该用例不存在或者已经被删除";
        return existCase.getCaseData();
    }
}
