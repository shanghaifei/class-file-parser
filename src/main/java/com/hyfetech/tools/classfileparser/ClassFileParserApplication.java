package com.hyfetech.tools.classfileparser;

import jdk.nashorn.internal.runtime.regexp.joni.Regex;

import java.io.*;
import java.math.BigInteger;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字节码文件解析器
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
 * @author shanghaifei
 * @date 2022/5/30
 */
public class ClassFileParserApplication {
    public static void main(String[] args) throws Exception {
        // 默认的字节码文件路径
        String defaultClassFilePath = "/Users/shanghaifei/Documents/source-code/openjdk/openjdk-jdk8u/jdk/src/share/classes/study/src/Main.class";
        System.out.println("请输入字节码文件路径：");
        Scanner scanner = new Scanner(System.in);
        String classFilePath = scanner.nextLine();
        if(classFilePath == null || classFilePath.isEmpty()) {
            classFilePath = defaultClassFilePath;
        }
        if (classFilePath == null || classFilePath.isEmpty()) {
            System.out.println("字节码文件路径未输入！");
            return;
        }
        File classFile = new File(classFilePath);
        if (!classFile.exists()) {
            System.out.println("字节码文件不存在！");
            return;
        }
        FileInputStream classFileInputStream = new FileInputStream(classFile);

        //region 解析字节码文件魔数和版本号

        /** magic：魔数（4字节） */
        String magic = binary(readUBytes(classFileInputStream,4),16);
        System.out.println(String.format("魔数：%s",magic));
        /** minor_version：次版本号（2字节） */
        int minorVersion = readU2(classFileInputStream);
        /** major_version：主版本号（2字节） */
        int majorVersion = readU2(classFileInputStream);
        System.out.println(String.format("版本号（主.次）：%s.%s",majorVersion,minorVersion));

        //endregion

        //region 解析常量池信息

        /** 常量池数量（2字节）：实际数量 = 常量池数量 - 1 */
        int constantPoolNumber = readU2(classFileInputStream);
        int actualConstantPoolNumber = constantPoolNumber - 1;
        System.out.println(String.format("常量池数量：%s",actualConstantPoolNumber));
        // 解析后的常量池信息
        String[] constantPoolParseInfos = new String[constantPoolNumber];
        for(int i = 1;i <= actualConstantPoolNumber;i++) {
            byte[] constantPoolInfoTagBytes = readUBytes(classFileInputStream,1);
            /** 常量池类型：下面的定义根据jdk8的官方文档来（1字节） */
            byte constantPoolInfoTag = constantPoolInfoTagBytes[0];
            switch (constantPoolInfoTag) {
                case 7:
                    /**
                     * 类型：CONSTANT_Class（3字节）
                     * CONSTANT_Class_info {
                     *  u1 tag;
                     *  u2 name_index;
                     * }
                     */
                    /** 名称索引（2字节） */
                    int referenceContantPoolIndex = readU2(classFileInputStream);
                    constantPoolParseInfos[i] = "#{" + referenceContantPoolIndex + "}";
                    System.out.println(String.format("【%s】号常量池->类型：CONSTANT_Class 名称：#{%s}",
                            i,referenceContantPoolIndex));
                    break;
                case 9:
                    /**
                     * 类型：CONSTANT_Fieldref（5字节）
                     * CONSTANT_Fieldref_info {
                     *  u1 tag;
                     *  u2 class_index;
                     *  u2 name_and_type_index;
                     * }
                     */
                    /** 类索引（2字节） */
                    int fieldClassIndex = readU2(classFileInputStream);
                    /** 名称和类型索引（2字节） */
                    int fieldNameAndTypeIndex = readU2(classFileInputStream);
                    constantPoolParseInfos[i] = String.format("[ref]#{%s}.#{%s}",
                            fieldClassIndex,fieldNameAndTypeIndex);
                    System.out.println(String.format("【%s】号常量池->类型：CONSTANT_Fieldref 类名称：#{%s} 字段签名：#{%s}",
                            i,fieldClassIndex,fieldNameAndTypeIndex));
                    break;
                case 10:
                    /**
                     * 类型：CONSTANT_Methodref
                     * CONSTANT_Methodref_info {
                     *  u1 tag;
                     *  u2 class_index;
                     *  u2 name_and_type_index;
                     * }
                     */
                    /** 类索引（2字节） */
                    int methodClassIndex = readU2(classFileInputStream);
                    /** 名称和类型索引（2字节） */
                    int methodNameAndTypeIndex = readU2(classFileInputStream);
                    constantPoolParseInfos[i] = String.format("[ref]#{%s}.#{%s}",
                            methodClassIndex,methodNameAndTypeIndex);
                    System.out.println(String.format("【%s】号常量池->类型：CONSTANT_Methodref 类名称：#{%s} 方法签名：#{%s}",
                            i,methodClassIndex,methodNameAndTypeIndex));
                    break;
                case 11:
                    /**
                     * 类型：CONSTANT_InterfaceMethodref
                     * CONSTANT_InterfaceMethodref_info {
                     *  u1 tag;
                     *  u2 class_index;
                     *  u2 name_and_type_index;
                     * }
                     */
                    /** 类索引（2字节） */
                    int interfaceMethodClassIndex = readU2(classFileInputStream);
                    /** 名称和类型索引（2字节） */
                    int interfaceMethodNameAndTypeIndex = readU2(classFileInputStream);
                    System.out.println(String.format("【%s】号常量池->类型：CONSTANT_InterfaceMethodref 类名称：#{%s} 方法签名：#{%s}",
                            i,interfaceMethodClassIndex,interfaceMethodNameAndTypeIndex));
                    break;
                case 8:
                    /**
                     * 类型：CONSTANT_String
                     * CONSTANT_String_info {
                     *  u1 tag;
                     *  u2 string_index;
                     * }
                     */
                    /** 字符串索引（2字节） */
                    int stringIndex = readU2(classFileInputStream);
                    constantPoolParseInfos[i] = String.valueOf(stringIndex);
                    System.out.println(String.format("【%s】号常量池->类型：CONSTANT_String 字符串：%s",
                            i,stringIndex));
                    break;
                case 3:
                    /**
                     * 类型：CONSTANT_Integer
                     * CONSTANT_Integer_info {
                     *  u1 tag;
                     *  u4 bytes;
                     * }
                     */
                    long intValue = readU4(classFileInputStream);
                    constantPoolParseInfos[i] = String.valueOf(intValue);
                    System.out.println(String.format("【%s】号常量池->类型：CONSTANT_Integer 值：%s",
                            i,intValue));
                    break;
                case 4:
                    /**
                     * 类型：CONSTANT_Float
                     * CONSTANT_Float_info {
                     *  u1 tag;
                     *  u4 bytes;
                     * }
                     */
                    long floatValue = readU4(classFileInputStream);
                    constantPoolParseInfos[i] = String.valueOf(floatValue);
                    System.out.println(String.format("【%s】号常量池->类型：CONSTANT_Float 值：%s",
                            i,floatValue));
                    break;
                case 5:
                    /**
                     * 类型：CONSTANT_Long
                     * CONSTANT_Long_info {
                     *  u1 tag;
                     *  u4 high_bytes;
                     *  u4 low_bytes;
                     * }
                     */
                    long longHighValue = readU4(classFileInputStream);
                    long longLowValue = readU4(classFileInputStream);
                    long longValue = (long)(longHighValue * Math.pow(256,4) + longLowValue);
                    constantPoolParseInfos[i] = String.valueOf(longValue);
                    System.out.println(String.format("【%s】号常量池->类型：CONSTANT_Long 值：%s",
                            i,longValue));
                    break;
                case 6:
                    /**
                     * 类型：CONSTANT_Double
                     * CONSTANT_Double_info {
                     *  u1 tag;
                     *  u4 high_bytes;
                     *  u4 low_bytes;
                     * }
                     */
                    /** 高位值（4字节） */
                    long doubleHighValue = readU4(classFileInputStream);
                    /** 低位值（4字节） */
                    long doubleLowValue = readU4(classFileInputStream);
                    double doubleValue = doubleHighValue * Math.pow(256,4) + doubleLowValue;
                    System.out.println(String.format("【%s】号常量池->类型：CONSTANT_Double 值：%s",
                            i,doubleValue));
                    break;
                case 12:
                    /**
                     * 类型：CONSTANT_NameAndType
                     * CONSTANT_NameAndType_info {
                     *  u1 tag;
                     *  u2 name_index;
                     *  u2 descriptor_index;
                     * }
                     */
                    /** 名称索引（2字节） */
                    int nameAndTypeNameIndex = readU2(classFileInputStream);
                    /** 描述符索引（2字节） */
                    int nameAndTypeDescriptorIndex = readU2(classFileInputStream);
                    constantPoolParseInfos[i] = String.format("#{%s}(签名：#{%s})",
                        nameAndTypeNameIndex,nameAndTypeDescriptorIndex);
                    System.out.println(String.format("【%s】号常量池->类型：CONSTANT_NameAndType 名称：#{%s} 描述符：#{%s}",
                            i,nameAndTypeNameIndex,nameAndTypeDescriptorIndex));
                    break;
                case 1:
                    /**
                     * 类型：CONSTANT_Utf8
                     * CONSTANT_Utf8_info {
                     *  u1 tag;
                     *  u2 length;
                     *  u1 bytes[length];
                     * }
                     */
                    int utf8Length = readU2(classFileInputStream);
                    byte[] utf8Bytes = new byte[utf8Length];
                    classFileInputStream.read(utf8Bytes);
                    StringBuilder contentSB = new StringBuilder();
                    if(utf8Bytes.length > 0) {
                        for(byte c : utf8Bytes) {
                            // 将assic码转换为对应的字符
                            contentSB.append((char)c);
                        }
                    }
                    constantPoolParseInfos[i] = contentSB.toString();
                    System.out.println(String.format("【%s】号常量池->类型：CONSTANT_Utf8 长度：%s 内容：%s",
                            i,utf8Length,contentSB));
                    break;
                case 15:
                    /**
                     * 类型：CONSTANT_MethodHandle
                     * CONSTANT_MethodHandle_info {
                     *  u1 tag;
                     *  u1 reference_kind;
                     *  u2 reference_index;
                     * }
                     */
                    /** 引用类型（2字节） */
                    int referenceKind = readU2(classFileInputStream);
                    /** 引用索引（2字节） */
                    int referenceIndex = readU2(classFileInputStream);
                    System.out.println(String.format("【%s】号常量池->类型：CONSTANT_MethodHandle 引用类型：%s 引用索引：#{%s}",
                            i,referenceKind,referenceIndex));
                    break;
                case 16:
                    /**
                     * 类型：CONSTANT_MethodType
                     * CONSTANT_MethodType_info {
                     *  u1 tag;
                     *  u2 descriptor_index;
                     * }
                     */
                    /** 描述符索引（2字节） */
                    int methodTypeDescriptorIndex = readU2(classFileInputStream);
                    System.out.println(String.format("【%s】号常量池->类型：CONSTANT_MethodType 描述索引：#{%s}",
                            i,methodTypeDescriptorIndex));
                    break;
                case 18:
                    /**
                     * 类型：CONSTANT_InvokeDynamic
                     * CONSTANT_InvokeDynamic_info {
                     *  u1 tag;
                     *  u2 bootstrap_method_attr_index;
                     *  u2 name_and_type_index;
                     * }
                     */
                    /** bootstrap_method_attr_index（2字节） */
                    int invokeDynamicBootStrapMethodIndex = readU2(classFileInputStream);
                    /** name_and_type_index（2字节） */
                    int invokeDynamicNameAndTypeIndex = readU2(classFileInputStream);
                    System.out.println(String.format("【%s】号常量池->类型：CONSTANT_InvokeDynamic 启动方法属性索引：#{%s} 名称类型索引：#{%s}",
                            i,invokeDynamicBootStrapMethodIndex,invokeDynamicNameAndTypeIndex));
                    break;
            }
        }
        /** 连接符号引用 */
        linkSymbolRefrence(constantPoolParseInfos);

        //endregion

        //region 解析类访问修饰符、当前类、父类和实现的接口信息

        /** access_flags：访问修饰符（2字节） */
        int accessFlags = readU2(classFileInputStream);
        System.out.println(String.format("类访问修饰符：%s",getClassAccessFlags(accessFlags)));
        /** this_class：当前类（2字节） */
        int thisClassConstantIndex = readU2(classFileInputStream);
        System.out.println(String.format("当前类：%s（引用常量池【%s】）",constantPoolParseInfos[thisClassConstantIndex],
                thisClassConstantIndex));
        /** super_class：父类（2字节） */
        int superClassConstantIndex = readU2(classFileInputStream);
        System.out.println(String.format("父类：%s（引用常量池【%s】）",constantPoolParseInfos[superClassConstantIndex],
                superClassConstantIndex));
        /** interfaces_count：接口数量（2字节） */
        int interfaceCount = readU2(classFileInputStream);
        System.out.println(String.format("接口数量：%s",interfaceCount));
        // interfaces：实现的所有接口信息
        String interfaceIndexs = "";
        for(int i = 1;i <= interfaceCount;i++) {
            /** 接口索引：指向常量池中的classinfo常量（2字节） */
            int interfaceIndex = readU2(classFileInputStream);
            interfaceIndexs += constantPoolParseInfos[interfaceIndex] + ",";
        }
        if(!interfaceIndexs.isEmpty()) {
            interfaceIndexs = interfaceIndexs.substring(0,interfaceIndexs.length() - 1);
        }
        System.out.println(String.format("实现接口：%s", interfaceIndexs));

        //endregion

        //region 解析字段信息

        /**
         * field_info {
         *  u2 access_flags;
         *  u2 name_index;
         *  u2 descriptor_index;
         *  u2 attributes_count;
         *  attribute_info attributes[attributes_count];
         * }
         */
        /** fields_count：字段数量（2字节） */
        int fieldCount = readU2(classFileInputStream);
        System.out.println(String.format("字段数量：%s",fieldCount));
        // 遍历所有字段信息
        for(int i = 1;i <= fieldCount;i++) {
            /** 字段访问修饰符（2字节） */
            int fieldAccessFlags = readU2(classFileInputStream);
            /** 字段名称索引(2字节） */
            int fieldNameIndex = readU2(classFileInputStream);
            /** 字段描述符索引（2字节） */
            int fieldDescriptorIndex = readU2(classFileInputStream);
            System.out.println(String.format("【字段%s】字段访问修饰符：%s，字段名称：%s，字段描述符：%s -> %s",
                    i,getFieldAccessFlags(fieldAccessFlags),constantPoolParseInfos[fieldNameIndex],
                    constantPoolParseInfos[fieldDescriptorIndex],getFullFieldType(constantPoolParseInfos[fieldDescriptorIndex])));
            // 解析属性信息
            parseAttributeInfo(classFileInputStream,2,constantPoolParseInfos);
        }

        //endregion

        //region 解析方法信息

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
        int methodCount = readU2(classFileInputStream);
        System.out.println(String.format("方法数量：%s",methodCount));
        // 遍历所有方法信息
        for(int i = 1;i <= methodCount;i++) {
            /** 方法访问修饰符（2字节） */
            int methodAccessFlags = readU2(classFileInputStream);
            /** 方法名称索引（2字节） */
            int methodNameIndex = readU2(classFileInputStream);
            /** 方法描述符索引（2字节） */
            int methodDescriptorIndex = readU2(classFileInputStream);
            System.out.println(String.format("【方法%s】方法访问修饰符：%s，方法名称：%s，方法描述符：%s",
                    i,getMethodAccessFlags(methodAccessFlags),constantPoolParseInfos[methodNameIndex],
                    constantPoolParseInfos[methodDescriptorIndex]));
            // 解析属性信息
            parseAttributeInfo(classFileInputStream,2,constantPoolParseInfos);
        }

        //endregion

        //region 解析属性信息

        parseAttributeInfo(classFileInputStream,1,constantPoolParseInfos);

        //endregion
    }

