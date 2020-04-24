package mr.ncu.com.client;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import static mr.ncu.com.client.MainActivity.net;

public class NetManager extends Service {
    private MyBinder mBinder = new MyBinder();
    private Thread rev;

    @Override
    public IBinder onBind(Intent intent){
        return mBinder;
    }

    @Override
    public void onCreate(){
        super.onCreate();
    }

    public class MyBinder extends Binder {
        public NetManager getService()
        {
            return NetManager.this;
        }

        public void receive(){
            rev = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        net.revMsg();
                    }
                }
            });
            rev.start();
        }
    }
    public void Init(){
        net = new Net(this);
        net.netConnect();
        try {
            net.netThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
