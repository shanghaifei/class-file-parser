package com.hyfetech.tools.classfileparser.parser.attribute.impl;

import com.hyfetech.tools.classfileparser.dto.AttributeInfo;
import com.hyfetech.tools.classfileparser.parser.attribute.AbstractAttributeInfoParser;
import com.hyfetech.tools.classfileparser.parser.attribute.IAttributeInfoParser;
import com.hyfetech.tools.classfileparser.utils.ReadClassFileUtil;

import java.io.InputStream;

/**
 * 【行号表】属性信息解析器
 * @author shanghaifei
 * @date 2022/6/1
 */
public class LineNumberTableAttributeInfoParser extends AbstractAttributeInfoParser {
    /**
     * 解析
     * LineNumberTable_attribute {
     *  u2 attribute_name_index;
     *  u4 attribute_length;
     *  u2 line_number_table_length;
     *  { u2 start_pc;
     *  u2 line_number;
     *  } line_number_table[line_number_table_length];
     * }
     * @param classFileInputStream   字节码文件输入流
     * @param attributeInfo          属性信息
     */
    @Override
    public void parse(InputStream classFileInputStream, AttributeInfo attributeInfo) throws Exception {
        String prefix = getPrintPrefix(attributeInfo.getLevel());
        /** 行号表长度（2字节） */
        int lineNumberTableLength = ReadClassFileUtil.readU2(classFileInputStream);
        StringBuilder sbLineNumberInfos = new StringBuilder();
        for(int index = 1;index <= lineNumberTableLength;index++) {
            /** start_pc：code里面的指令索引（2字节） */
            int startPc = ReadClassFileUtil.readU2(classFileInputStream);
            /** line_number：源代码的行号（2字节） */
            int lineNumber = ReadClassFileUtil.readU2(classFileInputStream);
            sbLineNumberInfos.append(String.format("code:%s -> 源代码行号:%s，", startPc,lineNumber));
        }
        if(!sbLineNumberInfos.toString().isEmpty()) {
            sbLineNumberInfos = new StringBuilder(sbLineNumberInfos.substring(0, sbLineNumberInfos.length() - 1));
        }
        System.out.println(String.format("%s【属性%s】属性名：%s，行号表信息：【%s】",
                prefix,attributeInfo.getAttributeIndex(),attributeInfo.getAttributeName(),sbLineNumberInfos));
    }
}
