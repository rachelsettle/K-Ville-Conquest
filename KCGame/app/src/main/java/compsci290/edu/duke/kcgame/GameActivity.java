package compsci290.edu.duke.kcgame;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import compsci290.edu.duke.kcgame.util.SoundHelper;

public class GameActivity extends AppCompatActivity implements View.OnTouchListener{
    private ImageView tent;
    private ViewGroup mRootLayout;
    private float _xDelta;
    int width;
    private SoundHelper mSoundHelper;
    private Obstacle mObstacle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        mRootLayout = (ViewGroup) findViewById(R.id.root);
        tent = (ImageView) mRootLayout.findViewById(R.id.imageView);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(150, 150);
        tent.setLayoutParams(layoutParams);
        tent.setOnTouchListener(this);
        getWindow().setBackgroundDrawableResource(R.drawable.kvillebackground);

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int height = displaymetrics.heightPixels;
        width = displaymetrics.widthPixels;
        tent.getLayoutParams().height = height - 50;

        mSoundHelper = new SoundHelper();
        mSoundHelper.prepMusicPlayer(this);
        mSoundHelper.playMusic();

        mObstacle = new Obstacle(this,0xFFFF0000,100);
        mObstacle.releaseObstacle(height,10);
    }


    public boolean onTouch(View view, MotionEvent event) {

        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:

                _xDelta = view.getX() - event.getRawX();
                break;

            case MotionEvent.ACTION_MOVE:
                float newX =  event.getRawX() +_xDelta;
                if((newX <= 0 || newX >= width-view.getWidth())){
                    break;
                }
                view.animate()
                        .x(event.getRawX() + _xDelta)
                        .setDuration(0)
                        .start();
                break;
            default:
                return false;
        }
        return true;
    }
    protected void onDestroy(){
        mSoundHelper.pauseMusic();
        super.onDestroy();
    }
}
