package mr.ncu.com.client;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;
import java.util.HashMap;

import mr.ncu.com.client.SQLite.DAO;
import mr.ncu.com.client.SQLite.DatabaseHelper;
import static mr.ncu.com.client.MainActivity.net;

public class PlayerNetActivity extends AppCompatActivity {

    public static Handler PlayerNetHandler;
    private String pixX;
    private String pixY;
    private SendMsgForm smf;
    private String rev = "null";
    private String room ;
    private int m_win;
    private int m_total;
    private int e_win;
    private int e_total;
    private String e_name;
    private String GAME_STATE = "false";

    DatabaseHelper databaseHelper;
    DAO playerdb ;
    int _id;//获取主键id号
    String win;
    String total;
    String name;

    private PlaynetChess panel;
    private AlertDialog.Builder builder;
    private Button regret,gameback;
    private ImageView enemy_chess,my_chess;
    private TextView enemyname,myname,enemycount,mycount;

    private AlertDialog over_dialog;//游戏结束时的对话框
    private ProgressDialog waitingDialog;//等待响应的对话框;

    private SoundManager mService = null;
    private SoundManager.MyBinder  mManager = null;

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mManager = (SoundManager.MyBinder) service;
            mService = mManager.getService();
            panel.SetMusicManager(mManager);
            AllListener();
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_net);
        panel = (PlaynetChess) findViewById(R.id.net_panel);
        enemyname=(TextView)findViewById(R.id.enemy_name);
        myname=(TextView)findViewById(R.id.myname);
        enemycount=(TextView)findViewById(R.id.enemy_count);
        mycount=(TextView)findViewById(R.id.my_count);
        enemy_chess=(ImageView)findViewById(R.id.enemy_chess);
        my_chess=(ImageView)findViewById(R.id.my_chess);
        gameback=(Button)findViewById(R.id.btn_ret);

        AnimationDrawable Anim_name = (AnimationDrawable)enemyname.getBackground();
        Anim_name.start();

        Intent MusicIntent = new Intent(this, SoundManager.class);
        bindService(MusicIntent, mServiceConnection, BIND_AUTO_CREATE);

        PlayerNetHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                rev = msg.obj.toString();
                String temp[] = rev.split("&");
                ContentValues contentValues = new ContentValues();
                switch (msg.what) {
                    case 0:
                        contentValues.put("e_token", temp[1]);
                        contentValues.put("e_pixX", temp[2]);
                        contentValues.put("e_pixY", temp[3]);
                        contentValues.put("e_win",  temp[4]);
                        contentValues.put("e_total",temp[5]);
                        contentValues.put("e_name",temp[6]);
                        playerdb.updatePlayerInfo(databaseHelper, _id, contentValues);
                        net.sendMsg(smf.createrPix(temp[1], pixX, pixY,win,total,name));
                        e_win = Integer.parseInt(temp[4]);
                        e_total = Integer.parseInt(temp[5]);
                        e_name=temp[6];
                        //有人加入后，界面更新敌方胜率,游戏状态置为start
                        GAME_STATE = "true";
                        enemyname.getBackground().setAlpha(0);
                        enemyname.setText(e_name);
                        if(e_total!=0) {
                            NumberFormat nf = NumberFormat.getNumberInstance();
                            nf.setMaximumFractionDigits(2);
                            enemycount.setText("胜率:" + nf.format(((double)e_win / (double)e_total) * 100) + "%");
                        }
                        panel.setUserBout(true);
                        break;
                    case 1:
                        contentValues.put("win", temp[1]);
                        contentValues.put("total", temp[2]);
                        contentValues.put("e_win",temp[3]);
                        contentValues.put("e_total",temp[4]);
                        playerdb.updatePlayerInfo(databaseHelper, _id, contentValues);
                        break;
                    case 2:
                        Toast.makeText(PlayerNetActivity.this, "对手已离开游戏", Toast.LENGTH_SHORT).show();
                        PlayerNetActivity.this.finish();
                        break;
                    case 3:
                        over_dialog.dismiss();
                        AlertDialog.Builder normalDialog =
                                new AlertDialog.Builder(PlayerNetActivity.this);
                        normalDialog.setTitle("对方请求再来一局");
                        normalDialog.setPositiveButton("接受",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        net.sendMsg(smf.agreeOrrefuse((String) playerdb.getPlayerState(databaseHelper).get("e_token"),"agree"));
                                        panel.restartGame();
                                        NumberFormat nf = NumberFormat.getNumberInstance();
                                        nf.setMaximumFractionDigits(2);
                                        mycount.setText("胜率:" + nf.format(((double) Integer.parseInt((String) playerdb.getPlayerState(databaseHelper).get("win")) / (double) Integer.parseInt((String) playerdb.getPlayerState(databaseHelper).get("total"))) * 100) + "%");
                                        enemycount.setText("胜率:" + nf.format(((double)Integer.parseInt((String)playerdb.getPlayerState(databaseHelper).get("e_win")) / (double)Integer.parseInt((String)playerdb.getPlayerState(databaseHelper).get("e_total"))) * 100) + "%");
                                    }
                                });
                        normalDialog.setNegativeButton("拒绝",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        net.sendMsg(smf.agreeOrrefuse((String) playerdb.getPlayerState(databaseHelper).get("e_token"),"refuse"));
                                        PlayerNetActivity.this.finish();
                                    }
                                });
                        normalDialog.show();
                        break;
                    case 4:
                        waitingDialog.dismiss();
                        if (temp[1].equals("agree")){
                            Toast.makeText(PlayerNetActivity.this, "对手同意再来一局", Toast.LENGTH_SHORT).show();
                            panel.restartGame();
                            NumberFormat nf = NumberFormat.getNumberInstance();
                            nf.setMaximumFractionDigits(2);
                            mycount.setText("胜率:" + nf.format(((double) Integer.parseInt((String)playerdb.getPlayerState(databaseHelper).get("win")) / (double) Integer.parseInt((String)playerdb.getPlayerState(databaseHelper).get("total"))) * 100) + "%");
                            enemycount.setText("胜率:" + nf.format(((double)Integer.parseInt((String)playerdb.getPlayerState(databaseHelper).get("e_win")) / (double)Integer.parseInt((String)playerdb.getPlayerState(databaseHelper).get("e_total"))) * 100) + "%");
                        }else {
                            Toast.makeText(PlayerNetActivity.this, "对手拒绝再来一局", Toast.LENGTH_SHORT).show();
                            PlayerNetActivity.this.finish();
                        }
                        break;
                }
            }
        } ;
        smf = new SendMsgForm();
        Intent intent = getIntent();
        room = intent.getStringExtra("room");
        databaseHelper = new DatabaseHelper(this.getApplicationContext(),"player.db",null,1);
        playerdb = new DAO();
        HashMap<String,Object> playermap = new HashMap<String, Object>();
        playermap = playerdb.getPlayerState(databaseHelper);
        _id = (int) playermap.get("_id");
        win = (String) playermap.get("win");
        total = (String) playermap.get("total");
        name = (String) playermap.get("name");
        m_win = Integer.parseInt((String)playermap.get("win"));
        m_total = Integer.parseInt((String)playermap.get("total"));
        if(m_total!=0) {
            NumberFormat nf = NumberFormat.getNumberInstance();
            nf.setMaximumFractionDigits(2);
            mycount.setText("胜率:" + nf.format(((double) m_win / (double) m_total) * 100) + "%");
        }
        if (!room.equals("maker")){
            e_win = Integer.parseInt((String)playermap.get("e_win"));
            e_total = Integer.parseInt((String)playermap.get("e_total"));
            if(e_total!=0) {
                NumberFormat nf = NumberFormat.getNumberInstance();
                nf.setMaximumFractionDigits(2);
                enemycount.setText("胜率:" + nf.format(((double)e_win / (double)e_total) * 100) + "%");
            }
            //更新敌方胜率
            e_name= (String) playermap.get("e_name");
            enemyname.getBackground().setAlpha(0);
            enemyname.setText(e_name);
        }
        //更新己方胜率
        myname.setText(name);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        pixX = String.valueOf(metrics.widthPixels);
        pixY = String.valueOf(metrics.heightPixels);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        if (room.equals("maker")) {
            panel.setWhite(false);
            my_chess.setImageDrawable(getResources().getDrawable(R.drawable.blackchess));
            enemy_chess.setImageDrawable(getResources().getDrawable(R.drawable.whitechess));
        }else {
            panel.setWhite(true);
            GAME_STATE = "true";
        }
    }

    public void AllListener() {
        panel.setOnGameListener(new PlaynetChess.onGameListener() {
            @Override
            public void onGameOver(int i) {
                builder= new AlertDialog.Builder(PlayerNetActivity.this);
                builder.setTitle("游戏结束");
                builder.setNegativeButton("退出", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        PlayerNetActivity.this.finish();
                        net.sendMsg(smf.leaveGame((String) playerdb.getPlayerState(databaseHelper).get("e_token")));
                    }
                });
                builder.setPositiveButton("再来一局", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface interface1, int which) {
                        panel.restartGame();
                        net.sendMsg(smf.playAgain((String) playerdb.getPlayerState(databaseHelper).get("e_token")));
                        //关闭当前对话框，显示等待对话框
                        waitingDialog=
                                new ProgressDialog(PlayerNetActivity.this);
                        waitingDialog.setTitle("等待对方响应");
                        waitingDialog.setMessage("等待中...");
                        waitingDialog.setIndeterminate(true);
                        waitingDialog.setCancelable(false);
                        waitingDialog.show();
                    }
                });
                String str = "";
                if (i== 1) {
                    str = "白方胜利！";
                    if (panel.isWhite()) {
                        net.sendMsg(smf.winGame(DeviceIdUtil.getDeviceId(PlayerNetActivity.this), (String) playerdb.getPlayerState(databaseHelper).get("e_token")));
                        //net.sendMsg(smf.loseGame((String) playerdb.getPlayerState(databaseHelper).get("e_token"), DeviceIdUtil.getDeviceId(PlayerNetActivity.this)));
                    }
                }else if (i== 0) {
                    str = "黑方胜利！";
                    if (!panel.isWhite()) {
                        net.sendMsg(smf.winGame(DeviceIdUtil.getDeviceId(PlayerNetActivity.this), (String) playerdb.getPlayerState(databaseHelper).get("e_token")));
                        //net.sendMsg(smf.loseGame((String) playerdb.getPlayerState(databaseHelper).get("e_token"), DeviceIdUtil.getDeviceId(PlayerNetActivity.this)));
                    }
                }
                builder.setMessage(str);
                builder.setCancelable(false);//不可用返回键取消
                over_dialog = builder.create();
                Window dialogWindow = over_dialog.getWindow();
                WindowManager.LayoutParams params = new WindowManager.LayoutParams();
                params.x = 0;
                params.y = 0;
                dialogWindow.setAttributes(params);//设置Dialog显示的位置
                over_dialog.setCanceledOnTouchOutside(false);//不可点击取消
                over_dialog.show();
            }
        } );

        gameback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder= new AlertDialog.Builder(PlayerNetActivity.this);
                builder.setTitle("离开游戏");
                builder.setNegativeButton("退出", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        if (GAME_STATE.equals("true")){
                            net.sendMsg(smf.winGame((String) playerdb.getPlayerState(databaseHelper).get("e_token"),DeviceIdUtil.getDeviceId(PlayerNetActivity.this)));
                            //net.sendMsg(smf.loseGame(DeviceIdUtil.getDeviceId(PlayerNetActivity.this),(String) playerdb.getPlayerState(databaseHelper).get("e_token")));
                            net.sendMsg(smf.leaveGame((String) playerdb.getPlayerState(databaseHelper).get("e_token")));
                        }else {
                            net.sendMsg(smf.leaveRoom());
                        }
                        PlayerNetActivity.this.finish();
                    }
                });
                builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface interface1, int which) {

                    }
                });
                String str = "是否退出游戏？";
                builder.setMessage(str);
                builder.setCancelable(false);//不可用返回键取消
                AlertDialog dialog = builder.create();
                Window dialogWindow = dialog.getWindow();
                WindowManager.LayoutParams params = new WindowManager.LayoutParams();
                params.x = 0;
                params.y = 0;
                dialogWindow.setAttributes(params);//设置Dialog显示的位置
                dialog.setCanceledOnTouchOutside(false);//不可点击取消
                dialog.show();
            }
        });
    }

    @Override
    public void onBackPressed(){
        gameback.performClick();
    }
}
