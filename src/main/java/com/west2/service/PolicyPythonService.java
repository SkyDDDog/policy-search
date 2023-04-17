package com.west2.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.west2.entity.vo.PolicyCollectVO;
import com.west2.entity.vo.PolicyVO;
import com.west2.util.BeanCustomUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class PolicyPythonService {

    private static String requestUrl;
    @Autowired
    private UserCollectionService userCollectionService;

    @Value("${api.policy.requestUrl}")
    public void setRequestUrl(String requestUrl) {
        PolicyPythonService.requestUrl = requestUrl;
    }

    private static JSONObject getObject(String url) throws IOException {
        return  httpObjectRestClient(url, HttpMethod.GET, null, new HttpHeaders());
    }

    private static JSONArray getArray(String url) throws IOException {
        return  httpArrayRestClient(url, HttpMethod.GET, null, new HttpHeaders());
    }

    private static JSONArray httpArrayRestClient(String url, HttpMethod method, MultiValueMap<String, String> params, HttpHeaders headers) {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(10*1000);
        requestFactory.setReadTimeout(10*1000);
        RestTemplate client = new RestTemplate(requestFactory);
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(params, headers);
        //  执行HTTP请求
        ResponseEntity<JSONArray> response = null;
        try{
            response = client.exchange(url, method, requestEntity, JSONArray.class);
            return response.getBody();
        } catch (HttpClientErrorException e){
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    private static JSONObject httpObjectRestClient(String url, HttpMethod method, MultiValueMap<String, String> params, HttpHeaders headers) throws IOException {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(10*1000);
        requestFactory.setReadTimeout(10*1000);
        RestTemplate client = new RestTemplate(requestFactory);
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(params, headers);
        //  执行HTTP请求
        ResponseEntity<JSONObject> response = null;
        try{
            response = client.exchange(url, method, requestEntity, JSONObject.class);
            return response.getBody();
        } catch (HttpClientErrorException e){
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    public PolicyVO getPolicyById(String policyId) {
        JSONObject json = this.getPolicyJsonById(policyId, false);
        return PolicyVO.convert2PolicyVO(json);
    }

    public PolicyVO getPolicyFirstById(String policyId) {
        JSONObject json = this.getPolicyJsonById(policyId, true);
        return PolicyVO.convert2PolicyVO(json);
    }

    public JSONObject getPolicyJsonById(String policyId, boolean first) {
        try {
            String request = requestUrl + policyId;
            if (first) {
                request += "?first=1";
            }
            log.info(request);
            return getObject(request);

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<PolicyVO> searchPolicy(String key, Integer pageNum, Integer pageSize, String grade,
                                       String province, String start, String end) {
        JSONObject jsonData = searchPolicyJson(key, pageNum, pageSize, grade, province, start, end);
        List<PolicyVO> result;
        if (jsonData!=null) {
            int total = jsonData.getIntValue("total");
            JSONArray array = jsonData.getJSONArray("result");
            result = PolicyVO.convert2PolicyVOList(array);
        } else {
            result = new ArrayList<>();
        }
        return result;
    }

//    public JSONArray searchPolicyJson(String key) {
//        return searchPolicyJson(key, null, null);
//    }
    public JSONObject searchPolicyJson(String key, Integer pageNum, Integer pageSize, String grade, String province, String start, String end) {
        StringBuilder builder = new StringBuilder(requestUrl).append("?")
                .append("key=").append(key);
        if (pageNum != null) {
            builder.append("&")
                    .append("page=").append(pageNum);
        }
        if (pageSize != null) {
            builder.append("&")
                    .append("size=").append(pageSize);
        }
        if (grade != null && !"全部".equals(grade)) {
            builder.append("&")
                    .append("grade=").append(grade);
        }
        if (StringUtils.hasLength(province)) {
            builder.append("&")
                    .append("province=").append(province);
        }
        if (StringUtils.hasLength(start)) {
            builder.append("&")
                    .append("start=").append(start);
        }
        if (StringUtils.hasLength(end)) {
            builder.append("&")
                    .append("end=").append(end);
        }
        String request = builder.toString();
        log.info("requestUrl: {}", request);
        try {
            return getObject(request);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    public PolicyCollectVO buildCollectVO(PolicyVO src, String userId) {
        PolicyCollectVO dst = new PolicyCollectVO();
        BeanCustomUtil.copyProperties(src, dst);
        dst.setCollected(userCollectionService.isCollected(userId, src.getPolicyId()));
        return dst;
    }
    public List<PolicyCollectVO> buildCollectVOList(List<PolicyVO> srcList, String userId) {
        ArrayList<PolicyCollectVO> result = new ArrayList<>();
        for (PolicyVO item : srcList) {
            result.add(this.buildCollectVO(item, userId));
        }
        return result;
    }

}
