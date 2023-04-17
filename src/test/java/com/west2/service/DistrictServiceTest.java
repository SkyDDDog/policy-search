package com.west2.service;

import com.west2.entity.vo.DistrictVO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@Service
class DistrictServiceTest {

    @Autowired
    private DistrictService districtService;

    @Test
    void get() {
    }


    @Test
    void testGetDistrict() {
        List<DistrictVO> district = districtService.getDistrict();
        log.info(Arrays.toString(district.stream().toArray()));
    }
}