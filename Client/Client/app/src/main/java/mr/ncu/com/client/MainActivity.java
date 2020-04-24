package mr.ncu.com.client;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.HashMap;

import mr.ncu.com.client.SQLite.DAO;
import mr.ncu.com.client.SQLite.DatabaseHelper;

public class MainActivity extends AppCompatActivity {

    public static boolean connect_flag = true;
    private ImageView Login;
    private ImageView Register;
    private SendMsgForm smf;
    DatabaseHelper databaseHelper;
    DAO playerdb ;

    private MusicManager mService = null;
    private MusicManager.MyBinder  mManager = null;

    private NetManager netService = null;
    private NetManager.MyBinder  netManager = null;

    public static Net net;

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mManager = (MusicManager.MyBinder) service;
            mService = mManager.getService();
            mService.InitPlayer();
            mManager.Play();
            AllListener();
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };

    private ServiceConnection NetServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            netManager = (NetManager.MyBinder) service;
            netService = netManager.getService();
            netService.Init();
            if(connect_flag)
            {
                netManager.receive();
            }

        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Login = (ImageView)findViewById(R.id.Login);
        Register = (ImageView)findViewById(R.id.Register);


        //建立数据库,数据库操作对象
        databaseHelper = new DatabaseHelper(this.getApplicationContext(),"player.db",null,1);
        playerdb = new DAO();

        smf = new SendMsgForm();

        HashMap<String,Object> notemap = new HashMap<String, Object>();
        notemap = playerdb.getPlayerState(databaseHelper);

        Intent MusicIntent = new Intent(this, MusicManager.class);
        bindService(MusicIntent, mServiceConnection, BIND_AUTO_CREATE);

        Intent NetIntent = new Intent(this, NetManager.class);
        bindService(NetIntent, NetServiceConnection, BIND_AUTO_CREATE);

        if ((int)notemap.get("num") != 0){
            if(connect_flag)
            {
                Intent intent = new Intent();
                intent.putExtra("relogin","true");
                intent.setClass(MainActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        }
    }

    public void AllListener() {
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(connect_flag)
                {
                    Intent intent = new Intent();
                    intent.putExtra("relogin","false");
                    intent.setClass(MainActivity.this,LoginActivity.class);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(MainActivity.this, "服务器连接失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(connect_flag){
                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this,RegisterActivity.class);
                    startActivity(intent);
                }else {
                    Toast.makeText(MainActivity.this, "服务器连接失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
