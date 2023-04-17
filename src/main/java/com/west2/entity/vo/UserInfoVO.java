package com.west2.entity.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 用户信息VO
 * @author 天狗
 */
@Data
@Accessors(chain = true)
public class UserInfoVO {

    private String company;
    private String industry;
    private String district;
    private String fund;
    private String foundDate;
    private String email;
    private List<String> industryCode;
    private List<String> districtCode;



}
