package com.west2.service;

import com.west2.entity.vo.Top10VO;
import com.west2.entity.vo.WordFrequencyVO;
import com.west2.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 缓存服务
 * @author 天狗
 */
@Slf4j
@Service
public class CacheService {

    private final SimpleDateFormat KEY_DATE_FORMAT = new SimpleDateFormat("yyyyMMdd");
    private final String REDIS_USER_ACCESS_KEY_PREFIX = "user_access_";
    private final String REDIS_USER_SEARCH_KEY_PREFIX = "user_search_";

    private final long REDIS_CACHE_EXPIRE_HOUR = 24;

    private String buildUserAccessKey() {
        return REDIS_USER_ACCESS_KEY_PREFIX+KEY_DATE_FORMAT.format(new Date());
    }

    private String buildUserSearchKey() {
        return REDIS_USER_SEARCH_KEY_PREFIX+KEY_DATE_FORMAT.format(new Date());
    }

    private String buildUserAccessKey(Date date) {
        return REDIS_USER_ACCESS_KEY_PREFIX+KEY_DATE_FORMAT.format(date);
    }

    private String buildUserSearchKey(Date date) {
        return REDIS_USER_SEARCH_KEY_PREFIX+KEY_DATE_FORMAT.format(date);
    }

    public boolean incUserAccess(String ip) {
        RedisUtil.HashOps.hPut(this.buildUserAccessKey(), ip, "1");
        return RedisUtil.HashOps.hExists(REDIS_USER_ACCESS_KEY_PREFIX, ip);
    }

    public boolean incUserAccess(String ip, Date date) {
        RedisUtil.HashOps.hPut(this.buildUserAccessKey(date), ip, "1");
        return RedisUtil.HashOps.hExists(REDIS_USER_ACCESS_KEY_PREFIX, ip);
    }

    public int getDayUserAccess() {
        return RedisUtil.HashOps.hKeys(this.buildUserAccessKey()).size();
    }

    public int getDayUserAccess(Date date) {
        return RedisUtil.HashOps.hKeys(this.buildUserAccessKey(date)).size();
    }

    public int getWeekUserAccess() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int cnt = 0;
        for (int i = 0; i < 7; i++) {
            cnt += this.getDayUserAccess(calendar.getTime());
            calendar.add(Calendar.DATE, -1);
        }
        return cnt;
    }

    public int getMonthUserAccess() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int cnt = 0;
        for (int i = 0; i < 30; i++) {
            cnt += this.getDayUserAccess(calendar.getTime());
            calendar.add(Calendar.DATE, -1);
        }
        return cnt;
    }

    public int getYearUserAccess() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int cnt = 0;
        for (int i = 0; i < 365; i++) {
            cnt += this.getDayUserAccess(calendar.getTime());
            calendar.add(Calendar.DATE, -1);
        }
        return cnt;
    }

    public List<Integer> getDayUserAccessList() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            list.add(this.getDayUserAccess(calendar.getTime()));
            calendar.add(Calendar.DATE, -1);
        }
        return list;
    }

    public boolean incUserSearch(String key) {
        long cnt = RedisUtil.HashOps.hIncrBy(this.buildUserSearchKey(), key, 1);
        return cnt>0;
    }

    public boolean incUserSearch(String key, Date date) {
        long cnt = RedisUtil.HashOps.hIncrBy(this.buildUserSearchKey(date), key, 1);
        return cnt>0;
    }

    public boolean incUserSearch(String key, Date date, int increment) {
        long cnt = RedisUtil.HashOps.hIncrBy(this.buildUserSearchKey(date), key, increment);
        return cnt>0;
    }


    public int getDayUserSearch() {
        AtomicInteger cnt = new AtomicInteger();
        RedisUtil.HashOps.hGetAll(this.buildUserSearchKey()).forEach((k, v) -> {
            cnt.addAndGet(Integer.parseInt((String) v));
        });
        return cnt.get();
    }

    public int getDayUserSearch(Date date) {
        AtomicInteger cnt = new AtomicInteger();
        RedisUtil.HashOps.hGetAll(this.buildUserSearchKey(date)).forEach((k, v) -> {
            cnt.addAndGet(Integer.parseInt((String) v));
        });
        return cnt.get();
    }

    public int getWeekUserSearch() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int cnt = 0;
        for (int i = 0; i < 7; i++) {
            cnt += this.getDayUserSearch(calendar.getTime());
            calendar.add(Calendar.DATE, -1);
        }
        return cnt;
    }

    public int getMonthUserSearch() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int cnt = 0;
        for (int i = 0; i < 30; i++) {
            cnt += this.getDayUserSearch(calendar.getTime());
            calendar.add(Calendar.DATE, -1);
        }
        return cnt;
    }

    public int getYearUserSearch() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int cnt = 0;
        for (int i = 0; i < 365; i++) {
            cnt += this.getDayUserSearch(calendar.getTime());
            calendar.add(Calendar.DATE, -1);
        }
        return cnt;
    }

    public List<Integer> getDayUserSearchList() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            list.add(this.getDayUserSearch(calendar.getTime()));
            calendar.add(Calendar.DATE, -1);
        }
        return list;
    }

    public List<Top10VO> getTop10Search() {
        ArrayList<Top10VO> result = new ArrayList<>();
        HashMap<String, Integer> searchCount = new HashMap<>();
        AtomicInteger total = new AtomicInteger();
        RedisUtil.KeyOps.keys(REDIS_USER_SEARCH_KEY_PREFIX+"*").forEach(key -> {
            RedisUtil.HashOps.hGetAll(key).forEach((k, v) -> {
                total.addAndGet(Integer.parseInt((String) v));
                searchCount.put((String) k, searchCount.getOrDefault(k, 0) + Integer.parseInt((String) v));
            });
        });
        List<Map.Entry<String, Integer>> list = new ArrayList<>(searchCount.entrySet());
        list.sort((o1, o2) -> o2.getValue().compareTo(o1.getValue()));
        for (int i = 0; i < 10 && i<list.size(); i++) {
            Top10VO top10VO = new Top10VO(list.get(i).getKey(), this.calculateSearchPercent(list.get(i).getValue(), total.get()));
            result.add(top10VO);
        }
        log.info("total: {}",total.get()+"");
        log.info("count: {}",list.get(0).toString());
        log.info("result: {}",result.toString());
        return result;
    }

    private double calculateSearchPercent(int cnt, int total) {
        return (cnt*100.0/total);
    }

    public List<WordFrequencyVO> getSearchWordFrequency() {
        ArrayList<WordFrequencyVO> result = new ArrayList<>();
        HashMap<String, Integer> map = new HashMap<>();
        RedisUtil.KeyOps.keys(this.REDIS_USER_SEARCH_KEY_PREFIX+"*").forEach(key -> {
            RedisUtil.HashOps.hGetAll(key).forEach((k, v) -> {
                map.put((String) k, map.getOrDefault(k, 0) + Integer.parseInt((String) v));
            });
        });
        map.forEach((k, v) -> {
            result.add(new WordFrequencyVO(k, v));
        });
        return result;
    }


}
