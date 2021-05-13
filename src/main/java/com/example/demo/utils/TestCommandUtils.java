package com.example.demo.utils;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.common.Constants;
import com.example.demo.common.ServiceException;
import com.example.demo.dto.task.RequestInfoDto;
import com.example.demo.entity.HogwartsTestJenkins;
import com.example.demo.entity.HogwartsTestTask;
import com.example.demo.entity.HogwartsTestcase;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author: JJJJ
 * @date:2021/5/12 20:09
 * @Description: TODO
 */
@Slf4j
public class TestCommandUtils {

    /**
     * 进行命令拼接
     * @param testCommand 测试命令容器
     * @param hogwartsTestJenkins 执行测试任务的jenkins
     * @param hogwartsTestcaseList 执行的测试用例列表
     */
    public static void spliceCommand(StringBuilder testCommand, HogwartsTestJenkins hogwartsTestJenkins, List<HogwartsTestcase> hogwartsTestcaseList) {
        // 先拼接pwd 并进行换行
        testCommand.append("pwd\n");

        // 获取命令类型 1 文本 2 文件
        Byte commandRunCaseType = hogwartsTestJenkins.getCommandRunCaseType();
        if (commandRunCaseType == 0)
            commandRunCaseType = 1;
        // 获取jenkins启动命令
        String runCommand = hogwartsTestJenkins.getTestCommand();
        // 根据不同的测试命令类型进行 不同的方式拼接
        if (commandRunCaseType == 1){
            // 先拼接jenkins启动命令
            testCommand.append(runCommand);
            // 遍历测试用例 拼接测试数据
            for (HogwartsTestcase hogwartsTestcase:hogwartsTestcaseList
            ) {
                testCommand.append(hogwartsTestcase.getCaseData());
                testCommand.append("\n");
            }
        }
        if (commandRunCaseType == 2){
            // 文件类型的 测试命令需要curl -o 方式 将caseDate存储成文件形式
            // 需要先获取文件形式的后缀名
            String commandRunCaseSuffix = hogwartsTestJenkins.getCommandRunCaseSuffix();
            if (commandRunCaseSuffix == null || commandRunCaseSuffix.equals(""))
                throw new ServiceException("使用文件类型测试命令类型时 文件后缀不能为空");

            for (HogwartsTestcase hogwartsTestcase:hogwartsTestcaseList
            ) {
                // 进行curl命令拼接 获取文件形式测试数据
                curlCommandSplice(testCommand,hogwartsTestcase,commandRunCaseSuffix);
                // 拼接jenkins启动命令
                testCommand.append("\n").append(runCommand);
                // 拼接上测试命令存储文件
                testCommand.append(" ").append(hogwartsTestcase.getCaseName()).append(".").append(commandRunCaseSuffix);
                // 拼接防错命令
                testCommand.append(" || true").append("\n");
            }

        }
        log.info("testCommand.toString()== "+testCommand.toString() + "  runCommand== " + runCommand);
    }


    /**
     * 获取修改任务状态命令
     * @param requestInfoDto 请求参数
     * @param resultTestTask 测试任务
     * @return
     */
    public static StringBuilder getUpdateStatusCommand(RequestInfoDto requestInfoDto, HogwartsTestTask resultTestTask) {

        StringBuilder updateStatusCommand = new StringBuilder();
        // 先拼接 curl命令
        updateStatusCommand.append("curl -X PUT ");
        // 拼接目的路径
        updateStatusCommand.append("\""+requestInfoDto.getBaseUrl()+"/spring/testTask/changeStatus\" ");
        // 拼接需要的请求头
        updateStatusCommand.append("-H \"Content-Type : application/json\"").append(" -H \"token : "+requestInfoDto.getToken()+"\"");
        // 组装json格式参数
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("taskId",resultTestTask.getId());
        jsonObject.put("status", Constants.STATUS_THREE);
        // 构建路径会已参数形式传给jenkins
        jsonObject.put("buildUrl","${BUILD_URL}");
        // 将参数进行拼接  必须以json格式
        updateStatusCommand.append(" -d '"+jsonObject.toJSONString()+"' ");
        return updateStatusCommand;
    }

    /**
     * 组装文件形式curl命令
     * @param testCommand 测试命令容器
     * @param hogwartsTestcase 测试用例
     * @param commandRunCaseSuffix 文件后缀名
     */
    public static void curlCommandSplice(StringBuilder testCommand, HogwartsTestcase hogwartsTestcase, String commandRunCaseSuffix) {
        // 先拼接curl -o命令
        testCommand.append("curl -o");
        // 拼接文件名
        testCommand.append(" ").append(hogwartsTestcase.getCaseName()).append(".").append(commandRunCaseSuffix);
        // 拼接测试数据获取接口url
        testCommand.append(" ").append("${aitestBaseUrl}/spring/testcase/data/").append(hogwartsTestcase.getId());
        // 拼接请求头
        testCommand.append(" -H \"token\":\"${token}\" ");
        // 拼接防错命令
        testCommand.append(" || true");
    }
}
