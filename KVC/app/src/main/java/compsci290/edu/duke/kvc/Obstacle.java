package compsci290.edu.duke.kvc;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;

import compsci290.edu.duke.kvc.util.PixelHelper;

/**
 * Created by grantbesner on 4/20/17.
 */

public class Obstacle extends android.support.v7.widget.AppCompatImageView implements Animator.AnimatorListener, ValueAnimator.AnimatorUpdateListener {
    private ValueAnimator mAnimator;
    private ObstacleListener mObstacleListener;
    private boolean mHit;
    private String mObstacleType;

    public Obstacle(Context context) {
        super(context);
        mObstacleListener = (ObstacleListener) context;
    }

    public Obstacle(Context context, String obstacleType, int rawHeight) {
        super(context);
        this.mObstacleType = obstacleType;

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
        // if(!mHit){
        //     mObstacleListener.collision(this,false);
        // }
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
    }

    //OnTouchEvent to handle what to do on collisions
    //public boolean onTouchEvent(MotionEvent event){
    //if(!mHit && event.getAction()==MotionEvent.ACTION_DOWN){
    //  mObstacleListener.collision(this,true);
    //   mHit = true;
    //  mAnimator.cancel();
    //  }
    // return super.onTouchEvent(event);
    // }

    public interface ObstacleListener{
        void collision(Obstacle obstacle, boolean userTouch);

    }

    public String getType(){
        return mObstacleType ;
    }
}
