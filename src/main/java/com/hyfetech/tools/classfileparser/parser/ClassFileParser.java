package com.hyfetech.tools.classfileparser.parser;

import com.hyfetech.tools.classfileparser.dto.AttributeInfo;
import com.hyfetech.tools.classfileparser.parser.attribute.impl.AttributesInfoParser;
import com.hyfetech.tools.classfileparser.parser.baseinfo.ClassFileConstantPoolParser;
import com.hyfetech.tools.classfileparser.parser.baseinfo.ClassFileMagicParser;
import com.hyfetech.tools.classfileparser.parser.baseinfo.ClassFileVersionParser;
import com.hyfetech.tools.classfileparser.parser.baseinfo.IClassFileInfoParser;
import com.hyfetech.tools.classfileparser.parser.field.ClassFileFieldParser;
import com.hyfetech.tools.classfileparser.parser.interfaces.ClassFileInterfaceParser;
import com.hyfetech.tools.classfileparser.parser.method.ClassFileMethodParser;
import com.hyfetech.tools.classfileparser.utils.AccessFlagUtil;
import com.hyfetech.tools.classfileparser.utils.ReadClassFileUtil;

import java.io.InputStream;

/**
 * 字节码文件解析器
 * @author shanghaifei
 * @date 2022/6/2
 */
public class ClassFileParser {
    /**
     * 解析
     * ClassFile {
     *  u4 magic;
     *  u2 minor_version;
     *  u2 major_version;
     *  u2 constant_pool_count;
     *  cp_info constant_pool[constant_pool_count-1];
     *  u2 access_flags;
     *  u2 this_class;
     *  u2 super_class;
     *  u2 interfaces_count;
     *  u2 interfaces[interfaces_count];
     *  u2 fields_count;
     *  field_info fields[fields_count];
     *  u2 methods_count;
     *  method_info methods[methods_count];
     *  u2 attributes_count;
     *  attribute_info attributes[attributes_count];
     * }
     * @param classFileInputStream 字节码文件输入流
     * @throws Exception
     */
    public void parse(InputStream classFileInputStream) throws Exception {
        /** 解析字节码文件魔数 */
        IClassFileInfoParser magicParser = new ClassFileMagicParser();
        magicParser.parse(classFileInputStream);
        /** 解析字节码文件版本号（主版本和次版本） */
        IClassFileInfoParser versionParser = new ClassFileVersionParser();
        versionParser.parse(classFileInputStream);
        /** 解析常量池信息 **/
        ClassFileConstantPoolParser constantPoolParser = ClassFileConstantPoolParser.getInstance();
        constantPoolParser.parse(classFileInputStream);
        String[] constantPoolParseInfos = constantPoolParser.getConstantPoolParseInfos();
        /** access_flags：访问修饰符（2字节） */
        int accessFlags = ReadClassFileUtil.readU2(classFileInputStream);
        System.out.println(String.format("类访问修饰符：%s", AccessFlagUtil.getClassAccessFlags(accessFlags)));
        /** this_class：当前类（2字节） */
        int thisClassConstantIndex = ReadClassFileUtil.readU2(classFileInputStream);
        System.out.println(String.format("当前类：%s（引用常量池【%s】）",constantPoolParseInfos[thisClassConstantIndex],
                thisClassConstantIndex));
        /** super_class：父类（2字节） */
        int superClassConstantIndex = ReadClassFileUtil.readU2(classFileInputStream);
        System.out.println(String.format("父类：%s（引用常量池【%s】）",constantPoolParseInfos[superClassConstantIndex],
                superClassConstantIndex));
        /** 解析接口信息（interfaces_count、interfaces[interfaces_count]） */
        ClassFileInterfaceParser classFileInterfaceParser = new ClassFileInterfaceParser(constantPoolParseInfos);
        classFileInterfaceParser.parse(classFileInputStream);
        /** 解析字段信息（fields_count、fields[fields_count]） */
        ClassFileFieldParser classFileFieldParser = new ClassFileFieldParser(constantPoolParseInfos);
        classFileFieldParser.parse(classFileInputStream);
        /** 解析方法信息（methods_count、methods[methods_count]） */
        ClassFileMethodParser classFileMethodParser = new ClassFileMethodParser(constantPoolParseInfos);
        classFileMethodParser.parse(classFileInputStream);
        /** 解析属性信息（attributes_count、attributes[attributes_count]） */
        AttributesInfoParser attributeInfoParser = AttributesInfoParser.getInstance();
        attributeInfoParser.parse(classFileInputStream, AttributeInfo.builder().level(1).build());
    }
}
