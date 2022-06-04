package com.hyfetech.tools.classfileparser.parser.constantpool.impl;

import com.hyfetech.tools.classfileparser.parser.constantpool.IConstantPoolInfoParser;
import com.hyfetech.tools.classfileparser.utils.ReadClassFileUtil;

import java.io.InputStream;

/**
 * 常量类信息解析器
 * 类型：CONSTANT_Class（3字节）
 * @author shanghaifei
 * @date 2022/6/1
 */
public class ConstantClassInfoParser implements IConstantPoolInfoParser {
    /**
     * 解析
     * CONSTANT_Class_info {
     *  u1 tag;
     *  u2 name_index;
     * }
     * @param classFileInputStream 字节码文件输入流
     * @param constantIndex        常量索引序号
     */
    @Override
    public String parse(InputStream classFileInputStream, int constantIndex) throws Exception {
        /** 名称索引（2字节） */
        int nameIndex = ReadClassFileUtil.readU2(classFileInputStream);
        String parseInfo = "#{" + nameIndex + "}";
        System.out.println(String.format("【%s】号常量池->类型：CONSTANT_Class 名称：#{%s}",
                constantIndex,nameIndex));
        return parseInfo;
    }
}
