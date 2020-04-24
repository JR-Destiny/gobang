package mr.ncu.com.client;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.util.HashMap;

import mr.ncu.com.client.SQLite.DAO;
import mr.ncu.com.client.SQLite.DatabaseHelper;

public class ModeActivity extends AppCompatActivity {

    private ImageView WithPlayer;
    private ImageView WithCom;
    DatabaseHelper databaseHelper;
    DAO playerdb ;
    int _id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mode);

        databaseHelper = new DatabaseHelper(this.getApplicationContext(),"player.db",null,1);
        playerdb = new DAO();

        WithPlayer = (ImageView)findViewById(R.id.WithPlayer);
        WithCom = (ImageView)findViewById(R.id.WithCom);

        WithPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(ModeActivity.this,RoomWayActivity.class);
                startActivity(intent);
            }
        });
        WithCom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(ModeActivity.this,GameAIActivity.class);//这里跳转到人机棋盘
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        String ad_title = "提示";
        new AlertDialog.Builder(ModeActivity.this)
                .setTitle(ad_title)
                .setMessage("是否退出登录")
                .setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                HashMap<String,Object> notemap = new HashMap<String, Object>();
                                notemap = playerdb.getPlayerState(databaseHelper);
                                _id = (int) notemap.get("_id");
                                ContentValues contentValues = new ContentValues();
                                contentValues.put("state", "false");
                                playerdb.updatePlayerInfo(databaseHelper,_id,contentValues);
                                ModeActivity.super.onBackPressed();
                            }
                        })
                .setNegativeButton("取消",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }).create().show();
    }
}
