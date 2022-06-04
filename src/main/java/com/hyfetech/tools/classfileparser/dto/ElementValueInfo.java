package com.hyfetech.tools.classfileparser.dto;

import lombok.Data;

/**
 * 元素值信息
 * @author shanghaifei
 * @date 2022/6/2
 */
@Data
public class ElementValueInfo {
    short tag;

    int constValueIndex;

    int typeNameIndex;

    int constNameIndex;

    int classInfoIndex;

    AnnotationInfo annotationValue;

    int numValues;

    ElementValueInfo[] values;
}
