
import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class kingbaseHelper {
    //连接信息
    private String userName = "cmisltx";//这里是你最开始刚下载mysql是输入的账号
    private String password = "cmisltx";//这里是你最开始mysql设置的密码，
    private Connection connection = null;
    private String url = "jdbc:kingbase8://127.0.0.1:54321/cmisltx";
    {
        try {
            //这里不用更改，如果你要连的不是mysql的话再换这个括号内的代码
            Class.forName("com.kingbase8.Driver");
            System.out.println("驱动加载成功！");
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            System.out.println("加载失败！");
            e.printStackTrace();
        }
    }


    public kingbaseHelper() {
        // TODO Auto-generated constructor stub
        try {
            this.connection =  DriverManager.getConnection(url, userName, password);//把上面的三个属性传过来，应该就连接成功了，不成功的话应该就是你的账号密码不正确
            System.out.println("数据库连接成功！");
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.out.println("连接失败！");
        }
    }

    //关闭数据库的方法
    public void closeConnection(){
        if(connection != null){
            try {
                connection.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }


   public List<Map<String,Object>> getDbVerInfo(){

       List<Map<String,Object>> lstDbInfo = new ArrayList<Map<String, Object>>();
       String sql = "select * from db_chn_his order by PUB_VERNO DESC ";
       Statement stmt = null;//创建Statement对象
       try {
           stmt = this.connection.createStatement();
           System.out.println("成功连接到数据库！");
           ResultSet rs = stmt.executeQuery(sql);//创建数据对象
           while (rs.next()){
               Map<String,Object> map = new LinkedHashMap<String, Object>();

               map.put("SEQ_START_NO",rs.getInt("SEQ_START_NO")) ;
               map.put("SEQ_END_NO",rs.getInt("SEQ_END_NO")) ;
               map.put("PUB_VERNO",rs.getString("PUB_VERNO")) ;

               lstDbInfo.add(map);
           }
           rs.close();
           stmt.close();
       } catch (SQLException e) {
           e.printStackTrace();
       }
       return  lstDbInfo;
   }

    public void dosql(String sql) {
        //遍历查询结果集
        try {
            if(!connection.isClosed())
                System.out.println("Succeeded connecting to the Database!");
            Statement statement = connection.createStatement(); //2.创建statement类对象，用来执行SQL语句！！

            if(statement.executeUpdate(sql)!=0)
                System.out.println(sql + "执行成功");
            else
                System.out.println(sql + "执行失败");
            statement.close();
        }	catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    public void doBatchsql(List<String> lstsql) {
        //遍历查询结果集
        try {
            if(!connection.isClosed())
                System.out.println("Succeeded connecting to the Database!");
            Statement statement = connection.createStatement(); //2.创建statement类对象，用来执行SQL语句！！
            for(String sql :lstsql){
                try {
                    statement.execute(sql.trim());
                }
                catch (Exception ex){
                    System.out.println(ex.getMessage());
                    continue;
                }
                //statement.addBatch(sql);
            }
            //statement.executeBatch();
            statement.close();
        }	catch (Exception e) {
            e.printStackTrace();
        }
    }


//    public void  dosqlFile(String filesq){
//        try {
//
//            // 创建ScriptRunner，用于执行SQL脚本
//            ScriptRunner runner = new ScriptRunner(conn);
//            runner.setErrorLogWriter(null);
//            runner.setLogWriter(null);
//            // 执行SQL脚本
//            runner.runScript(Resources.getResourceAsReader("sql/" + sqlFileName + ".sql"));
//
//            // 关闭连接
//            conn.close();
//
//            // 若成功，打印提示信息
//            System.out.println("====== SUCCESS ======");
//        } catch (IOException | SQLException e) {
//            e.printStackTrace();
//        }
//    }


}
