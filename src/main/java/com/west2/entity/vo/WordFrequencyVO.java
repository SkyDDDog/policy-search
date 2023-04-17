package com.west2.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 词频统计VO
 * @author 天狗
 */
@Data
@AllArgsConstructor
@Accessors(chain = true)
public class WordFrequencyVO {

    private String name;
    private Integer value;

}
