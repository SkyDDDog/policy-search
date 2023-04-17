package com.west2.service;

import com.west2.entity.District;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * 测试服务
 */
@Slf4j
@Service
public class TestService {

    @Autowired
    private CacheService cacheService;
    @Autowired
    private DistrictService districtService;

    public void test() throws InterruptedException {
        String ipBase = "127.0.0.";
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        List<District> districtList = districtService.getByLevel("0");

        for (int i = 0; i < 6; i++) {
            for (District district : districtList) {
                String key = district.getName();
                key = key.replaceAll("市", "").replaceAll("自治区", "").replaceAll("省", "");
                cacheService.incUserSearch(key, calendar.getTime(), new Random().nextInt(100));
            }
            for (int j = 0; j < 16; j++) {
                cacheService.incUserAccess(ipBase+j, calendar.getTime());
            }
            Random random = new Random();
            cacheService.incUserSearch("福建", calendar.getTime(),random.nextInt(100));
            Thread.sleep(1);
            cacheService.incUserSearch("绿色", calendar.getTime(),random.nextInt(100));
            Thread.sleep(1);
            cacheService.incUserSearch("儿童", calendar.getTime(),random.nextInt(100));
            Thread.sleep(1);
            cacheService.incUserSearch("公安", calendar.getTime(),random.nextInt(100));
            Thread.sleep(1);
            cacheService.incUserSearch("福建", calendar.getTime(),random.nextInt(100));
            Thread.sleep(1);
            cacheService.incUserSearch("图解", calendar.getTime(),random.nextInt(100));
            Thread.sleep(1);

            calendar.add(Calendar.DAY_OF_MONTH, -1);
        }

    }

}
