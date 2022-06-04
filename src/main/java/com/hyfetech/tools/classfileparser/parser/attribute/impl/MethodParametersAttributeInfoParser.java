package com.hyfetech.tools.classfileparser.parser.attribute.impl;

import com.hyfetech.tools.classfileparser.dto.AttributeInfo;
import com.hyfetech.tools.classfileparser.parser.attribute.AbstractAttributeInfoParser;
import com.hyfetech.tools.classfileparser.utils.AccessFlagUtil;
import com.hyfetech.tools.classfileparser.utils.ReadClassFileUtil;

import java.io.InputStream;

/**
 * 【方法参数列表】属性信息解析器
 * @author shanghaifei
 * @date 2022/6/2
 */
public class MethodParametersAttributeInfoParser extends AbstractAttributeInfoParser {
    /**
     * 解析
     * MethodParameters_attribute {
     *  u2 attribute_name_index;
     *  u4 attribute_length;
     *  u1 parameters_count;
     *  {
     *      u2 name_index;
     *      u2 access_flags;
     *  } parameters[parameters_count];
     * }
     * @param classFileInputStream 字节码文件输入流
     * @param attributeInfo        属性信息
     */
    @Override
    public void parse(InputStream classFileInputStream, AttributeInfo attributeInfo) throws Exception {
        String prefix = getPrintPrefix(attributeInfo.getLevel());
        /** 参数数量（1字节） */
        short parameterCount = ReadClassFileUtil.readU1(classFileInputStream);
        StringBuilder sbParameterInfo = new StringBuilder();
        for(int i = 1;i <= parameterCount;i++) {
            /** 名称索引（2字节） */
            int nameIndex = ReadClassFileUtil.readU2(classFileInputStream);
            /** 访问修饰符（2字节） */
            int accessFlags = ReadClassFileUtil.readU2(classFileInputStream);
            sbParameterInfo.append(String.format("参数名称：%s，访问修饰符：%s、",
                    constantPoolParseInfos[nameIndex], AccessFlagUtil.getMethodParameterAccessFlags(accessFlags)));
        }
        if(sbParameterInfo.length() > 0) {
            sbParameterInfo = new StringBuilder(sbParameterInfo.substring(0,sbParameterInfo.length() - 1));
        }
        System.out.println(String.format("%s【属性%s】属性名：%s，方法参数信息：%s",
                prefix,attributeInfo.getAttributeIndex(),attributeInfo.getAttributeName(),
                sbParameterInfo));
    }
}
