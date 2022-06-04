package com.hyfetech.tools.classfileparser.parser.attribute;

import com.hyfetech.tools.classfileparser.parser.attribute.impl.*;
import com.hyfetech.tools.classfileparser.parser.baseinfo.ClassFileConstantPoolParser;

/**
 * 属性解析器工厂
 * @author shanghaifei
 * @date 2022/6/1
 */
public class AttributeInfoParserFactory {
    /**
     * 创建解析器
     * @param attributeName 属性名称
     * @return 解析器
     */
    public static AbstractAttributeInfoParser createParser(String attributeName) {
        AbstractAttributeInfoParser parser = null;
        switch (attributeName) {
            case "ConstantValue":
                parser = new ConstantValueAttributeInfoParser();
                break;
            case "Code":
                parser = new CodeAttributeInfoParser();
                break;
            case "LineNumberTable":
                parser = new LineNumberTableAttributeInfoParser();
                break;
            case "Exceptions":
                parser = new ExceptionsAttributeInfoParser();
                break;
            case "SourceFile":
                parser = new SourceFileAttributeInfoParser();
                break;
            case "InnerClasses":
                parser = new InnerClassesAttributeInfoParser();
                break;
            case "EnclosingMethod":
                parser = new EnclosingMethodAttributeInfoParser();
                break;
            case "Synthetic":
                parser = new SyntheticAttributeInfoParser();
                break;
            case "Signature":
                parser = new SignatureAttributeInfoParser();
                break;
            case "Deprecated":
                parser = new DeprecatedAttributeInfoParser();
                break;
            case "LocalVariableTable":
                parser = new LocalVariableTableAttributeInfoParser();
                break;
            case "LocalVariableTypeTable":
                parser = new LocalVariableTypeTableAttributeInfoParser();
                break;
            case "SourceDebug":
                parser = new SourceDebugExtensionAttributeInfoParser();
                break;
            case "StackMapTable":
                parser = new StackMapTableAttributeInfoParser();
                break;
            case "RuntimeVisibleAnnotations":
                parser = new RuntimeVisibleAnnotationsAttributeInfoParser();
                break;
            case "RuntimeInvisibleAnnotations":
                parser = new RuntimeInvisibleAnnotationsAttributeInfoParser();
                break;
            case "MethodParameters":
                parser = new MethodParametersAttributeInfoParser();
                break;
            case "AnnotationDefault":
                parser = new AnnotationDefaultAttributeInfoParser();
                break;
            default:
                System.out.println("未提供解析器的属性名称：" + attributeName);
        }
        if(parser != null) {
            // 获取常量池解析信息
            parser.setConstantPoolParseInfos(
                    ClassFileConstantPoolParser.getInstance().getConstantPoolParseInfos());
        }
        return parser;
    }
}
