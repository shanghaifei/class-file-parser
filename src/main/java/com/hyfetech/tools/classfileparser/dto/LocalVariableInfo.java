package com.hyfetech.tools.classfileparser.dto;

import lombok.Data;

/**
 * 局部变量信息
 * @author shanghaifei
 * @date 2022/6/3
 */
@Data
public class LocalVariableInfo {
    int startPc;

    int length;

    int nameIndex;

    String name;

    int descriptorIndex;

    String descriptor;

    int index;
}
