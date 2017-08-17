package com.lwt.tools;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeMap;

import com.lwt.util.DataUtil;
import com.lwt.util.DirUtil;

/**
 * 用来处理文件的工具类
 *
 */
public class FileUtil {
	public static void main(String[] args) throws Exception{
		long start = System.currentTimeMillis(); //获取当前时间戳（ms）
		
		File src = new File("a.txt");
		File desc1 = new File("b.doc");
		isSameFile(src, desc1);
		
		long end = System.currentTimeMillis();   //获取当前时间戳（ms）
		System.out.println("程序执行时间：" + (end - start) + "ms");
	}

	

	/**
	 * 比较两个文件是否相同（逐字节比较）
	 * @param 文件1
	 * @param 文件3
	 * @return true相同，false不同
	 * @throws IOException 
	 */
	public static boolean isSameFile(File f1 , File f2) throws IOException {
		long len1 = f1.length();
		long len2 = f2.length();
		if(len1 != len2){
			return false;
		}
		
		BufferedInputStream is1 = new BufferedInputStream(new FileInputStream(f1));
		BufferedInputStream is2 = new BufferedInputStream(new FileInputStream(f2));
		byte[] buff1 = new byte[1024*1024*10];
		byte[] buff2 = new byte[1024*1024*10];
		
		boolean res = true;
		int n = 0;
		while(n != -1 && res){
			n = is1.read(buff1);
			is2.read(buff2);
			
			for(int i=0; i<n; i++){
				if(buff1[i] != buff2[i]){
					res = false;
					break;
				}
			}
		}
		
		is1.close();
		is2.close();
		return res;
	}

	/**
	 * 将文件夹内相同的文件重命名，如1.jpg和2.jpg文件相同，会将1.jpg重命名为：【2.jpg】1.jpj
	 * @param dir 要分析的文件夹
	 * @param isAll true：递归分析文件夹及子文件夹内所有文件； flase：只分析当前文件夹下的文件
	 * @throws IOException
	 */
	public static void flagSameFile(File dir, boolean isAll) throws IOException{
		System.out.println("==========flagSameFile开始执行==========");
		DirUtil dirUtil = new DirUtil(dir);
		File[] fs = null;
		if(isAll){
			fs = dirUtil.getAllFiles();
		}else{
			fs = dirUtil.getFiles();
		}
		System.out.println("共获取文件个数：" + fs.length);
		for(int i=0; i<fs.length; i++){
			for(int j=i+1; j<fs.length; j++){
				if(isSameFile(fs[i], fs[j])){
					String name = fs[i].getName();
					
					String sameFileName = fs[j].getName();
					if(!fs[i].getParent().equals(fs[j].getParent())){
						sameFileName = fs[j].getParent().replace(File.separatorChar, '_').replace(':', '=') + '_' + sameFileName;
					}
					name = "【" + sameFileName + "】" + name;
					String path = fs[i].getParent();
					if(!path.endsWith(File.separatorChar + "")){
						path += File.separatorChar;
					}
					File temp = new File(path + name);
					if(!fs[i].renameTo(temp)){
						System.out.println("重命名失败：" + fs[i].getAbsolutePath());
					}
					break;
				}
			}
		}
		System.out.println("==========flagSameFile执行完成==========");
	}

	/**
	 * 将不含文件的目录重命名加上【empty】标记。
	 * 注：若一个目录下只含空目录，当作空目录处理。
	 * 注意：为防止误删，removeFlag永远为空，只标记不删除，若要删除将方法内代码第一句注释掉
	 * @param dir 要分析的目录（会递归的分析其子目录）
	 * @param removeFlag 是否将空文件夹删除
	 */
	public static void flagEmptyDir(File dir, boolean removeFlag){
		System.out.println("==========flagEmptyDir开始执行==========");
		removeFlag = false; //目前只标记空目录不删除，若删除注释掉这一句
		DirUtil dirUtil = new DirUtil(dir);
		File[] dirs = dirUtil.getAllDirs();
		
		System.out.println("总获取到的目录个数：" + (dirs.length+1));
		
		int empty_dir_num = 0; //空子目录个数
		for(File d : dirs){
			DirUtil du = new DirUtil(d);
			if(du.getAllFiles().length == 0){
				empty_dir_num++;
			}
		}
		
		if(removeFlag){ //删除空目录
			while(empty_dir_num != 0){
				for(File d : dirs){
					DirUtil du = new DirUtil(d);
					if(du.getAllFiles().length == 0 && du.getAllDirs().length == 0){
						if(!d.delete()){
							System.out.println("删除失败：" + d.getAbsolutePath());
							empty_dir_num--;
						}else{
							empty_dir_num--;
						}
					}
				}
				dirs = dirUtil.getAllDirs();
			}
			if(dirUtil.getAllFiles().length == 0 && dirUtil.getAllDirs().length == 0){
				if(!dir.delete()){
					System.out.println("删除失败：" + dir.getAbsolutePath());
				}
			}
		}else{ //重命名空目录
			TreeMap<Integer, ArrayList<File>> emptyDir = new TreeMap<Integer, ArrayList<File>>();
			for(File d : dirs){
				DirUtil du = new DirUtil(d);
				if(du.getAllFiles().length == 0){
					ArrayList<File> dirList = new ArrayList<File>();
					int len = d.getAbsolutePath().length();
					if(emptyDir.containsKey(len)){
						dirList = emptyDir.get(len);
					}
					dirList.add(d);
					emptyDir.put(len, dirList);
				}
			}
			if(dirUtil.getAllFiles().length == 0){
				empty_dir_num++;
				ArrayList<File> FileList = new ArrayList<File>();
				int len = dir.getAbsolutePath().length();
				if(emptyDir.containsKey(len)){
					FileList = emptyDir.get(len);
				}
				FileList.add(dir);
				emptyDir.put(len, FileList);
			}
			
			System.out.println("空目录个数：" + empty_dir_num);
			
			int doRenameNum = 0;
			while(!emptyDir.isEmpty()){
				ArrayList<File> dirList = emptyDir.pollLastEntry().getValue();
				for(File d : dirList){
					String path = d.getParent();
					if(!path.endsWith(File.separatorChar + "")){
						path += File.separatorChar;
					}
					File temp = new File(path + "【empty】" + d.getName());
					if(!d.renameTo(temp)){
						System.out.println("重命名失败：" + d.getAbsolutePath());
					}else{
						doRenameNum++;
					}
				}
			}
			System.out.println("已处理空目录个数：" + doRenameNum);
			System.out.println("==========flagEmptyDir执行完成==========");
		}
	}
	
