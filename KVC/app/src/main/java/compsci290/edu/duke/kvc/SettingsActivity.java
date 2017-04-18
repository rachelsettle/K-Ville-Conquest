package compsci290.edu.duke.kvc;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

public class SettingsActivity extends AppCompatActivity {
    private SeekBar volumeSeekbar = null;
    private AudioManager audioManager = null;
    private SeekBar difficultySeekbar = null;
    private TextView diffText = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        setContentView(R.layout.settings_activity_main);
        MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.formation_sound_file); //Used the check seekBarVolume functionality
        mediaPlayer.start(); // no need to call prepare(); create() does that for you
        initControls();
    }
    public void initControls(){ // CODE BORROWED FROM http://stackoverflow.com/questions/10134338/using-seekbar-to-control-volume-in-android
        try
        {
            diffText = (TextView) findViewById(R.id.diffDescription);
            volumeSeekbar = (SeekBar)findViewById(R.id.seekBarVolume);
            difficultySeekbar = (SeekBar) findViewById(R.id.DifficultySeekBar);
            diffText.setText("Easy");
            audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            volumeSeekbar.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
            volumeSeekbar.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
            volumeSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
            {
                @Override
                public void onStopTrackingTouch(SeekBar arg0) {} // overridden bc no notification is necessary
                @Override
                public void onStartTrackingTouch(SeekBar arg0) {} // overridden bc no notification is necessary
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean arg2)
                {
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
                    Log.d("Vol", "onProgress_Vol is being used!");
                }
            });
            difficultySeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
            {
                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {}
                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {}
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    Log.d("Diff", "progress is" + progress);
                    if (progress == 0){
                        diffText.setText("Easy");
                    }
                    else if (progress == 1){
                        diffText.setText("Medium");
                    }
                    else if (progress == 2){
                        diffText.setText("Hard");
                    }
                }
            });
        }
        catch (Exception e) // need help handling the proper exception
        {
            e.printStackTrace();
        }
    }
    public void siteLink(View view){
        goToURL("http://rachelsettle.iss240.net/kville.html");
    }
    private void goToURL(String URL){
        Uri uriURL = Uri.parse(URL);
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriURL);
        startActivity(launchBrowser);
    }
}
