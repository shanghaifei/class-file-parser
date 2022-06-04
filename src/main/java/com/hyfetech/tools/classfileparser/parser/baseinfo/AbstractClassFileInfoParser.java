package com.hyfetech.tools.classfileparser.parser.baseinfo;

import lombok.Data;

/**
 * 抽象的字节码信息解析器
 * @author shanghaifei
 * @date 2022/6/2
 */
@Data
public abstract class AbstractClassFileInfoParser {
    /**
     * 常量池解析信息数组
     */
    protected String[] constantPoolParseInfos;
}