	/**
	 * 重命名已经存在的文件（用来找出那些已经存在目标文件夹下的文件，防止重复拷贝用）。如：
	 * 待比对目录下的文件c:\src_dir\1.file和比对目录下的d:\desc_dir2\2.file文件是一样的（文件字节相同），
	 * 则会把1.file重命名为【d=_desc_dir2_2.file】1.file
	 * @param src_dir 待比对目录，也是可能被重命名的文件的父目录
	 * @param desc_dir 比对目录，和位于这些目录中的文件进行比对，若比对结果一样则重命名src_dir中的文件
	 * @throws IOException
	 */
	public static void renameExistedFile(File src_dir, File... desc_dir) throws IOException{
		System.out.println("==========renameExistedFile开始执行==========");
		DirUtil src_du = new DirUtil(src_dir);
		File[] src_files = src_du.getAllFiles();
		System.out.println("待比对文件总数量：" + src_files.length);
		
		ArrayList<File> desc_files = new ArrayList<File>();
		for(File desc_d : desc_dir){
			if(desc_d.equals(src_dir)){
				throw new IllegalArgumentException("比对目录中不能有待比对目录");
			}
			DirUtil desc_du = new DirUtil(desc_d);
			for(File desc_sub_d : desc_du.getAllDirs()){
				if(desc_sub_d.equals(src_dir)){
					throw new IllegalArgumentException("比对目录不能是待比对目录的父目录");
				}
			}
			desc_files.addAll(Arrays.asList(desc_du.getAllFiles()));
		}
		
		System.out.println("比对文件总数量：" + desc_files.size());
		
		for(File src_file : src_files){
			for(File desc_file : desc_files){
				if(isSameFile(src_file, desc_file)){
					String name = src_file.getName();
					String sameFileName = desc_file.getAbsolutePath();
					sameFileName = sameFileName.replace(File.separatorChar, '_').replace(':', '=');
					name = "【" + sameFileName + "】" + name;
					
					String path = src_file.getParent();
					if(!path.endsWith(File.separatorChar + "")){
						path += File.separatorChar;
					}
					File temp = new File(path + name);
					if(!src_file.renameTo(temp)){
						System.out.println("重命名失败：" + src_file.getAbsolutePath());
					}
					break;
				}
			}
		}
		System.out.println("==========renameExistedFile执行完成==========");
	}
	
	/**
	 * 将文件重命名为其大小字节数，如一个abc.jpg文件大小为1234字节，将其重命名为123,4.jpg
	 * @param src_file 要重命名的文件，重命名前后文件路径不变
	 */
	public static void renameFileAsLen(File src_file){
		if(!src_file.isFile()){
			throw new IllegalArgumentException("不是有效文件：" + src_file.getAbsolutePath());
		}
		String name = src_file.getName();
		String[] nameArr = name.split("\\.");
		String extName = "";
		if(nameArr.length > 1){
			extName = "." + nameArr[nameArr.length-1];
		}
		String len = DataUtil.add_comma(src_file.length());
		len =  src_file.length() + ""; //若希望用逗号分隔文件名数字，注视此句
		String path = src_file.getParent();
		if(!path.endsWith(File.separatorChar + "")){
			path += File.separatorChar;
		}
		File temp = new File(path + len + extName);
		int i = 1;
		while(temp.isFile()){
			temp = new File(path + len + "_" + i + extName);
			i++;
		}
		if(!src_file.renameTo(temp)){
			System.out.println("重命名失败：" + src_file.getAbsolutePath());
		}
	}
	
	/**
	 * 将一个文件夹内的所有文件重命名为其大小字节数，如其中一个abc.jpg文件大小为1234字节，将其重命名为123,4.jpg
	 * @param dir 文件夹
	 * @param isAll 是否递归重命名子文件夹内文件
	 */
	public static void renameFileAsLen(File dir, boolean isAll){
		System.out.println("==========renameFileAsLen开始执行==========");
		DirUtil du = new DirUtil(dir);
		File[] files = null;
		if(isAll){
			files = du.getAllFiles();
		}else{
			files = du.getFiles();
		}
		System.out.println("待重命名文件总数量：" + files.length);
		for(File file : files){
			renameFileAsLen(file);
		}
		System.out.println("==========renameFileAsLen执行完成==========");
	}
	
}
