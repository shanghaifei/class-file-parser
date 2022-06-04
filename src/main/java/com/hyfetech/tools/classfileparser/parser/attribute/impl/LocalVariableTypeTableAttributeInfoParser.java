package com.hyfetech.tools.classfileparser.parser.attribute.impl;

import com.hyfetech.tools.classfileparser.dto.AttributeInfo;
import com.hyfetech.tools.classfileparser.dto.LocalVariableTypeInfo;
import com.hyfetech.tools.classfileparser.parser.attribute.AbstractAttributeInfoParser;
import com.hyfetech.tools.classfileparser.utils.ReadClassFileUtil;

import java.io.InputStream;

/**
 * 【局部变量类型表】属性信息解析器
 * @author shanghaifei
 * @date 2022/6/2
 */
public class LocalVariableTypeTableAttributeInfoParser extends AbstractAttributeInfoParser {
    /**
     * 解析
     * LocalVariableTypeTable_attribute {
     *  u2 attribute_name_index;
     *  u4 attribute_length;
     *  u2 local_variable_type_table_length;
     *  {
     *      u2 start_pc;
     *      u2 length;
     *      u2 name_index;
     *      u2 signature_index;
     *      u2 index;
     *  } local_variable_type_table[local_variable_type_table_length];
     *  }
     * @param classFileInputStream 字节码文件输入流
     * @param attributeInfo        属性信息
     */
    @Override
    public void parse(InputStream classFileInputStream, AttributeInfo attributeInfo) throws Exception {
        /** 局部变量类型表长度（2字节） */
        int localVariableTypeTableLength = ReadClassFileUtil.readU2(classFileInputStream);
        System.out.println(String.format("%s 局部变量类型数量：%s",getAttributeBaseInfo(attributeInfo),
                localVariableTypeTableLength));
        for(int i = 0;i < localVariableTypeTableLength;i++) {
            String prefix = getPrintPrefix(attributeInfo.getLevel() + 1);
            LocalVariableTypeInfo localVariableTypeInfo = new LocalVariableTypeInfo();
            /** 起始指令位置（2字节） */
            int startPc = ReadClassFileUtil.readU2(classFileInputStream);
            localVariableTypeInfo.setStartPc(startPc);
            /** 长度（2字节） */
            int length = ReadClassFileUtil.readU2(classFileInputStream);
            localVariableTypeInfo.setLength(length);
            /** 名称索引（2字节） */
            int nameIndex = ReadClassFileUtil.readU2(classFileInputStream);
            localVariableTypeInfo.setNameIndex(nameIndex);
            localVariableTypeInfo.setName(constantPoolParseInfos[nameIndex]);
            /** 签名索引（2字节） */
            int signatureIndex = ReadClassFileUtil.readU2(classFileInputStream);
            localVariableTypeInfo.setSignatureIndex(signatureIndex);
            localVariableTypeInfo.setSignature(constantPoolParseInfos[signatureIndex]);
            /** 索引（2字节） */
            int index = ReadClassFileUtil.readU2(classFileInputStream);
            localVariableTypeInfo.setIndex(index);

            System.out.println(prefix + String.format("【局部变量类型%s】%s",
                    prefix,i + 1,localVariableTypeInfo));
        }
    }
}
