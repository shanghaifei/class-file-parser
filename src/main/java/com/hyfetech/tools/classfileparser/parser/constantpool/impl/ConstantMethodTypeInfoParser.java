package com.hyfetech.tools.classfileparser.parser.constantpool.impl;

import com.hyfetech.tools.classfileparser.parser.constantpool.IConstantPoolInfoParser;
import com.hyfetech.tools.classfileparser.utils.ReadClassFileUtil;

import java.io.InputStream;

/**
 * 常量方法类型信息解析器
 * 类型：CONSTANT_MethodType（3字节）
 * @author shanghaifei
 * @date 2022/6/1
 */
public class ConstantMethodTypeInfoParser implements IConstantPoolInfoParser {
    /**
     * 解析
     * CONSTANT_MethodType_info {
     *  u1 tag;
     *  u2 descriptor_index;
     * }
     * @param classFileInputStream 字节码文件输入流
     * @param constantIndex        常量索引序号
     */
    @Override
    public String parse(InputStream classFileInputStream, int constantIndex) throws Exception {
        /** 描述符索引（2字节） */
        int methodTypeDescriptorIndex = ReadClassFileUtil.readU2(classFileInputStream);
        System.out.println(String.format("【%s】号常量池->类型：CONSTANT_MethodType 描述索引：#{%s}",
                constantIndex,methodTypeDescriptorIndex));
        return "";
    }
}
