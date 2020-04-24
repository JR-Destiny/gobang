package mr.ncu.com.client.SQLite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public static interface TableCreateInterface{
        public void onCreate(SQLiteDatabase db);
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        PlayerInfoDB.getInstance().onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion >= newVersion) return;
        PlayerInfoDB.getInstance().onUpgrade(db, oldVersion, newVersion);
    }
}
