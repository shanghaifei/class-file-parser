package com.hyfetech.tools.classfileparser.parser.attribute;

import com.hyfetech.tools.classfileparser.dto.AttributeInfo;
import com.hyfetech.tools.classfileparser.utils.ReadClassFileUtil;
import lombok.Data;

/**
 * 抽象的属性信息解析器
 * @author shanghaifei
 * @date 2022/6/1
 */
@Data
public abstract class AbstractAttributeInfoParser implements IAttributeInfoParser {
    /**
     * 常量池解析信息数组
     */
    protected String[] constantPoolParseInfos;

    /**
     * 获取打印前缀（根据前缀进行不同长度的缩进）
     * @param level 层级
     * @return
     */
    public String getPrintPrefix(int level) {
        String prefix = "";
        for(int i = 1;i < level;i++) {
            prefix += "....";
        }
        return prefix;
    }

    /**
     * 获取属性基本信息
     * @param attributeInfo
     * @return
     */
    public String getAttributeBaseInfo(AttributeInfo attributeInfo) {
        String printPrefix = getPrintPrefix(attributeInfo.getLevel());
        String attributeBaseInfo = String.format("%s【属性%s】属性名：%s",
                printPrefix,attributeInfo.getAttributeIndex(),attributeInfo.getAttributeName());
        return attributeBaseInfo;
    }
}
