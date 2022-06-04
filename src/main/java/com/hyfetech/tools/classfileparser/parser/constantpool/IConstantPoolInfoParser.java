package com.hyfetech.tools.classfileparser.parser.constantpool;

import java.io.InputStream;

/**
 * 常量池信息解析器接口
 * @author shanghaifei
 * @date 2022/6/1
 */
public interface IConstantPoolInfoParser {
    /**
     * 解析
     * @param classFileInputStream 字节码文件输入流
     * @param constantIndex 常量索引序号
     */
    String parse(InputStream classFileInputStream,int constantIndex) throws Exception;
}
