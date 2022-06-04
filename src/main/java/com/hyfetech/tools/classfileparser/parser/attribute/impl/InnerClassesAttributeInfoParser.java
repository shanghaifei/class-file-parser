package com.hyfetech.tools.classfileparser.parser.attribute.impl;

import com.hyfetech.tools.classfileparser.dto.AttributeInfo;
import com.hyfetech.tools.classfileparser.parser.attribute.AbstractAttributeInfoParser;
import com.hyfetech.tools.classfileparser.parser.attribute.IAttributeInfoParser;
import com.hyfetech.tools.classfileparser.utils.AccessFlagUtil;
import com.hyfetech.tools.classfileparser.utils.ReadClassFileUtil;

import java.io.InputStream;

/**
 * 【内部类】属性信息解析器
 * @author shanghaifei
 * @date 2022/6/1
 */
public class InnerClassesAttributeInfoParser extends AbstractAttributeInfoParser {
    /**
     * 解析
     * InnerClasses_attribute {
     *  u2 attribute_name_index;
     *  u4 attribute_length;
     *  u2 number_of_classes;
     *  { u2 inner_class_info_index;
     *  u2 outer_class_info_index;
     *  u2 inner_name_index;
     *  u2 inner_class_access_flags;
     *  } classes[number_of_classes];
     * }
     * @param classFileInputStream   字节码文件输入流
     * @param attributeInfo          属性信息
     */
    @Override
    public void parse(InputStream classFileInputStream, AttributeInfo attributeInfo) throws Exception {
        String prefix = getPrintPrefix(attributeInfo.getLevel());
        /** 内部类数量（2字节） */
        int numberOfClasses = ReadClassFileUtil.readU2(classFileInputStream);
        StringBuilder sbInnerClassInfos = new StringBuilder();
        for(int innerClassIndex = 1;innerClassIndex <= numberOfClasses;innerClassIndex++) {
            /** 内部类信息索引（2字节） */
            int innerClassIndexInfo = ReadClassFileUtil.readU2(classFileInputStream);
            /** 外部类信息索引（2字节） */
            int outerClassIndexInfo = ReadClassFileUtil.readU2(classFileInputStream);
            /** 内部名称索引（2字节） */
            int innerNameIndex = ReadClassFileUtil.readU2(classFileInputStream);
            /** 内部类访问修饰符（2字节） */
            int innerClassAccessFlags = ReadClassFileUtil.readU2(classFileInputStream);

            sbInnerClassInfos.append(String.format("内部类名称：%s|外部类名称：%s|内部名称：%s|内部类访问修饰符：%s,",
                    constantPoolParseInfos[innerClassIndexInfo],constantPoolParseInfos[outerClassIndexInfo],
                    constantPoolParseInfos[innerNameIndex], AccessFlagUtil.getNestedClassAccessFlags(innerClassAccessFlags)));
        }
        if(!sbInnerClassInfos.toString().isEmpty()) {
            sbInnerClassInfos = new StringBuilder(sbInnerClassInfos.substring(0, sbInnerClassInfos.length() - 1));
        }
        System.out.println(String.format("%s【属性%s】属性名：%s，内部类信息：【%s】",
                prefix,attributeInfo.getAttributeIndex(),attributeInfo.getAttributeName(),
                sbInnerClassInfos));
    }
}
