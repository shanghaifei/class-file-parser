package com.hyfetech.tools.classfileparser.parser.attribute.impl;

import com.hyfetech.tools.classfileparser.dto.AttributeInfo;
import com.hyfetech.tools.classfileparser.dto.LocalVariableInfo;
import com.hyfetech.tools.classfileparser.parser.attribute.AbstractAttributeInfoParser;
import com.hyfetech.tools.classfileparser.utils.ReadClassFileUtil;

import java.io.InputStream;

/**
 * 【局部变量表】属性信息解析器
 * @author shanghaifei
 * @date 2022/6/2
 */
public class LocalVariableTableAttributeInfoParser extends AbstractAttributeInfoParser {
    /**
     * 解析
     * LocalVariableTable_attribute {
     *   u2 attribute_name_index;
     *   u4 attribute_length;
     *   u2 local_variable_table_length;
     *   {
     *      u2 start_pc;
     *      u2 length;
     *      u2 name_index;
     *      u2 descriptor_index;
     *      u2 index;
     *   } local_variable_table[local_variable_table_length];
     * }
     * @param classFileInputStream 字节码文件输入流
     * @param attributeInfo        属性信息
     */
    @Override
    public void parse(InputStream classFileInputStream, AttributeInfo attributeInfo) throws Exception {
        /** 局部变量表长度（2字节） */
        int localVariableTableLength = ReadClassFileUtil.readU2(classFileInputStream);
        System.out.println(String.format("%s 局部变量数量：%s",getAttributeBaseInfo(attributeInfo),
                localVariableTableLength));
        for(int i = 0;i < localVariableTableLength;i++) {
            String prefix = getPrintPrefix(attributeInfo.getLevel() + 1);
            LocalVariableInfo localVariableInfo = new LocalVariableInfo();
            /** 起始指令位置（2字节） */
            int startPc = ReadClassFileUtil.readU2(classFileInputStream);
            localVariableInfo.setStartPc(startPc);
            /** 长度（2字节） */
            int length = ReadClassFileUtil.readU2(classFileInputStream);
            localVariableInfo.setLength(length);
            /** 名称索引（2字节） */
            int nameIndex = ReadClassFileUtil.readU2(classFileInputStream);
            localVariableInfo.setNameIndex(nameIndex);
            localVariableInfo.setName(constantPoolParseInfos[nameIndex]);
            /** 描述符索引（2字节） */
            int descriptorIndex = ReadClassFileUtil.readU2(classFileInputStream);
            localVariableInfo.setDescriptorIndex(descriptorIndex);
            localVariableInfo.setDescriptor(constantPoolParseInfos[descriptorIndex]);
            /** 索引（2字节） */
            int index = ReadClassFileUtil.readU2(classFileInputStream);
            localVariableInfo.setIndex(index);

            System.out.println(prefix + String.format("【局部变量%s】%s",
                    i + 1,localVariableInfo));
        }
    }
}
