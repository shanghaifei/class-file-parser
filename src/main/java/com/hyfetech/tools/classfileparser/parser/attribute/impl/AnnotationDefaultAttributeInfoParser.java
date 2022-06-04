package com.hyfetech.tools.classfileparser.parser.attribute.impl;

import com.hyfetech.tools.classfileparser.dto.AttributeInfo;
import com.hyfetech.tools.classfileparser.dto.ElementValueInfo;
import com.hyfetech.tools.classfileparser.parser.attribute.AbstractAttributeInfoParser;
import com.hyfetech.tools.classfileparser.parser.attribute.impl.common.AnnotationParseUtil;

import java.io.InputStream;

/**
 * 【注解默认值】属性信息解析器
 * @author shanghaifei
 * @date 2022/6/3
 */
public class AnnotationDefaultAttributeInfoParser extends AbstractAttributeInfoParser {
    /**
     * 解析
     * AnnotationDefault_attribute {
     *  u2 attribute_name_index;
     *  u4 attribute_length;
     *  element_value default_value;
     * }
     * @param classFileInputStream 字节码文件输入流
     * @param attributeInfo        属性信息
     */
    @Override
    public void parse(InputStream classFileInputStream, AttributeInfo attributeInfo) throws Exception {
        ElementValueInfo defaultValue = AnnotationParseUtil.parseElementValueInfo(classFileInputStream);
        System.out.println(String.format("%s 注解默认值：%s",getAttributeBaseInfo(attributeInfo),
                defaultValue));
    }
}
