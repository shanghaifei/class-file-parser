package com.hyfetech.tools.classfileparser.parser.constantpool.impl;

import com.hyfetech.tools.classfileparser.parser.constantpool.IConstantPoolInfoParser;
import com.hyfetech.tools.classfileparser.utils.ReadClassFileUtil;

import java.io.InputStream;

/**
 * 常量名称和类型信息解析器
 * 类型：CONSTANT_NameAndType（3字节）
 * @author shanghaifei
 * @date 2022/6/1
 */
public class ConstantNameAndTypeInfoParser implements IConstantPoolInfoParser {
    /**
     * 解析
     * CONSTANT_NameAndType_info {
     *  u1 tag;
     *  u2 name_index;
     *  u2 descriptor_index;
     * }
     * @param classFileInputStream 字节码文件输入流
     * @param constantIndex        常量索引序号
     */
    @Override
    public String parse(InputStream classFileInputStream, int constantIndex) throws Exception {
        /** 名称索引（2字节） */
        int nameAndTypeNameIndex = ReadClassFileUtil.readU2(classFileInputStream);
        /** 描述符索引（2字节） */
        int nameAndTypeDescriptorIndex = ReadClassFileUtil.readU2(classFileInputStream);
        String parseInfo = String.format("#{%s}(签名：#{%s})",
                nameAndTypeNameIndex,nameAndTypeDescriptorIndex);
        System.out.println(String.format("【%s】号常量池->类型：CONSTANT_NameAndType 名称：#{%s} 描述符：#{%s}",
                constantIndex,nameAndTypeNameIndex,nameAndTypeDescriptorIndex));
        return parseInfo;
    }
}