    //region 公共方法

    /**
     * 解析属性信息
     * attribute_info {
     *  u2 attribute_name_index;
     *  u4 attribute_length;
     *  u1 info[attribute_length];
     * }
     * @param classFileInputStream 字节码文件输入流
     * @param level 级别
     * @param constantPoolParseInfos 常量池解析信息数组
     */
    public static void parseAttributeInfo(FileInputStream classFileInputStream,
                                          int level,
                                          String[] constantPoolParseInfos) throws Exception {
        // 属性数量
        int attributesCount = readU2(classFileInputStream);
        String prefix = "";
        for(int i = 1;i < level;i++) {
            prefix += "    ";
        }
        System.out.println(String.format(prefix + "属性数量：%s",attributesCount));
        // 遍历属性信息
        for(int i = 1;i <= attributesCount;i++) {
            /** 属性名称索引（2字节） */
            int attributeNameIndex = readU2(classFileInputStream);
            String attributeName = constantPoolParseInfos[attributeNameIndex];
            /** 属性长度（4字节） */
            int attributeLength = Integer.parseInt(binary(readUBytes(classFileInputStream,4), 10));
            switch (attributeName) {
                case "ConstantValue":
                    //region 常量值

                    /**
                     * ConstantValue_attribute {
                     *  u2 attribute_name_index;
                     *  u4 attribute_length;
                     *  u2 constantvalue_index;
                     * }
                     */
                    /** 常量值索引（2字节） */
                    int constantValueIndex = readU2(classFileInputStream);
                    System.out.println(String.format(prefix + "【%s号属性】属性名：%s，属性长度：%s，属性值：%s",
                            i,attributeName,attributeLength,constantPoolParseInfos[constantValueIndex]));
                    break;

                    //endregion
                case "Code":
                    //region 代码

                    /**
                     * Code_attribute {
                     *  u2 attribute_name_index;
                     *  u4 attribute_length;
                     *  u2 max_stack;
                     *  u2 max_locals;
                     *  u4 code_length;
                     *  u1 code[code_length];
                     *  u2 exception_table_length;
                     *  { u2 start_pc;
                     *  u2 end_pc;
                     *  u2 handler_pc;
                     *  u2 catch_type;
                     *  } exception_table[exception_table_length];
                     *  u2 attributes_count;
                     *  attribute_info attributes[attributes_count];
                     * }
                     */
                    /** 最大栈深度（2字节） */
                    int maxStack = readU2(classFileInputStream);
                    /** maxLocals（2字节） */
                    int maxLocals = readU2(classFileInputStream);
                    /** 代码长度（4字节） */
                    long codeLength = readU4(classFileInputStream);
                    /** 代码 */
                    StringBuilder opDescSB = new StringBuilder();
                    if(codeLength > 0) {
                        for (int codeIndex = 0;codeIndex < codeLength;codeIndex++) {
                            if(codeIndex == 0) {
                                opDescSB.append(prefix + "  {\r\n");
                            }
                            // 操作指令code
                            byte[] opCodeBytes = readUBytes(classFileInputStream,1);
                            String opDesc = getOpDesc(Integer.parseInt(binary(opCodeBytes, 10)));
                            if(!opDesc.equals("nop")) {
                                if (opDesc.indexOf("|") == -1) {
                                    opDescSB.append(String.format("%s    %s：%s\r\n", prefix, codeIndex, opDesc));
                                }
                                else {
                                    String[] opDescInfos = opDesc.split("\\|");
                                    // 参数长度
                                    int parameterLength = Integer.parseInt(opDescInfos[1]);
                                    byte[] codeParameterIndexBytes = readUBytes(classFileInputStream,parameterLength);
                                    int codeParameterIndex = Integer.parseInt(binary(codeParameterIndexBytes, 10));
                                    opDescSB.append(String.format("%s    %s：%s %s\r\n", prefix, codeIndex, opDescInfos[0],
                                            constantPoolParseInfos[codeParameterIndex]));
                                    codeIndex += parameterLength;
                                }
                            }
                            if(codeIndex == codeLength - 1) {
                                opDescSB.append(prefix + "  }");
                            }
                        }
                    }
                    /** 异常表长度（2字节） */
                    int exceptionTableLength = readU2(classFileInputStream);
                    // 遍历异常表
                    for(int exceptionTableIndex = 1;exceptionTableIndex <= exceptionTableLength; exceptionTableIndex++) {
                        /** start_pc（2字节） */
                        int startPc = readU2(classFileInputStream);
                        /** end_pc（2字节） */
                        int endPc = readU2(classFileInputStream);
                        /** handler_pc（2字节） */
                        int handlerPc = readU2(classFileInputStream);
                        /** catch_type（2字节） */
                        int catchType = readU2(classFileInputStream);
                    }
                    System.out.println(String.format(prefix + "【%s号属性】属性名：%s，最大栈深度：%s，maxLocals：%s，代码长度：%s，代码指令如下：",
                            i,attributeName,maxStack,maxLocals,codeLength));
                    System.out.println(opDescSB);
                    // 解析属性信息
                    parseAttributeInfo(classFileInputStream,level + 1,constantPoolParseInfos);
                    break;

                    //endregion
                case "LineNumberTable":
                    //region 行号表

                    /**
                     * LineNumberTable_attribute {
                     *  u2 attribute_name_index;
                     *  u4 attribute_length;
                     *  u2 line_number_table_length;
                     *  { u2 start_pc;
                     *  u2 line_number;
                     *  } line_number_table[line_number_table_length];
                     * }
                     */
                    /** 行号表长度（2字节） */
                    int lineNumberTableLength = readU2(classFileInputStream);
                    StringBuilder sbLineNumberInfos = new StringBuilder();
                    for(int lineNumberTableIndex = 1;lineNumberTableIndex <= lineNumberTableLength;lineNumberTableIndex++) {
                        /** start_pc：code里面的指令索引（2字节） */
                        int startPc = readU2(classFileInputStream);
                        /** line_number：源代码的行号（2字节） */
                        int lineNumber = readU2(classFileInputStream);
                        sbLineNumberInfos.append(String.format("code:%s -> 源代码行号:%s，", startPc,lineNumber));
                    }
                    if(!sbLineNumberInfos.toString().isEmpty()) {
                        sbLineNumberInfos = new StringBuilder(sbLineNumberInfos.substring(0, sbLineNumberInfos.length() - 1));
                    }
                    System.out.println(String.format(prefix + "【%s号属性】属性名：%s，行号表信息：【%s】",
                            i,attributeName,sbLineNumberInfos));
                    break;

                    //endregion
                case "Exceptions":
                    //region 异常表

                    /**
                     * Exceptions_attribute {
                     *  u2 attribute_name_index;
                     *  u4 attribute_length;
                     *  u2 number_of_exceptions;
                     *  u2 exception_index_table[number_of_exceptions];
                     * }
                     */
                    /** 异常数量（2字节） */
                    int numberOfExceptions = readU2(classFileInputStream);
                    StringBuilder sbExceptionInfos = new StringBuilder();
                    for(int exceptionsIndex = 1;exceptionsIndex <= numberOfExceptions;exceptionsIndex++) {
                        /** 异常索引（2字节） */
                        int exceptionIndex = readU2(classFileInputStream);
                        sbExceptionInfos.append(String.format("%s,", constantPoolParseInfos[exceptionIndex]));
                    }
                    if(!sbExceptionInfos.toString().isEmpty()) {
                        sbExceptionInfos = new StringBuilder(sbExceptionInfos.substring(0, sbExceptionInfos.length() - 1));
                    }
                    System.out.println(String.format(prefix + "【%s号属性】属性名：%s，抛出的异常信息：【%s】",
                            i,attributeName,sbExceptionInfos));
                    break;

                    //endregion
                case "SourceFile":
                    //region 源文件名称

                    /**
                     * SourceFile_attribute {
                     *  u2 attribute_name_index;
                     *  u4 attribute_length;
                     *  u2 sourcefile_index;
                     * }
                     */
                    /** 源文件索引（2字节） */
                    int sourceFileIndex = readU2(classFileInputStream);
                    System.out.println(String.format(prefix + "【%s号属性】属性名：%s，源文件名称：%s",
                            i,attributeName,constantPoolParseInfos[sourceFileIndex]));
                    break;

                    //endregion
                case "InnerClasses":
                    //region 内部类

                    /**
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
                     */
                    /** 内部类数量（2字节） */
                    int numberOfClasses = readU2(classFileInputStream);
                    StringBuilder sbInnerClassInfos = new StringBuilder();
                    for(int innerClassIndex = 1;innerClassIndex <= numberOfClasses;innerClassIndex++) {
                        /** 内部类信息索引（2字节） */
                        int innerClassIndexInfo = readU2(classFileInputStream);
                        /** 外部类信息索引（2字节） */
                        int outerClassIndexInfo = readU2(classFileInputStream);
                        /** 内部名称索引（2字节） */
                        int innerNameIndex = readU2(classFileInputStream);
                        /** 内部类访问修饰符（2字节） */
                        int innerClassAccessFlags = readU2(classFileInputStream);

                        sbInnerClassInfos.append(String.format("内部类名称：%s|外部类名称：%s|内部名称：%s|内部类访问修饰符：%s,",
                                constantPoolParseInfos[innerClassIndexInfo],constantPoolParseInfos[outerClassIndexInfo],
                                constantPoolParseInfos[innerNameIndex],getNestedClassAccessFlags(innerClassAccessFlags)));
                    }
                    if(!sbInnerClassInfos.toString().isEmpty()) {
                        sbInnerClassInfos = new StringBuilder(sbInnerClassInfos.substring(0, sbInnerClassInfos.length() - 1));
                    }
                    System.out.println(String.format(prefix + "【%s号属性】属性名：%s，内部类信息：【%s】",
                            i,attributeName,sbInnerClassInfos));
                    break;

                    //endregion
                default:
                    System.out.println("未支持的属性名称：" + attributeName);
            }
        }
    }

