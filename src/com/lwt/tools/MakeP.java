package com.lwt.tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

/**
 * 将文件进行逻辑分段，在文件的原始分段基础上，合并一些不完整的分段，也就说经过本程序文件的分段只会减少不会增多。
 * @author liu
 */
public class MakeP {
	public static void main(String[] args) throws Exception{
		
		//文件读写：
		BufferedReader reader = new BufferedReader(new FileReader(new File("D:\\Linux\\布谷鸟的呼唤.txt")));
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File("file.out")));

		String line = "";
		String endLine = "";
		while((line = reader.readLine()) != null){
			endLine = "";
			if(isEndOfP(line) && isStrD(line)){
				endLine = "\n";
			}
			writer.write(line + endLine);
		}
		
		reader.close();
		writer.close();
	}
	
	//每一段的结尾必须是这些符号
	public static boolean isEndOfP(String line){
		String[] strArr = {"\"",".","”","。","！","？","!","?","……","…","》","：",":",";","；",
						   "1","2","3","4","5","6","7","8","9","0",
						   "章","部","录","著","译","言"};
		for(String str : strArr){
			if(line.endsWith(str))return true;
		}
		return false;
	}
	
	//每一段中这些符号必须成对出现
	public static boolean isStrD(String line){
		String[] strDa = {"\"","(","{","[","《","“","‘","（","{","【"};
		String[] strDb = {"\"",")","}","]","》","”","’","）","}","】"};
		for(int i=0; i<strDa.length; i++){
			if(countSubString(line, strDa[i]) != countSubString(line, strDb[i]))
				return false;
		}
		return true;
	}
	
	//统计line中substr子串出现的次数
	public static int countSubString(String line, String substr){
		if(line == null || line == "") return 0;
		int index = 0;
		int count = 0;
		while(index < line.length()){
			index = line.indexOf(substr, index) + 1;
			if(index == 0)break;
			count++;
		}
		return count;
	}
}
