package mr.ncu.com.client;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import static mr.ncu.com.client.MainActivity.net;

public class RoomWayActivity extends AppCompatActivity {

    private ImageView Create_Room;
    private ImageView Join_Room;
    private SendMsgForm smf;
    private String rev = "null";
    public static Handler RoomHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_way);

        RoomHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                rev = msg.obj.toString();
                String temp[] = rev.split("&");
                switch (msg.what) {
                    case 0:
                        if (temp[1].equals("true")) {
                            Toast.makeText(RoomWayActivity.this, "创建房间成功", Toast.LENGTH_SHORT).show();
                            //跳到棋盘界面
                            Intent intent=new Intent(RoomWayActivity.this,PlayerNetActivity.class);
                            intent.putExtra("room","maker");
                            startActivity(intent);
                        } else {
                            Toast.makeText(RoomWayActivity.this, "创建房间失败", Toast.LENGTH_SHORT).show();
                        }
                    break;
                    case 1:
                        if (!temp[1].equals("null")) {
                            Intent intent = new Intent();
                            intent.putExtra("roomlist", rev);
                            intent.setClass(RoomWayActivity.this, SelectRoomActivity.class);
                            startActivity(intent);
                        }else {
                            Toast.makeText(RoomWayActivity.this, "当前无玩家创建房间", Toast.LENGTH_SHORT).show();
                        }
                        break;
                }
            }
        } ;

        smf = new SendMsgForm();
        Create_Room = (ImageView)findViewById(R.id.Create_Room);
        Join_Room = (ImageView)findViewById(R.id.Join_Room);

        Create_Room.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                net.sendMsg(smf.createRoom());
            }
        });
        Join_Room.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                net.sendMsg(smf.queryRoom());
            }
        });

    }
}
