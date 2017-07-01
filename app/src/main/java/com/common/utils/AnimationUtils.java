package com.common.utils;

import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.support.annotation.AnimRes;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.animation.Animation;

/**
 *
 */
public class AnimationUtils {
    /**
     * Creates and updates a new ObjectAnimator.OfFloat
     *
     * @param object       Object to animate.
     * @param param        Attribute to animate.
     * @param startValue   start value.
     * @param endValue     end value.
     * @param duration     duration in millis.
     * @param delay        animation delay in millis.
     * @param interpolator TimeInterpolator to use in the animation.
     * @return The new and initialized ObjectAnimator.
     */
    public static ObjectAnimator newAnimatorOfFloat(Object object, String param, float startValue, float endValue, long duration, long delay, TimeInterpolator interpolator) {
        return decorateObjectAnimator(ObjectAnimator.ofFloat(object, param, startValue, endValue), duration, delay, interpolator);
    }

    /**
     * Creates and updates a new ObjectAnimator.OfInt
     *
     * @param object       Object to animate.
     * @param param        Attribute to animate.
     * @param startValue   start value.
     * @param endValue     end value.
     * @param duration     duration in millis.
     * @param delay        animation delay in millis.
     * @param interpolator TimeInterpolator to use in the animation.
     * @return The new and initialized ObjectAnimator.
     */
    public static ObjectAnimator newAnimatorOfInt(Object object, String param, int startValue, int endValue, long duration, long delay, TimeInterpolator interpolator) {
        return decorateObjectAnimator(ObjectAnimator.ofInt(object, param, startValue, endValue), duration, delay, interpolator);
    }

    /**
     * Updates an ObjectAnimator
     *
     * @param objectAnimator Object to update.
     * @param duration       duration in millis.
     * @param delay          animation delay in millis.
     * @param interpolator   TimeInterpolator to use in the animation.
     * @return The updated ObjectAnimator.
     */
    private static ObjectAnimator decorateObjectAnimator(ObjectAnimator objectAnimator, long duration, long delay, TimeInterpolator interpolator) {
        objectAnimator.setDuration(duration);
        objectAnimator.setInterpolator(interpolator);
        objectAnimator.setStartDelay(delay);
        return objectAnimator;
    }

    public static int evaluateColorChange(float fraction, int startValue, int endValue) {
        int startInt = startValue;
        int startA = (startInt >> 24) & 0xff;
        int startR = (startInt >> 16) & 0xff;
        int startG = (startInt >> 8) & 0xff;
        int startB = startInt & 0xff;

        int endInt = endValue;
        int endA = (endInt >> 24) & 0xff;
        int endR = (endInt >> 16) & 0xff;
        int endG = (endInt >> 8) & 0xff;
        int endB = endInt & 0xff;

        return (startA + (int) (fraction * (endA - startA))) << 24 |
                (startR + (int) (fraction * (endR - startR))) << 16 |
                (startG + (int) (fraction * (endG - startG))) << 8 |
                (startB + (int) (fraction * (endB - startB)));
    }

    /**
     * Animates a given view triggering a listener when the animation ends and then it animates again.
     *
     * @param viewToAnimate     View to animate.
     * @param anim1             First animation resource.
     * @param anim2             Second animation resource.
     * @param onFirstAnimChange Listener
     */
    public static void animSequence(final View viewToAnimate, @AnimRes int anim1, @AnimRes int anim2, final onFirstAnimChangeListener onFirstAnimChange) {
        final Animation animation1 = android.view.animation.AnimationUtils.loadAnimation(viewToAnimate.getContext(), anim1);

        final Animation animation2 = anim2 == 0 ? null : android.view.animation.AnimationUtils.loadAnimation(viewToAnimate.getContext(), anim2);
        Animation.AnimationListener al = new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (onFirstAnimChange != null) {
                    onFirstAnimChange.onFirstAnimChange();
                }
                if (animation2 != null) {
                    viewToAnimate.startAnimation(animation2);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        };
        animation1.setAnimationListener(al);
        viewToAnimate.startAnimation(animation1);
    }


    /**
     * Animates a few views with the same animation at the same time.
     *
     * @param anim           Animation resource.
     * @param viewsToAnimate Set of views.
     */
    public static void animateThemAll(@AnimRes int anim, View... viewsToAnimate) {
        for (View v : viewsToAnimate) {
            if (v != null) {
                v.startAnimation(android.view.animation.AnimationUtils.loadAnimation(v.getContext(), anim));
            }
        }
    }

    /**
     * Animates a few views with the same animation at the same time.
     *
     * @param anim              Animation resource.
     * @param onFirstAnimChange listener
     * @param viewsToAnimate    Set of views.
     */
    public static void animateThemAll(@AnimRes int anim, @Nullable final onFirstAnimChangeListener onFirstAnimChange, View... viewsToAnimate) {
        if (viewsToAnimate != null && viewsToAnimate.length > 0 && viewsToAnimate[0] != null) {
            Animation animation = android.view.animation.AnimationUtils.loadAnimation(viewsToAnimate[0].getContext(), anim);
            Animation.AnimationListener animationListener = new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    if (onFirstAnimChange != null)
                        onFirstAnimChange.onFirstAnimChange();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            };
            animation.setAnimationListener(animationListener);
            viewsToAnimate[0].startAnimation(animation);
        }
        if (viewsToAnimate != null)
            for (int i = 1; i < viewsToAnimate.length; i++) {
                if (viewsToAnimate[i] != null) {
                    viewsToAnimate[i].startAnimation(android.view.animation.AnimationUtils.loadAnimation(viewsToAnimate[i].getContext(), anim));
                }
            }
    }

    /**
     * AnimSequence Listener
     */
    public interface onFirstAnimChangeListener {
        void onFirstAnimChange();
    }
}
