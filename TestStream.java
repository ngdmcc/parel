import java.security.*;
import java.io.*;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Hex;

public class TestStream{
public static void testInputStream(String filePath){
   /**
    * 使用BouncyCastleProvider，需加载对应的包。
    */
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
 }

public static void main(String[] args){
  
  String filePath=args[0];
  testInputStream(filePath);
  
 }
}
