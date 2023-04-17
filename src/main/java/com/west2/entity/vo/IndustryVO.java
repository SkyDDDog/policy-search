package com.west2.entity.vo;

import com.west2.entity.IndustryCategory;
import lombok.Data;

import java.util.List;

@Data
public class IndustryVO {

    String classId;

    String className;

    List<IndustryCategory> category;

}
