package com.hyfetech.tools.classfileparser.dto.enums;

/**
 * 【元素标记】枚举
 * @author shanghaifei
 * @date 2022/6/2
 */
public enum ElementTagEnum {
    BYTE((short)'B',ElementValueEnum.CONST_VALUE_INDEX),
    CHAR((short)'C',ElementValueEnum.CONST_VALUE_INDEX),
    DOUBLE((short)'D',ElementValueEnum.CONST_VALUE_INDEX),
    FLOAT((short)'F',ElementValueEnum.CONST_VALUE_INDEX),
    INT((short)'I',ElementValueEnum.CONST_VALUE_INDEX),
    LONG((short)'J',ElementValueEnum.CONST_VALUE_INDEX),
    SHORT((short)'S',ElementValueEnum.CONST_VALUE_INDEX),
    BOOLEAN((short)'Z',ElementValueEnum.CONST_VALUE_INDEX),
    STRING((short)'s',ElementValueEnum.CONST_VALUE_INDEX),
    ENUMTYPE((short)'e',ElementValueEnum.ENUM_CONST_VALUE),
    CLASS((short)'c',ElementValueEnum.CLASS_INFO_INDEX),
    ANNOTATIONTYPE((short)'@',ElementValueEnum.ANNOTATION_VALUE),
    ARRAYTYPE((short)'[',ElementValueEnum.ARRAY_VALUE);

    ElementTagEnum(short tag,ElementValueEnum value) {
        this.tag = tag;
        this.value = value;
    }

    private short tag;

    private ElementValueEnum value;

    public short getTag() {
        return tag;
    }

    public void setTag(short tag) {
        this.tag = tag;
    }

    public ElementValueEnum getValue() {
        return value;
    }

    public void setValue(ElementValueEnum value) {
        this.value = value;
    }

    public static ElementTagEnum getEnumByTag(short tag) {
        ElementTagEnum[] values = ElementTagEnum.values();
        if(values != null && values.length > 0) {
            for(ElementTagEnum item : values) {
                if(item.getTag() == tag) {
                    return item;
                }
            }
        }
        return null;
    }
}
