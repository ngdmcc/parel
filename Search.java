import java.io.File;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
public class Search{
	// file info
	private static StringBuffer fileInfo;
	// dir info
	private static StringBuffer dirInfo;
	// child file info
	private static File[] fm;
	/**
	 * main
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		// 查找目录
		File dir = new File(".");
		// 要查找的关键字
		String key = "tarfile";
		//最后一批打包的文件包
		String last = "lastpack";
		// 打印文件夹信息
		printAllInfo(dir);
		System.out.println("\nsearch key : " + key);
		System.out.println("search results : ");
		for (File file : fm) {
			if (file.getName().indexOf(key) >= 0) {
				if (file.isFile()) {
					System.out.println("file : " + file.getName() + "   ");
					System.out.println("该批次文件已经到达，调用解包脚本");
					callShell("sh untar.sh");
				} else if (file.isDirectory()) {
					System.out.println("dir : " + file.getName() + "   ");
				}
			}
			if (file.getName().indexOf(last) >= 0){
			if (file.isFile()){
				System.out.println("file:"+file.getName()+" ");
				System.out.println("最后一批文件到达，调用解包脚本，并且退出整个程序，输出程序结束的提示！");
				callShell("sh untar.sh");
			}else if(file.isDirectory()){
				System.out.println("dir:"+file.getName()+"   ");
			}
			}
		}
	}
	/**
	 * print info of this directory
	 * 
	 * @param dir
	 */
	public static void printAllInfo(File dir) {
		fileInfo = new StringBuffer();
		dirInfo = new StringBuffer();
		fm = dir.listFiles();
		for (File file : fm) {
			if (file.isFile()) {
				fileInfo.append(file.getName() + "    ");
			} else if (file.isDirectory()) {
				dirInfo.append(file.getName() + "    ");
			}
		}
		System.out.println(dir.getAbsolutePath());
		System.out.println("contains : ");
		System.out.println("file ---> " + fileInfo);
		System.out.println("dir  ---> " + dirInfo);
	}
	//调用shell脚本的函数，成功返回0，失败则会出现报错信息
		public static void callShell(String shellString) {
		try {
			Process process = Runtime.getRuntime().exec(shellString);
			int exitValue = process.waitFor();
			if (0 != exitValue) {
				System.out.println("call shell failed. error code is :" + exitValue);
			}
		} catch (Throwable e) {
			System.out.println("call shell failed. " + e);
		}
	}
}
