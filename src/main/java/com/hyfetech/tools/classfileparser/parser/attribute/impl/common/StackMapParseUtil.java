package com.hyfetech.tools.classfileparser.parser.attribute.impl.common;

import com.hyfetech.tools.classfileparser.dto.StackMapFrameInfo;
import com.hyfetech.tools.classfileparser.dto.VerificationTypeInfo;
import com.hyfetech.tools.classfileparser.dto.enums.StackFrameTypeEnum;
import com.hyfetech.tools.classfileparser.dto.enums.VerificationTypeInfoTagEnum;
import com.hyfetech.tools.classfileparser.utils.ReadClassFileUtil;

import java.io.InputStream;

/**
 * 【栈映射表】属性解析通用类
 * @author shanghaifei
 * @date 2022/6/3
 */
public class StackMapParseUtil {
    /**
     * 解析栈帧信息
     * union stack_map_frame {
     *   same_frame;
     *   same_locals_1_stack_item_frame;
     *   same_locals_1_stack_item_frame_extended;
     *   chop_frame;
     *   same_frame_extended;
     *   append_frame;
     *   full_frame;
     * }
     *
     * same_frame {
     *   u1 frame_type = SAME; // 0-63
     * }
     *
     * same_locals_1_stack_item_frame {
     *   u1 frame_type = SAME_LOCALS_1_STACK_ITEM; // 64-127
     *   verification_type_info stack[1];
     * }
     *
     * same_locals_1_stack_item_frame_extended {
     *   u1 frame_type = SAME_LOCALS_1_STACK_ITEM_EXTENDED; // 247
     *   u2 offset_delta;
     *   verification_type_info stack[1];
     * }
     *
     * chop_frame {
     *   u1 frame_type = CHOP; // 248-250
     *   u2 offset_delta;
     * }
     *
     * same_frame_extended {
     *   u1 frame_type = SAME_FRAME_EXTENDED; // 251
     *   u2 offset_delta;
     * }
     *
     * append_frame {
     *   u1 frame_type = APPEND; // 252-254
     *   u2 offset_delta;
     *   verification_type_info locals[frame_type - 251];
     * }
     *
     * full_frame {
     *   u1 frame_type = FULL_FRAME; // 255
     *   u2 offset_delta;
     *   u2 number_of_locals;
     *   verification_type_info locals[number_of_locals];
     *   u2 number_of_stack_items;
     *   verification_type_info stack[number_of_stack_items];
     * }
     * @param classFileInputStream
     * @return
     * @throws Exception
     */
    public static StackMapFrameInfo parseStackMapFrameInfo(InputStream classFileInputStream)
            throws Exception {
        StackMapFrameInfo stackMapFrameInfo = new StackMapFrameInfo();
        /** 栈帧类型（1字节） */
        short frameType = ReadClassFileUtil.readU1(classFileInputStream);
        StackFrameTypeEnum frameTypeEnum = StackFrameTypeEnum.getEnumByType(frameType);
        stackMapFrameInfo.setFrameType(frameTypeEnum);
        if(frameTypeEnum == StackFrameTypeEnum.SAME_LOCALS_1_STACK_ITEM) {
            /** stack */
            stackMapFrameInfo.setNumberOfStackItems(1);
            VerificationTypeInfo verificationTypeInfo = parseVerificationTypeInfo(classFileInputStream);
            VerificationTypeInfo[] stack = new VerificationTypeInfo[1];
            stack[0] = verificationTypeInfo;
            stackMapFrameInfo.setStack(stack);
        }
        else if(frameTypeEnum == StackFrameTypeEnum.SAME_LOCALS_1_STACK_ITEM_EXTENDED) {
            /** offset_delta（2字节） */
            int offsetDelta = ReadClassFileUtil.readU2(classFileInputStream);
            stackMapFrameInfo.setOffsetDelta(offsetDelta);
            /** stack */
            stackMapFrameInfo.setNumberOfStackItems(1);
            VerificationTypeInfo verificationTypeInfo = parseVerificationTypeInfo(classFileInputStream);
            VerificationTypeInfo[] stack = new VerificationTypeInfo[1];
            stack[0] = verificationTypeInfo;
            stackMapFrameInfo.setStack(stack);
        }
        else if(frameTypeEnum == StackFrameTypeEnum.CHOP || frameTypeEnum == StackFrameTypeEnum.SAME_FRAME_EXTENDED) {
            /** offset_delta（2字节） */
            int offsetDelta = ReadClassFileUtil.readU2(classFileInputStream);
            stackMapFrameInfo.setOffsetDelta(offsetDelta);
        }
        else if(frameTypeEnum == StackFrameTypeEnum.APPEND) {
            /** offset_delta（2字节） */
            int offsetDelta = ReadClassFileUtil.readU2(classFileInputStream);
            stackMapFrameInfo.setOffsetDelta(offsetDelta);
            /** number_of_locals（2字节） */
            int numberOfLocals = frameType - 251;
            stackMapFrameInfo.setNumberOfLocals(numberOfLocals);
            /** locals（verification_type_info数组） */
            VerificationTypeInfo[] locals = new VerificationTypeInfo[numberOfLocals];
            for(int i = 0;i < numberOfLocals;i++) {
                locals[i] = parseVerificationTypeInfo(classFileInputStream);
            }
        }
        else if(frameTypeEnum == StackFrameTypeEnum.FULL_FRAME) {
            /** offset_delta（2字节） */
            int offsetDelta = ReadClassFileUtil.readU2(classFileInputStream);
            stackMapFrameInfo.setOffsetDelta(offsetDelta);
            /** number_of_locals（2字节） */
            int numberOfLocals = ReadClassFileUtil.readU2(classFileInputStream);
            stackMapFrameInfo.setNumberOfLocals(numberOfLocals);
            /** locals（verification_type_info数组） */
            VerificationTypeInfo[] locals = new VerificationTypeInfo[numberOfLocals];
            for(int i = 0;i < numberOfLocals;i++) {
                locals[i] = parseVerificationTypeInfo(classFileInputStream);
            }
            /** number_of_stack_items（2字节） */
            int numberOfStackItems = ReadClassFileUtil.readU2(classFileInputStream);
            stackMapFrameInfo.setNumberOfStackItems(numberOfStackItems);
            /** stack（verification_type_info数组） */
            VerificationTypeInfo[] stack = new VerificationTypeInfo[numberOfStackItems];
            for(int i = 0;i < numberOfStackItems;i++) {
                stack[i] = parseVerificationTypeInfo(classFileInputStream);
            }
        }
        return stackMapFrameInfo;
    }

