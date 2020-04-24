package mr.ncu.com.client;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import mr.ncu.com.client.SQLite.DAO;
import mr.ncu.com.client.SQLite.DatabaseHelper;

public class GameAIActivity extends AppCompatActivity {
    private GameChess panel;
    private AlertDialog.Builder builder;
    private Button regret,gameback;
    private TextView ai_myname;
    DatabaseHelper databaseHelper;
    DAO playerdb ;

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
        Log.d("执行","");
        setContentView(R.layout.activity_game_ai);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        regret=(Button)findViewById(R.id.btn_chessback);
        gameback=(Button)findViewById(R.id.btn_ret);
        panel = (GameChess)findViewById(R.id.main_panel);
        ai_myname = (TextView)findViewById(R.id.ai_myname) ;

        Intent MusicIntent = new Intent(this, SoundManager.class);
        bindService(MusicIntent, mServiceConnection, BIND_AUTO_CREATE);

        databaseHelper = new DatabaseHelper(this.getApplicationContext(),"player.db",null,1);
        playerdb = new DAO();
        ai_myname.setText((String) playerdb.getPlayerState(databaseHelper).get("name"));
    }

    public void AllListener() {
        panel.setOnGameListener(new GameChess.onGameListener() {
            @Override
            public void onGameOver(int i) {
                builder= new AlertDialog.Builder(GameAIActivity.this);
                builder.setTitle("游戏结束");
                builder.setNegativeButton("退出", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        GameAIActivity.this.finish();
                    }
                });
                builder.setPositiveButton("再来一局", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface interface1, int which) {

                        panel.restartGame();
                    }
                });
                String str = "";
                if (i== GameChess.WHITE_WIN) {
                    str = "白方胜利！";
                }else if (i== GameChess.BLACK_WIN) {
                    str = "黑方胜利！";
                }
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
        } );
        regret.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                panel.regret();
            }
        });

        gameback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder= new AlertDialog.Builder(GameAIActivity.this);
                builder.setTitle("离开游戏");
                builder.setNegativeButton("退出", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        GameAIActivity.this.finish();
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
}
