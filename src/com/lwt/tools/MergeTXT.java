package com.lwt.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import com.lwt.util.DirUtil;

/**
 * @author liu
 * 将多个txt文件合并为一个，原来的每个文件作为新文件的一章，原文件名为章节名称。
 */
public class MergeTXT {

	public static void main(String[] args) throws IOException {
		DirUtil dir = new DirUtil(new File("E:\\aaa"));
		File[] files = dir.getFiles();
		
		PrintWriter out = new PrintWriter(new File("d:/bbb.txt"));
		int i = 1;
		for(File file : files){
			if(i > 30){
//				break;
			}
			String name = file.getName();
			if(!name.endsWith(".txt")){
				continue;
			}else{
				name = name.substring(0, name.length() - 4);
			}
			out.println("第" + i + "章 " + name);
			i++;
			FileInputStream is = new FileInputStream( file );
			BufferedReader reader = new BufferedReader(  new InputStreamReader(is , "GBK")  );
			
			String line = "";
			while((line = reader.readLine()) != null){
				String newLine = doLine(line);
				if(newLine != null){
					out.println(newLine);
				}
			}
			out.println();
			out.println();
			reader.close();
		}
		out.close();
	}
	
	
	public static String doLine(String line){//简单处理一下原文件每一行
		String res = null;
		line = line.trim();
		line = line.replace('', ' ');
		if(!"".equals(line)){
			res = line;
		}
		return res;
	}
	
	

}
