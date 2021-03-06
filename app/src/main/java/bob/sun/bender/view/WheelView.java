package bob.sun.bender.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Region;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import bob.sun.bender.R;
import bob.sun.bender.controller.OnButtonListener;
import bob.sun.bender.controller.OnTickListener;
import bob.sun.bender.utils.VibrateUtil;

/**
 * Created by bob.sun on 2015/4/23.
 */
public class WheelView extends View {

    public enum RipplePoint {
        Top,
        Bottom,
        Left,
        Right,
    }

    private Point center;
    private Path viewBound;
    private int radiusOut,radiusIn;
    private Paint paintOut, paintIn, ripplePaint;
    private OnTickListener onTickListener;
    private float startDeg = Float.NaN;
    private OnButtonListener onButtonListener;
    private Point ripplePoint;
    private float buttonWidth, buttonHeight;

    private boolean animating;
    private long timer;
    private static final int duration = 350, frameRate = 1;
    private float maxRadius = 1000;
    private Runnable runnable;
    // Used to interpolate material ripple effect.
    private FastOutSlowInInterpolator fosInterpolator;
    private View mView;

    public WheelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        center = new Point();
        paintIn = new Paint();
        paintOut = new Paint();
        ripplePaint = new Paint();
        int rippleColor = 0;

