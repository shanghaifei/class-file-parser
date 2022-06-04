package com.hyfetech.tools.classfileparser.parser.constantpool.impl;

import com.hyfetech.tools.classfileparser.parser.constantpool.IConstantPoolInfoParser;
import com.hyfetech.tools.classfileparser.utils.ReadClassFileUtil;

import java.io.InputStream;

/**
 * 常量长整型信息解析器
 * @author shanghaifei
 * @date 2022/6/1
 */
public class ConstantLongInfoParser implements IConstantPoolInfoParser {
    /**
     * 解析
     * CONSTANT_Long_info {
     *  u1 tag;
     *  u4 high_bytes;
     *  u4 low_bytes;
     * }
     * @param classFileInputStream 字节码文件输入流
     * @param constantIndex        常量索引序号
     */
    @Override
    public String parse(InputStream classFileInputStream, int constantIndex) throws Exception {
        long longHighValue = ReadClassFileUtil.readU4(classFileInputStream);
        long longLowValue = ReadClassFileUtil.readU4(classFileInputStream);
        long longValue = (long)(longHighValue * Math.pow(256,4) + longLowValue);
        String parseInfo = String.valueOf(longValue);
        System.out.println(String.format("【%s】号常量池->类型：CONSTANT_Long 值：%s",
                constantIndex,longValue));
        return parseInfo;
    }
}
