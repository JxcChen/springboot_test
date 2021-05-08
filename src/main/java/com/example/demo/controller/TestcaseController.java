package com.example.demo.controller;

import com.example.demo.common.*;
import com.example.demo.dto.TokenDto;
import com.example.demo.dto.testcase.AddTestcaseDto;
import com.example.demo.dto.testcase.QueryTestcaseDto;
import com.example.demo.dto.testcase.UpdateTestcaseDto;
import com.example.demo.entity.HogwartsTestcase;
import com.example.demo.service.TestcaseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sun.nio.ch.IOUtil;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @author: JJJJ
 * @date:2021/5/7 17:26
 * @Description: 测试用例controller
 */

@RestController
@RequestMapping(value = "spring/testcase")
@Api(value = "Testcase Api")
@Slf4j
public class TestcaseController {

    @Autowired
    TestcaseService testcaseService;

    @Autowired
    TokenDb tokenDb;

    /**
     * 以文件方式添加测试用例
     * @param request 请求
     * @param caseFile 测试用例数据文件
     * @param addTestcaseDto 需要添加的测试用例信息
     */
    @ApiOperation(value = "以文件方式添加测试用例")
    @PostMapping("addFileTestcase")
    public ResultDto<HogwartsTestcase> addFileTestcase(HttpServletRequest request, @RequestParam("caseFile") MultipartFile caseFile, AddTestcaseDto addTestcaseDto) throws IOException {
       // 1、进行参数校验
        if (caseFile == null){
            return ResultDto.fail("请上传用例文件");
        }
        if(addTestcaseDto == null){
            return ResultDto.fail("用例名称不能为空");
        }
        String caseName = addTestcaseDto.getCaseName();
        if(caseName == null || caseName.equals(""))
            return ResultDto.fail("用例名称不能为空");
        log.info("添加测试用例 用例名称： "+ caseName);
        // 2、获取到文件中的用例数据
        // 2.1 先拿到inputStream
        InputStream inputStream = caseFile.getInputStream();
        // 2.2 在通过IoUtil 将文件内容转换成字符串
        // IOUtils.toString将流对象内容转换成字符串
        String caseData = IOUtils.toString(inputStream,"UTF-8");
        // 2.3 关闭流对象
        inputStream.close();
        // 3、判断数据是否为空
        if (caseData.equals(""))
            return  ResultDto.fail("测试数据文件不能为空");
        // 4、将测试数据赋给addTestcaseDto
        addTestcaseDto.setCaseData(caseData);
        // 5、获取到当钱token
        String token = request.getHeader(UserConstants.LOGIN_TOKEN);
        TokenDto userInfo = tokenDb.getUserInfo(token);
        // 6、调用service进行新增用例
        return testcaseService.save(userInfo,addTestcaseDto);
    }

    /**
     * 文本形式增加测试用例
     * @param request 请求
     * @param addTestcaseDto 添加测试用例的信息
     * @return
     */
    @ApiOperation(value = "以文本方式添加测试用例")
    @PostMapping("addTestcase")
    public ResultDto<HogwartsTestcase> addTestcase(HttpServletRequest request,@RequestBody AddTestcaseDto addTestcaseDto){
        // 1、先判断必要信息是否非空
        if (addTestcaseDto == null)
            return  ResultDto.fail("带星号为必填项");
        if (addTestcaseDto.getCaseName() == null || addTestcaseDto.getCaseName().equals(""))
            return ResultDto.fail("用例名称不能为空");
        if (addTestcaseDto.getCaseData() == null  || addTestcaseDto.getCaseData().equals(""))
            return ResultDto.fail("测试数据不能为空");
        // 2、获取token
        String token = request.getHeader(UserConstants.LOGIN_TOKEN);
        TokenDto userInfo = tokenDb.getUserInfo(token);
        return testcaseService.save(userInfo,addTestcaseDto);
    }

    /**
     * 通过测试用例ID查找用例
     * @param request 请求
     * @param caseId 用例id
     * @return
     */
    @ApiOperation(value = "通过ID获取用例")
    @GetMapping("/{caseId}")
    public ResultDto<HogwartsTestcase> getCaseById(HttpServletRequest request, @PathVariable Integer caseId){
        // 非空判断
        if (caseId == null)
            return ResultDto.fail("用例Id不能为空");
        log.info("根据用户ID获取id为： " + caseId+"的用例信息");
        // 获取用户id
        String token = request.getHeader(UserConstants.LOGIN_TOKEN);
        Integer userID = tokenDb.getUserInfo(token).getUserID();
        // 调用service进行查询
        return testcaseService.getCaseById(userID,caseId);
    }

    @ApiOperation(value = "修改用例数据")
    @PutMapping("updateCase")
    public ResultDto<HogwartsTestcase> updateCase(HttpServletRequest request,@RequestBody UpdateTestcaseDto updateTestcaseDto){
        // 1 非空判断
        if (updateTestcaseDto == null){
            return ResultDto.fail("请选择要修改的测试用例");
        }
        if (updateTestcaseDto.getCaseId() == null)
            return ResultDto.fail("请选择要修改的测试用例");
        if (updateTestcaseDto.getCaseName() == null || updateTestcaseDto.getCaseName().equals(""))
            return ResultDto.fail("测试用例名称不能为空");
        if (updateTestcaseDto.getCaseData() == null || updateTestcaseDto.getCaseData().equals(""))
            return ResultDto.fail("测试用例数据不能为空");
        log.info("修改测试用例");
        // 2 获取token
        String token = request.getHeader(UserConstants.LOGIN_TOKEN);
        TokenDto userInfo = tokenDb.getUserInfo(token);
        // 2 调用service进行数据更新
        return testcaseService.updateCase(userInfo,updateTestcaseDto);
    }

    @ApiOperation("删除用例")
    @DeleteMapping("deleteCase")
    public ResultDto deleteCase(HttpServletRequest request, @RequestParam Integer caseId){
        // 1 非空判断
        if (caseId == null)
            return ResultDto.fail("请选择你要删除的用例");
        log.info("删除id为: "+ caseId+"的用例");
        // 获取userid
        String token = request.getHeader(UserConstants.LOGIN_TOKEN);
        Integer userID = tokenDb.getUserInfo(token).getUserID();
        return testcaseService.deleteCase(userID,caseId);
    }


    @ApiOperation("根据用户ID分页查找用例列表")
    @GetMapping("getCaseList")
    public ResultDto<PageTableResponse<HogwartsTestcase>> getCaseList(HttpServletRequest request,@RequestParam PageTableRequest<QueryTestcaseDto> pageTableRequest){
        // 必要信息非空判断
        if (pageTableRequest == null)
            return ResultDto.fail("列表查询参数为空");
        log.info("查询用例列表");

        // 获取用户id
        String token = request.getHeader(UserConstants.LOGIN_TOKEN);
        Integer userID = tokenDb.getUserInfo(token).getUserID();
        // 将用户id设置到查询数据中
        QueryTestcaseDto param = pageTableRequest.getParam();
        if (param == null)
            param = new QueryTestcaseDto();

        param.setUserId(userID);
//        if (caseName != null)
//            param.setCaseName(caseName);
        pageTableRequest.setParam(param);
        // 调用service获取用例列表
        return testcaseService.getUserCaseList(pageTableRequest);
    }
}
