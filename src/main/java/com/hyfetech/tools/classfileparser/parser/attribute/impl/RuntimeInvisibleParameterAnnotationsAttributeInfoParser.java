package com.hyfetech.tools.classfileparser.parser.attribute.impl;

import com.hyfetech.tools.classfileparser.dto.AnnotationInfo;
import com.hyfetech.tools.classfileparser.dto.AttributeInfo;
import com.hyfetech.tools.classfileparser.parser.attribute.AbstractAttributeInfoParser;
import com.hyfetech.tools.classfileparser.parser.attribute.impl.common.AnnotationParseUtil;
import com.hyfetech.tools.classfileparser.utils.ReadClassFileUtil;

import java.io.InputStream;

/**
 * @author shanghaifei
 * @date 2022/6/2
 */
public class RuntimeInvisibleParameterAnnotationsAttributeInfoParser extends AbstractAttributeInfoParser {
    /**
     * 解析
     * RuntimeInvisibleParameterAnnotations_attribute {
     *   u2 attribute_name_index;
     *   u4 attribute_length;
     *   u1 num_parameters;
     *   {
     *      u2 num_annotations;
     *      annotation annotations[num_annotations];
     *   } parameter_annotations[num_parameters];
     * }
     * @param classFileInputStream 字节码文件输入流
     * @param attributeInfo        属性信息
     */
    @Override
    public void parse(InputStream classFileInputStream, AttributeInfo attributeInfo) throws Exception {
        String prefix = getPrintPrefix(attributeInfo.getLevel());
        /** 参数数量（2字节） */
        int numParameters = ReadClassFileUtil.readU2(classFileInputStream);
        for(int parameterIndex = 0;parameterIndex < numParameters;parameterIndex++) {
            /** 注解数量（2字节） */
            int numAnnotations = ReadClassFileUtil.readU2(classFileInputStream);
            for(int annotationIndex = 0;annotationIndex < numAnnotations;annotationIndex++) {
                /** 注解信息 */
                AnnotationInfo annotationInfo = AnnotationParseUtil.parseAnnotationInfo(classFileInputStream);
                System.out.println(String.format("%s【属性%s】属性名：%s，注解类型：%s",
                        prefix,attributeInfo.getAttributeIndex(),attributeInfo.getAttributeName(),
                        constantPoolParseInfos[annotationInfo.getTypeIndex()]));
            }
        }
    }
}
