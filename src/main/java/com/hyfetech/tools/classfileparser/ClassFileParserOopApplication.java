package com.hyfetech.tools.classfileparser;

import com.hyfetech.tools.classfileparser.parser.ClassFileParser;

import java.io.File;
import java.io.FileInputStream;
import java.util.Scanner;

/**
 * 字节码文件解析器应用程序
 * @author shanghaifei
 * @date 2022/5/30
 */
public class ClassFileParserOopApplication {
    public static void main(String[] args) throws Exception {
        // 默认的字节码文件路径
        String defaultClassFilePath = "/Users/shanghaifei/Documents/source-code/openjdk/openjdk-jdk8u/jdk/src/share/classes/study/src/Main.class";
        System.out.println("请输入字节码文件路径：");
        Scanner scanner = new Scanner(System.in);
        String classFilePath = scanner.nextLine();
        if(classFilePath == null || classFilePath.isEmpty()) {
            classFilePath = defaultClassFilePath;
        }
        // 判断字节码文件路径是否为空
        if (classFilePath == null || classFilePath.isEmpty()) {
            System.out.println("字节码文件路径未输入！");
            return;
        }
        // 判断字节码文件是否存在
        File classFile = new File(classFilePath);
        if (!classFile.exists()) {
            System.out.println("字节码文件不存在！");
            return;
        }
        // 字节码输入流
        FileInputStream classFileInputStream = new FileInputStream(classFile);
        // 创建字节码文件解析器并解析
        ClassFileParser classFileParser = new ClassFileParser();
        classFileParser.parse(classFileInputStream);
    }
}
