package com.example.demo.dto.report;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * author: JJJJ
 * date:2021/5/13 14:54
 * Description: TODO
 */
@ApiModel(value = "测试报告数据传输对象")
@Data
public class ReportDto {

    private Integer taskId;

    private String reportUrl;
}
