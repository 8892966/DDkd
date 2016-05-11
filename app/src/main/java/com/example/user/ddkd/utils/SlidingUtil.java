package com.example.user.ddkd.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.nineoldandroids.view.ViewHelper;

/**
 * Created by Administrator on 2016/5/10.
 */
public class SlidingUtil extends HorizontalScrollView{
    private LinearLayout mWapper;
    private ViewGroup mMenu;
    private ViewGroup  mContent;
    private int mScreenWidth;//屏幕的宽度；
    private int mMenuRightPadding = 50;//Activity到屏幕右侧的距离,单位是dp;
    private boolean once;
    private boolean isOpen;
    private int mMenuWidth;

    public SlidingUtil(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setHorizontalScrollBarEnabled(false);
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics=new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        mScreenWidth=outMetrics.widthPixels;
        mMenuRightPadding= (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0, context.getResources().getDisplayMetrics());
    }
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
        if (!once){
            mWapper= (LinearLayout) getChildAt(0);
            mMenu= (ViewGroup) mWapper.getChildAt(0);
            mContent = (ViewGroup) mWapper.getChildAt(1);
            mMenuWidth=mMenu.getLayoutParams().width=mScreenWidth-mMenuRightPadding;
            mContent.getLayoutParams().width = mScreenWidth;
            once=true;
        }
        super.onMeasure(widthMeasureSpec,heightMeasureSpec);
    }
    protected void onLayout(boolean changed, int l, int t, int r, int b)
    {
        super.onLayout(changed, l, t, r, b);
        if (changed)
        {
            this.scrollTo(mMenuWidth, 0);//将子View隐藏；
        }
    }
    public boolean onTouchEvent(MotionEvent ev)
    {
        int action = ev.getAction();
        switch (action)
        {
            case MotionEvent.ACTION_UP://
                // 隐藏在左边的宽度
                int scrollX = getScrollX();//当前需显示的内容相比比Activity的宽度多出来的内容；就相当于隐藏在左边的宽度；
                if (scrollX >= mMenuWidth / 2)//如果隐藏的宽度大于菜单栏的二分之一;
                {
                    this.smoothScrollTo(mMenuWidth, 0);//显示菜单;smoothScrollTo很好的一个动画效果显示；
                    isOpen = false;
                } else{
                    this.smoothScrollTo(0, 0);//否则隐藏；
                    isOpen = true;
                }
                return true;
        }
        return super.onTouchEvent(ev);
    }


    //*************************打开菜单
    public void openMenu(){
        if (isOpen)
            return;
//        Log.i("Change3_3", "Click");
        this.smoothScrollTo(0, 0);
        isOpen=true;
    }
    public void closeMenu(){
        if(!isOpen)
            return;
//        Log.i("Change2_2", "Click");
        this.smoothScrollTo(mMenuWidth, 0);
        isOpen=false;
    }

    //**************************切换菜单
    public void changeMenu(){
        if (isOpen){
            closeMenu();
//            Log.i("Change2", "Click");
        }else{
            openMenu();
//            Log.i("Change3", "Click");
        }
    }

    //重写onScrollChanged()方法：该方法在滚动发生的时候会被调用;
    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);

        float scale = l * 1.0f / mMenuWidth; // 子类的隐藏效果
        ViewHelper.setTranslationX(mMenu, mMenuWidth * scale * 0.8f);

//        float rightScale = 0.9f + 0.1f * scale;//父类内容区域的缩放效果；
        //设置缩放的中心点；
//        ViewHelper.setPivotX(mContent, 0);
//        ViewHelper.setPivotY(mContent, mContent.getHeight() / 2);
//        ViewHelper.setScaleX(mContent, rightScale);
//        ViewHelper.setScaleY(mContent, rightScale);

//        float leftScale = 1.0f - scale * 0.3f;//子类的缩放效果；
//        ViewHelper.setScaleX(mMenu, leftScale);
//        ViewHelper.setScaleY(mMenu, leftScale);

        float leftAlpha = 1f * (1 - scale);//菜单透明度的变化；
        ViewHelper.setAlpha(mMenu, leftAlpha);

        float leftAlpha1 =1f- 0.8f * (1 - scale);//父类透明度的变化；
        ViewHelper.setAlpha(mContent,leftAlpha1);
    }
}
