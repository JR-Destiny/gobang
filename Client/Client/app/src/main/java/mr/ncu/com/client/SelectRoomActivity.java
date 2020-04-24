package mr.ncu.com.client;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import mr.ncu.com.client.SQLite.DAO;
import mr.ncu.com.client.SQLite.DatabaseHelper;
import static mr.ncu.com.client.MainActivity.net;

public class SelectRoomActivity extends AppCompatActivity {

    public static Handler SelectRoomHandler;
    private List<RoomInfo> roomList = new ArrayList<RoomInfo>();
    public RoomListAdapter rmlistAdapter;
    private ListView rmListView;
    private String temp ;//接收传值
    private String rev;
    private String[] room;
    private int n; //特殊字符个数
    private String pixX;
    private String pixY;
    private SendMsgForm smf;
    DatabaseHelper databaseHelper;
    DAO playerdb ;
    int _id;
    String win;
    String total;
    String name;
    //定义手势管理器
    private GestureDetector gesture;

    public int countString(String str,String s) {
        int count = 0,len = str.length();
        while(str.indexOf(s) != -1) {
            str = str.substring(str.indexOf(s) + 1,str.length());
            count++;
        }
        return count;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_room);
        SelectRoomHandler= new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 0:
                        rev = msg.obj.toString();
                        String temp[] = rev.split("&");
                        ContentValues contentValues = new ContentValues();
                        contentValues.put("e_token", temp[1]);
                        contentValues.put("e_pixX", temp[2]);
                        contentValues.put("e_pixY", temp[3]);
                        contentValues.put("e_win", temp[4]);
                        contentValues.put("e_total", temp[5]);
                        contentValues.put("e_name", temp[6]);
                        playerdb.updatePlayerInfo(databaseHelper, _id, contentValues);
                        //跳到棋盘界面
                        Toast.makeText(SelectRoomActivity.this, "进入房间成功", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent();
                        intent.putExtra("room", "joiner");
                        intent.setClass(SelectRoomActivity.this, PlayerNetActivity.class);
                        startActivity(intent);
                        SelectRoomActivity.this.finish();
                        break;
                    case 1:
                        Toast.makeText(SelectRoomActivity.this, "该房间不存在，请下拉刷新", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        } ;

        smf = new SendMsgForm();

        databaseHelper = new DatabaseHelper(this.getApplicationContext(),"player.db",null,1);
        playerdb = new DAO();
        HashMap<String,Object> playermap = new HashMap<String, Object>();
        playermap = playerdb.getPlayerState(databaseHelper);
        _id = (int) playermap.get("_id");
        win = (String) playermap.get("win");
        total = (String) playermap.get("total");
        name = (String) playermap.get("name");

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        pixX = String.valueOf(metrics.widthPixels);
        pixY = String.valueOf(metrics.heightPixels);

        Intent intent = getIntent();
        temp = intent.getStringExtra("roomlist");
        room = temp.split("&");
        n = countString(temp,"&");
        for (int i = 1 ; i<= n ; i++){
            RoomInfo roomInfo = new RoomInfo();
            roomInfo.setRoom_Name(room[i]);
            roomList.add(roomInfo);
        }

        rmlistAdapter = new RoomListAdapter(SelectRoomActivity.this,roomList);
        rmListView = (ListView) findViewById(R.id.Room_List);
        rmListView.setAdapter(rmlistAdapter);

        rmListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String room_name = (String) rmlistAdapter.getItem(position);
                net.sendMsg(smf.joinRoom(room_name,pixX,pixY,win,total,name));
            }
        });


        gesture = new GestureDetector(this, new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                return false;
            }

            @Override
            public void onShowPress(MotionEvent e) {
            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return false;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                return false;
            }

            @Override
            public void onLongPress(MotionEvent e) {
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if(e1.getY() - e2.getY() < -50){
                    net.sendMsg(smf.queryRoom());
                    roomList.clear();
                    rmlistAdapter.refreshDataSet();
                    Toast.makeText(SelectRoomActivity.this, "刷新成功", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent e){
        return false;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        gesture.onTouchEvent(ev);
        return super.dispatchTouchEvent(ev);
    }
}