    //endregion

    //region 读取指定字节长度的内容

    /**
     * 读取2个无符号字节
     * @param classFileInputStream 字节码文件输入流
     * @return 读取的十进制内容
     * @throws Exception
     */
    public static int readU2(FileInputStream classFileInputStream) throws Exception {
        return Integer.parseInt(binary(readUBytes(classFileInputStream,2),10));
    }

    /**
     * 读取4个无符号字节
     * @param classFileInputStream 字节码文件输入流
     * @return 读取的十进制内容
     * @throws Exception
     */
    public static long readU4(FileInputStream classFileInputStream) throws Exception {
        return Integer.parseInt(binary(readUBytes(classFileInputStream,4),10));
    }

    /**
     * 读取指定位数的无符号字节
     * @param classFileInputStream 字节码文件输入流
     * @param byteCount 字节数量
     * @return 读取到的字节数据
     * @throws Exception
     */
    public static byte[] readUBytes(FileInputStream classFileInputStream,
                                 int byteCount) throws Exception {
        byte[] bytes = new byte[byteCount];
        classFileInputStream.read(bytes);
        return bytes;
    }

    //endregion

    //region 符号引用相关（动态替换等等）

    /**
     * 对符号引用进行链接操作
     * @param constantPoolParseInfos 常量池解析信息数组
     */
    public static void linkSymbolRefrence(String[] constantPoolParseInfos) {
        if(constantPoolParseInfos != null && constantPoolParseInfos.length > 0) {
            for(int i = 1;i < constantPoolParseInfos.length;i++) {
                /** 可以解析所有ClassInfo类型 */
                String constantPoolParseInfo = constantPoolParseInfos[i];
                if(constantPoolParseInfo != null && constantPoolParseInfo.startsWith("#{") && constantPoolParseInfo.endsWith("}")) {
                    linkSymbolRefrenceSingle(constantPoolParseInfos,constantPoolParseInfo,i);
                }
            }
            for(int i = 1;i < constantPoolParseInfos.length;i++) {
                /** 可以解析所有NameAndType类型 */
                String constantPoolParseInfo = constantPoolParseInfos[i];
                if(constantPoolParseInfo != null && !constantPoolParseInfo.startsWith("[ref]")
                        && constantPoolParseInfo.contains("#{") && constantPoolParseInfo.contains("}")) {
                    linkSymbolRefrenceSingle(constantPoolParseInfos,constantPoolParseInfo,i);
                }
            }
            for(int i = 1;i < constantPoolParseInfos.length;i++) {
                /** 可以解析所有Ref类型（MethodRef、FieldRef等） */
                String constantPoolParseInfo = constantPoolParseInfos[i];
                if(constantPoolParseInfo != null && constantPoolParseInfo.startsWith("[ref]")
                        && constantPoolParseInfo.contains("#{") && constantPoolParseInfo.contains("}")) {
                    linkSymbolRefrenceSingle(constantPoolParseInfos,
                            constantPoolParseInfo.replace("[ref]",""),i);
                }
            }
        }
    }

