package xxzx.mylibrary;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.ActionBar;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;


public class PromotedActionsLibrary {

    Context context;

    FrameLayout frameLayout;

    FloatingActionButton mainFabButton;

    RotateAnimation rotateOpenAnimation;

    RotateAnimation rotateCloseAnimation;

    ArrayList<FloatingActionButton> promotedActions;

    ObjectAnimator objectAnimator[];

    private int px;

    private static final int ANIMATION_TIME = 200;

    private boolean isMenuOpened;

    private View imgDangerNum;


    public PromotedActionsLibrary(View imgdangernum){this.imgDangerNum = imgdangernum;}

    public void setup(Context activityContext, FrameLayout layout) {
        context = activityContext;
        promotedActions = new ArrayList<FloatingActionButton>();
        frameLayout = layout;
        px = (int) context.getResources().getDimension(R.dimen.dim64dp) + 10;
        openRotation();
        closeRotation();
    }

    
    public FloatingActionButton addMainItem(Drawable drawable) {

        FloatingActionButton fab = (FloatingActionButton) LayoutInflater.from(context).inflate(R.layout.main_promoted_floating_action_button, frameLayout, false);

        fab.setImageDrawable(drawable);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isMenuOpened) {
                    if(imgDangerNum!=null) {
                        imgDangerNum.setVisibility(View.VISIBLE);
                    }
                    closePromotedActions().start();
                    isMenuOpened = false;
                } else {
                    if(imgDangerNum!=null) {
                        imgDangerNum.setVisibility(View.INVISIBLE);
                    }
                    isMenuOpened = true;
                    openPromotedActions().start();
                }
            }
        });

        frameLayout.addView(fab);

        mainFabButton = fab;

        return fab;
    }

    public void addItem(Drawable drawable,String fab_tag, View.OnClickListener onClickListener) {

        FloatingActionButton fab = (FloatingActionButton) LayoutInflater.from(context).inflate(R.layout.promoted_floating_action_button, frameLayout, false);

        fab.setImageDrawable(drawable);
        fab.setTag(fab_tag);
        fab.setOnClickListener(onClickListener);

        promotedActions.add(fab);
        frameLayout.addView(fab);
        return;
    }

    /**
     * Set close animation for promoted actions
     */
    public AnimatorSet closePromotedActions() {

        if (objectAnimator == null){
            objectAnimatorSetup();
        }

        AnimatorSet animation = new AnimatorSet();

        for (int i = 0; i < promotedActions.size(); i++) {

            objectAnimator[i] = setCloseAnimation(promotedActions.get(i), i);
        }

        if (objectAnimator.length == 0) {
            objectAnimator = null;
        }

        animation.playTogether(objectAnimator);
        animation.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                mainFabButton.startAnimation(rotateCloseAnimation);
                mainFabButton.setClickable(false);
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                mainFabButton.setClickable(true);
                hidePromotedActions();
            }

            @Override
            public void onAnimationCancel(Animator animator) {
                mainFabButton.setClickable(true);
            }

            @Override
            public void onAnimationRepeat(Animator animator) {}
        });

        return animation;
    }

    public AnimatorSet openPromotedActions() {

        if (objectAnimator == null){
            objectAnimatorSetup();
        }


        AnimatorSet animation = new AnimatorSet();

        for (int i = 0; i < promotedActions.size(); i++) {

            objectAnimator[i] = setOpenAnimation(promotedActions.get(i), i);
        }

        if (objectAnimator.length == 0) {
            objectAnimator = null;
        }

        animation.playTogether(objectAnimator);
        animation.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                mainFabButton.startAnimation(rotateOpenAnimation);
                mainFabButton.setClickable(false);
                showPromotedActions();
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                mainFabButton.setClickable(true);
            }

            @Override
            public void onAnimationCancel(Animator animator) {
                mainFabButton.setClickable(true);
            }

            @Override
            public void onAnimationRepeat(Animator animator) {}
        });


        return animation;
    }


    public boolean isMenuOpened() {
        return isMenuOpened;
    }

    private void objectAnimatorSetup() {

        objectAnimator = new ObjectAnimator[promotedActions.size()];
    }


    /**
     * Set close animation for single button
     *
     * @param promotedAction
     * @param position
     * @return objectAnimator
     */
    private ObjectAnimator setCloseAnimation(FloatingActionButton promotedAction, int position) {

        ObjectAnimator objectAnimator;

        if(context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {

            objectAnimator = ObjectAnimator.ofFloat(promotedAction, View.TRANSLATION_Y, -px * (promotedActions.size() - position), 0f);
            objectAnimator.setRepeatCount(0);
            objectAnimator.setDuration(ANIMATION_TIME * (promotedActions.size() - position));

        } else {

            objectAnimator = ObjectAnimator.ofFloat(promotedAction, View.TRANSLATION_X, -px * (promotedActions.size() - position), 0f);
            objectAnimator.setRepeatCount(0);
            objectAnimator.setDuration(ANIMATION_TIME * (promotedActions.size() - position));
        }

        return objectAnimator;
    }

    /**
     * Set open animation for single button
     *
     * @param promotedAction
     * @param position
     * @return objectAnimator
     */
    private ObjectAnimator setOpenAnimation(FloatingActionButton promotedAction, int position) {

        ObjectAnimator objectAnimator;

        if(context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {

            objectAnimator = ObjectAnimator.ofFloat(promotedAction, View.TRANSLATION_Y, 0f, -px * (promotedActions.size() - position));
            objectAnimator.setRepeatCount(0);
            objectAnimator.setDuration(ANIMATION_TIME * (promotedActions.size() - position));

        } else {
            objectAnimator = ObjectAnimator.ofFloat(promotedAction, View.TRANSLATION_X, 0f, -px * (promotedActions.size() - position));
            objectAnimator.setRepeatCount(0);
            objectAnimator.setDuration(ANIMATION_TIME * (promotedActions.size() - position));
        }

        return objectAnimator;
    }

    private void hidePromotedActions() {

        for (int i = 0; i < promotedActions.size(); i++) {
            promotedActions.get(i).setVisibility(View.GONE);
        }
    }

    private void showPromotedActions() {

        for (int i = 0; i < promotedActions.size(); i++) {
            promotedActions.get(i).setVisibility(View.VISIBLE);
        }
    }

    private void openRotation() {

        rotateOpenAnimation = new RotateAnimation(0, 45, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        rotateOpenAnimation.setFillAfter(true);
        rotateOpenAnimation.setFillEnabled(true);
        rotateOpenAnimation.setDuration(ANIMATION_TIME);
    }

    private void closeRotation() {

        rotateCloseAnimation = new RotateAnimation(45, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateCloseAnimation.setFillAfter(true);
        rotateCloseAnimation.setFillEnabled(true);
        rotateCloseAnimation.setDuration(ANIMATION_TIME);
    }
}
