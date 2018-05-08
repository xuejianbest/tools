package com.lwt.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author liu
 * 将文件内容转换为伪html树格式，随机转换
 */
public class File2Html {
	static String[] tag1 = {"<p>", "<div>", "<table>", "<span>", "<div>"};
	static int[] n = {20, 25, 30, 33, 35, 25, 37};
	static int randtag = tag1.length;
	static int randlen = n.length;
	static BufferedReader reader = null;
//	标志文件是否未结束
	static boolean flag = true;
	
//	程序思路：构造一个随机的标签树，然后打印输出，每个标签节点在打印时顺便打印源文件的一行。
	public static void main(String[] args) throws Exception {
		File file = new File("d:/a.txt");
		FileInputStream is = new FileInputStream(file);
		reader = new BufferedReader(new InputStreamReader(is , "utf-8"));
		File2Html test = new File2Html();
		
		StringBuilder rtn = new StringBuilder();
		while(flag){
			ArrayList<TreeNode> children = new ArrayList<TreeNode>();
			int leaf_n = (int)(Math.random()*7) + 1;
			
			for(int i=0; i<leaf_n; i++){
				TreeNode child = test.new TreeNode(null);
				children.add(child);
			}
			
			rtn.append(test.go(children).toString() + '\n');
		}
		
		PrintWriter out = new PrintWriter("d:/t.txt");
		out.println(rtn);
		out.close();
	}
	
//	读取文件一个内容不为空的行并返回。	
	public static String read() {
		String line = null;
		try {
			line = reader.readLine();
			if(line == null){
				flag = false;
				return "";
			}
			
			while("".equals(line.trim())){
				line = reader.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return line;
	}
	
//	从子节点开始递归构造一颗标签树：传入叶子标签节点，随机构造一棵树并返回顶层祖先标签节点。
	public TreeNode go(ArrayList<TreeNode> children) throws Exception{
		int children_num = children.size();
		if(children_num == 1){
			return children.get(0);
		}
		
		Map<Integer, ArrayList<TreeNode>> map = new HashMap<Integer, ArrayList<TreeNode>>();
		for(int i=0; i<children_num; i++){
			int index = (int)(Math.random()*(children_num-1));
			
			ArrayList<TreeNode> list;
			list = map.get(index);
			if(list == null){
				list = new ArrayList<TreeNode>();
			}
			list.add(children.get(i));
			map.put(index, list);
		}
		
		if(map.size() == 1){
			TreeNode rtn = new TreeNode(map.get(map.keySet().iterator().next()));
			return rtn;
		}
		
		ArrayList<TreeNode> parents = new ArrayList<TreeNode>();
		for(int key : map.keySet()){
			ArrayList<TreeNode> value = map.get(key);
			if(key == 0){
				parents.addAll(value);
			}else{
				TreeNode parent = new TreeNode(value);
				parents.add(parent);
			}
		}
		
		return go(parents);
	}
	
//	html树节点，有一个标签组成，包含子节点，toString()方法递归输出子节点。
	public class TreeNode{
		ArrayList<TreeNode> children;
		String tag;
		
		public TreeNode(ArrayList<TreeNode> children) {
			this.children = children;
			
			int t_index = (int)(Math.random()*randtag);
			tag = tag1[t_index];
		}

		public String tabString(String str){
			StringBuilder rtn = new StringBuilder();
			rtn.append('\t');
			rtn.append(str.replace("\n", "\n\t"));
			return rtn.toString();
		}
		
		@Override
		public String toString() {
			StringBuilder rtn = new StringBuilder();
			rtn.append(tag);
			
			if(Math.random() >= 0.8){
				if(children == null || children.size() == 0){
					rtn.append("\n\t");
				}else{
					for(TreeNode child : children){
						rtn.append("\n" + tabString(child.toString()));
					}
					rtn.append("\n\t");
				}
				rtn.append(read() + "\n");
			}else{
				rtn.append("\n\t" + read());
				if(children == null || children.size() == 0){
					rtn.append("\n");
				}else{
					rtn.append("\n");
					for(TreeNode child : children){
						rtn.append(tabString(child.toString()) + "\n");
					}
				}
			}
			
			rtn.append(tag.replace("<", "</"));
			return rtn.toString();
		}
	}
	
	
	
}