    /**
     * 对符号引用进行链接操作（单个）
     * @param constantPoolParseInfos 常量池解析信息数组
     * @param constantPoolParseInfo 当前常量池解析信息
     */
    public static void linkSymbolRefrenceSingle(String[] constantPoolParseInfos,
                                  String constantPoolParseInfo,int index) {
        String pattern = "#\\{(\\d+)\\}";
        // 创建 Pattern 对象
        Pattern r = Pattern.compile(pattern);
        // 现在创建 matcher 对象
        Matcher m = r.matcher(constantPoolParseInfo);
        while (m.find()) {
            // 符号引用
            String symbolRefrence = m.group();
            // 解析出来的常量池索引
            int symbolRefrenceNumber = Integer.parseInt(m.group(1));
            // 使用常量池信息替换符号引用
            constantPoolParseInfo = constantPoolParseInfo.replace(symbolRefrence,constantPoolParseInfos[symbolRefrenceNumber]);
        }
        constantPoolParseInfos[index] = constantPoolParseInfo;
    }

    //endregion

    //region 获取访问修饰符信息

    /**
     * 获取嵌套类访问修饰符信息
     * @param accessFlags 10进制的访问修饰符
     * @return 访问修饰符信息
     */
    public static String getNestedClassAccessFlags(int accessFlags) {
        HashMap<Integer,String> accessFlagMap = new HashMap<>();
        accessFlagMap.put(0x0001,"ACC_PUBLIC");
        accessFlagMap.put(0x0002,"ACC_PRIVATE");
        accessFlagMap.put(0x0004,"ACC_PROTECTED");
        accessFlagMap.put(0x0008,"ACC_STATIC");
        accessFlagMap.put(0x0010,"ACC_FINAL");
        accessFlagMap.put(0x0200,"ACC_INTERFACE");
        accessFlagMap.put(0x0400,"ACC_ABSTRACT");
        accessFlagMap.put(0x1000,"ACC_SYNTHETIC");
        accessFlagMap.put(0x2000,"ACC_ANNOTATION");
        accessFlagMap.put(0x4000,"ACC_ENUM");
        return getAccessFlags(accessFlagMap,accessFlags);
    }

