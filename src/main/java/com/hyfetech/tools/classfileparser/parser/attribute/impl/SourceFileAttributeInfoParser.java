package com.hyfetech.tools.classfileparser.parser.attribute.impl;

import com.hyfetech.tools.classfileparser.dto.AttributeInfo;
import com.hyfetech.tools.classfileparser.parser.attribute.AbstractAttributeInfoParser;
import com.hyfetech.tools.classfileparser.parser.attribute.IAttributeInfoParser;
import com.hyfetech.tools.classfileparser.utils.ReadClassFileUtil;

import java.io.InputStream;

/**
 * 【源文件】属性信息解析器
 * @author shanghaifei
 * @date 2022/6/1
 */
public class SourceFileAttributeInfoParser extends AbstractAttributeInfoParser {
    /**
     * 解析
     * SourceFile_attribute {
     *  u2 attribute_name_index;
     *  u4 attribute_length;
     *  u2 sourcefile_index;
     * }
     * @param classFileInputStream   字节码文件输入流
     * @param attributeInfo          属性信息
     */
    @Override
    public void parse(InputStream classFileInputStream, AttributeInfo attributeInfo) throws Exception {
        String prefix = getPrintPrefix(attributeInfo.getLevel());
        /** 源文件索引（2字节） */
        int sourceFileIndex = ReadClassFileUtil.readU2(classFileInputStream);
        System.out.println(String.format("%s【属性%s】属性名：%s，源文件名称：%s",
                prefix,attributeInfo.getAttributeIndex(),attributeInfo.getAttributeName(),
                constantPoolParseInfos[sourceFileIndex]));
    }
}
