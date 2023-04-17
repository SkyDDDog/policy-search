package com.west2.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.west2.entity.UserCollection;
import com.west2.entity.base.BaseEntity;
import com.west2.entity.vo.PolicyCollectVO;
import com.west2.entity.vo.PolicyVO;
import com.west2.mapper.UserCollectionMapper;
import com.west2.service.base.CrudService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 收藏 Service
 * @author 天狗
 */
@Service
public class UserCollectionService extends CrudService<UserCollectionMapper, UserCollection> {

    @Autowired
    private PolicyPythonService policyPythonService;

    public UserCollection getCollectItem(String userId, String policyId) {
        if (policyPythonService.getPolicyById(policyId)==null) {
            return null;
        }
        QueryWrapper<UserCollection> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId)
                .eq("policy_id", policyId);
        List<UserCollection> list = this.findAllList(wrapper);
        if (list.size()==0) {
            return null;
        } else {
            return list.get(0);
        }
    }

    public boolean isCollected(String userId, String policyId) {
        QueryWrapper<UserCollection> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId)
                .eq("policy_id", policyId);
        return this.findList(wrapper).size()>0;
    }

    public int collectPolicy(String userId, String policyId) {
        UserCollection item = this.getCollectItem(userId, policyId);
        UserCollection userCollection;
        if (item==null) {
            userCollection = new UserCollection();
            userCollection.setUserId(userId);
            userCollection.setPolicyId(policyId);
        } else {
            userCollection = item;
            userCollection.setNewRecord(false);
            userCollection.setDelFlag(BaseEntity.DEL_FLAG_NORMAL);
        }

        return this.save(userCollection);
    }

    public int unCollectPolicy(String userId, String policyId) {
        UserCollection collectItem = this.getCollectItem(userId, policyId);
        if (collectItem==null) {
            return 0;
        }
        return this.delete(collectItem);
    }

    private List<UserCollection> getCollectRecord(String userId) {
        QueryWrapper<UserCollection> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        return this.findList(wrapper);
    }

    public List<PolicyCollectVO> getCollectPolicy(String userId) {
        List<UserCollection> recordList = this.getCollectRecord(userId);
        ArrayList<PolicyCollectVO> result = new ArrayList<>();
        for (UserCollection uc : recordList) {
//            Policy policy = policyService.get(uc.getPolicyId());
            PolicyVO policy = policyPythonService.getPolicyById(uc.getPolicyId());
            PolicyCollectVO vo = new PolicyCollectVO();
            BeanUtils.copyProperties(policy, vo);
            vo.setCollected(true);
            result.add(vo);
        }
        return result;
    }

}