    /**
     * 解析校验类型信息
     * union verification_type_info {
     *  Top_variable_info;
     *  Integer_variable_info;
     *  Float_variable_info;
     *  Long_variable_info;
     *  Double_variable_info;
     *  Null_variable_info;
     *  UninitializedThis_variable_info;
     *  Object_variable_info;
     *  Uninitialized_variable_info;
     * }
     *
     * Top_variable_info {
     *  u1 tag = ITEM_Top; // 0
     * }
     *
     * Integer_variable_info {
     *  u1 tag = ITEM_Integer; // 1
     * }
     *
     * Float_variable_info {
     *  u1 tag = ITEM_Float; // 2
     * }
     *
     * Null_variable_info {
     *  u1 tag = ITEM_Null; // 5
     * }
     *
     * UninitializedThis_variable_info {
     *  u1 tag = ITEM_UninitializedThis; // 6
     * }
     *
     * Object_variable_info {
     *  u1 tag = ITEM_Object; // 7
     *  u2 cpool_index;
     * }
     *
     * Uninitialized_variable_info {
     *  u1 tag = ITEM_Uninitialized; // 8
     *  u2 offset;
     * }
     *
     * Long_variable_info {
     *  u1 tag = ITEM_Long; // 4
     * }
     *
     * Double_variable_info {
     *  u1 tag = ITEM_Double; // 3
     * }
     *
     * @param classFileInputStream 字节码文件输入流
     * @return 校验类型信息
     * @throws Exception
     */
    public static VerificationTypeInfo parseVerificationTypeInfo(InputStream classFileInputStream)
            throws Exception {
        VerificationTypeInfo verificationTypeInfo = new VerificationTypeInfo();
        /** 标记索引（1字节） */
        short tag = ReadClassFileUtil.readU1(classFileInputStream);
        VerificationTypeInfoTagEnum tagEnum = VerificationTypeInfoTagEnum.getEnumByTag(tag);
        verificationTypeInfo.setTag(tagEnum);
        if(tagEnum == VerificationTypeInfoTagEnum.ITEM_OBJECT) {
            /** cpool_index（2字节） */
            int cPoolIndex = ReadClassFileUtil.readU2(classFileInputStream);
            verificationTypeInfo.setCPoolIndex(cPoolIndex);
        }
        else if(tagEnum == VerificationTypeInfoTagEnum.ITEM_UNINITIALIZED) {
            /** offset（2字节） */
            int offset = ReadClassFileUtil.readU2(classFileInputStream);
            verificationTypeInfo.setOffset(offset);
        }
        return verificationTypeInfo;
    }
}
