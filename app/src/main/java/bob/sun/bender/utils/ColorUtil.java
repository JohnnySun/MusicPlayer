package bob.sun.bender.utils;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.graphics.Palette;

import java.text.DecimalFormat;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Johnny on 西暦17/04/11.
 */

@SuppressWarnings("unused")
public class ColorUtil {

    // 400
    public static final String COLOR_BLUE = "#42a5f5";
    public static final String COLOR_LIGHT_BLUE = "#29b6f6";
    public static final String COLOR_CYAN = "#26c6da";
    public static final String COLOR_TEAL = "#26a69a";
    public static final String COLOR_GREEN = "#66bb6a";
    public static final String COLOR_LIGHT_GREEN = "#9ccc65";
    public static final String COLOR_LIME = "#d4e157";
    public static final String COLOR_YELLOW = "#ffee58";
    public static final String COLOR_AMBER = "#ffca28";
    public static final String COLOR_ORANGE = "#ffa726";
    public static final String COLOR_DEEP_ORANGE = "#ff7043";

    /**
     * 根据步频返回对应的背景配色
     * @param steps 每分钟的步频
     * @return 返回的颜色 RGB表示
     */
    public static String getColorFromMinuteSteps(double steps) {
        if(steps > 160) {
            // 步频大于160，则直接返回深红色
            return COLOR_DEEP_ORANGE;
        }
        if (steps < 60) {
            // 步频小于60，直接返回蓝色
            return COLOR_BLUE;
        }

        double calc = (steps - 60) / 100.0f;
        DecimalFormat df = new DecimalFormat("#.#");

        switch (df.format(calc)) {
            case "0.0":
                return COLOR_BLUE;
            case "0.1":
                return COLOR_LIGHT_BLUE;
            case "0.2":
                return COLOR_CYAN;
            case "0.3":
                return COLOR_TEAL;
            case "0.4":
                return COLOR_GREEN;
            case "0.5":
                return COLOR_LIGHT_GREEN;
            case "0.6":
                return COLOR_LIME;
            case "0.7":
                return COLOR_YELLOW;
            case "0.8":
                return COLOR_AMBER;
            case "0.9":
                return COLOR_ORANGE;
            case "1.0":
                return COLOR_DEEP_ORANGE;
        }
        return COLOR_BLUE;
    }

    public static void getMainColor(final Bitmap bitmap, final GetColorCallback callback) {
        Observable.just(bitmap)
                .subscribeOn(Schedulers.io())
                .map(new Function<Bitmap, Integer>() {
                    @Override
                    public Integer apply(@NonNull Bitmap bitmap) {
                        try {
                            //首先获取一个Palette.Builder（稍后用到）
                            Palette.Builder b = new Palette.Builder(bitmap);
                            //设置好我们需要获取到多少种颜色
                            b.maximumColorCount(1);
                            //异步的进行颜色分析
                            Palette palette = b.generate();
                            Palette.Swatch swatch = palette.getSwatches().get(0);
                            bitmap.recycle();
                            return swatch.getRgb();
                        } catch (Exception e) {
                            return Color.parseColor(COLOR_BLUE);
                        }
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(@NonNull Integer integer) {
                        callback.onCallBack(integer);
                    }
                });
    }

    public interface GetColorCallback {
        void onCallBack(int color);
    }
}
