package com.lwt.util;

import java.text.DecimalFormat;
import java.util.Arrays;


/**
 * @author liu
 * 处理数据的工具类
 */
public class DataUtil {
	
	/* 将一个int转化为长度为4的byte数组，小端字节序方式。
	 * 
	 * */
	public static byte[] int2ByteArr_le(int i){
		byte[] b = new byte[4];
		for (int j = 0; j < 4; j++) {
			b[j] = (byte)i;
			i = i >>> 8;
		}
		return b;
	}
	
	/* 将一个长度为4的byte数组转化为一个int，小端字节序方式。
	 * 
	 * */
	public static int byteArr2Int_le(byte[] b){
		int i = 0;
		for (int j = 0; j < 4; j++) {
			i = i << 8;
			i += (b[3-j] & 0xff);
		}
		return i;
	}
	
	/* 获取start到end的随机数，[start, end)
	 * 
	 * */
	public static int random_int(int start, int end){
		if(!(start < end)){
			throw new ArithmeticException("指定上界必须大于指定下界。");
		}
		return (int)(Math.random()*(end - start)) + start;
	}
	
	/* 将多个数组合并为1个，返回一个新创建的数组
	 * 
	 * */
	public static <T> T[] combinationArray(T[] afirst, T[] ... aaRests){
		int iLenOfRests = 0;
		for(T[] aOneOfRests : aaRests){
			iLenOfRests += aOneOfRests.length;
		}
		
		T[] aResult = Arrays.copyOf(afirst, iLenOfRests + afirst.length);
		int iLenOfPreArr = afirst.length;
		for(T[] aOneOfRests : aaRests){
			System.arraycopy(aOneOfRests, 0, aResult, iLenOfPreArr, aOneOfRests.length);
			iLenOfPreArr += aOneOfRests.length;
		}
		return aResult;
	}
	/* 将多个数组合并为1个，返回一个新创建的数组
	 * 
	 * */
	public static byte[] combinationArray(byte[] afirst, byte[]... aaRests){
		int iLenOfRests = 0;
		for(byte[] aOneOfRests : aaRests){
			iLenOfRests += aOneOfRests.length;
		}
		
		byte[] aResult = Arrays.copyOf(afirst, iLenOfRests + afirst.length);
		int iLenOfPreArr = afirst.length;
		for(byte[] aOneOfRests : aaRests){
			System.arraycopy(aOneOfRests, 0, aResult, iLenOfPreArr, aOneOfRests.length);
			iLenOfPreArr += aOneOfRests.length;
		}
		return aResult;
	}
	
	/**
	 * 将一个数字三位加一个逗号，输出字符串，如：1234567会输出1,234,567
	 * @param num 要添加逗号的数字，long型
	 * @return
	 */
	public static String add_comma(long num){
		DecimalFormat df = new DecimalFormat("#,###");
		String res = df.format(num);
		return res;
	}
}