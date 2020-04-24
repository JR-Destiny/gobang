import java.sql.*;

public class DBconnect_close {
    private static String driverName="com.mysql.jdbc.Driver";                  //驱动程序名
    private static String userName="root";                                     //数据库用户名
    private static String userPwd="NCUMrY2019!";                                    //密码
    private static String dbName="gobang";                                     //数据库名
    public static Connection getDBconnection(){
        String url1="jdbc:mysql://localhost:3306/"+dbName;
        String url2="?user="+userName+"&password="+userPwd;
        String url3="&useUnicode=true&characterEncoding=UTF-8&useSSL=false";
        String url=url1+url2+url3;                                   //形成带数据库读写编码的数据库连接字
        try{
            Class.forName(driverName);                                   //加载并注册驱动程序
            Connection con= DriverManager.getConnection(url);            //创建并连接对象
            return  con;
        }catch (Exception e){
            e.printStackTrace();
        }
        return  null;
    }
    public static void  closeDB(Connection con, PreparedStatement pstm, ResultSet rs){
        try{
            if (rs!=null) rs.close();
            if (pstm!=null) pstm.close();
            if (con!=null) con.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}
