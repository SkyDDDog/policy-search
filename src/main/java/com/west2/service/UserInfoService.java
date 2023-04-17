package com.west2.service;

import com.west2.entity.District;
import com.west2.entity.IndustryCategory;
import com.west2.entity.UserDetail;
import com.west2.entity.UserInfo;
import com.west2.entity.vo.UserInfoVO;
import com.west2.mapper.UserInfoMapper;
import com.west2.service.base.CrudService;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class UserInfoService extends CrudService<UserInfoMapper, UserInfo> {

    @Autowired
    private UserDetailService userDetailService;
    @Autowired
    private DistrictService districtService;
    @Autowired
    private IndustryCategoryService industryCategoryService;

    public UserInfoVO getUserInfoVO(String id) {
        UserInfo userInfo = this.get(id);
        if (userInfo == null) {
            log.info("userInfo is null: {}", id);
            return null;
        }
        return this.buildUserInfoVO(userInfo);
    }

    public UserInfoVO buildUserInfoVO(UserInfo src) {
        UserDetail userDetail = userDetailService.get(src.getId());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        UserInfoVO result = new UserInfoVO();
        result.setCompany(src.getCompanyName())
                .setIndustry(src.getIndustryCategory())
                .setIndustry(industryCategoryService.getIndustryCategoryName(src.getIndustryCategory()))
                .setDistrict(districtService.getDistrictName(src.getDistrict()))
                .setDistrictCode(this.getDistrictCodeList(src.getDistrict()))
                .setIndustryCode(this.getIndustryCodeList(src.getIndustryCategory()))
//                .setDistrict(src.getDistrict())
                .setFund(src.getFund())
                .setFoundDate(sdf.format(src.getFoundDate()))
                .setEmail(userDetail.getUsername());
        return result;
    }

    private List<String> getDistrictCodeList(String innerCode) {
        ArrayList<String> result = new ArrayList<>(3);
        result.add(innerCode);
        while ( !"0".equals(innerCode)) {
            String pid = districtService.getPid(innerCode);
            District district = districtService.get(pid);
            if (district==null) {
                break;
            }
            innerCode = district.getCode();
            result.add(innerCode);
        }
        Collections.reverse(result);
        return result;
    }

    private List<String> getIndustryCodeList(String id) {
        ArrayList<String> result = new ArrayList<>(2);
        IndustryCategory ic = industryCategoryService.get(id);
        result.add(ic.getClassId());
        result.add(ic.getId());
        return result;
    }

}