    /**
     * 获取类访问修饰符信息
     * @param accessFlags 10进制的访问修饰符
     * @return 访问修饰符信息
     */
    public static String getClassAccessFlags(int accessFlags) {
        HashMap<Integer,String> accessFlagMap = new HashMap<>();
        accessFlagMap.put(0x0001,"ACC_PUBLIC");
        accessFlagMap.put(0x0010,"ACC_FINAL");
        accessFlagMap.put(0x0020,"ACC_SUPER");
        accessFlagMap.put(0x0200,"ACC_INTERFACE");
        accessFlagMap.put(0x0400,"ACC_ABSTRACT");
        accessFlagMap.put(0x1000,"ACC_SYNTHETIC");
        accessFlagMap.put(0x2000,"ACC_ANNOTATION");
        accessFlagMap.put(0x4000,"ACC_ENUM");
        return getAccessFlags(accessFlagMap,accessFlags);
    }

    /**
     * 获取字段访问修饰符信息
     * @param accessFlags 10进制的访问修饰符
     * @return 访问修饰符信息
     */
    public static String getFieldAccessFlags(int accessFlags) {
        HashMap<Integer,String> accessFlagMap = new HashMap<>();
        accessFlagMap.put(0x0001,"ACC_PUBLIC");
        accessFlagMap.put(0x0002,"ACC_PRIVATE");
        accessFlagMap.put(0x0004,"ACC_PROTECTED");
        accessFlagMap.put(0x0008,"ACC_STATIC");
        accessFlagMap.put(0x0010,"ACC_FINAL");
        accessFlagMap.put(0x0040,"ACC_VOLATILE");
        accessFlagMap.put(0x0080,"ACC_TRANSIENT");
        accessFlagMap.put(0x1000,"ACC_SYNTHETIC");
        accessFlagMap.put(0x4000,"ACC_ENUM");
        return getAccessFlags(accessFlagMap,accessFlags);
    }

