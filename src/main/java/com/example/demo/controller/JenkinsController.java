package com.example.demo.controller;

import com.example.demo.common.*;
import com.example.demo.dto.TokenDto;
import com.example.demo.dto.jenkins.AddJenkinsDto;
import com.example.demo.dto.jenkins.QueryJenkinsListDto;
import com.example.demo.dto.jenkins.UpdateJenkinsDto;
import com.example.demo.entity.HogwartsTestJenkins;
import com.example.demo.service.JenkinsService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author: JJJJ
 * @date:2021/4/26 11:07
 * @Description: 对jenkins进行操控
 */

@RestController
@RequestMapping(value = "spring/jenkins")
@Api(value = "jenkins api")
@Slf4j
public class JenkinsController {
    @Autowired
    private JenkinsService jenkinsService;

    @Autowired
    private TokenDb tokenDb;


    @PostMapping("/addJenkins")
    public ResultDto<HogwartsTestJenkins> addJenkins(@RequestBody AddJenkinsDto addJenkinsDto, HttpServletRequest request) throws Exception {
        log.info("新增jenkins");
        // 先对必要信息进行非空判断
        if (addJenkinsDto == null){
            return ResultDto.fail("jenkins相关信息不能为空");
        }
        if(addJenkinsDto.getName() == null || addJenkinsDto.getName().equals(""))
            return ResultDto.fail("jenkins名称不能为空");
        // 获取到token
        String token = request.getHeader(UserConstants.LOGIN_TOKEN);

        // 根据token获取到tokenDto
        TokenDto userInfo = tokenDb.getUserInfo(token);
        // 设置jenkins createUserId
        addJenkinsDto.setCreateUserId(userInfo.getUserID());

        return jenkinsService.save(userInfo,addJenkinsDto);
    }


    @PutMapping("/update")
    public ResultDto<HogwartsTestJenkins> updateJenkins(HttpServletRequest request,@RequestBody UpdateJenkinsDto updateJenkinsDto){
        // 校验必要参数
        if (updateJenkinsDto == null){
            return ResultDto.fail("请先选中需要删除的Jenkins");
        }
        Integer id = updateJenkinsDto.getId();
        if (id == null){
            return ResultDto.fail("请先选中需要删除的Jenkins");
        }

        HogwartsTestJenkins hogwartsTestJenkins = new HogwartsTestJenkins();
        BeanUtils.copyProperties(updateJenkinsDto,hogwartsTestJenkins);
        // 修改后缀
        String commandRunCaseSuffix = hogwartsTestJenkins.getCommandRunCaseSuffix();
        if (commandRunCaseSuffix.startsWith(".")){
            String newCommand = commandRunCaseSuffix.replace(".", "");
            hogwartsTestJenkins.setCommandRunCaseSuffix(newCommand);
        }

        // 获取tokenDto
        TokenDto userInfo = tokenDb.getUserInfo(request.getHeader(UserConstants.LOGIN_TOKEN));

        return jenkinsService.update(userInfo,hogwartsTestJenkins);
    }

    /**
     * 根据登录用户查询Jenkins列表接口
     * @param request http请求
     * @param pageTableRequest 查询jenkins列表数据
     * @return
     */
    @GetMapping("/list")
    public ResultDto<PageTableResponse<HogwartsTestJenkins>> getJenkinsList(HttpServletRequest request, PageTableRequest<QueryJenkinsListDto> pageTableRequest){
        // 校验数据
        if(pageTableRequest == null){
            return ResultDto.fail("查询条件不得为空");
        }
        log.info("查询jenkins列表数据");
        // 通过token获取用户数据
        TokenDto userInfo = tokenDb.getUserInfo(request.getHeader(UserConstants.LOGIN_TOKEN));
        // 判断特定查询参数是否存在
        QueryJenkinsListDto queryJenkinsListDto = pageTableRequest.getParam();
        if (queryJenkinsListDto == null){
            // 创建一个queryJenkinsListDto
            queryJenkinsListDto = new QueryJenkinsListDto();
            queryJenkinsListDto.setCreateUserId(userInfo.getUserID());
        }
        pageTableRequest.setParam(queryJenkinsListDto);

        return jenkinsService.list(pageTableRequest);

    }

    @DeleteMapping("/delete")
    public ResultDto delete(HttpServletRequest request,@RequestParam Integer jenkinsId){
        log.info("删除id为："+jenkinsId+"的jenkins");
        if (jenkinsId == null){
            return ResultDto.fail("请选择你要删除的jenkins");
        }
        TokenDto userInfo = tokenDb.getUserInfo(request.getHeader(UserConstants.LOGIN_TOKEN));
        return jenkinsService.delete(userInfo,jenkinsId);
    }
}
