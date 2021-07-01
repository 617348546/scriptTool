import com.sun.xml.internal.ws.util.StringUtils;

import javax.annotation.Resources;
import java.io.*;
import java.util.*;


public class scriptMain {


    private static kingbaseHelper kingbaseHelper = null;

    public static void main(String[] args) {

        kingbaseHelper = new kingbaseHelper();
        List<Map<String, Object>> lstVers = kingbaseHelper.getDbVerInfo();
        int size = lstVers.size();
        if (lstVers.size() > 0) {
            Map<String, Object> mapVerInfo = lstVers.get(0);
            String verNUM = mapVerInfo.get("PUB_VERNO") == null ? "" : mapVerInfo.get("PUB_VERNO").toString();
            if (isNotEmpty(verNUM)) {
                String installPath = "";
                if(isWindows()){
                    installPath = getSoftInstallPath();
                }
                else {
                    installPath = getLtxInstallPath();
                }
                doScript(installPath, verNUM);
            } else {
                System.out.println("db_his没有初始化发布版本信息！");
            }
        }
    }


    public static boolean isWindows() {
        return System.getProperties().getProperty("os.name").toUpperCase().indexOf("WINDOWS") != -1;
    }


    /**
     * java在linux环境下执行linux命令，然后返回命令返回值。
     *
     * @author lee
     */
    public static Object exec(String cmd) {
        try {
            String[] cmdA = {"/bin/sh", "-c", cmd};
            Process process = Runtime.getRuntime().exec(cmdA);
            LineNumberReader br = new LineNumberReader(new InputStreamReader(
                    process.getInputStream()));
            StringBuffer sb = new StringBuffer();
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
                sb.append(line).append("\n");
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    private static String getLtxInstallPath() {
        return exec("which 全国离退休信息管理系统").toString();
    }

    //which java

    private static String getSoftInstallPath() {
        String path = "";
        try {
            System.out.println("通过注册表获取程序安装路径");
            String softName = "{7FEAEC80-8C8A-4E72-926A-4E28695DDC01}_is1";
            String strKeyName = "InstallLocation";
            // int sysOperate = getSysOperate();

            Properties props = System.getProperties();
            String cpuInfo = props.getProperty("sun.cpu.isalist"); //操作系统版本

            String regeditPath = "SOFTWARE\\Wow6432Node\\Microsoft\\Windows\\CurrentVersion\\Uninstall\\"; //64位系统
            if (!cpuInfo.contains("64")) {
                regeditPath = "SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\Uninstall\\";
            }
            Map<String, String> map = registry.valuesForPath(registry.HKEY_LOCAL_MACHINE, regeditPath + softName);
            String installPath = map.get("InstallLocation") == null ? "" : map.get("InstallLocation").toString();
            path = installPath;
            System.out.println("安装路径：" + installPath);
        } catch (Exception ex) {
            //Log.Error("获取注册表中的程序路径失败", ex);
            System.out.println("获取安装路径失败：" + ex.getMessage());
        }
        return path;
    }


    public static String doScript(String softInstallPath, String verNum) {
        try {
            if (!isNotEmpty(softInstallPath)) {
                return "未找到安装路径";  //未找到安装路径
            }
            String scriptPath = softInstallPath + "\\script\\";
            File scriptPathDir = new File(scriptPath);
            if (!scriptPathDir.exists()) {
                System.out.println("脚本文件夹路径不存在");
                return "脚本文件夹路径不存在";//脚本文件路径不存在
            }

            List<String> lstFileNames = new ArrayList<String>();
            List<String> fileNames = getAllFile(scriptPath, false);// Directory.GetFiles(softInstallPath + "script\\"); //所有的脚本文件路径
            // Dictionary<string, string> dicVerMapFileName = new Dictionary<string, string>();
            Map<String, Object> dicVerMapFileName = new HashMap<String, Object>(); //版本与脚本文件对应关系
            for (int i = 0; i < fileNames.size(); i++) {
                String fileName = fileNames.get(i);
                Integer indexStart = fileName.indexOf('[');
                Integer indexEnd = fileName.indexOf(']');
                String verNo = fileName.substring(indexStart + 1, indexEnd);
                dicVerMapFileName.put(verNo, fileName);
            }

            verNum = verNum.toLowerCase();
            verNum = verNum.replaceAll("v", "");
            verNum = verNum.replaceAll("\\.", "");
            Integer pubVerNo = Integer.parseInt(verNum);
            for (String key : dicVerMapFileName.keySet()) {
                String tmpKey = key.toLowerCase();
                tmpKey = tmpKey.replaceAll("v", "");
                tmpKey = tmpKey.replaceAll("\\.", "");
                Integer scriptVerNo = Integer.parseInt(tmpKey);
                if (pubVerNo < scriptVerNo) {
                    lstFileNames.add(dicVerMapFileName.get(key).toString()); //所有需要刷脚本的文件
                }
            }

            //文件排序从小到大
            Collections.sort(lstFileNames, new Comparator<String>() {
                public int compare(String o1, String o2) {
                    /*
                     * int compare(String o1, String o2) 返回一个基本类型的整型，
                     * 返回负数表示：o1 小于o2，
                     * 返回0 表示：o1和o2相等，
                     * 返回正数表示：o1大于o2
                     */
                    if (o1.compareTo(o2) > 0) {
                        return 1;
                    } else if (o1.compareTo(o2) > 0) {
                        return 0;
                    } else {
                        return -1;
                    }
                }
            });

            for (Integer i = 0; i < lstFileNames.size(); i++) {
                try {
                    System.out.println("开始执行脚本：" + lstFileNames.get(i));
                    //调用Myscript
                    String strContextSql = readToString(lstFileNames.get(i));// File.ReadAllText(lstFileNames[i], Encoding.Default);
                    //解析一条条执行
                    String[] Scripts = strContextSql.split("\r\n");
                    String strContextSql_temp = "";
                    List<String> lstSql = new ArrayList<String>();
                    for (String script : Scripts) {
                        String scriptTmp = script;
                        if (scriptTmp.startsWith("-") || scriptTmp.startsWith("/") || scriptTmp.startsWith("*") || scriptTmp.contains("  发布流水:")
                                || scriptTmp.contains("  发布日期:") || scriptTmp.contains("  发布说明:") || scriptTmp.contains("  备注:")
                                || scriptTmp.contains("  版本类别:") || scriptTmp.contains("  版本号:")) {
                            continue;
                        } else {
                            if (script.endsWith(";")) {
                                script = script + ";##";
                            }
                            strContextSql_temp += " " + script;
                            // lstSql.add(script);
                        }
                    }
                    String[] Scripts_new = strContextSql_temp.split(";##");
                    for (String script : Scripts_new) {
                        if (isNotEmpty(script) && !script.equals(" ")) {
                            if(script.startsWith(" ")){
                                script = script.substring(1);
                            }
                            lstSql.add(script);
                        }
                    }
                    kingbaseHelper.doBatchsql(lstSql);
                    System.out.println("脚本：" + lstFileNames.get(i) + "执行完成");
                } catch (Exception ex) {
                    System.out.println("执行脚本：" + lstFileNames.get(i) + "失败！");
                    continue;
                }
            }
            System.out.println("脚本执行完成！");
            kingbaseHelper.closeConnection();
        } catch (Exception ex) {
            System.out.println("程序执行失败：" + ex.getMessage());
            return "执行失败";
        }
        return "执行完成";
    }

    public static boolean isNotEmpty(String string) {
        boolean result = false;
        if (string != null && !"".equals(string.trim())) {
            result = true;
        }
        return result;
    }

    public static List<String> getAllFile(String directoryPath, boolean isAddDirectory) {
        List<String> list = new ArrayList<String>();
        File baseFile = new File(directoryPath);
        if (baseFile.isFile() || !baseFile.exists()) {
            return list;
        }
        File[] files = baseFile.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                if (isAddDirectory) {
                    list.add(file.getAbsolutePath());
                }
                list.addAll(getAllFile(file.getAbsolutePath(), isAddDirectory));
            } else {
                list.add(file.getAbsolutePath());
            }
        }
        return list;
    }

    public static String readToString(String fileName) {
        String encoding = "UTF-8";
        File file = new File(fileName);
        Long filelength = file.length();
        byte[] filecontent = new byte[filelength.intValue()];
        try {
            FileInputStream in = new FileInputStream(file);
            in.read(filecontent);
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            return new String(filecontent, encoding);
        } catch (UnsupportedEncodingException e) {
            System.err.println("The OS does not support " + encoding);
            e.printStackTrace();
            return null;
        }

    }
}
