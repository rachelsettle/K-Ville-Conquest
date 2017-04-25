package compsci290.edu.duke.kvc.util;

import android.content.Context;
import android.media.MediaPlayer;

import compsci290.edu.duke.kvc.R;


/**
 * Created by grantbesner on 4/17/17.
 */

public class SoundHelper {
    private MediaPlayer mMusicPlayer;
    public float left;
    public float right;

    public SoundHelper() {
    }

    public void prepMusicPlayer(Context context){
        //Set song
        mMusicPlayer = MediaPlayer.create(context.getApplicationContext(),
                R.raw.cascada);
        //Change the volume from settings.
        left = 1f;
        right = 1f;
        mMusicPlayer.setVolume(0.5f,0.5f);
        mMusicPlayer.setLooping(true);
    }

    public void playMusic(){
        if(mMusicPlayer!=null){
            //play music
            mMusicPlayer.start();
        }
    }

    public void pauseMusic(){
        if(mMusicPlayer!=null && mMusicPlayer.isPlaying()){
            mMusicPlayer.pause();
        }
    }

    public void stopMusic(){
        if(mMusicPlayer!=null && mMusicPlayer.isPlaying()){
            mMusicPlayer.stop();
        }
    }
}
