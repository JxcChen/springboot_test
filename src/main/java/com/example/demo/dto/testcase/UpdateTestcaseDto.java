package com.example.demo.dto.testcase;

import com.example.demo.dto.BaseDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
@ApiModel(value = "添加用例信息传输对象")
public class UpdateTestcaseDto extends BaseDto {

        /**
         * 用例ID
         */
        @ApiModelProperty(value = "测试用例id", required = true)
        private Integer caseId;

        /**
         * 用例名称
         */
        @ApiModelProperty(value = "测试用例名称",required = true)
        private String caseName;

        /**
         * 备注
         */
        @ApiModelProperty(value = "用例备注")
        private String remark;


        /**
         * 测试用例内容
         */
        @ApiModelProperty(value = "用例数据",required = true)
        private String caseData;
}
