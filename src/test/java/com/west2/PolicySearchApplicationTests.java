package com.west2;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.west2.config.JwtProperties;
import com.west2.entity.District;
import com.west2.entity.vo.PolicyVO;
import com.west2.service.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

@Slf4j
@SpringBootTest
class PolicySearchApplicationTests {

    @Autowired
    private JwtProperties jwtProperties;

    @Autowired
    private DistrictService districtService;
    @Autowired
    private CacheService cacheService;
    @Autowired
    private UserCollectionService userCollectionService;
    @Autowired
    private PolicyPythonService policyPythonService;

    @Test
    void testPage() {

        IPage<District> result = districtService.findAllListPage(0, 5, new QueryWrapper<>());
        log.info(result.toString());
        log.info("current: "+result.getCurrent());
        log.info("records: "+result.getRecords());
        log.info("pages: "+result.getPages());
        log.info("size: "+result.getSize());
        log.info("total: "+result.getTotal());

    }

//    @Test
//    void importDatabaseData() throws FileNotFoundException {
//        // 创建tsv解析器settings配置对象
//        TsvParserSettings settings = new TsvParserSettings();
//        // 文件中使用 '\n' 作为行分隔符
//        // 确保像MacOS和Windows这样的系统,也可以正确处理（MacOS使用'\r'；Windows使用'\r\n'）
//        settings.setMaxCharsPerColumn(10000000);
//        settings.getFormat().setLineSeparator("\n");
//        // 创建TSV解析器（将分隔符传入对象）
//        TsvParser parser = new TsvParser(settings);
//        //对tsv文件,调用parseAll
//        List<String[]> allRows = parser.parseAll(new FileInputStream("F:\\各类文档\\FZU\\考试竞赛\\第十四届服创大赛\\policyinfo.tsv"), "GBK");
//        for (int i = 1; i < allRows.size(); i++){	//忽略第一行
////            log.info(String.valueOf(Arrays.asList(allRows.get(i))));
//            Policy policy = buildPolicyEntity(allRows.get(i));
//            if (policy==null) {
//                continue;
//            }
////            log.info(policy.toString());
//            policyService.save(policy);
////            break;
//        }
//        log.info("size: {}", allRows.size());
//        log.info("Done!");
//
//
//        // 在读取结束时自动关闭所有资源，
//        // 或者当错误发生时，可以在任何使用调用stopParsing()
//
//        // 只有在不是读取所有内容的情况下调用下面方法
//        // 但如果不调用也没有非常严重的问题
//        parser.stopParsing();
//    }



//    private Policy buildPolicyEntity(String[] src) {
//        Policy result = new Policy();
//        if (src.length < 13) {
//            log.error("数据长度不正确: {}", Arrays.toString(src));
//            return null;
//        }
//        result.setPolicyId(src[0])
//                .setPolicyTitle(src[1])
//                .setPolicyGrade(src[2])
//                .setPubAgencyId(src[3])
//                .setPubAgency(src[4])
//                .setPubAgencyFullname(src[5])
//                .setPubNumber(src[6])
//                .setPubTime(src[7])
//                .setPolicyType(src[8])
//                .setPolicyBody(src[9])
//                .setProvince(src[10])
//                .setCity(src[11])
//                .setPolicySource(src[12]);
//        return result;
//    }

    @Test
    void addTestChartData() throws InterruptedException {
        String ipBase = "127.0.0.";
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        List<District> districtList = districtService.getByLevel("2");

        for (int i = 0; i < 6; i++) {
            for (District district : districtList) {
                String key = district.getName();
                key = key.substring(0, key.length()-1);
                cacheService.incUserSearch(key, calendar.getTime(), -new Random().nextInt(100));
            }
            for (int j = 0; j < 16; j++) {
                cacheService.incUserAccess(ipBase+j, calendar.getTime());
            }
//            Random random = new Random();
//            cacheService.incUserSearch("福建", calendar.getTime(),random.nextInt(100000));
//            Thread.sleep(1);
//            cacheService.incUserSearch("绿色", calendar.getTime(),random.nextInt(100000));
//            Thread.sleep(1);
//            cacheService.incUserSearch("儿童", calendar.getTime(),random.nextInt(100000));
//            Thread.sleep(1);
//            cacheService.incUserSearch("公安", calendar.getTime(),random.nextInt(100000));
//            Thread.sleep(1);
//            cacheService.incUserSearch("福建", calendar.getTime(),random.nextInt(100000));
//            Thread.sleep(1);
//            cacheService.incUserSearch("图解", calendar.getTime(),random.nextInt(100000));
//            Thread.sleep(1);

//            for (int j = 0; j < 13; j++) {
//                cacheService.incUserSearch("辽宁", calendar.getTime());
//            }
//            for (int j = 0; j < 45; j++) {
//                cacheService.incUserSearch("绿色", calendar.getTime());
//            }
//            for (int j = 0; j < 38; j++) {
//                cacheService.incUserSearch("办公室", calendar.getTime());
//            }
//            for (int j = 0; j < 19; j++) {
//                cacheService.incUserSearch("儿童", calendar.getTime());
//                cacheService.incUserSearch("公安", calendar.getTime());
//            }
            calendar.add(Calendar.DAY_OF_MONTH, -1);
        }



    }


    @Test
    void userCollectTest() {
        boolean collected = userCollectionService.isCollected("1631327384979914753", "1642800964359946244");
        log.info("collected: {}", collected);
    }

    @Test
    void testPythonPolicyById() {
        JSONObject policyById = policyPythonService.getPolicyJsonById("100217048", true);
        log.info(policyById.toJSONString());
        PolicyVO policyVO = PolicyVO.convert2PolicyVO(policyById);
        log.info(policyVO.toString());
    }

    @Test
    void testPythonPolicySearch() {
        JSONObject data = policyPythonService.searchPolicyJson("新能源", null, null,null, null, null, null);
        log.info(data.toJSONString());
    }

}
