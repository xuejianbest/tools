package com.lwt.util;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * 随机抽样工具类
 * @author liu
 *
 */
public class Sampling {
	/**
	 * 蓄水池抽样
	 * @param reader 输入流
	 * @param k 随机抽样行数
	 * @return 随机抽样数据数组
	 */
	public static String[] reservoir_sampling(BufferedReader reader, int k) throws IOException {
		String[] R = new String[k];
		long index = 0;
		while(index < k && (R[(int)index++] = reader.readLine()) != null);
		
		String read = "";
		while((read = reader.readLine()) != null){
			int j = (int)(Math.random() * ++index);
			if(j < k){
				R[j] = read;
			}
		}
		
		return R;
	}
	
}
