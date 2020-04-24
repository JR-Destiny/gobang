package mr.ncu.com.client;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import mr.ncu.com.client.SQLite.DAO;
import mr.ncu.com.client.SQLite.DatabaseHelper;

import static mr.ncu.com.client.MainActivity.net;
public class PlaynetChess extends GameChess implements Runnable{

    private String rev = "null";
    public static Handler NetChessHandler;
    private SendMsgForm smf;
    private int revX;
    private int revY;
    DatabaseHelper databaseHelper;
    DAO playerdb ;

    private boolean whiteWin=false;
    private boolean isGemOver=false;        //游戏结束

    private boolean isWhite = true;  //判断是否是白棋先手，或当前为白棋下子
    private List<Point> myWhiteArray = new ArrayList<Point>();  //白棋子位置信息
    private List<Point> myBlackArray = new ArrayList<Point>();  //黑棋子位置信息

    private Handler mHandlerUI ;
    private onGameListener onGameListener;  //回调接口
    private boolean isUserBout=false;
    private static SoundManager net_service = null;
    private static SoundManager.MyBinder net_manager = null;
    public PlaynetChess(Context context) {
        this(context, null);
    }
    public PlaynetChess(Context context ,AttributeSet attributeSet){            //构造函数
        super(context , attributeSet);
        NetChessHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                rev = msg.obj.toString();
                String temp[] = rev.split("&");
                revX = Integer.parseInt(temp[1]);
                revY = Integer.parseInt(temp[2]);
                Point p=new Point();
                p.x=revX;
                p.y=revY;
                DrawEnemyChess(p);
                isUserBout = true;
            }
        } ;
        smf = new SendMsgForm();
        databaseHelper = new DatabaseHelper(context,"player.db",null,1);
        playerdb = new DAO();
        HashMap<String,Object> playermap = new HashMap<String, Object>();
        playermap = playerdb.getPlayerState(databaseHelper);

        mHandlerUI=new Handler() {
            public void handleMessage(Message msg) {
                if (isGemOver) {
                    Log.i("run",""+isGemOver);
                    if (onGameListener != null) {
                        onGameListener.onGameOver(whiteWin ? 1 : 0);
                    }
                }
            }
        };
    }

    // 用于回调的接口
    public interface onGameListener {
        void onGameOver(int i);
    }
    //自定义接口，用于显示dialog
    public void setOnGameListener(PlaynetChess.onGameListener onGameListener) {
        this.onGameListener = onGameListener;
    }


    @Override
    public void run() {
        isGemOver=super.isGemOver();
        if (isGemOver) {
            whiteWin=super.isWhitewin();
            Message msg = new Message();
            mHandlerUI.sendMessage(msg);
        }
    }

    protected void  onDraw(Canvas canvas) {
        super.onDraw(canvas);
        new Thread(this).start();
    }

    public boolean onTouchEvent(MotionEvent event){
        if (isGemOver||!isUserBout) {
            return false;
        }
        int action = event.getAction();
        if (action == MotionEvent.ACTION_UP) {
            int x = (int) event.getX();
            int y = (int) event.getY();
            Point p = super.getVaLidPoint(x,y);

            if (myWhiteArray.contains(p)|| myBlackArray.contains(p)) {
                return false;
            }
            if (isWhite) {
                myWhiteArray.add(p);
                super.setMyWhiteArray(myWhiteArray);
                net.sendMsg(smf.playChess((String) playerdb.getPlayerState(databaseHelper).get("e_token"),String.valueOf(p.x),String.valueOf(p.y)));
                isUserBout = false;
            }else {
                myBlackArray.add(p);
                super.setMyBlackArray(myBlackArray);
                net.sendMsg(smf.playChess((String) playerdb.getPlayerState(databaseHelper).get("e_token"),String.valueOf(p.x),String.valueOf(p.y)));
                isUserBout = false;
            }
            net_manager.PlaySound();
            super.invalidate();         //invalidate()是用来刷新View的，必须在UI线程中使用
        }
        return true;
    }

    public void SetMusicManager(SoundManager.MyBinder mManager){
        net_manager = mManager;
        net_service = net_manager.getService();
        net_service.InitSound();
    }

    public void DrawEnemyChess(Point p)
    {
        if(isWhite) {
            myBlackArray.add(p);
            super.setMyBlackArray(myBlackArray);
        }else {
            myWhiteArray.add(p);
            super.setMyWhiteArray(myWhiteArray);
        }
        net_manager.PlaySound();
        super.invalidate();
    }

    public boolean isWhite() {
        return isWhite;
    }

    public void setWhite(boolean white) {
        isWhite = white;
    }

    public void setUserBout(boolean userBout) {
        isUserBout = userBout;
    }

}

