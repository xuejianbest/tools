package com.lwt.util;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author liu
 * 此类用来（递归）获取一个目录下面的所有文件或目录。使用前先new一个本类
 * 的实例：构造函数接收一个表示目录的File实例。
 */
public class DirUtil {
	private final File dir; 
	
	public static FileFilter dirFilter = new Dir_Filter();
	public static FileFilter fileFilter = new File_Filter();
	
	private static class Dir_Filter implements FileFilter{
		@Override
		public boolean accept(File pathname) {
			if(pathname.isDirectory()){
				return true;
			}
			return false;
		}
	}
	private static class File_Filter implements FileFilter{
		@Override
		public boolean accept(File pathname) {
			if(pathname.isFile()){
				return true;
			}
			return false;
		}
	}
	
	
	/**
	 * 构造函数
	 * @param dir 想要获取其下文件（或目录）的目录
	 * 
	 */
	public DirUtil(File dir){
		if(dir == null || !dir.isDirectory()){
			throw new IllegalArgumentException("The parameter must be a existed directory.\n" + dir.getAbsolutePath());
		}
		if(!dir.canRead()){
			throw new IllegalArgumentException("Permission denied: The directory can't be read.\n" + dir.getAbsolutePath());
		}
		this.dir = dir;
	}
	
	/**
	 * @return 当前目录（不包括子目录）下的文件
	 */
	public File[] getFiles(){
		File[] files = dir.listFiles(fileFilter);
		if(files == null){
			System.err.println("new File(" + dir + ").listFiles(fileFilter) return null.");
			files = new File[0];
		}
		return files;
	}
	
	/**
	 * @return 当前目录（不包括子目录）下的目录
	 */
	public File[] getDirs(){
		File[] files = dir.listFiles(dirFilter);
		if(files == null){
			System.err.println("new File(" + dir + ").listFiles(fileFilter) return null.");
			files = new File[0];
		}
		return files;
	}
	
	/**
	 * @return 当前目录和其子目录下所有文件
	 */
	public File[] getAllFiles(){
		ArrayList<File> filesArr = new ArrayList<File>();
		File[] files = getFiles();
		filesArr.addAll(Arrays.asList(files));
		
		File[] dirs = getDirs();
		for(File dir : dirs){
			try{
				DirUtil dirUtil = new DirUtil(dir);
				filesArr.addAll(Arrays.asList(dirUtil.getAllFiles()));
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
		return filesArr.toArray(new File[0]);
	}
	
	/**
	 * @return 当前目录和其子目录下所有目录
	 */
	public File[] getAllDirs(){
		ArrayList<File> filesArr = new ArrayList<File>();
		File[] dirs = getDirs();
		filesArr.addAll(Arrays.asList(dirs));
		
		for(File dir : dirs){
			try{
				DirUtil dirUtil = new DirUtil(dir);
				filesArr.addAll(Arrays.asList(dirUtil.getAllDirs()));
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
		return filesArr.toArray(new File[0]);
	}
	
}