package com.example.demo.controller;

import com.example.demo.common.ResultDto;
import com.example.demo.common.Token;
import com.example.demo.common.TokenDb;
import com.example.demo.common.UserConstants;
import com.example.demo.dto.TokenDto;
import com.example.demo.dto.testcase.AddTestcaseDto;
import com.example.demo.entity.HogwartsTestcase;
import com.example.demo.service.TestcaseService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import sun.nio.ch.IOUtil;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author: JJJJ
 * @date:2021/5/7 17:26
 * @Description: 测试用例controller
 */

@RestController
@RequestMapping(value = "spring/testcase")
@Api(value = "testcase api")
@Slf4j
public class TestcaseController {

    @Autowired
    TestcaseService testcaseService;

    @Autowired
    TokenDb tokenDb;

    @PostMapping("saveFileCase")
    public ResultDto<HogwartsTestcase> saveFileCase(HttpServletRequest request, @RequestParam("caseFile") MultipartFile caseFile, AddTestcaseDto addTestcaseDto) throws IOException {
       // 1、进行参数校验
        if (caseFile == null){
            return ResultDto.fail("请上传用例文件");
        }
        if(addTestcaseDto == null){
            return ResultDto.fail("用例名称不能为空");
        }
        if(addTestcaseDto.getCaseName() == null)
            return ResultDto.fail("用例名称不能为空");
        // 2、获取到文件中的用例数据
        // 2.1 先拿到inputStream
        InputStream inputStream = caseFile.getInputStream();
        // 2.2 在通过IoUtil 将文件内容转换成字符串·
        String caseData = IOUtils.toString(inputStream);
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
        // 调用service进行新增用例
        return testcaseService.save(userInfo,addTestcaseDto);

    }


}
