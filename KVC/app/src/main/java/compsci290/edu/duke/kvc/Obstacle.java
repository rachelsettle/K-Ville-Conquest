package compsci290.edu.duke.kvc;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.media.Image;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
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
        int tentX = loc[0];
        int tentY = loc[1];
       // final Rect rc1 = new Rect(loc[0], loc[1],
         //       loc[0] + tent.getWidth(), 300);
        obstacle.getLocationInWindow(loc);
        int obstacleX = loc[0];
        int obstacleY = loc[1];

        View obstacleView = (View) obstacle;
        //Rect rc2 = new Rect(loc[0], loc[1],
          //      loc[0] + obstacle.getWidth(), loc[1] + obstacle.getHeight());
        return isCollisionDetected(tent,tentX,tentY,obstacleView,obstacleX,obstacleY);
    }

    public static boolean isCollisionDetected(ImageView tent, int x1, int y1,
                                              View obstacle, int x2, int y2) {

        Bitmap bitmap1 = getViewBitmap(tent);
        Bitmap bitmap2 = getViewBitmap(obstacle);

        if (bitmap1 == null || bitmap2 == null) {
            throw new IllegalArgumentException("bitmaps cannot be null");
        }

        Rect bounds1 = new Rect(x1, y1, x1 + bitmap1.getWidth(), y1 + bitmap1.getHeight());
        Rect bounds2 = new Rect(x2, y2, x2 + bitmap2.getWidth(), y2 + bitmap2.getHeight());

        if (Rect.intersects(bounds1, bounds2)) {
            Rect collisionBounds = getCollisionBounds(bounds1, bounds2);
            for (int i = collisionBounds.left; i < collisionBounds.right; i++) {
                for (int j = collisionBounds.top; j < collisionBounds.bottom; j++) {
                    int bitmap1Pixel = bitmap1.getPixel(i - x1, j - y1);
                    int bitmap2Pixel = bitmap2.getPixel(i - x2, j - y2);
                    if (isFilled(bitmap1Pixel) && isFilled(bitmap2Pixel)) {
                        bitmap1.recycle();
                        bitmap1 = null;
                        bitmap2.recycle();
                        bitmap2 = null;
                        return true;
                    }
                }
            }
        }
        bitmap1.recycle();
        bitmap1 = null;
        bitmap2.recycle();
        bitmap2 = null;
        return false;
    }

    private static Bitmap getViewBitmap(View v) {
        if (v.getMeasuredHeight() <= 0) {
            int specWidth = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            v.measure(specWidth, specWidth);
            Bitmap b = Bitmap.createBitmap(v.getLayoutParams().width, v.getLayoutParams().height, Bitmap.Config.ARGB_8888);
            Canvas c = new Canvas(b);
            v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
            v.draw(c);
            return b;
        }
        Bitmap b = Bitmap.createBitmap(v.getLayoutParams().width, v.getLayoutParams().height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.layout(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
        v.draw(c);
        return b;
    }

    private static Rect getCollisionBounds(Rect rect1, Rect rect2) {
        int left = Math.max(rect1.left, rect2.left);
        int top = Math.max(rect1.top, rect2.top);
        int right = Math.min(rect1.right, rect2.right);
        int bottom = Math.min(rect1.bottom, rect2.bottom);
        return new Rect(left, top, right, bottom);
    }

    private static boolean isFilled(int pixel) {
        return pixel != Color.TRANSPARENT;
    }

    public String getType(){
        return mObstacleType ;
    }

    public int getPoints(){
        int points = 0;
        switch (this.getType()){
            case "cup":
                points++;
                break;
            case "pizza_obstacle":
                points=points+3;
                break;
            case "pillow_obstacle":
                points=points+5;
                break;
            case "horn_obstacle":
                points=points-1;
                break;
            case "mud_obstacle":
                points=points-3;
                break;
            case "puddle_obstacle":
                points=points-5;
                break;
            case "uncfan_obstacle":
                points=points-10;
                break;
        }
        return points;
    }
}
