package com.hyfetech.tools.classfileparser.parser.baseinfo;

import com.hyfetech.tools.classfileparser.parser.constantpool.ConstantPoolInfoParserFactory;
import com.hyfetech.tools.classfileparser.parser.constantpool.IConstantPoolInfoParser;
import com.hyfetech.tools.classfileparser.utils.ReadClassFileUtil;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字节码文件常量池解析器
 * @author shanghaifei
 * @date 2022/6/2
 */
@Data
public class ClassFileConstantPoolParser extends AbstractClassFileInfoParser implements IClassFileInfoParser {
    private static ClassFileConstantPoolParser instance = new ClassFileConstantPoolParser();

    private ClassFileConstantPoolParser() {}

    public static ClassFileConstantPoolParser getInstance() {
        return instance;
    }

    /**
     * 解析
     * @param classFileInputStream 字节码文件输入流
     */
    @Override
    public String parse(InputStream classFileInputStream) throws Exception {
        /** 常量池数量（2字节）：实际数量 = 常量池数量 - 1 */
        int constantPoolNumber = ReadClassFileUtil.readU2(classFileInputStream);
        int actualConstantPoolNumber = constantPoolNumber - 1;
        System.out.println(String.format("常量池数量：%s",actualConstantPoolNumber));
        // 解析后的常量池信息
        instance.constantPoolParseInfos = new String[constantPoolNumber];
        for(int i = 1;i <= actualConstantPoolNumber;i++) {
            byte[] constantPoolInfoTagBytes = ReadClassFileUtil.readUBytes(classFileInputStream,1);
            /** 常量池类型：下面的定义根据jdk8的官方文档来（1字节） */
            byte constantPoolInfoTag = constantPoolInfoTagBytes[0];
            IConstantPoolInfoParser constantPoolInfoParser = ConstantPoolInfoParserFactory.createParser(constantPoolInfoTag);
            String parseInfo = constantPoolInfoParser.parse(classFileInputStream, i);
            instance.constantPoolParseInfos[i] = parseInfo;
        }
        // 链接符号引用
        linkSymbolRefrence(instance.constantPoolParseInfos);
        return null;
    }

    //region 符号引用相关

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
}
