package com.hyfetech.tools.classfileparser.parser.attribute.impl;

import com.hyfetech.tools.classfileparser.dto.AttributeInfo;
import com.hyfetech.tools.classfileparser.dto.StackMapFrameInfo;
import com.hyfetech.tools.classfileparser.parser.attribute.AbstractAttributeInfoParser;
import com.hyfetech.tools.classfileparser.parser.attribute.impl.common.StackMapParseUtil;
import com.hyfetech.tools.classfileparser.utils.ReadClassFileUtil;

import java.io.InputStream;

/**
 * 栈映射表（栈帧数组）
 * @author shanghaifei
 * @date 2022/6/2
 */
public class StackMapTableAttributeInfoParser extends AbstractAttributeInfoParser {
    /**
     * 解析
     * StackMapTable_attribute {
     *   u2 attribute_name_index;
     *   u4 attribute_length;
     *   u2 number_of_entries;
     *   stack_map_frame entries[number_of_entries];
     * }
     *
     * @param classFileInputStream 字节码文件输入流
     * @param attributeInfo        属性信息
     */
    @Override
    public void parse(InputStream classFileInputStream, AttributeInfo attributeInfo) throws Exception {
        String prefix = getPrintPrefix(attributeInfo.getLevel());
        /** 栈帧数量（2字节） */
        int numberOfEntries = ReadClassFileUtil.readU2(classFileInputStream);
        System.out.println(String.format("%s【属性%s】属性名：%s 栈帧数量：%s",
                prefix,attributeInfo.getAttributeIndex(),attributeInfo.getAttributeName(),numberOfEntries));
        for(int stackMapFrameIndex = 0;stackMapFrameIndex < numberOfEntries;stackMapFrameIndex++) {
            prefix = getPrintPrefix(attributeInfo.getLevel() + 1);
            StackMapFrameInfo stackMapFrameInfo = StackMapParseUtil.parseStackMapFrameInfo(classFileInputStream);
            System.out.println(String.format("%s【栈映射%s】%s",
                    prefix,stackMapFrameIndex + 1,stackMapFrameInfo));
        }
    }
}
