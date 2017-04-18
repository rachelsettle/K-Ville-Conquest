package compsci290.edu.duke.kcgame;

/**
 * Created by grantbesner on 4/15/17.
 */

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.Log;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import compsci290.edu.duke.kcgame.util.PixelHelper;

public class Obstacle extends ImageView
        implements Animator.AnimatorListener, ValueAnimator.AnimatorUpdateListener {

    private ValueAnimator mAnimator;

    public Obstacle(Context context) {
        super(context);
    }

    public Obstacle(Context context, int color, int rawHeight) {
        super(context);
        Log.d("Tag","created obstacle");

        this.setImageResource(R.drawable.tent);
        this.setColorFilter(color);

        int rawWidth = rawHeight;

        int dpHeight = PixelHelper.pixelsToDp(rawHeight, context);
        int dpWidth = PixelHelper.pixelsToDp(rawWidth, context);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(dpWidth, dpHeight);
        setLayoutParams(params);

    }

    public void releaseObstacle(int screenHeight, int duration) {
        Log.d("TAG","released obstacle");
        mAnimator = new ValueAnimator();
        mAnimator.setDuration(duration);
        mAnimator.setFloatValues(screenHeight, 0f);
        mAnimator.setInterpolator(new LinearInterpolator());
        mAnimator.setTarget(this);
        mAnimator.addListener(this);
        mAnimator.addUpdateListener(this);
        mAnimator.start();
    }


    @Override
    public void onAnimationStart(Animator animator) {

    }

    @Override
    public void onAnimationEnd(Animator animator) {

    }

    @Override
    public void onAnimationCancel(Animator animator) {

    }

    @Override
    public void onAnimationRepeat(Animator animator) {

    }

    @Override
    public void onAnimationUpdate(ValueAnimator valueAnimator) {

    }
}
