package com.hyfetech.tools.classfileparser.parser.interfaces;

import com.hyfetech.tools.classfileparser.parser.baseinfo.AbstractClassFileInfoParser;
import com.hyfetech.tools.classfileparser.parser.baseinfo.IClassFileInfoParser;
import com.hyfetech.tools.classfileparser.utils.ReadClassFileUtil;

import java.io.InputStream;

/**
 * 字节码文件的接口解析器
 * @author shanghaifei
 * @date 2022/6/2
 */
public class ClassFileInterfaceParser extends AbstractClassFileInfoParser implements IClassFileInfoParser {
    public ClassFileInterfaceParser(String[] constantPoolParseInfos) {
        this.constantPoolParseInfos = constantPoolParseInfos;
    }

    /**
     * 解析
     * @param classFileInputStream 字节码文件输入流
     */
    @Override
    public String parse(InputStream classFileInputStream) throws Exception {
        /** interfaces_count：接口数量（2字节） */
        int interfaceCount = ReadClassFileUtil.readU2(classFileInputStream);
        System.out.println(String.format("接口数量：%s",interfaceCount));
        // interfaces：实现的所有接口信息
        String interfaceIndexs = "";
        for(int i = 1;i <= interfaceCount;i++) {
            /** 接口索引：指向常量池中的classinfo常量（2字节） */
            int interfaceIndex = ReadClassFileUtil.readU2(classFileInputStream);
            interfaceIndexs += constantPoolParseInfos[interfaceIndex] + ",";
        }
        if(!interfaceIndexs.isEmpty()) {
            interfaceIndexs = interfaceIndexs.substring(0,interfaceIndexs.length() - 1);
        }
        System.out.println(String.format("实现接口：%s", interfaceIndexs));
        return null;
    }
}
