package com.hyfetech.tools.classfileparser.parser.constantpool.impl;

import com.hyfetech.tools.classfileparser.parser.constantpool.IConstantPoolInfoParser;
import com.hyfetech.tools.classfileparser.utils.ReadClassFileUtil;

import java.io.InputStream;

/**
 * 常量字段引用信息解析器
 * @author shanghaifei
 * @date 2022/6/1
 */
public class ConstantFieldRefInfoParser implements IConstantPoolInfoParser {
    /**
     * 解析
     * CONSTANT_Fieldref_info {
     *  u1 tag;
     *  u2 class_index;
     *  u2 name_and_type_index;
     * }
     * @param classFileInputStream 字节码文件输入流
     * @param constantIndex        常量索引序号
     */
    @Override
    public String parse(InputStream classFileInputStream, int constantIndex) throws Exception {
        /** 类索引（2字节） */
        int fieldClassIndex = ReadClassFileUtil.readU2(classFileInputStream);
        /** 名称和类型索引（2字节） */
        int fieldNameAndTypeIndex = ReadClassFileUtil.readU2(classFileInputStream);
        String parseInfo = String.format("[ref]#{%s}.#{%s}",
                fieldClassIndex,fieldNameAndTypeIndex);
        System.out.println(String.format("【%s】号常量池->类型：CONSTANT_Fieldref 类名称：#{%s} 字段签名：#{%s}",
                constantIndex,fieldClassIndex,fieldNameAndTypeIndex));
        return parseInfo;
    }
}
