package com.example.demo.service.impl;

import com.example.demo.common.PageTableRequest;
import com.example.demo.common.PageTableResponse;
import com.example.demo.common.ResultDto;
import com.example.demo.common.TokenDb;
import com.example.demo.dao.HogwartsTestJenkinsMapper;
import com.example.demo.dao.HogwartsTestUserMapper;
import com.example.demo.dto.JenkinsParamDto;
import com.example.demo.dto.TokenDto;
import com.example.demo.dto.jenkins.AddJenkinsDto;
import com.example.demo.dto.jenkins.QueryJenkinsListDto;
import com.example.demo.entity.HogwartsTestJenkins;
import com.example.demo.entity.HogwartsTestUser;
import com.example.demo.service.JenkinsService;
import com.example.demo.utils.JenkinsUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author: JJJJ
 * @date:2021/4/26 11:19
 * @Description: TODO
 */
@Service
public class JenkinsServiceImpl implements JenkinsService {

    @Autowired
    private HogwartsTestJenkinsMapper hogwartsTestJenkinsMapper;
    @Autowired
    private HogwartsTestUserMapper hogwartsTestUserMapper;
    @Autowired
    private TokenDb tokenDb;

    @Override
    public ResultDto createJob(JenkinsParamDto jenkinsParamDto) throws Exception {
        JenkinsUtil.createJob(jenkinsParamDto);
        return ResultDto.success("构建成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultDto<HogwartsTestJenkins> save(TokenDto userInfo, AddJenkinsDto addJenkinsDto) {
        HogwartsTestJenkins hogwartsTestJenkins = new HogwartsTestJenkins();
        // 1、 将可能存在的.yml .json 修改成yml和json
        String caseSuffix = addJenkinsDto.getCommandRunCaseSuffix();
        if (caseSuffix != null && caseSuffix.startsWith(".")){
            String replaceSuffix = caseSuffix.replace(".", "");
            addJenkinsDto.setCommandRunCaseSuffix(replaceSuffix);
        }
        // 将addJenkinsDto数据赋给hogwartsTestJenkins
        BeanUtils.copyProperties(addJenkinsDto,hogwartsTestJenkins);
        // 设置创建和更新时间
        hogwartsTestJenkins.setCreateTime(new Date());
        hogwartsTestJenkins.setUpdateTime(new Date());
        // 数据落库
        int insert = hogwartsTestJenkinsMapper.insertUseGeneratedKeys(hogwartsTestJenkins);

        // 查看jenkins默认选项是否勾选
        Integer defaultFlag = hogwartsTestJenkins.getDefaultFlag();
        if (defaultFlag != null && defaultFlag == 1){
            // 若勾选了默认 则需要修改用户表中的默认jenkinsId 还需要将别的jenkins设置为非默认
            // 对用户表进行更新
            HogwartsTestUser hogwartsTestUser = new HogwartsTestUser();
            hogwartsTestUser.setId(hogwartsTestJenkins.getCreateUserId());
            hogwartsTestUser.setDefaultJenkinsId(hogwartsTestJenkins.getId());
            hogwartsTestUserMapper.updateByPrimaryKeySelective(hogwartsTestUser);

            // 将其余jenkins DefaultFlag设为0
            hogwartsTestJenkinsMapper.changeDefaultFlag(userInfo.getUserID(),hogwartsTestJenkins.getId());

            // 更新token中的jenkinsId
            userInfo.setDefaultJenkinsId(hogwartsTestJenkins.getId());
            tokenDb.addUserInfo(hogwartsTestUser.getId(),userInfo.getToken(),userInfo);
        }
        return ResultDto.success("添加成功",hogwartsTestJenkins);
    }

    @Override
    public ResultDto<PageTableResponse<HogwartsTestJenkins>> list(PageTableRequest<QueryJenkinsListDto> pageTableRequest) {
        // 获取相关信息
        Integer pageNum = pageTableRequest.getPageNum();
        Integer pageSize = pageTableRequest.getPageSize();
        QueryJenkinsListDto params = pageTableRequest.getParam();
        Integer createUserId = params.getCreateUserId();

        HogwartsTestUser queryUser = new HogwartsTestUser();
        queryUser.setId(createUserId);
        // 根据用户id查询用户 获取默认jenkinsId
        HogwartsTestUser hogwartsTestUser = hogwartsTestUserMapper.selectOne(queryUser);
        Integer defaultJenkinsId = hogwartsTestUser.getDefaultJenkinsId();

        // 查询总数
        int totalNum = hogwartsTestJenkinsMapper.count(params);
        // 分页查询列表
        List<HogwartsTestJenkins> list = hogwartsTestJenkinsMapper.list((pageNum - 1) * pageSize, pageSize, params);

        // 遍历列表设置默认jenkins
        for (HogwartsTestJenkins hogwartsTestJenkins: list
             ) {
            if (hogwartsTestJenkins.getId().equals(defaultJenkinsId)){
                hogwartsTestJenkins.setDefaultFlag(1);
            }
        }

        // 封装返回对象
        PageTableResponse<HogwartsTestJenkins> pageTableResponse = new PageTableResponse();
        pageTableResponse.setRecordsTotal(totalNum);
        pageTableResponse.setData(list);

        return ResultDto.success("成功",pageTableResponse);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultDto<HogwartsTestJenkins> update(TokenDto userInfo, HogwartsTestJenkins hogwartsTestJenkins) {
        // 先判断是否存在该jenkins任务
        Integer jenkinsId = hogwartsTestJenkins.getId();
        HogwartsTestJenkins queryJenkins = new HogwartsTestJenkins();
        queryJenkins.setId(jenkinsId);
        HogwartsTestJenkins existHogwartsTestJenkins = hogwartsTestJenkinsMapper.selectOne(queryJenkins);
        if (existHogwartsTestJenkins == null)
            return ResultDto.fail("要修改的jenkins不存在");

        // 获取创建者ID
        Integer userId = userInfo.getUserID();
        // 设置修改时间
        hogwartsTestJenkins.setUpdateTime(new Date());
        // 进行数据修改
        hogwartsTestJenkinsMapper.updateByPrimaryKeySelective(hogwartsTestJenkins);
        Integer defaultFlag = hogwartsTestJenkins.getDefaultFlag();
        if (defaultFlag != null && defaultFlag == 1){
            // 修改用户表中的默认jenkins
            HogwartsTestUser hogwartsTestUser = new HogwartsTestUser();
            hogwartsTestUser.setId(userId);
            hogwartsTestUser.setDefaultJenkinsId(jenkinsId);
            hogwartsTestUserMapper.updateByPrimaryKeySelective(hogwartsTestUser);
            // 设置token中默认jenkins
            userInfo.setDefaultJenkinsId(jenkinsId);
            // 将其余jenkins DefaultFlag设为0
            hogwartsTestJenkinsMapper.changeDefaultFlag(userId,jenkinsId);
            // 设置token中的默认jenkinsID
            userInfo.setDefaultJenkinsId(jenkinsId);
            tokenDb.addUserInfo(userId,userInfo.getToken(),userInfo);
        }

        return ResultDto.success("修改成功",hogwartsTestJenkinsMapper.selectOne(queryJenkins));
    }

    @Override
    public ResultDto delete(TokenDto userInfo, Integer jenkinsId) {
        // 先判断该jenkins是否存在
        HogwartsTestJenkins hogwartsTestJenkins = new HogwartsTestJenkins();
        hogwartsTestJenkins.setId(jenkinsId);
        HogwartsTestJenkins existJenkins = hogwartsTestJenkinsMapper.selectOne(hogwartsTestJenkins);
        if (existJenkins == null){
            return ResultDto.fail("该jenkins不存在");
        }
        // 删除jenkins
        hogwartsTestJenkinsMapper.deleteByIds(jenkinsId.toString());
        // 查看该jenkins是否为默认jenkins
        Integer defaultFlag = existJenkins.getDefaultFlag();
        if (defaultFlag != null && defaultFlag == 1){
            // 如果所删除的jenkins是默认jenkins 则需要将自动找一个ID最小的jenkins设为默认，还需要将用户中的默认jenkinsId进行修改
            // todo：后续可让用户选择默认jenkins
            Integer userId = userInfo.getUserID();
            // 将其余排第一的jenkins设为默认
            HogwartsTestJenkins minIdJenkins = hogwartsTestJenkinsMapper.selectMinIdJenkins(userId);
            Integer newDefaultId = minIdJenkins.getId();
            minIdJenkins.setDefaultFlag(1);
            hogwartsTestJenkinsMapper.updateByPrimaryKeySelective(minIdJenkins);
            // 设置用户中的默认jenkinsId
            HogwartsTestUser hogwartsTestUser = new HogwartsTestUser();
            hogwartsTestUser.setId(userId);
            hogwartsTestUser.setDefaultJenkinsId(newDefaultId);
            hogwartsTestUserMapper.updateByPrimaryKeySelective(hogwartsTestUser);
            // 设置token中的默认jenkinsID
            userInfo.setDefaultJenkinsId(newDefaultId);
            tokenDb.addUserInfo(userId,userInfo.getToken(),userInfo);
        }

        System.out.println(tokenDb.getUserInfo(userInfo.getToken()).getDefaultJenkinsId());
        return ResultDto.success("删除成功");
    }


}
