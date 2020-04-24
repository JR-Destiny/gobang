package mr.ncu.com.client;

import android.content.Context;
import android.os.Message;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;

import static mr.ncu.com.client.LoginActivity.LoginHandler;
import static mr.ncu.com.client.MainActivity.connect_flag;
import static mr.ncu.com.client.RegisterActivity.RegisterHandler;
import static mr.ncu.com.client.RoomWayActivity.RoomHandler;
import static mr.ncu.com.client.SelectRoomActivity.SelectRoomHandler;
import static mr.ncu.com.client.PlayerNetActivity.PlayerNetHandler;
import static mr.ncu.com.client.PlaynetChess.NetChessHandler;

public class Net {
    private Context context;
    public Socket socket;
    private OutputStream os;
    private OutputStreamWriter osr;
    private BufferedWriter bw;
    public  String msg = null;
    public Thread netThread;
    public Thread sendThread;
    public Thread revThread;
    private String line = null;

    Net(Context con){
        this.context = con;
    }

    public void netConnect(){
        netThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    InetSocketAddress socketAddress = new InetSocketAddress("182.92.0.242", Integer.parseInt("8899"));
                    //socket = new Socket("182.92.0.242", 8899);
                    socket = new Socket();
                    socket.connect(socketAddress,1000);
                    if(socket.isConnected())
                    {
                        socket.setSoTimeout(1 * 1000);
                        //建立连接后发送手机的唯一标识
                        os = socket.getOutputStream();
                        osr = new OutputStreamWriter(os);
                        bw = new BufferedWriter(osr);
                        bw.write((DeviceIdUtil.getDeviceId(context) + "\n"));
                        bw.flush();
                    }
                } catch (Exception e) {
                    connect_flag = false;
                }
            }
        });
        netThread.start();
    }

    public void disConnect(){
        try {
            if (socket!=null) {
                socket.shutdownInput();
                socket.shutdownOutput();
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMsg(final String msg){
        sendThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // 步骤1：从Socket 获得输出流对象OutputStream
                    // 该对象作用：发送数据
                    os = socket.getOutputStream();
                    osr = new OutputStreamWriter(os);
                    bw = new BufferedWriter(osr);
                    // 步骤2：写入需要发送的数据到输出流对象中
                    bw.write(msg+"\n");
                    bw.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        sendThread.start();
    }

    public void revMsg(){
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(), "utf-8"));
            if ((line = br.readLine()) != null) {
                String[] temp = line.split("&");
                Message msg = new Message();
                switch (temp[0]){
                    case SendMsgForm.CreateRoom:    //false or true
                        msg.what = 0;
                        msg.obj = line;
                        RoomHandler.sendMessage(msg);
                        break;
                    case SendMsgForm.QueryRoom:     //房间名 & 房间名 &...
                        msg.what = 1;
                        msg.obj = line;
                        RoomHandler.sendMessage(msg);
                        break;
                    case SendMsgForm.JoinRoom:      //false or token & 像素X & 像素Y & win & total & name
                        //发到棋盘Activity的handler
                        if (temp[1].equals("false")){
                            msg.what = 1;
                            msg.obj = line;
                            SelectRoomHandler.sendMessage(msg);
                        }else {
                            msg.what = 0;
                            msg.obj = line;
                            PlayerNetHandler.sendMessage(msg);
                        }
                        break;
                    case SendMsgForm.CreaterPix:    //token & 像素X & 像素Y & win & total & name
                        msg.what = 0;
                        msg.obj = line;
                        SelectRoomHandler.sendMessage(msg);
                        break;
                    case SendMsgForm.PlayChess:     //坐标X & 坐标Y
                        msg.what = 0;
                        msg.obj = line;
                        NetChessHandler.sendMessage(msg);
                        break;
                    case SendMsgForm.LeaveGame:   //true
                        //发到棋盘Activity的handler
                        msg.what = 2;
                        msg.obj = line;
                        PlayerNetHandler.sendMessage(msg);
                        break;
                    case SendMsgForm.WinGame:       //胜场 & 总场
                        //发到棋盘Activity的handler
                        msg.what = 1;
                        msg.obj = line;
                        PlayerNetHandler.sendMessage(msg);
                        break;
                    case SendMsgForm.LoseGame:      //胜场 & 总场
                        //发到棋盘Activity的handler
                        msg.what = 1;
                        msg.obj = line;
                        PlayerNetHandler.sendMessage(msg);
                        break;
                    case SendMsgForm.PlayAgain:
                        msg.what = 3;
                        msg.obj = line;
                        PlayerNetHandler.sendMessage(msg);
                        break;
                    case SendMsgForm.AgreeOrRefuse:
                        msg.what = 4;
                        msg.obj = line;
                        PlayerNetHandler.sendMessage(msg);
                        break;
                    case SendMsgForm.CreatePlayer:  //"注册的用户名已存在" or "注册成功" or "注册失败"
                        msg.what = 0;
                        msg.obj = line;
                        RegisterHandler.sendMessage(msg);
                        break;
                    case SendMsgForm.PlayerLogin:   //胜场 & 总场 or false
                        msg.what = 0;
                        msg.obj = line;
                        LoginHandler.sendMessage(msg);
                        break;
                    default:
                        break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
