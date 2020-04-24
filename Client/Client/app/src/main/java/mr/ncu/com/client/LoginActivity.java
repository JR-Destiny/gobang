package mr.ncu.com.client;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.HashMap;

import mr.ncu.com.client.SQLite.DAO;
import mr.ncu.com.client.SQLite.DatabaseHelper;

import static mr.ncu.com.client.MainActivity.connect_flag;
import static mr.ncu.com.client.MainActivity.net;

public class LoginActivity extends AppCompatActivity {

    private ImageView Login_Confirm;
    private EditText Login_Name;
    private EditText Login_Password;
    private String relogin;
    private String rev = "null";
    private String name;
    private String psw;
    public static Handler LoginHandler;
    private SendMsgForm smf;
    DatabaseHelper databaseHelper;
    DAO playerdb ;
    int _id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        LoginHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                rev = msg.obj.toString();
                String temp[] = rev.split("&");
                if (!temp[1].equals("false")){
                    Toast.makeText(LoginActivity.this,"登陆成功",Toast.LENGTH_SHORT).show();
                    HashMap<String,Object> notemap = new HashMap<String, Object>();
                    notemap = playerdb.getPlayerName(databaseHelper,name);
                    if ((int)notemap.get("num") == 0){
                        ContentValues contentValues = new ContentValues();
                        contentValues.put("name", name);
                        contentValues.put("psw", psw);
                        contentValues.put("state", "true");
                        contentValues.put("win", temp[1]);
                        contentValues.put("total", temp[2]);
                        playerdb.insertPlayer(databaseHelper, contentValues);
                    }else {
                        _id = (int) notemap.get("_id");
                        ContentValues contentValues = new ContentValues();
                        contentValues.put("state", "true");
                        contentValues.put("win", temp[1]);
                        contentValues.put("total", temp[2]);
                        playerdb.updatePlayerInfo(databaseHelper,_id,contentValues);
                    }
                    Intent intent = new Intent();
                    intent.setClass(LoginActivity.this,ModeActivity.class);
                    startActivity(intent);
                }else {
                    Toast.makeText(LoginActivity.this,"账户名或密码错误",Toast.LENGTH_SHORT).show();
                }
            }
        } ;

        //建立数据库,数据库操作对象
        databaseHelper = new DatabaseHelper(this.getApplicationContext(),"player.db",null,1);
        playerdb = new DAO();
        smf = new SendMsgForm();

        Login_Name = (EditText) findViewById(R.id.Login_Name);
        Login_Password = (EditText) findViewById(R.id.Login_Password);
        Login_Confirm = (ImageView)findViewById(R.id.Login_Confirm);

        Intent intent = getIntent();
        relogin = intent.getStringExtra("relogin");
        if (relogin.equals("true")){
            HashMap<String,Object> notemap = new HashMap<String, Object>();
            notemap = playerdb.getPlayerState(databaseHelper);
            Login_Name.setText((String) notemap.get("name"));
            Login_Password.setText((String)notemap.get("psw"));
            name = Login_Name.getText().toString();
            psw = Login_Password.getText().toString();
            net.sendMsg(smf.playerLogin((String) notemap.get("name"),(String)notemap.get("psw")));
        }

        Login_Confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(connect_flag){
                    name = Login_Name.getText().toString();
                    psw = Login_Password.getText().toString();
                    net.sendMsg(smf.playerLogin(name,psw));
                }
                else {
                    Toast.makeText(LoginActivity.this, "服务器连接失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}
