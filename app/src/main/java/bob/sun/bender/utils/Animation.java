package bob.sun.bender.utils;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.view.View;

/**
 * Created by bmy001 on 西暦17/04/25.
 */

public class Animation {

    public static void startColorAnimation(final View view, final AnimationUpdateCallback callback,
                                    int startColor, int endColor) {
        //创建动画,这里的关键就是使用ArgbEvaluator, 后面2个参数就是 开始的颜色,和结束的颜色.
        ValueAnimator colorAnimator = ValueAnimator.ofObject(
                new ArgbEvaluator(),startColor, endColor);
        colorAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int color = (int) animation.getAnimatedValue();
                view.setBackgroundColor(color);
                callback.onAnimationUpdate(color);
            }
        });
        colorAnimator.setDuration(700);
        colorAnimator.start();
    }

    public interface AnimationUpdateCallback {
        void onAnimationUpdate(Object... params);
    }

}
