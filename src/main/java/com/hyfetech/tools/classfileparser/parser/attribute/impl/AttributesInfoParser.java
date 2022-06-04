package com.hyfetech.tools.classfileparser.parser.attribute.impl;

import com.hyfetech.tools.classfileparser.dto.AttributeInfo;
import com.hyfetech.tools.classfileparser.parser.attribute.AbstractAttributeInfoParser;
import com.hyfetech.tools.classfileparser.parser.attribute.AttributeInfoParserFactory;
import com.hyfetech.tools.classfileparser.parser.attribute.IAttributeInfoParser;
import com.hyfetech.tools.classfileparser.parser.baseinfo.ClassFileConstantPoolParser;
import com.hyfetech.tools.classfileparser.utils.ReadClassFileUtil;

import java.io.InputStream;

/**
 * 属性列表信息解析器
 * @author shanghaifei
 * @date 2022/6/1
 */
public class AttributesInfoParser extends AbstractAttributeInfoParser {
    /**
     * 实例对象
     */
    private static AttributesInfoParser instance = new AttributesInfoParser();

    /**
     * 获取实例对象
     * @return
     */
    public static AttributesInfoParser getInstance() {
        if(instance.getConstantPoolParseInfos() == null) {
            instance.setConstantPoolParseInfos(
                    ClassFileConstantPoolParser.getInstance().getConstantPoolParseInfos());
        }
        return instance;
    }

    /**
     * 解析
     * attribute_info {
     *  u2 attribute_name_index;
     *  u4 attribute_length;
     *  u1 info[attribute_length];
     * }
     * @param classFileInputStream   字节码文件输入流
     * @param attributeInfo          属性信息
     */
    @Override
    public void parse(InputStream classFileInputStream, AttributeInfo attributeInfo) throws Exception {
        // 属性层级
        int level = attributeInfo.getLevel();
        String prefix = getPrintPrefix(level);
        /** 属性数量（2字节） */
        int attributesCount = ReadClassFileUtil.readU2(classFileInputStream);
        System.out.println(String.format("%s【%s级属性】属性数量：%s",prefix,level,attributesCount));
        // 遍历属性信息
        for(int attributeIndex = 1;attributeIndex <= attributesCount;attributeIndex++) {
            /** 属性名称索引（2字节） */
            int attributeNameIndex = ReadClassFileUtil.readU2(classFileInputStream);
            String attributeName = constantPoolParseInfos[attributeNameIndex];
            /** 属性长度（4字节） */
            long attributeLength = ReadClassFileUtil.readU4(classFileInputStream);
            // 根据属性名称创建解析器
            IAttributeInfoParser attributeInfoParser = AttributeInfoParserFactory.createParser(attributeName);
            AttributeInfo attributeItem = AttributeInfo.builder().attributeIndex(attributeIndex)
                    .attributeName(attributeName).attributeLength(attributeLength)
                    .level(level).build();
            attributeInfoParser.parse(classFileInputStream,attributeItem);
        }
    }
}