        paintOut.setColor(getResources().getColor(android.R.color.white));
        paintIn.setColor(getContext().getResources().getColor(R.color.colorPrimary));
        paintOut.setAntiAlias(true);
        paintIn.setAntiAlias(true);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            rippleColor = getContext().getResources().getColor(R.color.colorPrimary, null);
        } else {
            rippleColor = getContext().getResources().getColor(R.color.colorPrimary);
        }
        ripplePaint.setColor(rippleColor);
        ripplePaint.setAlpha(80);
        paintOut.setShadowLayer(8f, 0.0f, 8f,
                Color.GRAY);

        buttonWidth = getResources().getDimensionPixelSize(R.dimen.button_width);
        buttonHeight = getResources().getDimensionPixelSize(R.dimen.button_height);
        animating = false;
        fosInterpolator = new FastOutSlowInInterpolator();
        mView = this;
        runnable = new Runnable() {
            @Override
            public void run() {
                invalidate();
            }
        };
    }

    public void setColor(int color) {
        paintOut.setColor(getResources().getColor(android.R.color.white));
        paintIn.setColor(color);
        paintOut.setAntiAlias(true);
        paintIn.setAntiAlias(true);
        ripplePaint.setColor(color);
        ripplePaint.setAlpha(80);
        paintOut.setShadowLayer(8f, 0.0f, 8f,
                Color.GRAY);
        invalidate();
    }

    @Override
    protected void onMeasure(int measureWidthSpec,int measureHeightSpec){
        super.onMeasure(measureWidthSpec,measureHeightSpec);
        int measuredHeight = measureHeight(measureHeightSpec);
        int measuredWidth = measuredHeight;
        this.setMeasuredDimension(measuredWidth,measuredHeight);
        radiusOut = (measuredHeight - 20)/ 2;
        radiusIn = radiusOut/3;
        center.x = measuredWidth/2;
        center.y = measuredHeight/2;
        setLayerType(LAYER_TYPE_SOFTWARE, null);

        maxRadius = radiusOut * 2f;
        viewBound = new Path();
        viewBound.addCircle(center.x, center.y, radiusOut, Path.Direction.CW);
    }

    @Override
    public void onDraw(Canvas canvas){
        canvas.drawCircle(center.x,center.y,radiusOut,paintOut);
        canvas.save();
        if (viewBound != null)
            canvas.clipPath(viewBound, Region.Op.REPLACE);

        if (animating) {
            if (System.currentTimeMillis() >= timer + duration) {
                getHandler().removeCallbacks(runnable);
                animating = false;
            } else {
                //postDelayed(runnable, (long) frameRate);
                float progress = (System.currentTimeMillis() - timer) / (float) duration;
                canvas.drawCircle(ripplePoint.x, ripplePoint.y,
                        fosInterpolator.getInterpolation(progress) * maxRadius + buttonWidth,
                        ripplePaint);
                canvas.drawCircle(center.x,center.y,radiusIn,paintIn);

                if (Build.VERSION.SDK_INT != 23)
                    canvas.restore();
                invalidate();
            }
        } else {
            getHandler().removeCallbacks(runnable);
        }

        canvas.drawCircle(center.x,center.y,radiusIn,paintIn);

        if (Build.VERSION.SDK_INT != 23)
            canvas.restore();
    }

    private float xyToDegrees(float x, float y) {
        float distanceFromCenter = PointF.length((x - 0.5f), (y - 0.5f));
        if (distanceFromCenter < 0.15f
                || distanceFromCenter > 0.5f) { // ignore center and out of bounds events
            return Float.NaN;
        } else {
            return (float) Math.toDegrees(Math.atan2(x - 0.5f, y - 0.5f));
        }
    }


    private int measureWidth(int measureSpec) {
       int specMode = MeasureSpec.getMode(measureSpec);
       int specSize = MeasureSpec.getSize(measureSpec);
       int result = 0;
       if (specMode == MeasureSpec.AT_MOST) {
               result = getWidth();
           } else if (specMode == MeasureSpec.EXACTLY) {
               result = specSize;
           }
       return result;
    }
    private int measureHeight(int measureSpec) {
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        int result = 0;
        if (specMode == MeasureSpec.AT_MOST) {
            result = getHeight();
            if (result == 0)
                result = specSize;
        } else if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        }
        return result;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                float x = event.getX() / (center.x * 2);
                float y = event.getY() / (center.y * 2);

                startDeg = xyToDegrees(x, y);
                return true;

            case MotionEvent.ACTION_MOVE:
                if (!Float.isNaN(startDeg)) {
                    float currentDeg = xyToDegrees((event.getX() - (getWidth() - getHeight())/2) / ((float) getHeight()),
                            event.getY() / getHeight());

                    if (!Float.isNaN(currentDeg)) {
                        float degPerTick = 72f;
                        float deltaDeg = startDeg - currentDeg;
                        if(Math.abs(deltaDeg) < 72f){
                            return true;
                        }
                        int ticks = (int) (Math.signum(deltaDeg)
                                * Math.floor(Math.abs(deltaDeg) / degPerTick));
                        if(ticks == 1){
                            startDeg = currentDeg;
                            if(onTickListener !=null)
                                onTickListener.onNextTick();
                            VibrateUtil.getStaticInstance(null).TickVibrate();
                        }
                        if(ticks == -1){
                            startDeg = currentDeg;
                            if(onTickListener !=null)
                                onTickListener.onPreviousTick();
                            VibrateUtil.getStaticInstance(null).TickVibrate();
                        }
                    }
                    startDeg = currentDeg;
                    return true;
                } else {
                    return false;
                }

            case MotionEvent.ACTION_UP:
                if ((Math.pow(event.getX() - getWidth() / 2f,2) + Math.pow(event.getY() - getHeight() / 2f,2) <= radiusIn*radiusIn )){
                    if(onButtonListener !=null)
                        onButtonListener.onSelect();
                    return true;
                }
                //TODO
                return true;
            default:
                return super.onTouchEvent(event);
        }
    }

    public void setOnTickListener(OnTickListener listener){
        this.onTickListener = listener;
    }

    public void setOnButtonListener(OnButtonListener listener){
        this.onButtonListener = listener;
    }

    public void rippleFrom(RipplePoint point) {
        if (animating)
            return;
        animating = false;
        switch (point) {
            case Top:
                ripplePoint = new Point(getWidth() / 2, (int) (buttonHeight / 2));
                break;
            case Bottom:
                ripplePoint = new Point(getWidth() / 2, (int) (getHeight() - (buttonHeight / 2)));
                break;
            case Left:
                ripplePoint = new Point((int) ((getWidth() - radiusOut - buttonWidth) / 2), getHeight() / 2);
                break;
            case Right:
                ripplePoint = new Point((int) ((getWidth() + radiusOut + buttonWidth) / 2), getHeight() / 2);
                break;
        }
        timer = System.currentTimeMillis();
        animating = true;
        invalidate();
    }

    @Override
    public void setElevation(float elevation) {
        if (mView != null) {
            ViewCompat.setElevation(mView, elevation);
        } else {
            super.setElevation(elevation);
        }
    }}
