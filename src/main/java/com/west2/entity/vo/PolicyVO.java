package com.west2.entity.vo;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

/**
 * 政策信息视图层
 */
@Data
@Accessors(chain = true)
public class PolicyVO {

    private String city;
    private String policyBody;
    private String policyGrade;
    private String policyId;
    private String policySource;
    private String policyTitle;
    private String policyType;
    private String province;
    private String pubAgency;
    private String pubAgencyFullname;
    private String pubAgencyId;
    private String pubNumber;
    private String pubTime;
    private int readCount;
    private String updateDate;

    public static PolicyVO convert2PolicyVO(JSONObject jsonData) {
        PolicyVO policyVO = new PolicyVO();
        policyVO.setCity(jsonData.getString("CITY"))
                .setPolicyBody(jsonData.getString("POLICY_BODY"))
                .setPolicyGrade(jsonData.getString("POLICY_GRADE"))
                .setPolicyId(jsonData.getString("POLICY_ID"))
                .setPolicySource(jsonData.getString("POLICY_SOURCE"))
                .setPolicyTitle(jsonData.getString("POLICY_TITLE"))
                .setPolicyType(jsonData.getString("POLICY_TYPE"))
                .setProvince(jsonData.getString("PROVINCE"))
                .setPubAgency(jsonData.getString("PUB_AGENCY"))
                .setPubAgencyFullname(jsonData.getString("PUB_AGENCY_FULLNAME"))
                .setPubAgencyId(jsonData.getString("PUB_AGENCY_ID"))
                .setPubNumber(jsonData.getString("PUB_NUMBER"))
                .setPubTime(jsonData.getString("PUB_TIME").substring(0, 10))
                .setReadCount(jsonData.getIntValue("READING"))
                .setUpdateDate(jsonData.getString("UPDATE_DATE").substring(0, 10));
        return policyVO;
    }

    public static List<PolicyVO> convert2PolicyVOList(JSONArray jsonArray) {
        List<PolicyVO> policyVOList = new ArrayList<>();
        if (jsonArray==null) {
            return policyVOList;
        }
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            PolicyVO policyVO = convert2PolicyVO(jsonObject);
            policyVOList.add(policyVO);
        }
        return policyVOList;
    }

}
