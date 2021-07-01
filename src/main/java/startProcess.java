import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.util.*;

public class startProcess {


    private static Integer mysqlPort = 45019;
    private static Integer tomcatPort = 45018;

    public static void main(String[] args) {

        boolean isMysqlOpen = false;
        boolean isTomcatOpen = false;
        System.out.println("检测mysql端口");
        isMysqlOpen = NetUtil.isPortAvailable(mysqlPort);
        if(isMysqlOpen == false){
            //启动mysql
        }
        Process process = null;


//        boolean flag=false;
//        flag=isPortAvailable(45019);
//        if(flag){
//            System.out.println("未被占用");
//        }else{
//            System.out.println("已占有");
//        }
    }


    private static String getSoftInstallPath()
    {
        String path = "";
        try
        {
            System.out.println("通过注册表获取程序安装路径");
            String softName = "{7FEAEC80-8C8A-4E72-926A-4E28695DDC01}_is1";
            String strKeyName = "InstallLocation";
            // int sysOperate = getSysOperate();

            Properties props =System.getProperties();
            String cpuInfo = props.getProperty("sun.cpu.isalist"); //操作系统版本

            String regeditPath = "SOFTWARE\\Wow6432Node\\Microsoft\\Windows\\CurrentVersion\\Uninstall\\"; //64位系统
            if (!cpuInfo.contains("64"))
            {
                regeditPath = "SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\Uninstall\\";
            }
            Map<String, String> map = registry.valuesForPath(registry.HKEY_LOCAL_MACHINE, regeditPath+softName);
            String installPath = map.get("InstallLocation") == null?"":map.get("InstallLocation").toString();
            path = installPath;
            System.out.println("安装路径：" + installPath);
        }
        catch (Exception ex)
        {
            //Log.Error("获取注册表中的程序路径失败", ex);
            System.out.println("获取安装路径失败："+ex.getMessage());
        }
        return path;
    }


}
