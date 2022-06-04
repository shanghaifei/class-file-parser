package com.hyfetech.tools.classfileparser.dto.enums;

/**
 * 【栈帧类型】枚举
 * @author shanghaifei
 * @date 2022/6/3
 */
public enum StackFrameTypeEnum {
    SAME((short)0,(short)63),
    SAME_LOCALS_1_STACK_ITEM((short)64,(short)127),
    SAME_LOCALS_1_STACK_ITEM_EXTENDED((short)247,(short)247),
    CHOP((short)248,(short)250),
    SAME_FRAME_EXTENDED((short)251,(short)251),
    APPEND((short)252,(short)254),
    FULL_FRAME((short)255,(short)255);

    StackFrameTypeEnum(short typeRangeStart, short typeRangeEnd) {
        this.typeRangeStart = typeRangeStart;
        this.typeRangeEnd = typeRangeEnd;
    }

    private short typeRangeStart;

    private short typeRangeEnd;

    public short getTypeRangeStart() {
        return typeRangeStart;
    }

    public void setTypeRangeStart(short typeRangeStart) {
        this.typeRangeStart = typeRangeStart;
    }

    public short getTypeRangeEnd() {
        return typeRangeEnd;
    }

    public void setTypeRangeEnd(short typeRangeEnd) {
        this.typeRangeEnd = typeRangeEnd;
    }

    public static StackFrameTypeEnum getEnumByType(short type) {
        StackFrameTypeEnum[] values = StackFrameTypeEnum.values();
        if(values != null && values.length > 0) {
            for(StackFrameTypeEnum item : values) {
                if(type >= item.getTypeRangeStart() && type <= item.getTypeRangeEnd()) {
                    return item;
                }
            }
        }
        return null;
    }
}
