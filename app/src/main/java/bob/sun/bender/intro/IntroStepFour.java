package bob.sun.bender.intro;

import android.support.v4.app.Fragment;

import com.heinrichreimersoftware.materialintro.slide.Slide;

import bob.sun.bender.R;

/**
 * Used for setting up MiBand
 * Created by JohnnySun on 西暦17/03/30.
 */

public class IntroStepFour implements Slide {
    Fragment fragment;

    @Override
    public Fragment getFragment() {
        if (fragment == null)
            fragment = new AnimateIntroFragment().setStep(AnimateIntroFragment.IntroStep.Four);
        return fragment;
    }

    @Override
    public int getBackground() {
        return R.color.colorPrimary;
    }

    @Override
    public int getBackgroundDark() {
        return R.color.colorPrimary;
    }

    @Override
    public boolean canGoForward() {
        return false;
    }

    @Override
    public boolean canGoBackward() {
        return true;
    }
}
