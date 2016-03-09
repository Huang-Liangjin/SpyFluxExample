package jp.co.drecom.spyfluxexample.util;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Build;
import android.view.View;
import android.view.ViewAnimationUtils;

/**
 * Created by huang_liangjin on 2016/03/08.
 */
public class AnimationUtil {

    private static int x;
    private static int y;
    private static int endRadius;

    public static void hideRevealEffect (final View v, AnimatorListenerAdapter listener) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return;
        }
        v.setVisibility(View.VISIBLE);

        // create the animation (the final radius is zero)
        Animator anim = ViewAnimationUtils.createCircularReveal(
                v, x, y, endRadius, 0);

        anim.setDuration(350);

        anim.addListener(listener);

        anim.start();
    }

    public static void showRevealEffect(final View v, final int centerX, final int centerY, Animator.AnimatorListener lis) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return;
        }

        v.setVisibility(View.VISIBLE);

        x = centerX;
        y = centerY;
        endRadius = Math.max(v.getHeight(), v.getWidth());

        Animator anim = ViewAnimationUtils.createCircularReveal(
                    v, centerX, centerY, 0, endRadius);

        anim.setDuration(350);

        anim.addListener(lis);
        anim.start();
    }
}