    /**
     * 获取方法访问修饰符信息
     * @param accessFlags 10进制的访问修饰符
     * @return 访问修饰符信息
     */
    public static String getMethodAccessFlags(int accessFlags) {
        HashMap<Integer,String> accessFlagMap = new HashMap<>();
        accessFlagMap.put(0x0001,"ACC_PUBLIC");
        accessFlagMap.put(0x0002,"ACC_PRIVATE");
        accessFlagMap.put(0x0004,"ACC_PROTECTED");
        accessFlagMap.put(0x0008,"ACC_STATIC");
        accessFlagMap.put(0x0010,"ACC_FINAL");
        accessFlagMap.put(0x0020,"ACC_SYNCHRONIZED");
        accessFlagMap.put(0x0040,"ACC_BRIDGE");
        accessFlagMap.put(0x0080,"ACC_VARARGS");
        accessFlagMap.put(0x0100,"ACC_NATIVE");
        accessFlagMap.put(0x0400,"ACC_ABSTRACT");
        accessFlagMap.put(0x0800,"ACC_STRICT");
        accessFlagMap.put(0x1000,"ACC_SYNTHETIC");
        accessFlagMap.put(0x4000,"ACC_ENUM");
        return getAccessFlags(accessFlagMap,accessFlags);
    }

    /**
     * 获取访问标识符信息
     * @param accessFlagMap 访问标识符map
     * @param accessFlags 10进制的访问修饰符
     * @return 访问标识符信息
     */
    private static String getAccessFlags(HashMap<Integer,String> accessFlagMap,
                                         int accessFlags) {
        String accessFlagsDesc = "";
        for (Map.Entry<Integer,String> accessFlag : accessFlagMap.entrySet()) {
            if((accessFlags & accessFlag.getKey().intValue()) != 0) {
                accessFlagsDesc += accessFlag.getValue() + "、";
            }
        }
        if(!accessFlagsDesc.isEmpty()) {
            accessFlagsDesc = accessFlagsDesc.substring(0,accessFlagsDesc.length() - 1);
        }
        return accessFlagsDesc;
    }

    //endregion

    //region 字段、方法类型

    /**
     * 根据简短的字段类型获取完整的字段类型
     * @param shortFieldType 简短的字段类型
     * @return 完整的字段类型
     */
    public static String getFullFieldType(String shortFieldType) {
        String first = shortFieldType.substring(0,1);
        String fullFieldType = "";
        switch (first) {
            case "B":
                fullFieldType = "byte";
                break;
            case "C":
                fullFieldType = "char";
                break;
            case "D":
                fullFieldType = "double";
                break;
            case "F":
                fullFieldType = "float";
                break;
            case "I":
                fullFieldType = "int";
                break;
            case "J":
                fullFieldType = "long";
                break;
            case "L":
                fullFieldType = "类" + shortFieldType.substring(1,shortFieldType.length());
                break;
            case "S":
                fullFieldType = "short";
                break;
            case "Z":
                fullFieldType = "boolean";
                break;
            case "[":
                fullFieldType = "数组";
                break;
        }
        return fullFieldType;
    }

