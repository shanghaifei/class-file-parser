package com.hyfetech.tools.classfileparser.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * 访问修饰符通用类
 * @author shanghaifei
 * @date 2022/6/1
 */
public class AccessFlagUtil {
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
     * 获取方法参数访问修饰符信息
     * @param accessFlags 10进制的访问修饰符
     * @return 访问修饰符信息
     */
    public static String getMethodParameterAccessFlags(int accessFlags) {
        HashMap<Integer,String> accessFlagMap = new HashMap<>();
        accessFlagMap.put(0x0010,"ACC_FINAL");
        accessFlagMap.put(0x1000,"ACC_SYNTHETIC");
        accessFlagMap.put(0x8000,"ACC_MANDATED");
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
}
