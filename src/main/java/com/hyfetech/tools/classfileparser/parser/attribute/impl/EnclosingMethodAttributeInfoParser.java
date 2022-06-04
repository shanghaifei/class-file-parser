package com.hyfetech.tools.classfileparser.parser.attribute.impl;

import com.hyfetech.tools.classfileparser.dto.AttributeInfo;
import com.hyfetech.tools.classfileparser.parser.attribute.AbstractAttributeInfoParser;
import com.hyfetech.tools.classfileparser.utils.ReadClassFileUtil;

import java.io.InputStream;

/**
 * 【封闭方法】属性解析器
 * @author shanghaifei
 * @date 2022/6/2
 */
public class EnclosingMethodAttributeInfoParser extends AbstractAttributeInfoParser {
    /**
     * 解析
     * EnclosingMethod_attribute {
     *  u2 attribute_name_index;
     *  u4 attribute_length;
     *  u2 class_index;
     *  u2 method_index;
     * }
     * @param classFileInputStream 字节码文件输入流
     * @param attributeInfo        属性信息
     */
    @Override
    public void parse(InputStream classFileInputStream, AttributeInfo attributeInfo) throws Exception {
        String prefix = getPrintPrefix(attributeInfo.getLevel());
        /** 类索引（2字节） */
        int classIndex = ReadClassFileUtil.readU2(classFileInputStream);
        /** 方法索引（2字节） */
        int methodIndex = ReadClassFileUtil.readU2(classFileInputStream);
        System.out.println(String.format("%s【属性%s】属性名：%s，属性长度：%s，封闭方法信息：%s.%s",
                prefix,attributeInfo.getAttributeIndex(),attributeInfo.getAttributeName(),
                attributeInfo.getAttributeLength(),constantPoolParseInfos[classIndex],
                constantPoolParseInfos[methodIndex]));
    }
}
