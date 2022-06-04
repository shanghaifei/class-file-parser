package com.hyfetech.tools.classfileparser.parser.constantpool.impl;

import com.hyfetech.tools.classfileparser.parser.constantpool.IConstantPoolInfoParser;
import com.hyfetech.tools.classfileparser.utils.ReadClassFileUtil;

import java.io.InputStream;

/**
 * 常量utf8信息解析器
 * 类型：CONSTANT_Class（3字节）
 * @author shanghaifei
 * @date 2022/6/1
 */
public class ConstantUtf8InfoParser implements IConstantPoolInfoParser {
    /**
     * 解析
     * CONSTANT_Utf8_info {
     *  u1 tag;
     *  u2 length;
     *  u1 bytes[length];
     * }
     * @param classFileInputStream 字节码文件输入流
     * @param constantIndex        常量索引序号
     */
    @Override
    public String parse(InputStream classFileInputStream, int constantIndex) throws Exception {
        int utf8Length = ReadClassFileUtil.readU2(classFileInputStream);
        byte[] utf8Bytes = new byte[utf8Length];
        classFileInputStream.read(utf8Bytes);
        StringBuilder contentSB = new StringBuilder();
        if(utf8Bytes.length > 0) {
            for(byte c : utf8Bytes) {
                // 将assic码转换为对应的字符
                contentSB.append((char)c);
            }
        }
        String parseInfo = contentSB.toString();
        System.out.println(String.format("【%s】号常量池->类型：CONSTANT_Utf8 长度：%s 内容：%s",
                constantIndex,utf8Length,contentSB));
        return parseInfo;
    }
}
