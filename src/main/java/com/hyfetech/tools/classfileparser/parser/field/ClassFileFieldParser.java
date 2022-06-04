package com.hyfetech.tools.classfileparser.parser.field;

import com.hyfetech.tools.classfileparser.dto.AttributeInfo;
import com.hyfetech.tools.classfileparser.parser.baseinfo.AbstractClassFileInfoParser;
import com.hyfetech.tools.classfileparser.parser.baseinfo.IClassFileInfoParser;
import com.hyfetech.tools.classfileparser.parser.attribute.impl.AttributesInfoParser;
import com.hyfetech.tools.classfileparser.utils.AccessFlagUtil;
import com.hyfetech.tools.classfileparser.utils.FieldTypeUtil;
import com.hyfetech.tools.classfileparser.utils.ReadClassFileUtil;

import java.io.InputStream;

/**
 * 字节码文件的字段解析器
 * @author shanghaifei
 * @date 2022/6/2
 */
public class ClassFileFieldParser extends AbstractClassFileInfoParser implements IClassFileInfoParser {
    /**
     * 构造函数
     * @param constantPoolParseInfos
     */
    public ClassFileFieldParser(String[] constantPoolParseInfos) {
        this.constantPoolParseInfos = constantPoolParseInfos;
    }

    /**
     * 解析
     * @param classFileInputStream 字节码文件输入流
     */
    @Override
    public String parse(InputStream classFileInputStream) throws Exception {
        /** fields_count：字段数量（2字节） */
        int fieldCount = ReadClassFileUtil.readU2(classFileInputStream);
        System.out.println(String.format("字段数量：%s",fieldCount));
        // 遍历所有字段信息
        for(int i = 1;i <= fieldCount;i++) {
            /** 字段访问修饰符（2字节） */
            int fieldAccessFlags = ReadClassFileUtil.readU2(classFileInputStream);
            /** 字段名称索引(2字节） */
            int fieldNameIndex = ReadClassFileUtil.readU2(classFileInputStream);
            /** 字段描述符索引（2字节） */
            int fieldDescriptorIndex = ReadClassFileUtil.readU2(classFileInputStream);
            System.out.println(String.format("【字段%s】字段访问修饰符：%s，字段名称：%s，字段描述符：%s -> %s",
                    i, AccessFlagUtil.getFieldAccessFlags(fieldAccessFlags),constantPoolParseInfos[fieldNameIndex],
                    constantPoolParseInfos[fieldDescriptorIndex], FieldTypeUtil.getFullFieldType(constantPoolParseInfos[fieldDescriptorIndex])));
            // 解析属性信息
            AttributesInfoParser.getInstance().parse(classFileInputStream,
                    AttributeInfo.builder().level(2).build());
        }
        return null;
    }
}
