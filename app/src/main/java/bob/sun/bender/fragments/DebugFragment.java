package bob.sun.bender.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.NumberPicker;
import android.widget.Switch;

import bob.sun.bender.R;
import bob.sun.bender.controller.OnTickListener;
import bob.sun.bender.model.SelectionDetail;
import bob.sun.bender.model.BandRepo;

import static bob.sun.bender.fragments.BandConnectFragment.preference_file_key;

/**
 * Created by bmy001 on 西暦17/04/25.
 */

public class DebugFragment extends Fragment implements OnTickListener {

    public static final String TAG = "DebugFragment";
    private NumberPicker avgStepsPicker;
    private Switch debugSwitch;

    private void saveData(int value) {
        Log.i(TAG, "save dev setp data: " + value);
        SharedPreferences sharedPref = getActivity()
                .getSharedPreferences(preference_file_key, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("avgStep", value);
        editor.apply();
    }

    private void saveDebugState(boolean isDebug) {
        Log.i(TAG, "save debug state: " + isDebug);
        SharedPreferences sharedPref = getActivity()
                .getSharedPreferences(preference_file_key, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("debug", isDebug);
        editor.apply();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View ret = inflater.inflate(R.layout.layout_debug_tools, parent, false);
        avgStepsPicker = (NumberPicker) ret.findViewById(R.id.avg_steps_picker);
        debugSwitch = (Switch) ret.findViewById(R.id.debug_switch);

        debugSwitch.setChecked(BandRepo.isDebugger());
        debugSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                BandRepo.setDebugger(isChecked);
                saveDebugState(isChecked);
            }
        });

        avgStepsPicker.setMinValue(0);
        avgStepsPicker.setMaxValue(200);
        avgStepsPicker.setValue(60);

        avgStepsPicker.setOnScrollListener(new NumberPicker.OnScrollListener() {
            @Override
            public void onScrollStateChange(NumberPicker view, int scrollState) {
                switch (scrollState) {
                    case NumberPicker.OnScrollListener.SCROLL_STATE_FLING:
                        Log.d(TAG, "SCROLL_STATE_FLING");
                        //惯性滑动
                        break;
                    case NumberPicker.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
                        //手动滑动
                        Log.d(TAG, "SCROLL_STATE_TOUCH_SCROLL");
                        break;
                    case NumberPicker.OnScrollListener.SCROLL_STATE_IDLE:
                        //停止滑动
                        Log.d(TAG, "SCROLL_STATE_IDLE");
                        int value = avgStepsPicker.getValue();
                        BandRepo.setDevAvgStepsPerMin(value);
                        //mTextView.setText(CITYS[mSeletedIndex]);

                        //Toast.makeText(MainActivity.this, CITYS[mSeletedIndex], Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
        return ret;
    }

    @Override
    public void onNextTick() {

    }

    @Override
    public void onPreviousTick() {

    }

    @Override
    public SelectionDetail getCurrentSelection() {
        return null;
    }
}
