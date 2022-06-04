package com.hyfetech.tools.classfileparser.parser.attribute.impl;

import com.hyfetech.tools.classfileparser.dto.AttributeInfo;
import com.hyfetech.tools.classfileparser.parser.attribute.AbstractAttributeInfoParser;
import com.hyfetech.tools.classfileparser.utils.ReadClassFileUtil;

import java.io.InputStream;

/**
 * 【签名】属性信息解析器（修饰在泛型定义的接口、方法、类等等）
 * @author shanghaifei
 * @date 2022/6/2
 */
public class SignatureAttributeInfoParser extends AbstractAttributeInfoParser {
    /**
     * 解析
     * Signature_attribute {
     *  u2 attribute_name_index;
     *  u4 attribute_length;
     *  u2 signature_index;
     * }
     * @param classFileInputStream 字节码文件输入流
     * @param attributeInfo        属性信息
     */
    @Override
    public void parse(InputStream classFileInputStream, AttributeInfo attributeInfo) throws Exception {
        String prefix = getPrintPrefix(attributeInfo.getLevel());
        /** 签名信息索引（2字节） */
        int signatureIndex = ReadClassFileUtil.readU2(classFileInputStream);
        System.out.println(String.format("%s【属性%s】属性名：%s，泛型的签名信息：%s",
                prefix,attributeInfo.getAttributeIndex(),attributeInfo.getAttributeName(),
                constantPoolParseInfos[signatureIndex]));
    }
}
