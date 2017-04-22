package compsci290.edu.duke.kvc;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.io.IOException;
import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Environment;
import android.widget.Button;
import android.media.MediaPlayer;
import android.widget.Toast;
import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class VictoryScreech extends Activity{

    private static Button stopButton;
    private static Button playButton;
    private static Button doneButon;
    private static Button recordButton;
    private boolean isRecording = false;
    public static final int RequestPermissionCode = 1;
    private static String audioFilePath;
    private static MediaPlayer mediaPlayer;
    private static MediaRecorder mediaRecorder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.victory_screech_activity);
        recordButton = (Button) findViewById(R.id.record);
        stopButton = (Button) findViewById(R.id.stopRecording);
        playButton = (Button) findViewById(R.id.play);
        doneButon = (Button) findViewById(R.id.done);
        if (!hasMicrophone())
        {
            stopButton.setEnabled(false);
            playButton.setEnabled(false);
            recordButton.setEnabled(false);
            doneButon.setEnabled(false);
        } else {
            doneButon.setEnabled(false);
            playButton.setEnabled(false);
            stopButton.setEnabled(false);
        }
        audioFilePath =
                Environment.getExternalStorageDirectory().getAbsolutePath()
                        + "/myaudio.3gp";
        requestPermission();
    }
    public void recordAudio (View view) throws IOException {
        final String LOG_TAG = "AudioRecordTest";
        isRecording = true;
        stopButton.setEnabled(true);
        playButton.setEnabled(false);
        recordButton.setEnabled(false);
        // Verify that the device has a mic first
        if (hasMicrophone()) {
            // Set the file location for the audio
            // Create the recorder
            mediaRecorder = new MediaRecorder();
            Log.d("TAG","Media Record is made");
            Log.d("mediaNullMade", (mediaRecorder == null) + "");
            // Set the audio format and encoder
            mediaRecorder.reset();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            Log.d("TAG","Set Audio Source");
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            Log.d("TAG","Set Output Format");
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            Log.d("TAG","Set Audio Encoder");
            // Setup the output location
            mediaRecorder.setOutputFile(audioFilePath);
            Log.d("TAG","Set Output File");
            // Start the recording
            try {
                mediaRecorder.prepare();
                mediaRecorder.start();
            } catch (IOException e) {
                Log.e(LOG_TAG, "prepare() failed");
            }
        } else { // no mic on device
            Toast.makeText(this, "This device doesn't have a mic!", Toast.LENGTH_LONG).show();
        }
    }
    public void stopClicked (View view)
    {
        Log.d("mediaNullStop", (mediaRecorder == null) + "");
        stopButton.setEnabled(false);
        playButton.setEnabled(true);
        if (isRecording)
        {
            recordButton.setEnabled(false);
            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder = null;
            isRecording = false;
        } else {
            mediaPlayer.release();
            mediaPlayer = null;
            recordButton.setEnabled(true);
        }
    }
    public void playAudio (View view) throws IOException
    {
        playButton.setEnabled(true);
        recordButton.setEnabled(true);
        stopButton.setEnabled(false);
        doneButon.setEnabled(true);
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setDataSource(audioFilePath);
        mediaPlayer.prepare();
        mediaPlayer.start();
    }

    public static void screech() throws IOException
    {
        mediaPlayer.start();
    }

    public void doneRecord (View view){
        doneButon.setEnabled(false);
        Intent i = new Intent(VictoryScreech.this, CharacterSelectScreen.class);
        startActivity(i);
    }
    protected boolean hasMicrophone() {
        PackageManager pManager = this.getPackageManager();
        return pManager.hasSystemFeature(
                PackageManager.FEATURE_MICROPHONE);
    }
    private void requestPermission() {
        ActivityCompat.requestPermissions(VictoryScreech.this, new
                String[]{WRITE_EXTERNAL_STORAGE, RECORD_AUDIO}, RequestPermissionCode);
    }
}
