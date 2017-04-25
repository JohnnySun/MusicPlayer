package bob.sun.bender.model;

/**
 * Created by bmy001 on 西暦17/04/17.
 */

public class StepRepo {

    private static boolean debugger;
    private static int lastSteps = 0;
    private static Long lastSystemTime = 0L;
    private static double avgStepsPerMin = 0;

    private static double devAvgStepsPerMin = 0;

    public static int getLastSteps() {
        return lastSteps;
    }

    public static void setLastSteps(int mLastSteps) {
        lastSteps = mLastSteps;
    }

    public static Long getLastSystemTime() {
        return lastSystemTime;
    }

    public static void setLastSystemTime(Long mLastSystemTime) {
        lastSystemTime = mLastSystemTime;
    }

    public static double getAvgStepsPerMin() {
        if (!debugger) {
            return avgStepsPerMin;
        }
        return devAvgStepsPerMin;
    }

    public static void setAvgStepsPerMin(double mAvgStepsPerMin) {
        avgStepsPerMin = mAvgStepsPerMin;
    }

    public static boolean isDebugger() {
        return debugger;
    }

    public static void setDebugger(boolean setBool) {
        debugger = setBool;
        //devAvgStepsPerMin = avgStepsPerMin;
    }

    public static void setDevAvgStepsPerMin(int avgStepsPerMin) {
        devAvgStepsPerMin = avgStepsPerMin;
    }

}
