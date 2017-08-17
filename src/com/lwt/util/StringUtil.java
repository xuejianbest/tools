package com.lwt.util;

/** 
 * 字符串相关的工具类，所有方法都为静态。
 */
public class StringUtil {
   /** 
    * 判断一个字符串是否为空或null 
    * @param str 字符串
    * @return 为空字符串或null返回true 
    */
	public static boolean isEmpty(final String str){
		if(str == null || "".equals(str)){
			return true;
		}
		return false;
	}
	
   /** 
    * 返回一个字符串去除所有空字符后的字符串，将所有匹配\s+的子串替换为空。
    * @param str 字符串
    * @return 替换后的字符串，不影响传入字符串
    */
	public static String delBlank(final String str){
		if(str == null) return null;
		String pattern = "\\s+";
		return str.replaceAll(pattern, "");
	}
	
	/** 
	 * 将一个String中所有的全角数字替换为半角的。
	 * @param str 字符串
	 * @return 替换后的字符串，不影响传入字符串
	 */
	public static String changeNum(final String str){
		char[] sc = {'０', '１', '２', '３', '４', '５', '６', '７', '８', '９'};
		char[] sd = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
		String rtn = str;
		for(int i=0; i<sc.length; i++){
			rtn = rtn.replace(sc[i], sd[i]);
		}
		return rtn;
	}
	
//	/** 
//	 * 将一个String中所有的全角空格和字母替换为半角的。
//	 * @param str 字符串
//	 * @return 替换后的字符串，不影响传入字符串
//	 */
//	public static String changeChar(final String str){
//		char[] sc = {'　', '', '２', '３', '４', '５', '６', '７', '８', '９'};
//		char[] sd = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
//		String rtn = str;
//		for(int i=0; i<sc.length; i++){
//			rtn = rtn.replace(sc[i], sd[i]);
//		}
//		return rtn;
//	}
	
}
