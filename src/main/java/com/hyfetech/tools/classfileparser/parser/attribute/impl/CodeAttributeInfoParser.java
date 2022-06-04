package com.hyfetech.tools.classfileparser.parser.attribute.impl;

import com.hyfetech.tools.classfileparser.dto.AttributeInfo;
import com.hyfetech.tools.classfileparser.parser.attribute.AbstractAttributeInfoParser;
import com.hyfetech.tools.classfileparser.parser.attribute.IAttributeInfoParser;
import com.hyfetech.tools.classfileparser.utils.ReadClassFileUtil;

import java.io.InputStream;
import java.util.HashMap;

/**
 * 【代码】属性信息解析器
 * @author shanghaifei
 * @date 2022/6/1
 */
public class CodeAttributeInfoParser extends AbstractAttributeInfoParser {
    /**
     * 解析
     * Code_attribute {
     *  u2 attribute_name_index;
     *  u4 attribute_length;
     *  u2 max_stack;
     *  u2 max_locals;
     *  u4 code_length;
     *  u1 code[code_length];
     *  u2 exception_table_length;
     *  { u2 start_pc;
     *  u2 end_pc;
     *  u2 handler_pc;
     *  u2 catch_type;
     *  } exception_table[exception_table_length];
     *  u2 attributes_count;
     *  attribute_info attributes[attributes_count];
     * }
     * @param classFileInputStream   字节码文件输入流
     * @param attributeInfo          属性信息
     */
    @Override
    public void parse(InputStream classFileInputStream, AttributeInfo attributeInfo) throws Exception {
        String prefix = getPrintPrefix(attributeInfo.getLevel());
        String codeContentPrefix = getPrintPrefix(attributeInfo.getLevel() + 1);
        /** 最大栈深度（2字节） */
        int maxStack = ReadClassFileUtil.readU2(classFileInputStream);
        /** maxLocals（2字节） */
        int maxLocals = ReadClassFileUtil.readU2(classFileInputStream);
        /** 代码长度（4字节） */
        long codeLength = ReadClassFileUtil.readU4(classFileInputStream);
        /** 代码 */
        StringBuilder opDescSB = new StringBuilder();
        if(codeLength > 0) {
            for (int codeIndex = 0;codeIndex < codeLength;codeIndex++) {
                if(codeIndex == 0) {
                    opDescSB.append(String.format("%s{\r\n",prefix));
                }
                // 操作指令code
                byte[] opCodeBytes = ReadClassFileUtil.readUBytes(classFileInputStream,1);
                String opDesc = getOpDesc(Integer.parseInt(ReadClassFileUtil.binary(opCodeBytes, 10)));
                if(!opDesc.equals("nop")) {
                    if (opDesc.indexOf("|") == -1) {
                        opDescSB.append(String.format("%s%s：%s\r\n", codeContentPrefix, codeIndex, opDesc));
                    }
                    else {
                        String[] opDescInfos = opDesc.split("\\|");
                        // 参数长度
                        int parameterLength = Integer.parseInt(opDescInfos[1]);
                        byte[] codeParameterIndexBytes = ReadClassFileUtil.readUBytes(classFileInputStream,parameterLength);
                        // 参数指向常量池中的索引
                        int codeParameterIndex = Integer.parseInt(ReadClassFileUtil.binary(codeParameterIndexBytes, 10));
                        opDescSB.append(String.format("%s%s：%s %s\r\n", codeContentPrefix, codeIndex, opDescInfos[0],
                                constantPoolParseInfos[codeParameterIndex]));
                        codeIndex += parameterLength;
                    }
                }
                if(codeIndex == codeLength - 1) {
                    opDescSB.append(String.format("%s}",prefix));
                }
            }
        }
        /** 异常表长度（2字节） */
        int exceptionTableLength = ReadClassFileUtil.readU2(classFileInputStream);
        // 遍历异常表
        for(int exceptionTableIndex = 1;exceptionTableIndex <= exceptionTableLength; exceptionTableIndex++) {
            /** start_pc（2字节） */
            int startPc = ReadClassFileUtil.readU2(classFileInputStream);
            /** end_pc（2字节） */
            int endPc = ReadClassFileUtil.readU2(classFileInputStream);
            /** handler_pc（2字节） */
            int handlerPc = ReadClassFileUtil.readU2(classFileInputStream);
            /** catch_type（2字节） */
            int catchType = ReadClassFileUtil.readU2(classFileInputStream);
        }
        System.out.println(String.format("%s【属性%s】属性名：%s，最大栈深度：%s，maxLocals：%s，代码长度：%s，代码指令如下：",
                prefix,attributeInfo.getAttributeIndex(),attributeInfo.getAttributeName(),
                maxStack,maxLocals,codeLength));
        System.out.println(opDescSB);
        // 解析属性信息
        IAttributeInfoParser attributeInfoParser = AttributesInfoParser.getInstance();
        attributeInfo.setLevel(attributeInfo.getLevel() + 1);
        attributeInfoParser.parse(classFileInputStream,attributeInfo);
    }

