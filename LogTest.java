import java.io.*;
import java.security.*;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Hex;

public class LogTest{
	public static void readFileByLines(String fileName,String[] LogInfo) {
        File file = new File(fileName);
        BufferedReader reader = null;
		String reStr= null;
		String[] arrayLog=LogInfo;
        try {
            //System.out.println("以行为单位读取文件内容，一次读一整行：");
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            int line = 1;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                // 显示行号
                //System.out.println("line " + line + ": " + tempString);
				//System.out.println(tempString);
                //reStr = tempString;
				arrayLog[line-1]=tempString;
				//System.out.println(arrayLog[line-1]);
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
		//return reStr;
    }
	//得到文件名并处理的函数
	
	//获取文件MD5值的函数
	public static String testInputStream(String filePath){
   /**
    * 使用BouncyCastleProvider，需加载对应的包。
    */
	
   String returnStr= null;
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
   returnStr =new String("MD5:"+outputStr);
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
 //调用shell脚本的函数
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
	public static void main(String[] args)
	{
		String[] array=new String[20];//1、先判断log文件是不是最新的，如果是则进行日志文件提取工作，
		String MD5 = null;
		//String  logfile = "log."+"20";    //包括文件名和MD5值，并将其存放在字符串中。
		String logfile=args[0];
		readFileByLines(logfile,array);//2、按照1中取得文件名，对当前文件夹中的文件进行MD5值的提取
		//System.out.println(array[0]);//3、将2中与1中取得的值进行比较，相等则调用解包脚本进行解包，否则报错退出
		for(int i=0; i < (array.length-1);i++)
		{
			MD5=testInputStream(array[i]);
			//testInputStream(array[i],MD5);
			//System.out.println("输出MD5的值并查看");
			
			//System.out.println("MD5"+MD5);
			//System.out.println("line137");
			//System.out.println(array[i]);
			//if(MD5.equals(array[i+1])==true)
			//if(MD5.equals("MD5:d41d8cd98f00b204e9800998ecf8427e")==true)
			if((MD5.equals(array[i+1]))==true)
			{
				System.out.println("MD5验证成功");
				callShell("sh untar.sh");
				System.out.println("该文件解包成功");
			}
			i++;
			System.out.println("输出I的值");
			System.out.println("i="+i);
		}
		System.out.println("验证结束");
	}                                //本次循环进入对下一个文件的验证和解包工作，直到所有的文件都被验证和操作。
}									//4、										