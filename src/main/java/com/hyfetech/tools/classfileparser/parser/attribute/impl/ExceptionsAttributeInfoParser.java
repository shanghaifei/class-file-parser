package com.hyfetech.tools.classfileparser.parser.attribute.impl;

import com.hyfetech.tools.classfileparser.dto.AttributeInfo;
import com.hyfetech.tools.classfileparser.parser.attribute.AbstractAttributeInfoParser;
import com.hyfetech.tools.classfileparser.utils.ReadClassFileUtil;

import java.io.InputStream;

/**
 * 【异常表】属性信息解析器
 * @author shanghaifei
 * @date 2022/6/1
 */
public class ExceptionsAttributeInfoParser extends AbstractAttributeInfoParser {
    /**
     * 解析
     * Exceptions_attribute {
     *  u2 attribute_name_index;
     *  u4 attribute_length;
     *  u2 number_of_exceptions;
     *  u2 exception_index_table[number_of_exceptions];
     * }
     * @param classFileInputStream   字节码文件输入流
     * @param attributeInfo          属性信息
     */
    @Override
    public void parse(InputStream classFileInputStream, AttributeInfo attributeInfo) throws Exception {
        String prefix = getPrintPrefix(attributeInfo.getLevel());
        /** 异常数量（2字节） */
        int numberOfExceptions = ReadClassFileUtil.readU2(classFileInputStream);
        StringBuilder sbExceptionInfos = new StringBuilder();
        for(int exceptionsIndex = 1;exceptionsIndex <= numberOfExceptions;exceptionsIndex++) {
            /** 异常索引（2字节） */
            int exceptionIndex = ReadClassFileUtil.readU2(classFileInputStream);
            sbExceptionInfos.append(String.format("%s,", constantPoolParseInfos[exceptionIndex]));
        }
        if(!sbExceptionInfos.toString().isEmpty()) {
            sbExceptionInfos = new StringBuilder(sbExceptionInfos.substring(0, sbExceptionInfos.length() - 1));
        }
        System.out.println(String.format("%s【属性%s】属性名：%s，抛出的异常信息：【%s】",
                prefix,attributeInfo.getAttributeIndex(),attributeInfo.getAttributeName(),sbExceptionInfos));
    }
}
