package com.hyfetech.tools.classfileparser.dto;

import lombok.Data;

/**
 * 行号信息
 * @author shanghaifei
 * @date 2022/6/7
 */
@Data
public class LineNumberInfo {
    Integer startPc;

    /**
     * 行号
     */
    Integer lineNumber;
}
