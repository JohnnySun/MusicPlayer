package bob.sun.bender.utils;

import java.text.DecimalFormat;

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
}
