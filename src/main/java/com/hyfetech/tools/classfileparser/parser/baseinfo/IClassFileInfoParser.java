package com.hyfetech.tools.classfileparser.parser.baseinfo;

import java.io.InputStream;

/**
 * @author shanghaifei
 * @date 2022/6/2
 */
public interface IClassFileInfoParser {
    /**
     * 解析
     * @param classFileInputStream 字节码文件输入流
     */
    String parse(InputStream classFileInputStream) throws Exception;
}
