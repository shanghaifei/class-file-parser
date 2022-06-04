package com.hyfetech.tools.classfileparser.dto;

import lombok.Builder;
import lombok.Data;

/**
 * 属性信息
 * @author shanghaifei
 * @date 2022/6/1
 */
@Builder
@Data
public class AttributeInfo {
    /**
     * 属性下标
     */
    private int attributeIndex;

    /**
     * 属性蜜菓奶茶
     */
    private String attributeName;

    /**
     * 属性长度
     */
    private long attributeLength;

    /**
     * 层级（1级、2级等）
     */
    private int level;
}
