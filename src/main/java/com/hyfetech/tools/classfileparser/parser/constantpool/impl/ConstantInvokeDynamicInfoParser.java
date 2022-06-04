package com.hyfetech.tools.classfileparser.parser.constantpool.impl;

import com.hyfetech.tools.classfileparser.parser.constantpool.IConstantPoolInfoParser;
import com.hyfetech.tools.classfileparser.utils.ReadClassFileUtil;

import java.io.InputStream;

/**
 * 常量【执行动态】信息解析器
 * 类型：CONSTANT_InvokeDynamic（5字节）
 * @author shanghaifei
 * @date 2022/6/1
 */
public class ConstantInvokeDynamicInfoParser implements IConstantPoolInfoParser {
    /**
     * 解析
     * CONSTANT_InvokeDynamic_info {
     *  u1 tag;
     *  u2 bootstrap_method_attr_index;
     *  u2 name_and_type_index;
     * }
     * @param classFileInputStream 字节码文件输入流
     * @param constantIndex        常量索引序号
     */
    @Override
    public String parse(InputStream classFileInputStream, int constantIndex) throws Exception {
        /** bootstrap_method_attr_index（2字节） */
        int invokeDynamicBootStrapMethodIndex = ReadClassFileUtil.readU2(classFileInputStream);
        /** name_and_type_index（2字节） */
        int invokeDynamicNameAndTypeIndex = ReadClassFileUtil.readU2(classFileInputStream);
        System.out.println(String.format("【%s】号常量池->类型：CONSTANT_InvokeDynamic 启动方法属性索引：#{%s} 名称类型索引：#{%s}",
                constantIndex,invokeDynamicBootStrapMethodIndex,invokeDynamicNameAndTypeIndex));
        return "";
    }
}
