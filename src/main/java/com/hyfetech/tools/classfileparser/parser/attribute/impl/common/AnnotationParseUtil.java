package com.hyfetech.tools.classfileparser.parser.attribute.impl.common;

import com.hyfetech.tools.classfileparser.dto.AnnotationInfo;
import com.hyfetech.tools.classfileparser.dto.ElementValueInfo;
import com.hyfetech.tools.classfileparser.dto.enums.ElementTagEnum;
import com.hyfetech.tools.classfileparser.dto.enums.ElementValueEnum;
import com.hyfetech.tools.classfileparser.utils.ReadClassFileUtil;

import java.io.InputStream;
import java.util.HashMap;

/**
 * 【注解】属性信息解析通用类
 * @author shanghaifei
 * @date 2022/6/2
 */
public class AnnotationParseUtil {
    /**
     * 解析注解信息
     * annotation {
     *  u2 type_index;
     *  u2 num_element_value_pairs;
     *  {
     *      u2 element_name_index;
     *      element_value value;
     *  } element_value_pairs[num_element_value_pairs];
     * }
     * @param classFileInputStream 字节码文件输入流
     * @return 注解信息
     * @throws Exception
     */
    public static AnnotationInfo parseAnnotationInfo(InputStream classFileInputStream)
            throws Exception {
        AnnotationInfo annotationInfo = new AnnotationInfo();
        /** 类型索引（2字节） */
        int typeIndex = ReadClassFileUtil.readU2(classFileInputStream);
        /** 元素值数量（2字节） */
        int numElementValuePairs = ReadClassFileUtil.readU2(classFileInputStream);
        HashMap<Integer,ElementValueInfo> elementValuePairs = new HashMap<>();
        for(int i = 0;i < numElementValuePairs;i++) {
            /** 元素名称索引（2字节） */
            int elementNameIndex = ReadClassFileUtil.readU2(classFileInputStream);
            /** 元素值（element_value） */
            ElementValueInfo elementValueInfo = parseElementValueInfo(classFileInputStream);
            elementValuePairs.put(elementNameIndex,elementValueInfo);
        }
        annotationInfo.setTypeIndex(typeIndex);
        annotationInfo.setNumElementValuePairs(numElementValuePairs);
        annotationInfo.setElementValuePairs(elementValuePairs);
        return annotationInfo;
    }

    /**
     * 解析元素值信息
     * element_value {
     *  u1 tag;
     *  union {
     *      u2 const_value_index;
     *      {
     *          u2 type_name_index;
     *          u2 const_name_index;
     *      } enum_const_value;
     *      u2 class_info_index;
     *      annotation annotation_value;
     *      {
     *          u2 num_values;
     *          element_value values[num_values];
     *      } array_value;
     *  } value;
     * }
     * @param classFileInputStream 字节码文件输入流
     * @return 元素值信息
     */
    public static ElementValueInfo parseElementValueInfo(InputStream classFileInputStream)
            throws Exception {
        ElementValueInfo result = new ElementValueInfo();
        /** tag（1字节） */
        short tag = ReadClassFileUtil.readU1(classFileInputStream);
        ElementValueEnum elementValueEnum = ElementTagEnum.getEnumByTag(tag).getValue();
        result.setTag(tag);
        if(elementValueEnum == ElementValueEnum.CONST_VALUE_INDEX) {
            /** 常量值索引（2字节） */
            int constValueIndex = ReadClassFileUtil.readU2(classFileInputStream);
            result.setConstValueIndex(constValueIndex);
        }
        else if(elementValueEnum == ElementValueEnum.ENUM_CONST_VALUE) {
            /** 类型名称（2字节） */
            int typeNameIndex = ReadClassFileUtil.readU2(classFileInputStream);
            /** 常量名称（2字节） */
            int constNameIndex = ReadClassFileUtil.readU2(classFileInputStream);
            result.setTypeNameIndex(typeNameIndex);
            result.setConstNameIndex(constNameIndex);
        }
        else if(elementValueEnum == ElementValueEnum.CLASS_INFO_INDEX) {
            /** 类信息索引（2字节） */
            int classInfoIndex = ReadClassFileUtil.readU2(classFileInputStream);
            result.setClassInfoIndex(classInfoIndex);
        }
        else if(elementValueEnum == ElementValueEnum.ANNOTATION_VALUE) {
            /** 注解值（annotation类型） */
            AnnotationInfo annotationValue = parseAnnotationInfo(classFileInputStream);
            result.setAnnotationValue(annotationValue);
        }
        else if(elementValueEnum == ElementValueEnum.ARRAY_VALUE) {
            /** value数量（2字节） */
            int numValues = ReadClassFileUtil.readU2(classFileInputStream);
            ElementValueInfo[] values = new ElementValueInfo[numValues];
            for (int i = 0; i < numValues; i++) {
                /** 元素值信息（element_value类型） */
                ElementValueInfo elementValueInfo = parseElementValueInfo(classFileInputStream);
                values[i] = elementValueInfo;
            }
            result.setNumValues(numValues);
            result.setValues(values);
        }
        return result;
    }
}
