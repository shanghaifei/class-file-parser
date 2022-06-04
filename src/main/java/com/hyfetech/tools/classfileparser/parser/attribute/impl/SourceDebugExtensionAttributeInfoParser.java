package com.hyfetech.tools.classfileparser.parser.attribute.impl;

import com.hyfetech.tools.classfileparser.dto.AttributeInfo;
import com.hyfetech.tools.classfileparser.parser.attribute.AbstractAttributeInfoParser;

import java.io.InputStream;

/**
 * @author shanghaifei
 * @date 2022/6/2
 */
public class SourceDebugExtensionAttributeInfoParser extends AbstractAttributeInfoParser {
    /**
     * 解析
     * SourceDebugExtension_attribute {
     *  u2 attribute_name_index;
     *  u4 attribute_length;
     *  u1 debug_extension[attribute_length];
     * }
     * @param classFileInputStream 字节码文件输入流
     * @param attributeInfo        属性信息
     */
    @Override
    public void parse(InputStream classFileInputStream, AttributeInfo attributeInfo) throws Exception {

    }
}
