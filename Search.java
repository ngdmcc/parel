import java.security.*;
import java.io.*;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Hex;

public class Search{
	// file info
	private static StringBuffer fileInfo;
	// dir info
	private static StringBuffer dirInfo;
	// child file info
	private static File[] fm;
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
	//testInputStream
	//MD5验证程序，获取制定文件的MD5值以此验证文件的完整性
	public static String testInputStream(String filePath){
   /**
    * 使用BouncyCastleProvider，需加载对应的包。
    */
	String returnStr=null;
   Security.addProvider(new BouncyCastleProvider());
   Provider provider = Security.getProvider("BC");
   try {
    /**
     * 使用MD5进行完整性校验
     */
   MessageDigest digest = MessageDigest.getInstance("MD5",provider);
   /**
    * 获取文件流
    */
   FileInputStream in=new FileInputStream(filePath);
   DigestInputStream din =new DigestInputStream(in,digest);
   int bfSize=40000000;
   int length=in.available();
   int currentSize=0;
   boolean goon=true;
   /**
    * 根据文件流更新摘要数据源
    */
   while(goon){
    byte[] buffer =new byte[bfSize];
    int readSize=bfSize;
    if((length-currentSize)<bfSize){
     goon=false;
     readSize=length-currentSize;
     
    }else{
     goon=true;
     readSize=bfSize;
     
    }
    din.read(buffer, 0, readSize);
    if(goon==false){
     currentSize +=readSize;
    }else{
     currentSize+=bfSize;
    }
    
   }
   /**
    * 生成摘要信息
    */
   byte[] output = digest.digest();
   /**
    * 生成十六进制字符串
    */
   String outputStr =new String(Hex.encode(output));
   System.out.println("filePath:"+filePath);
   System.out.println("MD5:"+outputStr);
   returnStr = "MD5:"+outputStr;
  } catch (NoSuchAlgorithmException e) {
   // TODO Auto-generated catch block
   e.printStackTrace();
  } catch (FileNotFoundException e) {
   // TODO Auto-generated catch block
   e.printStackTrace();
  } catch (IOException e) {
   // TODO Auto-generated catch block
   e.printStackTrace();
  }
  return returnStr;
 }
 /*
 读取日志文件函数
 */
 public static String readFileByLines(String fileName) {
        File file = new File(fileName);
        BufferedReader reader = null;
		String reStr= null;
        try {
            //System.out.println("以行为单位读取文件内容，一次读一整行：");
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            int line = 1;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                // 显示行号
                //System.out.println("line " + line + ": " + tempString);
				System.out.println(tempString);
                reStr = tempString;
				line++;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
		return reStr;
    }
	/**
	 * main
	 * 
	 * @param args
	 */
	 
	public static void main(String[] args) {
		// 查找目录
		File dir = new File(".");
		// 要查找的关键字
		String key = "tarfile20";
		//最后一批打包的文件包
		String last = "lastpack";
		//检查文件完整性的路径
		//保存MD5值的字符串
		String logInfo = null;
		//保存接收端文件的MD5值
		String MD5Info = null;
		// 打印文件夹信息
		printAllInfo(dir);
		System.out.println("\nsearch key : " + key);
		System.out.println("search results : ");
		for (File file : fm) {
			//判断是不是预期的文件
			if (file.getName().indexOf(key) >= 0) {
				if (file.isFile()) {
					System.out.println("file : " + file.getName() + "   ");
					System.out.println("正在对该批次文件进行数据完整性验证");
					/*while(!testInputStream(filepath)){
							sleep(10);
					}*/
					MD5Info=testInputStream(key);
					System.out.println("从日志文件中提取源文件的MD5值，并以此作为与现有的MD5值作参照");
					logInfo=readFileByLines("log.20");
					/*System.out.println("测试返回值");
					System.out.println(MD5Info);
					System.out.println(logInfo);*/
					if(MD5Info.equals(logInfo))
					{
						System.out.println("该批次文件已经到达且完整，现在调用解包脚本进行解包");
					}
					else
					{
						System.out.println("MD5值不相等，传输错误");
					}
					callShell("sh untar.sh");
					System.out.println("解包程序执行完毕");
				} else if (file.isDirectory()) {
					System.out.println("dir : " + file.getName() + "   ");
				}
			}
			//判断是不是最后一个到达的文件
			if (file.getName().indexOf(last) >= 0){
			if (file.isFile()){
				System.out.println("file:"+file.getName()+" ");
				System.out.println("最后一批文件到达，调用解包脚本，并且退出整个程序，输出程序结束的提示！");
			}else if(file.isDirectory()){
				System.out.println("dir:"+file.getName()+"   ");
			}
			}
		}
	}
	
 }

