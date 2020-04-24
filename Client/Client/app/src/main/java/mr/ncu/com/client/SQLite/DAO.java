package mr.ncu.com.client.SQLite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.HashMap;

public class DAO {

    public void insertPlayer(DatabaseHelper dbHelper, ContentValues userValues){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.insert(PlayerInfoDB.tableName,null,userValues);
        Log.d("db","success");
        db.close();
    }


    public void updatePlayerInfo(DatabaseHelper dbHelper, int _id, ContentValues infoValues){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.update(PlayerInfoDB.tableName,infoValues,PlayerInfoDB._id + " =? ",new String[] { _id + ""});
        db.close();
    }

    public HashMap<String , Object> getPlayerState(DatabaseHelper dbHelper){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        HashMap<String,Object> notemap = new HashMap<String, Object>();

        Cursor cursor = db.query(
                PlayerInfoDB.tableName,
                null,
                PlayerInfoDB.state + " =? ",
                new String[] { "true" + ""},
                null,null,null);
        if (cursor.getCount() != 0) {
            cursor.moveToFirst();
            notemap.put("num",1);
            notemap.put(PlayerInfoDB._id, cursor.getInt(cursor.getColumnIndex(PlayerInfoDB._id)));
            notemap.put(PlayerInfoDB.name, cursor.getString(cursor.getColumnIndex(PlayerInfoDB.name)));
            notemap.put(PlayerInfoDB.psw, cursor.getString(cursor.getColumnIndex(PlayerInfoDB.psw)));
            notemap.put(PlayerInfoDB.state, cursor.getString(cursor.getColumnIndex(PlayerInfoDB.state)));
            notemap.put(PlayerInfoDB.win, cursor.getString(cursor.getColumnIndex(PlayerInfoDB.win)));
            notemap.put(PlayerInfoDB.total, cursor.getString(cursor.getColumnIndex(PlayerInfoDB.total)));
            notemap.put(PlayerInfoDB.e_token, cursor.getString(cursor.getColumnIndex(PlayerInfoDB.e_token)));
            notemap.put(PlayerInfoDB.e_pixX, cursor.getString(cursor.getColumnIndex(PlayerInfoDB.e_pixX)));
            notemap.put(PlayerInfoDB.e_pixY, cursor.getString(cursor.getColumnIndex(PlayerInfoDB.e_pixY)));
            notemap.put(PlayerInfoDB.e_win, cursor.getString(cursor.getColumnIndex(PlayerInfoDB.e_win)));
            notemap.put(PlayerInfoDB.e_total, cursor.getString(cursor.getColumnIndex(PlayerInfoDB.e_total)));
            notemap.put(PlayerInfoDB.e_name, cursor.getString(cursor.getColumnIndex(PlayerInfoDB.e_name)));
        }else{
            notemap.put("num",0);
        }
        cursor.close();
        db.close();
        return notemap;
    }

    public HashMap<String , Object> getPlayerName(DatabaseHelper dbHelper ,String name){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        HashMap<String,Object> notemap = new HashMap<String, Object>();

        Cursor cursor = db.query(
                PlayerInfoDB.tableName,
                null,
                PlayerInfoDB.name + " =? ",
                new String[] { name + ""},
                null,null,null);
        if (cursor.getCount() != 0) {
            cursor.moveToFirst();
            notemap.put("num",1);
            notemap.put(PlayerInfoDB._id, cursor.getInt(cursor.getColumnIndex(PlayerInfoDB._id)));
            notemap.put(PlayerInfoDB.name, cursor.getString(cursor.getColumnIndex(PlayerInfoDB.name)));
            notemap.put(PlayerInfoDB.psw, cursor.getString(cursor.getColumnIndex(PlayerInfoDB.psw)));
            notemap.put(PlayerInfoDB.state, cursor.getString(cursor.getColumnIndex(PlayerInfoDB.state)));
            notemap.put(PlayerInfoDB.win, cursor.getString(cursor.getColumnIndex(PlayerInfoDB.win)));
            notemap.put(PlayerInfoDB.total, cursor.getString(cursor.getColumnIndex(PlayerInfoDB.total)));
            notemap.put(PlayerInfoDB.e_token, cursor.getString(cursor.getColumnIndex(PlayerInfoDB.e_token)));
            notemap.put(PlayerInfoDB.e_pixX, cursor.getString(cursor.getColumnIndex(PlayerInfoDB.e_pixX)));
            notemap.put(PlayerInfoDB.e_pixY, cursor.getString(cursor.getColumnIndex(PlayerInfoDB.e_pixY)));
            notemap.put(PlayerInfoDB.e_win, cursor.getString(cursor.getColumnIndex(PlayerInfoDB.e_win)));
            notemap.put(PlayerInfoDB.e_total, cursor.getString(cursor.getColumnIndex(PlayerInfoDB.e_total)));
            notemap.put(PlayerInfoDB.e_name, cursor.getString(cursor.getColumnIndex(PlayerInfoDB.e_name)));
        }else{
            notemap.put("num",0);
        }
        cursor.close();
        db.close();
        return notemap;
    }


}
