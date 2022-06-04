package com.hyfetech.tools.classfileparser.dto;

import com.hyfetech.tools.classfileparser.dto.enums.StackFrameTypeEnum;
import lombok.Data;

/**
 * 栈帧信息
 * @author shanghaifei
 * @date 2022/6/3
 */
@Data
public class StackMapFrameInfo {
    StackFrameTypeEnum frameType;

    int offsetDelta;

    int numberOfLocals;

    VerificationTypeInfo[] locals;

    int numberOfStackItems;

    VerificationTypeInfo[] stack;
}
