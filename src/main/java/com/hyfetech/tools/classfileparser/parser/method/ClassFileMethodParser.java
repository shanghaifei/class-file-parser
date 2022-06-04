package com.hyfetech.tools.classfileparser.parser.method;

import com.hyfetech.tools.classfileparser.dto.AttributeInfo;
import com.hyfetech.tools.classfileparser.parser.baseinfo.AbstractClassFileInfoParser;
import com.hyfetech.tools.classfileparser.parser.baseinfo.IClassFileInfoParser;
import com.hyfetech.tools.classfileparser.parser.attribute.impl.AttributesInfoParser;
import com.hyfetech.tools.classfileparser.utils.AccessFlagUtil;
import com.hyfetech.tools.classfileparser.utils.ReadClassFileUtil;

import java.io.InputStream;

/**
 * 字节码文件的方法解析器
 * @author shanghaifei
 * @date 2022/6/2
 */
public class ClassFileMethodParser extends AbstractClassFileInfoParser implements IClassFileInfoParser {
    /**
     * 构造函数
     * @param constantPoolParseInfos
     */
    public ClassFileMethodParser(String[] constantPoolParseInfos) {
        this.constantPoolParseInfos = constantPoolParseInfos;
    }

    /**
     * 解析
     * @param classFileInputStream 字节码文件输入流
     */
    @Override
    public String parse(InputStream classFileInputStream) throws Exception {
        /**
         * method_info {
         *  u2 access_flags;
         *  u2 name_index;
         *  u2 descriptor_index;
         *  u2 attributes_count;
         *  attribute_info attributes[attributes_count];
         * }
         */
        /** method_count：方法数量（2字节） */
        int methodCount = ReadClassFileUtil.readU2(classFileInputStream);
        System.out.println(String.format("方法数量：%s",methodCount));
        // 遍历所有方法信息
        for(int i = 1;i <= methodCount;i++) {
            /** 方法访问修饰符（2字节） */
            int methodAccessFlags = ReadClassFileUtil.readU2(classFileInputStream);
            /** 方法名称索引（2字节） */
            int methodNameIndex = ReadClassFileUtil.readU2(classFileInputStream);
            /** 方法描述符索引（2字节） */
            int methodDescriptorIndex = ReadClassFileUtil.readU2(classFileInputStream);
            System.out.println(String.format("【方法%s】方法访问修饰符：%s，方法名称：%s，方法描述符：%s",
                    i, AccessFlagUtil.getMethodAccessFlags(methodAccessFlags),constantPoolParseInfos[methodNameIndex],
                    constantPoolParseInfos[methodDescriptorIndex]));
            // 解析属性信息
            AttributesInfoParser.getInstance().parse(classFileInputStream, AttributeInfo.builder().level(2).build());
        }
        return null;
    }
}
