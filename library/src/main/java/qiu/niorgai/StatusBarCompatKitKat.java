package qiu.niorgai;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

/**
 * After kitkat add fake status bar
 * Created by qiu on 8/27/16.
 */
@TargetApi(Build.VERSION_CODES.KITKAT)
public class StatusBarCompatKitKat {

    //Get status bar height
    private static int getStatusBarHeight(Context context) {
        int result = 0;
        int resId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resId > 0) {
            result = context.getResources().getDimensionPixelOffset(resId);
        }
        return result;
    }

    public static void setStatusBarColor(Activity activity, int statusColor) {
        Window window = activity.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        ViewGroup mContentView = (ViewGroup) window.findViewById(Window.ID_ANDROID_CONTENT);
        ViewGroup mDecorView = (ViewGroup) window.getDecorView();
        if (mDecorView.getTag() != null && mDecorView.getTag() instanceof Boolean && (Boolean)mDecorView.getTag()) {
            //if has add fake status bar view
            View mStatusBarView = mDecorView.getChildAt(0);
            if (mStatusBarView != null) {
                mStatusBarView.setBackgroundColor(statusColor);
            }
        } else {
            int statusBarHeight = getStatusBarHeight(activity);
            //add margin
            View mContentChild = mContentView.getChildAt(0);
            if (mContentChild != null) {
                ViewCompat.setFitsSystemWindows(mContentChild, false);
                FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mContentChild.getLayoutParams();
                lp.topMargin += statusBarHeight;
                mContentChild.setLayoutParams(lp);
            }
            //add fake status bar view
            View mStatusBarView = new View(activity);
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, statusBarHeight);
            layoutParams.gravity = Gravity.TOP;
            mStatusBarView.setLayoutParams(layoutParams);
            mStatusBarView.setBackgroundColor(statusColor);
            mDecorView.addView(mStatusBarView, 0);
            mDecorView.setTag(true);
        }
    }

    public static void setStatusBarColorWithCollapsingToolbar(Activity activity, int statusColor) {
        Window window = activity.getWindow();
        ViewGroup mContentView = (ViewGroup) window.findViewById(Window.ID_ANDROID_CONTENT);
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        int statusBarHeight = getStatusBarHeight(activity);
        View mContentChild = mContentView.getChildAt(0);

        ViewGroup mDecorView = (ViewGroup) window.getDecorView();
        if (mDecorView.getTag() != null && mDecorView.getTag() instanceof Boolean && (Boolean)mDecorView.getTag()) {
            //if has add fake status bar view
            View mStatusBarView = mDecorView.getChildAt(0);
            if (mStatusBarView != null) {
                mStatusBarView.setBackgroundColor(statusColor);
            }
            //remove margin if exist
            if (mContentChild != null && mContentChild.getTag() == null) {
                ViewCompat.setFitsSystemWindows(mContentChild, true);
                FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mContentChild.getLayoutParams();
                lp.topMargin -= statusBarHeight;
                mContentChild.setLayoutParams(lp);
                mContentChild.setTag(true);
            }
        } else {
            //add margin
            if (mContentChild != null) {
                ViewCompat.setFitsSystemWindows(mContentChild, true);
            }
            //add fake status bar view
            final View mStatusBarView = new View(activity);
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, statusBarHeight);
            layoutParams.gravity = Gravity.TOP;
            mStatusBarView.setLayoutParams(layoutParams);
            mStatusBarView.setBackgroundColor(statusColor);
            mDecorView.addView(mStatusBarView, 0);
            mDecorView.setTag(true);
        }
    }

    public static void translucentStatusBar(Activity activity) {
        Window window = activity.getWindow();
        ViewGroup mContentView = (ViewGroup) activity.findViewById(Window.ID_ANDROID_CONTENT);

        //set child View not fill the system window
        View mChildView = mContentView.getChildAt(0);
        if (mChildView != null) {
            ViewCompat.setFitsSystemWindows(mChildView, false);
        }

        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        int statusBarHeight = getStatusBarHeight(activity);
        ViewGroup mDecorView = (ViewGroup) window.getDecorView();
        if (mDecorView.getTag() != null && mDecorView.getTag() instanceof Boolean && (Boolean)mDecorView.getTag()) {
            mChildView = mDecorView.getChildAt(0);
            //remove fake status bar view.
            mContentView.removeView(mChildView);
            mChildView = mContentView.getChildAt(0);
            if (mChildView != null) {
                FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mChildView.getLayoutParams();
                //cancel the margin top
                if (lp != null && lp.topMargin >= statusBarHeight) {
                    lp.topMargin -= statusBarHeight;
                    mChildView.setLayoutParams(lp);
                }
            }
            mDecorView.setTag(false);
        }
    }
}