    //region 公共方法

    /**
     * 获取操作名称
     * @param opCode 操作code
     * @return 访问修饰符信息
     */
    public static String getOpDesc(int opCode) {
        HashMap<Integer,String> opCodeDescMap = new HashMap<>();
        /** Constants */
        opCodeDescMap.put(0,"nop");
        opCodeDescMap.put(1,"aconst_null");
        opCodeDescMap.put(2,"iconst_m1");
        opCodeDescMap.put(3,"iconst_0");
        opCodeDescMap.put(4,"iconst_1");
        opCodeDescMap.put(5,"iconst_2");
        opCodeDescMap.put(6,"iconst_3");
        opCodeDescMap.put(7,"iconst_4");
        opCodeDescMap.put(8,"iconst_5");
        opCodeDescMap.put(9,"lconst_0");
        opCodeDescMap.put(10,"lconst_1");
        opCodeDescMap.put(11,"fconst_0");
        opCodeDescMap.put(12,"fconst_1");
        opCodeDescMap.put(13,"fconst_2");
        opCodeDescMap.put(14,"dconst_0");
        opCodeDescMap.put(15,"dconst_1");
        opCodeDescMap.put(16,"bipush");
        opCodeDescMap.put(17,"sipush");
        opCodeDescMap.put(18,"ldc");
        opCodeDescMap.put(19,"ldc_w");
        opCodeDescMap.put(20,"ldc2_w");
        /** Loads */
        opCodeDescMap.put(21,"iload");
        opCodeDescMap.put(22,"lload");
        opCodeDescMap.put(23,"fload");
        opCodeDescMap.put(24,"dload");
        opCodeDescMap.put(25,"aload");
        opCodeDescMap.put(26,"iload_0");
        opCodeDescMap.put(27,"iload_1");
        opCodeDescMap.put(28,"iload_2");
        opCodeDescMap.put(29,"iload_3");
        opCodeDescMap.put(30,"lload_0");
        opCodeDescMap.put(31,"lload_1");
        opCodeDescMap.put(32,"lload_2");
        opCodeDescMap.put(33,"lload_3");
        opCodeDescMap.put(34,"fload_0");
        opCodeDescMap.put(35,"fload_1");
        opCodeDescMap.put(36,"fload_2");
        opCodeDescMap.put(37,"fload_3");
        opCodeDescMap.put(38,"dload_0");
        opCodeDescMap.put(39,"dload_1");
        opCodeDescMap.put(40,"dload_2");
        opCodeDescMap.put(41,"dload_3");
        opCodeDescMap.put(42,"aload_0");
        opCodeDescMap.put(43,"aload_1");
        opCodeDescMap.put(44,"aload_2");
        opCodeDescMap.put(45,"aload_3");
        opCodeDescMap.put(46,"iaload");
        opCodeDescMap.put(47,"laload");
        opCodeDescMap.put(48,"faload");
        opCodeDescMap.put(49,"daload");
        opCodeDescMap.put(50,"aaload");
        opCodeDescMap.put(51,"baload");
        opCodeDescMap.put(52,"caload");
        opCodeDescMap.put(53,"saload");
        /** Stores */
        opCodeDescMap.put(54,"istore");
        opCodeDescMap.put(55,"lstore");
        opCodeDescMap.put(56,"fstore");
        opCodeDescMap.put(57,"dstore");
        opCodeDescMap.put(58,"astore");
        opCodeDescMap.put(59,"istore_0");
        opCodeDescMap.put(60,"istore_1");
        opCodeDescMap.put(61,"istore_2");
        opCodeDescMap.put(62,"istore_3");
        opCodeDescMap.put(63,"lstore_0");
        opCodeDescMap.put(64,"lstore_1");
        opCodeDescMap.put(65,"lstore_2");
        opCodeDescMap.put(66,"lstore_3");
        opCodeDescMap.put(67,"fstore_0");
        opCodeDescMap.put(68,"fstore_1");
        opCodeDescMap.put(69,"fstore_2");
        opCodeDescMap.put(70,"fstore_3");
        opCodeDescMap.put(71,"dstore_0");
        opCodeDescMap.put(72,"dstore_1");
        opCodeDescMap.put(73,"dstore_2");
        opCodeDescMap.put(74,"dstore_3");
        opCodeDescMap.put(75,"astore_0");
        opCodeDescMap.put(76,"astore_1");
        opCodeDescMap.put(77,"astore_2");
        opCodeDescMap.put(78,"astore_3");
        opCodeDescMap.put(79,"iastore");
        opCodeDescMap.put(80,"lastore");
        opCodeDescMap.put(81,"fastore");
        opCodeDescMap.put(82,"dastore");
        opCodeDescMap.put(83,"aastore");
        opCodeDescMap.put(84,"bastore");
        opCodeDescMap.put(85,"castore");
        opCodeDescMap.put(86,"sastore");
        /** Stack */
        opCodeDescMap.put(87,"pop");
        opCodeDescMap.put(88,"pop2");
        opCodeDescMap.put(89,"dup");
        opCodeDescMap.put(90,"dup_x1");
        opCodeDescMap.put(91,"dup_x2");
        opCodeDescMap.put(92,"dup2");
        opCodeDescMap.put(93,"dup2_x1");
        opCodeDescMap.put(94,"dup2_x2");
        opCodeDescMap.put(95,"swap");
        /** Math */
        opCodeDescMap.put(96 ,"iadd");
        opCodeDescMap.put(97 ,"ladd");
        opCodeDescMap.put(98 ,"fadd");
        opCodeDescMap.put(99 ,"dadd");
        opCodeDescMap.put(100,"isub");
        opCodeDescMap.put(101,"lsub");
        opCodeDescMap.put(102,"fsub");
        opCodeDescMap.put(103,"dsub");
        opCodeDescMap.put(104,"imul");
        opCodeDescMap.put(105,"lmul");
        opCodeDescMap.put(106,"fmul");
        opCodeDescMap.put(107,"dmul");
        opCodeDescMap.put(108,"idiv");
        opCodeDescMap.put(109,"ldiv");
        opCodeDescMap.put(110,"fdiv");
        opCodeDescMap.put(111,"ddiv");
        opCodeDescMap.put(112,"irem");
        opCodeDescMap.put(113,"lrem");
        opCodeDescMap.put(114,"frem");
        opCodeDescMap.put(115,"drem");
        opCodeDescMap.put(116,"ineg");
        opCodeDescMap.put(117,"lneg");
        opCodeDescMap.put(118,"fneg");
        opCodeDescMap.put(119,"dneg");
        opCodeDescMap.put(120,"ishl");
        opCodeDescMap.put(121,"lshl");
        opCodeDescMap.put(122,"ishr");
        opCodeDescMap.put(123,"lshr");
        opCodeDescMap.put(124,"iushr");
        opCodeDescMap.put(125,"lushr");
        opCodeDescMap.put(126,"iand");
        opCodeDescMap.put(127,"land");
        opCodeDescMap.put(128,"ior");
        opCodeDescMap.put(129,"lor");
        opCodeDescMap.put(130,"ixor");
        opCodeDescMap.put(131,"lxor");
        opCodeDescMap.put(132,"iinc");
        /** Conversions */
        opCodeDescMap.put(133,"i2l");
        opCodeDescMap.put(134,"i2f");
        opCodeDescMap.put(135,"i2d");
        opCodeDescMap.put(136,"l2i");
        opCodeDescMap.put(137,"l2f");
        opCodeDescMap.put(138,"l2d");
        opCodeDescMap.put(139,"f2i");
        opCodeDescMap.put(140,"f2l");
        opCodeDescMap.put(141,"f2d");
        opCodeDescMap.put(142,"d2i");
        opCodeDescMap.put(143,"d2l");
        opCodeDescMap.put(144,"d2f");
        opCodeDescMap.put(145,"i2b");
        opCodeDescMap.put(146,"i2c");
        opCodeDescMap.put(147,"i2s");
        /** Comparisons */
        opCodeDescMap.put(148,"lcmp");
        opCodeDescMap.put(149,"fcmpl");
        opCodeDescMap.put(150,"fcmpg");
        opCodeDescMap.put(151,"dcmpl");
        opCodeDescMap.put(152,"dcmpg");
        opCodeDescMap.put(153,"ifeq");
        opCodeDescMap.put(154,"ifne");
        opCodeDescMap.put(155,"iflt");
        opCodeDescMap.put(156,"ifge");
        opCodeDescMap.put(157,"ifgt");
        opCodeDescMap.put(158,"ifle");
        opCodeDescMap.put(159,"if_icmpeq");
        opCodeDescMap.put(160,"if_icmpne");
        opCodeDescMap.put(161,"if_icmplt");
        opCodeDescMap.put(162,"if_icmpge");
        opCodeDescMap.put(163,"if_icmpgt");
        opCodeDescMap.put(164,"if_icmple");
        opCodeDescMap.put(165,"if_acmpeq");
        opCodeDescMap.put(166,"if_acmpne");
        /** Control */
        opCodeDescMap.put(167,"goto");
        opCodeDescMap.put(168,"jsr");
        opCodeDescMap.put(169,"ret");
        opCodeDescMap.put(170,"tableswitch");
        opCodeDescMap.put(171,"lookupswitch");
        opCodeDescMap.put(172,"ireturn");
        opCodeDescMap.put(173,"lreturn");
        opCodeDescMap.put(174,"freturn");
        opCodeDescMap.put(175,"dreturn");
        opCodeDescMap.put(176,"areturn");
        opCodeDescMap.put(177,"return");
        /** References */
        opCodeDescMap.put(178,"getstatic|2");
        opCodeDescMap.put(179,"putstatic|2");
        opCodeDescMap.put(180,"getfield|2");
        opCodeDescMap.put(181,"putfield|2");
        opCodeDescMap.put(182,"invokevirtual|2");
        opCodeDescMap.put(183,"invokespecial|2");
        opCodeDescMap.put(184,"invokestatic|2");
        opCodeDescMap.put(185,"invokeinterface|2");
        opCodeDescMap.put(186,"invokedynamic|2");
        opCodeDescMap.put(187,"new|2");
        opCodeDescMap.put(188,"newarray");
        opCodeDescMap.put(189,"anewarray");
        opCodeDescMap.put(190,"arraylength");
        opCodeDescMap.put(191,"athrow");
        opCodeDescMap.put(192,"checkcast");
        opCodeDescMap.put(193,"instanceof");
        opCodeDescMap.put(194,"monitorenter");
        opCodeDescMap.put(195,"monitorexit");
        /** Extended */
        opCodeDescMap.put(196,"wide");
        opCodeDescMap.put(197,"multianewarray");
        opCodeDescMap.put(198,"ifnull");
        opCodeDescMap.put(199,"ifnonnull");
        opCodeDescMap.put(200,"goto_w");
        opCodeDescMap.put(201,"jsr_w");
        /** Reserved */
        opCodeDescMap.put(202,"breakpoint");
        opCodeDescMap.put(254,"impdep1");
        opCodeDescMap.put(255,"impdep2");
        String opDesc = opCodeDescMap.get(opCode);
        if(opDesc == null || opCodeDescMap.isEmpty()) {
            opDesc = "未知指令";
        }
        return opDesc;
    }

    //endregion
}
