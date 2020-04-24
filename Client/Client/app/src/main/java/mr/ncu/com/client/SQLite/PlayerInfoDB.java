package mr.ncu.com.client.SQLite;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class PlayerInfoDB implements DatabaseHelper.TableCreateInterface {
    //定义表名
    public static String tableName = "playerInfo";
    //定义个字段名
    public static String _id = "_id";//_id是SQlite中自动生成的主键，用于标识唯一的记录，为了方便使用，此处对应字段名
    public static String name = "name";
    public static String psw = "psw";
    public static String state = "state";
    public static String win = "win";
    public static String total =  "total";
    public static String e_token = "e_token";
    public static String e_pixX = "e_pixX";
    public static String e_pixY = "e_pixY";
    public static String e_win = "e_win";
    public static String e_total = "e_total";
    public static String e_name = "e_name";

    //返回表的实例进行创建和更新
    private static PlayerInfoDB playerInfoDB =new PlayerInfoDB();

    public static PlayerInfoDB getInstance(){
        return PlayerInfoDB.playerInfoDB;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE "
                + PlayerInfoDB.tableName
                + " ( "
                + "_id integer primary key autoincrement, "
                + PlayerInfoDB.name + " TEXT, "     //昵称
                + PlayerInfoDB.psw + " TEXT, "     //密码
                + PlayerInfoDB.state + " TEXT, "    //上次登录状态
                + PlayerInfoDB.win + " TEXT, "      //胜场
                + PlayerInfoDB.total + " TEXT, "    //总场
                + PlayerInfoDB.e_token + " TEXT, "   //对手的token令牌
                + PlayerInfoDB.e_pixX +  " TEXT, "
                + PlayerInfoDB.e_pixY +  " TEXT, "
                + PlayerInfoDB.e_win +  " TEXT, "
                + PlayerInfoDB.e_total +  " TEXT, "
                + PlayerInfoDB.e_name + " TEXT "
                + ");";
        db.execSQL(sql);
        Log.d("DB", "database has already created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < newVersion){
            String sql = "DROP TABLE IF EXISTS " + PlayerInfoDB.tableName;
            db.execSQL(sql);
            this.onCreate( db );
        }
    }
}
