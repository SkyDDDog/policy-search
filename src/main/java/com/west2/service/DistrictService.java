package com.west2.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.west2.entity.District;
import com.west2.entity.vo.AreaVO;
import com.west2.entity.vo.CityVO;
import com.west2.entity.vo.DistrictVO;
import com.west2.mapper.DistrictMapper;
import com.west2.service.base.CrudService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class DistrictService extends CrudService<DistrictMapper, District> {

    public List<DistrictVO> getDistrict() {
        QueryWrapper<District> provinceWrapper = new QueryWrapper<>();
        provinceWrapper.eq("level", 0);
        List<District> provinceList = this.findList(provinceWrapper);
        ArrayList<DistrictVO> result = new ArrayList<>(provinceList.size());
        for (District province : provinceList) {
            DistrictVO pro = new DistrictVO();

            QueryWrapper<District> cityWrapper = new QueryWrapper<>();
            cityWrapper.eq("level", 1).
                    eq("pid", province.getId());
            List<District> cityList = this.findList(cityWrapper);
            ArrayList<CityVO> cityResult = new ArrayList<>(cityList.size());
            for (District city : cityList) {
                CityVO ctVO = new CityVO();
                QueryWrapper<District> areaWrapper = new QueryWrapper<>();
                areaWrapper.eq("level", 2)
                        .eq("pid", city.getId());
                List<District> areaList = this.findList(areaWrapper);
                ArrayList<AreaVO> areaResult = new ArrayList<>(areaList.size());
                for (District area : areaList) {
                    AreaVO arVO = new AreaVO();
                    arVO.setAreaId(area.getCode())
                            .setArea(area.getName());
                    areaResult.add(arVO);
                }
                ctVO.setCityId(city.getCode())
                        .setCity(city.getName())
                        .setAreas(areaResult);
                cityResult.add(ctVO);
            }
            pro.setProvinceId(province.getCode()).
                    setProvince(province.getName()).
                    setCitys(cityResult);
            result.add(pro);
        }
        return result;
    }

    public String getDistrictName(String code) {
        QueryWrapper<District> wrapper = new QueryWrapper<>();
        wrapper.eq("code", code);
        District district = this.findOne(wrapper);
        if (district== null){
            log.info("district is null: {}", code);
            return null;
        }
        return district.getName();
    }

    public String getPid(String code) {
        QueryWrapper<District> wrapper = new QueryWrapper<>();
        wrapper.eq("code", code);
        District district = this.findOne(wrapper);
        if (district== null){
            return "0";
        }
        return district.getPid();
    }

    public List<District> getByLevel(String level) {
        QueryWrapper<District> wrapper = new QueryWrapper<>();
        wrapper.eq("level", level);
        return this.findList(wrapper);
    }

    private String getCodeById(String id) {
        District district = this.get(id);
        if (district==null) {
            return null;
        }
        return district.getCode();
    }

}
