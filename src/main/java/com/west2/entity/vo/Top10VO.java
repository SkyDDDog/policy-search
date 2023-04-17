package com.west2.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 搜索词top10统计VO
 * @Author 天狗
 */
@Data
@AllArgsConstructor
@Accessors(chain = true)
public class Top10VO {

    String label;

    Double value;

}
