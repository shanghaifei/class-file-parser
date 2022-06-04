package com.hyfetech.tools.classfileparser.parser.baseinfo;

import com.hyfetech.tools.classfileparser.utils.ReadClassFileUtil;

import java.io.InputStream;

/**
 * 字节码文件魔数解析器（0xCAFEBABE）
 * @author shanghaifei
 * @date 2022/6/2
 */
public class ClassFileMagicParser implements IClassFileInfoParser {
    /**
     * 解析
     * @param classFileInputStream 字节码文件输入流
     */
    @Override
    public String parse(InputStream classFileInputStream) throws Exception {
        /** magic：魔数（4字节） */
        String magic = ReadClassFileUtil.binary(ReadClassFileUtil.readUBytes(classFileInputStream,4),16);
        System.out.println(String.format("魔数：%s",magic));
        return null;
    }
}
