package com.hyfetech.tools.classfileparser.utils;

/**
 * 字段类型帮助类
 * @author shanghaifei
 * @date 2022/6/2
 */
public class FieldTypeUtil {
    /**
     * 根据简短的字段类型获取完整的字段类型
     * @param shortFieldType 简短的字段类型
     * @return 完整的字段类型
     */
    public static String getFullFieldType(String shortFieldType) {
        String fullFieldType = "";
        if(shortFieldType != null && !shortFieldType.isEmpty()) {
            String fieldType = shortFieldType.substring(0, 1);
            switch (fieldType) {
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
                    fullFieldType = "类" + shortFieldType.substring(1);
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
        }
        return fullFieldType;
    }
}
