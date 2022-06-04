package com.hyfetech.tools.classfileparser.dto;

import com.hyfetech.tools.classfileparser.dto.enums.VerificationTypeInfoTagEnum;
import lombok.Data;

/**
 * 校验类型信息
 * @author shanghaifei
 * @date 2022/6/3
 */
@Data
public class VerificationTypeInfo {
    VerificationTypeInfoTagEnum tag;

    int cPoolIndex;

    int offset;
}
