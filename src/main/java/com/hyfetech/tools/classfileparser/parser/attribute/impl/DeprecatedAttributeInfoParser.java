package com.hyfetech.tools.classfileparser.parser.attribute.impl;

import com.hyfetech.tools.classfileparser.dto.AttributeInfo;
import com.hyfetech.tools.classfileparser.parser.attribute.AbstractAttributeInfoParser;

import java.io.InputStream;

/**
 * 【过时的，不赞成使用】属性解析器
 * @author shanghaifei
 * @date 2022/6/2
 */
public class DeprecatedAttributeInfoParser extends AbstractAttributeInfoParser {
    /**
     * 解析
     * Deprecated_attribute {
     *  u2 attribute_name_index;
     *  u4 attribute_length;
     * }
     * @param classFileInputStream 字节码文件输入流
     * @param attributeInfo        属性信息
     */
    @Override
    public void parse(InputStream classFileInputStream, AttributeInfo attributeInfo) throws Exception {
        String prefix = getPrintPrefix(attributeInfo.getLevel());
        System.out.println(String.format("%s【属性%s】属性名：%s，属性长度：%s（已过时）",
                prefix,attributeInfo.getAttributeIndex(),attributeInfo.getAttributeName(),
                attributeInfo.getAttributeLength()));
    }
}
