import java.io.File;
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
		String key = "parel.c";
		// 打印文件夹信息
		printAllInfo(dir);
		System.out.println("/nsearch key : " + key);
		System.out.println("search results : ");
		for (File file : fm) {
			if (file.getName().indexOf(key) >= 0) {
				if (file.isFile()) {
					System.out.println("file : " + file.getName() + "   ");
				} else if (file.isDirectory()) {
					System.out.println("dir : " + file.getName() + "   ");
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
}
