package com.hyfetech.tools.classfileparser.parser.constantpool.impl;

import com.hyfetech.tools.classfileparser.parser.constantpool.IConstantPoolInfoParser;
import com.hyfetech.tools.classfileparser.utils.ReadClassFileUtil;

import java.io.InputStream;

/**
 * 常量方法处理解析器
 * 类型：CONSTANT_Class（3字节）
 * @author shanghaifei
 * @date 2022/6/1
 */
public class ConstantMethodHandleInfoParser implements IConstantPoolInfoParser {
    /**
     * 解析
     * CONSTANT_MethodHandle_info {
     *  u1 tag;
     *  u1 reference_kind;
     *  u2 reference_index;
     * }
     * @param classFileInputStream 字节码文件输入流
     * @param constantIndex        常量索引序号
     */
    @Override
    public String parse(InputStream classFileInputStream, int constantIndex) throws Exception {
        /** 引用类型（2字节） */
        int referenceKind = ReadClassFileUtil.readU2(classFileInputStream);
        /** 引用索引（2字节） */
        int referenceIndex = ReadClassFileUtil.readU2(classFileInputStream);
        System.out.println(String.format("【%s】号常量池->类型：CONSTANT_MethodHandle 引用类型：%s 引用索引：#{%s}",
                constantIndex,referenceKind,referenceIndex));
        return "";
    }
}
