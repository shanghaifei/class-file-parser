package com.hyfetech.tools.classfileparser.parser.baseinfo;

import com.hyfetech.tools.classfileparser.utils.ReadClassFileUtil;

import java.io.InputStream;
import java.util.HashMap;

/**
 * 字节码文件的版本解析器
 * @author shanghaifei
 * @date 2022/6/2
 */
public class ClassFileVersionParser implements IClassFileInfoParser {
    /**
     * 解析
     * @param classFileInputStream 字节码文件输入流
     */
    @Override
    public String parse(InputStream classFileInputStream) throws Exception {
        /** minor_version：次版本号（2字节） */
        int minorVersion = ReadClassFileUtil.readU2(classFileInputStream);
        /** major_version：主版本号（2字节） */
        int majorVersion = ReadClassFileUtil.readU2(classFileInputStream);
        System.out.println(String.format("版本号（主.次）：%s.%s（%s）",majorVersion,minorVersion,
                getJDKVersion(majorVersion,minorVersion)));
        return null;
    }

    /**
     * 获取JDK版本
     * @param majorVersion 主版本号
     * @param minorVersion 次版本号
     * @return
     */
    public String getJDKVersion(int majorVersion,int minorVersion) {
        HashMap<String,String> versionMap = new HashMap<>();
        versionMap.put("52.0","JDK 1.8");
        versionMap.put("51.0","JDK 1.7");
        versionMap.put("50.0","JDK 1.6");
        versionMap.put("49.0","JDK 1.5");
        versionMap.put("48.0","JDK 1.4");
        versionMap.put("47.0","JDK 1.3");
        versionMap.put("46.0","JDK 1.2");
        versionMap.put("45.3","JDK 1.1");
        versionMap.put("44.3","JDK 1.0");
        return versionMap.get(majorVersion + "." + minorVersion);
    }
}
