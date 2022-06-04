package com.hyfetech.tools.classfileparser.dto;

import lombok.Data;

/**
 * 局部变量类型信息
 * @author shanghaifei
 * @date 2022/6/4
 */
@Data
public class LocalVariableTypeInfo {
    int startPc;

    int length;

    int nameIndex;

    String name;

    int signatureIndex;

    String signature;

    int index;
}
