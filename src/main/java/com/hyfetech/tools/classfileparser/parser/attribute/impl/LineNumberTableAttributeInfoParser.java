package com.hyfetech.tools.classfileparser.parser.attribute.impl;

import com.hyfetech.tools.classfileparser.dto.AttributeInfo;
import com.hyfetech.tools.classfileparser.dto.LineNumberInfo;
import com.hyfetech.tools.classfileparser.parser.attribute.AbstractAttributeInfoParser;
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
     *   u2 attribute_name_index;
     *   u4 attribute_length;
     *   u2 line_number_table_length;
     *   {
     *      u2 start_pc;
     *      u2 line_number;
     *   } line_number_table[line_number_table_length];
     * }
     * @param classFileInputStream   字节码文件输入流
     * @param attributeInfo          属性信息
     */
    @Override
    public void parse(InputStream classFileInputStream, AttributeInfo attributeInfo) throws Exception {
        /** 行号表长度（2字节） */
        int lineNumberTableLength = ReadClassFileUtil.readU2(classFileInputStream);
        System.out.println(String.format("%s 行号表长度：%s",getAttributeBaseInfo(attributeInfo),
                lineNumberTableLength));
        for(int i = 0;i < lineNumberTableLength;i++) {
            String prefix = getPrintPrefix(attributeInfo.getLevel() + 1);
            LineNumberInfo lineNumberInfo = new LineNumberInfo();
            /** start_pc：code里面的指令索引（2字节） */
            int startPc = ReadClassFileUtil.readU2(classFileInputStream);
            lineNumberInfo.setStartPc(startPc);
            /** line_number：源代码的行号（2字节） */
            int lineNumber = ReadClassFileUtil.readU2(classFileInputStream);
            lineNumberInfo.setLineNumber(lineNumber);

            System.out.println(prefix + String.format("【行号信息%s】%s",
                    i + 1,lineNumberInfo));
        }
    }
}
