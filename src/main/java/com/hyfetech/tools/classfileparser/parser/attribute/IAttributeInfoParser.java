package com.hyfetech.tools.classfileparser.parser.attribute;

import com.hyfetech.tools.classfileparser.dto.AttributeInfo;

import java.io.InputStream;

/**
 * 属性信息解析器接口
 * @author shanghaifei
 * @date 2022/6/1
 */
public interface IAttributeInfoParser {
    /**
     * 解析
     * @param classFileInputStream 字节码文件输入流
     * @param attributeInfo 属性信息
     */
    void parse(InputStream classFileInputStream, AttributeInfo attributeInfo) throws Exception;
}