    //endregion

    //region 字节码操作指令相关

    /**
     * 获取操作名称
     * @param opCode 操作code
     * @return 访问修饰符信息
     */
    public static String getOpDesc(int opCode) {
        HashMap<Integer,String> opCodeDescMap = new HashMap<>();
        /** Constants */
        opCodeDescMap.put(0,"nop");
        opCodeDescMap.put(1,"aconst_null");
        opCodeDescMap.put(2,"iconst_m1");
        opCodeDescMap.put(3,"iconst_0");
        opCodeDescMap.put(4,"iconst_1");
        opCodeDescMap.put(5,"iconst_2");
        opCodeDescMap.put(6,"iconst_3");
        opCodeDescMap.put(7,"iconst_4");
        opCodeDescMap.put(8,"iconst_5");
        opCodeDescMap.put(9,"lconst_0");
        opCodeDescMap.put(10,"lconst_1");
        opCodeDescMap.put(11,"fconst_0");
        opCodeDescMap.put(12,"fconst_1");
        opCodeDescMap.put(13,"fconst_2");
        opCodeDescMap.put(14,"dconst_0");
        opCodeDescMap.put(15,"dconst_1");
        opCodeDescMap.put(16,"bipush");
        opCodeDescMap.put(17,"sipush");
        opCodeDescMap.put(18,"ldc");
        opCodeDescMap.put(19,"ldc_w");
        opCodeDescMap.put(20,"ldc2_w");
        /** Loads */
        opCodeDescMap.put(21,"iload");
        opCodeDescMap.put(22,"lload");
        opCodeDescMap.put(23,"fload");
        opCodeDescMap.put(24,"dload");
        opCodeDescMap.put(25,"aload");
        opCodeDescMap.put(26,"iload_0");
        opCodeDescMap.put(27,"iload_1");
        opCodeDescMap.put(28,"iload_2");
        opCodeDescMap.put(29,"iload_3");
        opCodeDescMap.put(30,"lload_0");
        opCodeDescMap.put(31,"lload_1");
        opCodeDescMap.put(32,"lload_2");
        opCodeDescMap.put(33,"lload_3");
        opCodeDescMap.put(34,"fload_0");
        opCodeDescMap.put(35,"fload_1");
        opCodeDescMap.put(36,"fload_2");
        opCodeDescMap.put(37,"fload_3");
        opCodeDescMap.put(38,"dload_0");
        opCodeDescMap.put(39,"dload_1");
        opCodeDescMap.put(40,"dload_2");
        opCodeDescMap.put(41,"dload_3");
        opCodeDescMap.put(42,"aload_0");
        opCodeDescMap.put(43,"aload_1");
        opCodeDescMap.put(44,"aload_2");
        opCodeDescMap.put(45,"aload_3");
        opCodeDescMap.put(46,"iaload");
        opCodeDescMap.put(47,"laload");
        opCodeDescMap.put(48,"faload");
        opCodeDescMap.put(49,"daload");
        opCodeDescMap.put(50,"aaload");
        opCodeDescMap.put(51,"baload");
        opCodeDescMap.put(52,"caload");
        opCodeDescMap.put(53,"saload");
        /** Stores */
        opCodeDescMap.put(54,"istore");
        opCodeDescMap.put(55,"lstore");
        opCodeDescMap.put(56,"fstore");
        opCodeDescMap.put(57,"dstore");
        opCodeDescMap.put(58,"astore");
        opCodeDescMap.put(59,"istore_0");
        opCodeDescMap.put(60,"istore_1");
        opCodeDescMap.put(61,"istore_2");
        opCodeDescMap.put(62,"istore_3");
        opCodeDescMap.put(63,"lstore_0");
        opCodeDescMap.put(64,"lstore_1");
        opCodeDescMap.put(65,"lstore_2");
        opCodeDescMap.put(66,"lstore_3");
        opCodeDescMap.put(67,"fstore_0");
        opCodeDescMap.put(68,"fstore_1");
        opCodeDescMap.put(69,"fstore_2");
        opCodeDescMap.put(70,"fstore_3");
        opCodeDescMap.put(71,"dstore_0");
        opCodeDescMap.put(72,"dstore_1");
        opCodeDescMap.put(73,"dstore_2");
        opCodeDescMap.put(74,"dstore_3");
        opCodeDescMap.put(75,"astore_0");
        opCodeDescMap.put(76,"astore_1");
        opCodeDescMap.put(77,"astore_2");
        opCodeDescMap.put(78,"astore_3");
        opCodeDescMap.put(79,"iastore");
        opCodeDescMap.put(80,"lastore");
        opCodeDescMap.put(81,"fastore");
        opCodeDescMap.put(82,"dastore");
        opCodeDescMap.put(83,"aastore");
        opCodeDescMap.put(84,"bastore");
        opCodeDescMap.put(85,"castore");
        opCodeDescMap.put(86,"sastore");
        /** Stack */
        opCodeDescMap.put(87,"pop");
        opCodeDescMap.put(88,"pop2");
        opCodeDescMap.put(89,"dup");
        opCodeDescMap.put(90,"dup_x1");
        opCodeDescMap.put(91,"dup_x2");
        opCodeDescMap.put(92,"dup2");
        opCodeDescMap.put(93,"dup2_x1");
        opCodeDescMap.put(94,"dup2_x2");
        opCodeDescMap.put(95,"swap");
        /** Math */
        opCodeDescMap.put(96 ,"iadd");
        opCodeDescMap.put(97 ,"ladd");
        opCodeDescMap.put(98 ,"fadd");
        opCodeDescMap.put(99 ,"dadd");
        opCodeDescMap.put(100,"isub");
        opCodeDescMap.put(101,"lsub");
        opCodeDescMap.put(102,"fsub");
        opCodeDescMap.put(103,"dsub");
        opCodeDescMap.put(104,"imul");
        opCodeDescMap.put(105,"lmul");
        opCodeDescMap.put(106,"fmul");
        opCodeDescMap.put(107,"dmul");
        opCodeDescMap.put(108,"idiv");
        opCodeDescMap.put(109,"ldiv");
        opCodeDescMap.put(110,"fdiv");
        opCodeDescMap.put(111,"ddiv");
        opCodeDescMap.put(112,"irem");
        opCodeDescMap.put(113,"lrem");
        opCodeDescMap.put(114,"frem");
        opCodeDescMap.put(115,"drem");
        opCodeDescMap.put(116,"ineg");
        opCodeDescMap.put(117,"lneg");
        opCodeDescMap.put(118,"fneg");
        opCodeDescMap.put(119,"dneg");
        opCodeDescMap.put(120,"ishl");
        opCodeDescMap.put(121,"lshl");
        opCodeDescMap.put(122,"ishr");
        opCodeDescMap.put(123,"lshr");
        opCodeDescMap.put(124,"iushr");
        opCodeDescMap.put(125,"lushr");
        opCodeDescMap.put(126,"iand");
        opCodeDescMap.put(127,"land");
        opCodeDescMap.put(128,"ior");
        opCodeDescMap.put(129,"lor");
        opCodeDescMap.put(130,"ixor");
        opCodeDescMap.put(131,"lxor");
        opCodeDescMap.put(132,"iinc");
        /** Conversions */
        opCodeDescMap.put(133,"i2l");
        opCodeDescMap.put(134,"i2f");
        opCodeDescMap.put(135,"i2d");
        opCodeDescMap.put(136,"l2i");
        opCodeDescMap.put(137,"l2f");
        opCodeDescMap.put(138,"l2d");
        opCodeDescMap.put(139,"f2i");
        opCodeDescMap.put(140,"f2l");
        opCodeDescMap.put(141,"f2d");
        opCodeDescMap.put(142,"d2i");
        opCodeDescMap.put(143,"d2l");
        opCodeDescMap.put(144,"d2f");
        opCodeDescMap.put(145,"i2b");
        opCodeDescMap.put(146,"i2c");
        opCodeDescMap.put(147,"i2s");
        /** Comparisons */
        opCodeDescMap.put(148,"lcmp");
        opCodeDescMap.put(149,"fcmpl");
        opCodeDescMap.put(150,"fcmpg");
        opCodeDescMap.put(151,"dcmpl");
        opCodeDescMap.put(152,"dcmpg");
        opCodeDescMap.put(153,"ifeq");
        opCodeDescMap.put(154,"ifne");
        opCodeDescMap.put(155,"iflt");
        opCodeDescMap.put(156,"ifge");
        opCodeDescMap.put(157,"ifgt");
        opCodeDescMap.put(158,"ifle");
        opCodeDescMap.put(159,"if_icmpeq");
        opCodeDescMap.put(160,"if_icmpne");
        opCodeDescMap.put(161,"if_icmplt");
        opCodeDescMap.put(162,"if_icmpge");
        opCodeDescMap.put(163,"if_icmpgt");
        opCodeDescMap.put(164,"if_icmple");
        opCodeDescMap.put(165,"if_acmpeq");
        opCodeDescMap.put(166,"if_acmpne");
        /** Control */
        opCodeDescMap.put(167,"goto");
        opCodeDescMap.put(168,"jsr");
        opCodeDescMap.put(169,"ret");
        opCodeDescMap.put(170,"tableswitch");
        opCodeDescMap.put(171,"lookupswitch");
        opCodeDescMap.put(172,"ireturn");
        opCodeDescMap.put(173,"lreturn");
        opCodeDescMap.put(174,"freturn");
        opCodeDescMap.put(175,"dreturn");
        opCodeDescMap.put(176,"areturn");
        opCodeDescMap.put(177,"return");
        /** References */
        opCodeDescMap.put(178,"getstatic|2");
        opCodeDescMap.put(179,"putstatic|2");
        opCodeDescMap.put(180,"getfield|2");
        opCodeDescMap.put(181,"putfield|2");
        opCodeDescMap.put(182,"invokevirtual|2");
        opCodeDescMap.put(183,"invokespecial|2");
        opCodeDescMap.put(184,"invokestatic|2");
        opCodeDescMap.put(185,"invokeinterface|2");
        opCodeDescMap.put(186,"invokedynamic|2");
        opCodeDescMap.put(187,"new|2");
        opCodeDescMap.put(188,"newarray");
        opCodeDescMap.put(189,"anewarray");
        opCodeDescMap.put(190,"arraylength");
        opCodeDescMap.put(191,"athrow");
        opCodeDescMap.put(192,"checkcast");
        opCodeDescMap.put(193,"instanceof");
        opCodeDescMap.put(194,"monitorenter");
        opCodeDescMap.put(195,"monitorexit");
        /** Extended */
        opCodeDescMap.put(196,"wide");
        opCodeDescMap.put(197,"multianewarray");
        opCodeDescMap.put(198,"ifnull");
        opCodeDescMap.put(199,"ifnonnull");
        opCodeDescMap.put(200,"goto_w");
        opCodeDescMap.put(201,"jsr_w");
        /** Reserved */
        opCodeDescMap.put(202,"breakpoint");
        opCodeDescMap.put(254,"impdep1");
        opCodeDescMap.put(255,"impdep2");
        String opDesc = opCodeDescMap.get(opCode);
        if(opDesc == null || opCodeDescMap.isEmpty()) {
            opDesc = "未知指令";
        }
        return opDesc;
    }

    //endregion

    //region 进制之间的转换

    /**
     * 字节数组转换为16进制
     * @param bytes 字节数组
     * @return 16进制字符串
     */
    public static String hex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte aByte : bytes) {
            result.append(String.format("%02x", aByte));
            // upper case
            // result.append(String.format("%02X", aByte));
        }
        return result.toString();
    }

    /**
     * 将字节数组转换为各种进制的字符串
     * @param bytes 字节数组
     * @param radix 进制
     * @return 进制字符串
     */
    public static String binary(byte[] bytes, int radix) {
        // 这里的1代表正数
        return new BigInteger(1, bytes).toString(radix);
    }

    //endregion
}
