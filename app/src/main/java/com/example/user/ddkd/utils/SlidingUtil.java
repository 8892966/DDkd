package com.example.user.ddkd.utils;

import android.content.Context;
import android.content.SyncAdapterType;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
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

    private int startX;//记录按下时候的X值
    private int endX;//记录抬起时候的Y值
    private boolean isSliding;

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


    private long startTime;//记录按下时候的时间
    private long endTime;//记录抬起时候的时间
    public boolean onTouchEvent(MotionEvent ev)
    {
        int action = ev.getAction();
        switch (action)
        {
            case MotionEvent.ACTION_UP:
                endX= (int) ev.getX();
                endTime=SystemClock.uptimeMillis();
                int move=endX-startX;
                int speed= (int) (move/(endTime-startTime));

                if (speed>1.5) {
                    openMenu();
                    isSliding=true;
                }else if(speed<(-1.5)){
                    closeMenu();
                    isSliding=true;
                }else{
                    if(isOpen)
                        this.smoothScrollTo(0, 0);
                    if(!isOpen)
                        this.smoothScrollTo(mMenuWidth, 0);
                }

                //**************************判断当前是否有快速滑动的操作
                if (!isSliding){
                    int scrollX = getScrollX();//当前需显示的内容相比比Activity的宽度多出来的内容；就相当于隐藏在左边的宽度；
                    if (scrollX >= mMenuWidth / 2){
                        this.smoothScrollTo(mMenuWidth, 0);//否则隐藏；
                        isOpen = false;
                    }else{
                        this.smoothScrollTo(0, 0);//显示菜单;smoothScrollTo很好的一个动画效果显示；
                        isOpen = true;
                    }
                }
                isSliding=false;
                startX=0;
                endX=0;
                startTime=0;
                endTime=0;
                return true;
            case MotionEvent.ACTION_MOVE:
                if(startX==0){
                    startX= (int) ev.getX();
                    Log.i("XXXXX1_1",String.valueOf(startX));
                }
                if(startTime==0){
                    startTime= SystemClock.uptimeMillis();
                    Log.i("XXXXX_startTime_1",String.valueOf(startTime));
                }
                break;
            case MotionEvent.ACTION_DOWN:
                startX= (int) ev.getX();
                Log.i("XXXXX1",String.valueOf(startX));
                startTime= SystemClock.uptimeMillis();
                Log.i("XXXXX_startTime",String.valueOf(startTime));
                break;
        }
        return super.onTouchEvent(ev);
    }

    //*************************打开菜单
    public void openMenu(){
        if (isOpen)
            return;
        this.smoothScrollTo(0, 0);
        isOpen=true;
    }
    public void closeMenu(){
        if(!isOpen)
            return;
        this.smoothScrollTo(mMenuWidth, 0);
        isOpen=false;

    }

    //**************************切换菜单
    public void changeMenu(){
        if (isOpen){
            closeMenu();
        }else{
            openMenu();
        }
    }

    //重写onScrollChanged()方法：该方法在滚动发生的时候会被调用;
    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        float scale = l * 1.0f / mMenuWidth; // 子类的隐藏效果
//        ViewHelper.setTranslationX(mMenu, mMenuWidth * scale * 0.8f);

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
