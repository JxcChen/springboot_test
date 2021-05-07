package com.example.demo.dto.testcase;

import com.example.demo.dto.BaseDto;
import com.example.demo.entity.BaseEntityNew;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
@ApiModel(value = "添加用例信息传输对象")
public class AddTestcaseDto extends BaseDto {

        /**
         * 用例名称
         */
        @ApiModelProperty(value = "测试用里名称", required = true)
        private String caseName;

        /**
         * 备注
         */
        @ApiModelProperty(value = "用例备注")
        private String remark;


        /**
         * 测试用例内容
         */
        @ApiModelProperty(value = "用例数据")
        private String caseData;
}
