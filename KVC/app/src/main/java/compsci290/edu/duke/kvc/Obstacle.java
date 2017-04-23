package compsci290.edu.duke.kvc;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.Toast;

import compsci290.edu.duke.kvc.util.PixelHelper;

/**
 * Created by grantbesner on 4/20/17.
 */

public class Obstacle extends android.support.v7.widget.AppCompatImageView implements Animator.AnimatorListener, ValueAnimator.AnimatorUpdateListener {
    private ValueAnimator mAnimator;
    private ObstacleListener mObstacleListener;
    private boolean mHit = false;
    private String mObstacleType;
    private ImageView tent;

    public Obstacle(Context context) {
        super(context);
        mObstacleListener = (ObstacleListener) context;
    }

    public Obstacle(Context context, String obstacleType, int rawHeight, ImageView tent) {
        super(context);
        mObstacleListener = (ObstacleListener) context;
        this.mObstacleType = obstacleType;
        this.tent = tent;

        //Gets resource ID from file name String
        Resources resources = context.getResources();
        final int obstacleTypeInt = resources.getIdentifier(obstacleType, "drawable",
                context.getPackageName());
        this.setImageResource(obstacleTypeInt);

        //Sets params

        int rawWidth = rawHeight;
        int dpHeight = (PixelHelper.pixelsToDp(rawHeight, context))/3;
        int dpWidth = (PixelHelper.pixelsToDp(rawWidth, context))/3;
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(dpWidth,dpHeight);
        setLayoutParams(params);
    }

    public void releaseObstacle(int screenHeight, int duration){
        mAnimator = new ValueAnimator();
        mAnimator.setDuration(duration);
        mAnimator.setFloatValues(0f, screenHeight);
        mAnimator.setInterpolator(new LinearInterpolator());
        mAnimator.setTarget(this);
        mAnimator.addListener(this);
        mAnimator.addUpdateListener(this);
        mAnimator.start();
    }


    @Override
    public void onAnimationStart(Animator animation) {

    }

    @Override
    public void onAnimationEnd(Animator animation) {

    }

    @Override
    public void onAnimationCancel(Animator animation) {

    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        setY((Float) animation.getAnimatedValue());
        if(collide(tent,this)){
            mObstacleListener.didCollide(this,true);
            mHit = true;
            mAnimator.cancel();
        }

    }


    //OnTouchEvent to handle what to do on collisions
    public boolean onTouchEvent(MotionEvent event){
    if(!mHit && event.getAction()==MotionEvent.ACTION_DOWN){
      }
     return super.onTouchEvent(event);
     }

    public interface ObstacleListener{
        void didCollide(Obstacle obstacle, boolean userTouch);
    }

    public boolean collide(ImageView tent, Obstacle obstacle) {

        // Location holder
        final int[] loc = new int[2];

        tent.getLocationInWindow(loc);
        final Rect rc1 = new Rect(loc[0], loc[1],
                loc[0] + tent.getWidth(), 300);
        obstacle.getLocationInWindow(loc);
        Rect rc2 = new Rect(loc[0], loc[1],
                loc[0] + obstacle.getWidth(), loc[1] + obstacle.getHeight());

        Log.d("rect1Left", rc1.left + "");
        Log.d("rect1Right", rc1.right + "");
        Log.d("rect1Top", rc1.top + "");
        Log.d("rect1Bottom", rc1.bottom + "");

        Log.d("rect2Left", rc2.left + "");
        Log.d("rect2Right", rc2.right + "");
        Log.d("rect2Top", rc2.top + "");
        Log.d("rect2Bottom", rc2.bottom + "");

        if (Rect.intersects(rc1, rc2)) {
            return true;
        }
        return false;
    }

    public String getType(){
        return mObstacleType ;
    }
}
