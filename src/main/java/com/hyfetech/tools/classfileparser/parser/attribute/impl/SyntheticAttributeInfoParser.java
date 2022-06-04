package com.hyfetech.tools.classfileparser.parser.attribute.impl;

import com.hyfetech.tools.classfileparser.dto.AttributeInfo;
import com.hyfetech.tools.classfileparser.parser.attribute.AbstractAttributeInfoParser;

import java.io.InputStream;

/**
 * 【合成】属性信息解析器（表示字段、方法是编译器自己生成的，不会出现在源代码）
 * @author shanghaifei
 * @date 2022/6/2
 */
public class SyntheticAttributeInfoParser extends AbstractAttributeInfoParser {
    /**
     * 解析
     * Synthetic_attribute {
     *  u2 attribute_name_index;
     *  u4 attribute_length;
     * }
     * @param classFileInputStream 字节码文件输入流
     * @param attributeInfo        属性信息
     */
    @Override
    public void parse(InputStream classFileInputStream, AttributeInfo attributeInfo) throws Exception {
        String prefix = getPrintPrefix(attributeInfo.getLevel());
        System.out.println(String.format("%s【属性%s】属性名：%s（表示字段、方法是编译器自己生成的，不会出现在源代码）",
                prefix,attributeInfo.getAttributeIndex(),attributeInfo.getAttributeName()));
    }
}
