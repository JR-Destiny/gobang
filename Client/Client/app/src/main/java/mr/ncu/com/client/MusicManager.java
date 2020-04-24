package mr.ncu.com.client;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;

public class MusicManager extends Service {
    private MediaPlayer mPlayer;
    private MyBinder mBinder = new MyBinder();

    @Override
    public IBinder onBind(Intent intent){
        return mBinder;
    }

    @Override
    public void onCreate(){
        super.onCreate();
        InitPlayer();
    }

    public class MyBinder extends Binder {
        public MusicManager getService()
        {
            return MusicManager.this;
        }

        public void Play(){
            mPlayer.setVolume(0.8f,0.8f);
            mPlayer.start();
        }
    }
    public void InitPlayer(){
        mPlayer = MediaPlayer.create(this,R.raw.bg_music);
        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mPlayer.setLooping(true);
    }
}
