package com.hyfetech.tools.classfileparser.parser.constantpool;

import com.hyfetech.tools.classfileparser.parser.constantpool.impl.*;

/**
 * 常量池解析器工厂
 * @author shanghaifei
 * @date 2022/6/1
 */
public class ConstantPoolInfoParserFactory {
    /**
     * 创建解析器
     * @param constantPoolTag 常量池标记
     * @return 解析器
     */
    public static IConstantPoolInfoParser createParser(byte constantPoolTag) {
        switch (constantPoolTag) {
            case 7:
                return new ConstantClassInfoParser();
            case 9:
                return new ConstantFieldRefInfoParser();
            case 10:
                return new ConstantMethodRefInfoParser();
            case 11:
                return new ConstantInterfaceMethodRefInfoParser();
            case 8:
                return new ConstantStringInfoParser();
            case 3:
                return new ConstantIntegerInfoParser();
            case 4:
                return new ConstantFloatInfoParser();
            case 5:
                return new ConstantLongInfoParser();
            case 6:
                return new ConstantDoubleInfoParser();
            case 12:
                return new ConstantNameAndTypeInfoParser();
            case 1:
                return new ConstantUtf8InfoParser();
            case 15:
                return new ConstantMethodHandleInfoParser();
            case 16:
                return new ConstantMethodTypeInfoParser();
            case 18:
                return new ConstantInvokeDynamicInfoParser();
            default:
                System.out.println("未知的常量池标记：" + constantPoolTag);
        }
        return null;
    }
}
