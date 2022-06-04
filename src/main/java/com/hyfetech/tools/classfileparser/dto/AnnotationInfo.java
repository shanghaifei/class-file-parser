package com.hyfetech.tools.classfileparser.dto;

import lombok.Data;

import java.util.HashMap;

/**
 * 注解信息
 * @author shanghaifei
 * @date 2022/6/2
 */
@Data
public class AnnotationInfo {
    int typeIndex;

    int numElementValuePairs;

    HashMap<Integer,ElementValueInfo> elementValuePairs;
}
