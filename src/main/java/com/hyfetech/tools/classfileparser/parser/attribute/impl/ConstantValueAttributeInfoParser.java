package com.hyfetech.tools.classfileparser.parser.attribute.impl;

import com.hyfetech.tools.classfileparser.dto.AttributeInfo;
import com.hyfetech.tools.classfileparser.parser.attribute.AbstractAttributeInfoParser;
import com.hyfetech.tools.classfileparser.utils.ReadClassFileUtil;

import java.io.InputStream;

/**
 * 【常量值】属性信息解析器
 * @author shanghaifei
 * @date 2022/6/1
 */
public class ConstantValueAttributeInfoParser extends AbstractAttributeInfoParser {
    /**
     * 解析
     * ConstantValue_attribute {
     *  u2 attribute_name_index;
     *  u4 attribute_length;
     *  u2 constantvalue_index;
     * }
     * @param classFileInputStream   字节码文件输入流
     * @param attributeInfo          属性信息
     */
    @Override
    public void parse(InputStream classFileInputStream, AttributeInfo attributeInfo) throws Exception {
        String prefix = getPrintPrefix(attributeInfo.getLevel());
        /** 常量值索引（2字节） */
        int constantValueIndex = ReadClassFileUtil.readU2(classFileInputStream);
        System.out.println(String.format("%s【属性%s】属性名：%s，属性长度：%s，属性值：%s",
                prefix,attributeInfo.getAttributeIndex(),attributeInfo.getAttributeName(),
                attributeInfo.getAttributeLength(),constantPoolParseInfos[constantValueIndex]));
    }
}
