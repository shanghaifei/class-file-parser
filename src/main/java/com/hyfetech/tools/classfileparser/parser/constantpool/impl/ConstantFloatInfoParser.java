package com.hyfetech.tools.classfileparser.parser.constantpool.impl;

import com.hyfetech.tools.classfileparser.parser.constantpool.IConstantPoolInfoParser;
import com.hyfetech.tools.classfileparser.utils.ReadClassFileUtil;

import java.io.InputStream;

/**
 * 常量单精度浮点数信息解析器
 * @author shanghaifei
 * @date 2022/6/1
 */
public class ConstantFloatInfoParser implements IConstantPoolInfoParser {
    /**
     * 解析
     * CONSTANT_Float_info {
     *  u1 tag;
     *  u4 bytes;
     * }
     * @param classFileInputStream 字节码文件输入流
     * @param constantIndex        常量索引序号
     */
    @Override
    public String parse(InputStream classFileInputStream, int constantIndex) throws Exception {
        long floatValue = ReadClassFileUtil.readU4(classFileInputStream);
        String parseInfo = String.valueOf(floatValue);
        System.out.println(String.format("【%s】号常量池->类型：CONSTANT_Float 值：%s",
                constantIndex,floatValue));
        return parseInfo;
    }
}
