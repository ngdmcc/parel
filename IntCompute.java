import java.io.*
public class IntCompute
{
    public static void main(String[] s) throws FileNotFoundException
    {
        CheckFile check = new CheckFile("E:\\web.xml");
        check.start();
    }
}
 
class CheckFile extends Thread {
    private File file = null;
    private long lastModifieTime = 0L;
    public CheckFile(String fileName) throws FileNotFoundException {
        file = new File(fileName);
        if (!file.exists()) {
            throw new FileNotFoundException();
        }
        lastModifieTime = file.lastModified();
    }
     
    @Override
    public void run() {
        long newTime = 0L;
        // 开始检查是否有修改
        while(true) {
            newTime = file.lastModified();
            // 如果有修改，就打印信息
            if (newTime != lastModifieTime) {
                lastModifieTime = newTime;
                System.out.println("File has been modified.");
            }
             
            // 线程暂停一秒
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}