package com.west2.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.west2.entity.IndustryCategory;
import com.west2.entity.IndustryClass;
import com.west2.entity.vo.IndustryVO;
import com.west2.mapper.IndustryCategoryMapper;
import com.west2.service.base.CrudService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class IndustryCategoryService extends CrudService<IndustryCategoryMapper, IndustryCategory> {

    @Autowired
    private IndustryClassService industryClassService;

    public List<IndustryVO> getIndustryCategory() {
        List<IndustryClass> classList = industryClassService.findList(new QueryWrapper<>());
        ArrayList<IndustryVO> result = new ArrayList<>();
        for (IndustryClass ic : classList) {
            IndustryVO vo = new IndustryVO();
            QueryWrapper<IndustryCategory> wrapper = new QueryWrapper<>();
            wrapper.eq("class_id", ic.getId());
            List<IndustryCategory> list = this.findList(wrapper);
            vo.setClassId(ic.getId());
            vo.setClassName(ic.getIndustryClass());
            vo.setCategory(list);
            result.add(vo);
        }
        return result;
    }

    public String getIndustryCategoryName(String id) {
        IndustryCategory industryCategory = this.get(id);
        if (industryCategory == null) {
            log.info("industryCategory is null: {}", id);
            return null;
        }
        return industryCategory.getIndustryCategory();
    }

}
