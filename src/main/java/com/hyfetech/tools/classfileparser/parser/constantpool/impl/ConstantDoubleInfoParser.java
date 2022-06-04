package com.hyfetech.tools.classfileparser.parser.constantpool.impl;

import com.hyfetech.tools.classfileparser.parser.constantpool.IConstantPoolInfoParser;
import com.hyfetech.tools.classfileparser.utils.ReadClassFileUtil;

import java.io.InputStream;

/**
 * 常量双精度浮点数信息解析器
 * @author shanghaifei
 * @date 2022/6/1
 */
public class ConstantDoubleInfoParser implements IConstantPoolInfoParser {
    /**
     * 解析
     * CONSTANT_Double_info {
     *  u1 tag;
     *  u4 high_bytes;
     *  u4 low_bytes;
     * }
     * @param classFileInputStream 字节码文件输入流
     * @param constantIndex        常量索引序号
     */
    @Override
    public String parse(InputStream classFileInputStream, int constantIndex) throws Exception {
        /** 高位值（4字节） */
        long doubleHighValue = ReadClassFileUtil.readU4(classFileInputStream);
        /** 低位值（4字节） */
        long doubleLowValue = ReadClassFileUtil.readU4(classFileInputStream);
        double doubleValue = doubleHighValue * Math.pow(256,4) + doubleLowValue;
        System.out.println(String.format("【%s】号常量池->类型：CONSTANT_Double 值：%s",
                constantIndex,doubleValue));
        return String.valueOf(doubleValue);
    }
}
