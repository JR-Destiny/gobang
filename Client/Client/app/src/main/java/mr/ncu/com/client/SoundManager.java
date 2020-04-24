package mr.ncu.com.client;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;

public class SoundManager extends Service {
    private MediaPlayer mPlayer;
    private MyBinder mBinder = new MyBinder();

    @Override
    public IBinder onBind(Intent intent){
        return mBinder;
    }

    @Override
    public void onCreate(){
        super.onCreate();
        InitSound();
    }

    public class MyBinder extends Binder {
        public SoundManager getService()
        {
            return SoundManager.this;
        }

        public void PlaySound(){
            mPlayer.setVolume(1.0f,1.0f);
            mPlayer.start();
        }

    }
    public void InitSound(){
        mPlayer = MediaPlayer.create(this,R.raw.down);
        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mPlayer.setLooping(false);
    }
}
