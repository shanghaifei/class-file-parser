package com.hyfetech.tools.classfileparser.dto.enums;

/**
 * 【校验类型信息标记】枚举
 * @author shanghaifei
 * @date 2022/6/3
 */
public enum VerificationTypeInfoTagEnum {
    ITEM_TOP((short)0),
    ITEM_INTEGER((short)1),
    ITEM_FLOAT((short)2),
    ITEM_NULL((short)5),
    ITEM_UNINITIALIZEDTHIS((short)6),
    ITEM_OBJECT((short)7),
    ITEM_UNINITIALIZED((short)8),
    ITEM_LONG((short)4),
    ITEM_DOUBLE((short)3);

    VerificationTypeInfoTagEnum(short tag) {
        this.tag = tag;
    }

    private short tag;

    public short getTag() {
        return tag;
    }

    public void setTag(short tag) {
        this.tag = tag;
    }

    public static VerificationTypeInfoTagEnum getEnumByTag(short tag) {
        VerificationTypeInfoTagEnum[] values = VerificationTypeInfoTagEnum.values();
        if(values != null && values.length > 0) {
            for(VerificationTypeInfoTagEnum item : values) {
                if(item.getTag() == tag) {
                    return item;
                }
            }
        }
        return null;
    }
}
