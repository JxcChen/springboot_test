package com.example.demo.common;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author: JJJJ
 * @date:2021/4/22 12:02
 * @Description: TODO
 */
@Data
public class PageTableResponse<T> implements Serializable {
    private Integer recordsTotal;
    private List<T> data;
}
