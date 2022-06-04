package com.hyfetech.tools.classfileparser.parser.constantpool.impl;

import com.hyfetech.tools.classfileparser.parser.constantpool.IConstantPoolInfoParser;
import com.hyfetech.tools.classfileparser.utils.ReadClassFileUtil;

import java.io.InputStream;

/**
 * 常量整型信息解析器
 * @author shanghaifei
 * @date 2022/6/1
 */
public class ConstantIntegerInfoParser implements IConstantPoolInfoParser {
    /**
     * 解析
     * CONSTANT_Integer_info {
     *  u1 tag;
     *  u4 bytes;
     * }
     * @param classFileInputStream 字节码文件输入流
     * @param constantIndex        常量索引序号
     */
    @Override
    public String parse(InputStream classFileInputStream, int constantIndex) throws Exception {
        /** 字符串索引（2字节） */
        int stringIndex = ReadClassFileUtil.readU2(classFileInputStream);
        String parseInfo = String.valueOf(stringIndex);
        System.out.println(String.format("【%s】号常量池->类型：CONSTANT_String 字符串：%s",
                constantIndex,stringIndex));
        return parseInfo;
    }
}
