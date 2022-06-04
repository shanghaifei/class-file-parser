package com.hyfetech.tools.classfileparser.utils;

import java.io.InputStream;
import java.math.BigInteger;

/**
 * 读取字节码文件通用类
 * @author shanghaifei
 * @date 2022/6/1
 */
public class ReadClassFileUtil {
    /**
     * 读取1个无符号字节
     * @param classFileInputStream 字节码文件输入流
     * @return 读取的十进制内容
     * @throws Exception
     */
    public static short readU1(InputStream classFileInputStream) throws Exception {
        return Short.parseShort(binary(readUBytes(classFileInputStream,1),10));
    }

    /**
     * 读取2个无符号字节
     * @param classFileInputStream 字节码文件输入流
     * @return 读取的十进制内容
     * @throws Exception
     */
    public static int readU2(InputStream classFileInputStream) throws Exception {
        return Integer.parseInt(binary(readUBytes(classFileInputStream,2),10));
    }

    /**
     * 读取4个无符号字节
     * @param classFileInputStream 字节码文件输入流
     * @return 读取的十进制内容
     * @throws Exception
     */
    public static long readU4(InputStream classFileInputStream) throws Exception {
        return Integer.parseInt(binary(readUBytes(classFileInputStream,4),10));
    }

    /**
     * 读取指定位数的无符号字节
     * @param classFileInputStream 字节码文件输入流
     * @param byteCount 字节数量
     * @return 读取到的字节数据
     * @throws Exception
     */
    public static byte[] readUBytes(InputStream classFileInputStream,
                                    int byteCount) throws Exception {
        byte[] bytes = new byte[byteCount];
        classFileInputStream.read(bytes);
        return bytes;
    }

    //region 进制之间的转换

    /**
     * 字节数组转换为16进制
     * @param bytes 字节数组
     * @return 16进制字符串
     */
    public static String hex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte aByte : bytes) {
            result.append(String.format("%02x", aByte));
            // upper case
            // result.append(String.format("%02X", aByte));
        }
        return result.toString();
    }

    /**
     * 将字节数组转换为各种进制的字符串
     * @param bytes 字节数组
     * @param radix 进制
     * @return 进制字符串
     */
    public static String binary(byte[] bytes, int radix) {
        // 这里的1代表正数
        return new BigInteger(1, bytes).toString(radix);
    }

    //endregion
}
