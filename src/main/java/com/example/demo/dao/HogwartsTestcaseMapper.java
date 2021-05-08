package com.example.demo.dao;

import com.example.demo.common.MySqlExtensionMapper;
import com.example.demo.dto.testcase.QueryTestcaseDto;
import com.example.demo.entity.HogwartsTestcase;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HogwartsTestcaseMapper extends MySqlExtensionMapper<HogwartsTestcase> {

    HogwartsTestcase selectOneCaseByName(@Param("caseId") Integer caseId,@Param("caseName") String caseName);

    Integer countCaseNum (@Param("param")QueryTestcaseDto param);

    List<HogwartsTestcase> getCaseList(@Param("param")QueryTestcaseDto param,@Param("pageNum")int pageNum,@Param("pageSize")int pageSize );
}