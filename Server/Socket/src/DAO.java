import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class DAO {
    protected  String INSERT_ROOM = "insert into room(player1) values(?)";
    protected  String INSERT_PLAYER = "insert into player(token,name,psw,win,total) values(?,?,?,?,?)";
    protected  String REPETITION_SQL="select name from player where name=?";
    protected  String QUERY_PLAYER_NAME = "select name from player where token=?";
    protected  String QUERY_PLAYER_TOKEN = "select token from player where name=?";
    protected  String QUERY_ROOM = "select player1 from room";
    protected  String DELETE_ROOM = "delete from room where player1=?";
    protected  String LOGIN_SQL= "select name,psw from player where name=?";
    protected  String QUERY_PLAYER_INFO = "select win,total from player where token=?";
    protected  String UPDATE_PLAYER_INFO = "update player set ? = ? where token = ?";
    protected  String UPDATE_PLAYER_TOKEN = "update player set token = ? where name = ?";

    public String createRoom(String player1name){
        String flag="false";
        Connection con=null;
        PreparedStatement prepStmt=null;
        ResultSet rs=null;
        try{
            con=DBconnect_close.getDBconnection();
            prepStmt=con.prepareStatement(INSERT_ROOM);
            prepStmt.setString(1,player1name);
            int n=prepStmt.executeUpdate();
            if(n==1) flag="true";
        }catch (Exception e){
        }finally {
            DBconnect_close.closeDB(con,prepStmt,rs);
        }
        return flag;
    }

    public String queryPlayerName(String token){
        String flag="null";
        Connection con=null;
        PreparedStatement prepStmt=null;
        ResultSet rs=null;
        try{
            con=DBconnect_close.getDBconnection();
            prepStmt=con.prepareStatement(QUERY_PLAYER_NAME);
            prepStmt.setString(1,token);
            rs=prepStmt.executeQuery();
            while(rs.next()) {
                flag = rs.getString("name");
            }
        }catch (Exception e){
        }finally {
            DBconnect_close.closeDB(con,prepStmt,rs);
        }
        return flag;
    }

    public String queryPlayerToken(String name){
        String flag="null";
        Connection con=null;
        PreparedStatement prepStmt=null;
        ResultSet rs=null;
        try{
            con=DBconnect_close.getDBconnection();
            prepStmt=con.prepareStatement(QUERY_PLAYER_TOKEN);
            prepStmt.setString(1,name);
            rs=prepStmt.executeQuery();
            while(rs.next()) {
                flag = rs.getString("token");
            }
        }catch (Exception e){
        }finally {
            DBconnect_close.closeDB(con,prepStmt,rs);
        }
        return flag;
    }

    public String queryRoom(){
        String roomlist = null;
        int all ;//题目总数目
        Connection con=null;
        PreparedStatement prepStmt=null;
        ResultSet rs=null;
        try {
            con=DBconnect_close.getDBconnection();
            prepStmt=con.prepareStatement(QUERY_ROOM);
            rs=prepStmt.executeQuery();
            while (rs.next()){
                roomlist = roomlist +"&"+ rs.getString("player1") ;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DBconnect_close.closeDB(con,prepStmt,rs);
        }
        return roomlist;
    }

    public String deleteRoom(String player1){
        String flag="false";
        Connection con=null;
        PreparedStatement prepStmt=null;
        ResultSet rs=null;
        try{
            con=DBconnect_close.getDBconnection();
            prepStmt=con.prepareStatement(DELETE_ROOM);
            prepStmt.setString(1,player1);
            int n=prepStmt.executeUpdate();
            if(n>=1) flag="true";
        }catch (Exception e){
        }finally {
            DBconnect_close.closeDB(con,prepStmt,rs);
        }
        return flag;
    }

    public String createPlayer(String token ,String name,String psw){
        String flag="false";
        Connection con=null;
        PreparedStatement prepStmt=null;
        ResultSet rs=null;
        try{
            con=DBconnect_close.getDBconnection();
            prepStmt=con.prepareStatement(INSERT_PLAYER);
            prepStmt.setString(1,token);
            prepStmt.setString(2,name);
            prepStmt.setString(3,psw);
            prepStmt.setInt(4,0);
            prepStmt.setInt(5,0);
            int n=prepStmt.executeUpdate();
            if(n==1) flag="true";
        }catch (Exception e){
        }finally {
            DBconnect_close.closeDB(con,prepStmt,rs);
        }
        return flag;
    }

    public String repetition (String name) {
        String flag="false";
        Connection con=null;
        PreparedStatement prepStmt=null;
        ResultSet rs=null;
        String name0;//数据库中已存放的学号
        try{
            con=DBconnect_close.getDBconnection();
            prepStmt=con.prepareStatement(REPETITION_SQL);
            prepStmt.setString(1,name);
            rs=prepStmt.executeQuery();
            while(rs.next()) {
                name0 = rs.getString("name");//查询数据库，把学号字段提取出来
                if(name0.equals(name)) flag="true";//如果查到重复，返回true
            }
        }catch (Exception e){
        }finally {
            DBconnect_close.closeDB(con,prepStmt,rs);
        }
        return flag;
    }

    public String playerLogin(String name ,String psw) {
        String  flag="false";
        Connection con=null;
        PreparedStatement prepStmt=null;
        ResultSet rs=null;
        String password0=null; //数据库中已存放的密码
        try {
            con=DBconnect_close.getDBconnection();
            prepStmt=con.prepareStatement(LOGIN_SQL);
            prepStmt.setString(1,name);
            rs=prepStmt.executeQuery();
            while (rs.next()){
                password0 = rs.getString("psw");
                if (password0.equals(psw)) {
                    flag = "true";
                    break;
                }
            }
        }catch (Exception e){
        }finally {
            DBconnect_close.closeDB(con,prepStmt,rs);
        }
        return  flag;
    }

    public String queryPlayerInfo(String token){
        String flag="null";
        Connection con=null;
        PreparedStatement prepStmt=null;
        ResultSet rs=null;
        try{
            con=DBconnect_close.getDBconnection();
            prepStmt=con.prepareStatement(QUERY_PLAYER_INFO);
            prepStmt.setString(1,token);
            rs=prepStmt.executeQuery();
            while(rs.next()) {
                flag = rs.getInt("win") + "&" + rs.getInt("total");
            }
        }catch (Exception e){
        }finally {
            DBconnect_close.closeDB(con,prepStmt,rs);
        }
        return flag;
    }

    public String updatePlayerInfo(String tag,String token){
        String flag = "false";
        Connection con=null;
        PreparedStatement prepStmt=null;
        ResultSet rs=null;
        if (tag.equals("win")) {
            UPDATE_PLAYER_INFO="update player set win = win + 1 where token = ? ";
        }
        else if (tag.equals("total")) {
            UPDATE_PLAYER_INFO="update player set total = total + 1 where token = ? ";
        }
        try {
            con=DBconnect_close.getDBconnection();
            prepStmt=con.prepareStatement(UPDATE_PLAYER_INFO);
            prepStmt.setString(1,token);
            int n=prepStmt.executeUpdate();
            if(n==1) flag="true";
        }catch (Exception e){
        }finally {
            DBconnect_close.closeDB(con,prepStmt,rs);
        }
        return flag;
    }

    public String updatePlayerToken(String token ,String name){
        String flag = "false";
        Connection con=null;
        PreparedStatement prepStmt=null;
        ResultSet rs=null;
        try {
            con=DBconnect_close.getDBconnection();
            prepStmt=con.prepareStatement(UPDATE_PLAYER_TOKEN);
            prepStmt.setString(1,token);
            prepStmt.setString(2,name);
            int n=prepStmt.executeUpdate();
            if(n==1) flag="true";
        }catch (Exception e){
        }finally {
            DBconnect_close.closeDB(con,prepStmt,rs);
        }
        return flag;
    }
}
