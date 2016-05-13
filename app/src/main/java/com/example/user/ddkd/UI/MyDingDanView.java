package com.example.user.ddkd.UI;

import android.content.Context;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

/**
 * Created by User on 2016-05-06.
 */
public class MyDingDanView extends ViewGroup {
    private Context context;
    private final static String TAG="MyDingDanView";
    //显示当前的下标
    private int currId=0;
    private Scroller myScroller;
    private int firstx=0;
    private int firsty=0;
    private boolean isFling;//判断是否发生快速滑动
    private MyDingDanChangeListener myDingDanChangeListener;

    public MyDingDanView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initview();
    }

    private void initview() {
        myScroller=new Scroller(context);
    }

    public MyDingDanView(Context context) {
        super(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        for(int i=0;i<getChildCount();i++){
            View v=getChildAt(i);
            v.measure(widthMeasureSpec,heightMeasureSpec);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            view.layout(0 + i * getWidth(), 0, getWidth() + i * getWidth(), getHeight());
        }
    }
    private int startx=0;
    private int endx=0;
    private long startTime=0;
    private long endTime=0;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        //添加自己的事件解析
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                firstx = (int) event.getX();
                startx = endx = (int) event.getX();
                startTime = SystemClock.uptimeMillis();
                break;
            case MotionEvent.ACTION_UP:
                endTime = SystemClock.uptimeMillis();
                float velocityX = ((float) startx - (float) firstx) / (endTime - startTime);
                if (Math.abs(velocityX)>2){
                    isFling = true;
//                Log.e(TAG, velocityX + "");
                if (velocityX > 0 && currId > 0) {
                    currId--;
                } else if (velocityX < 0 && currId < getChildCount() - 1) {
                    currId++;
                }
                moveToDest(currId);
                }
                if(!isFling) {//在没有发生快速滑动时的动作
                    int nextid = 0;
                    if (event.getX() - firstx > getWidth() / 2) {//手指向右滑动，超过屏幕的1/2当前的currid-1
                        nextid = currId - 1;
                    } else if (firstx - event.getX() >= getWidth() / 2) {//手指向左滑动，超过屏幕的1/2当前的currid+1
                        nextid = currId + 1;
                    } else {
                        nextid = currId;
                    }
                    moveToDest(nextid);
                }
                isFling=false;
                firstx=0;
                startTime=0;
                startx=0;
                break;
            case MotionEvent.ACTION_MOVE:
                if(firstx==0){
                    firstx=(int) event.getX();
                }
                if(startTime==0){
                    startTime=SystemClock.uptimeMillis();
            }
                endx= (int) event.getX();
                if (startx != 0) {
                    int distanceX=startx-endx;
                    scrollBy(distanceX, 0);
                }
                startx=endx;
                break;
        }
        return true;
    }
    //移动view到目的地
    public void moveToDest(int nextid) {
        //对nextid进行判断，确保是在合理的范围
        currId=(nextid>=0)?nextid:0;
        if(currId==0)nextid=0;
        currId=(nextid<=(getChildCount()-1)?nextid:(getChildCount()-1));
//        scrollTo(currId*getWidth(),0);

        if(myDingDanChangeListener!=null){
            myDingDanChangeListener.moveToDest(currId+1);
        }

        int distance=currId*getWidth()-getScrollX();
//        myScroller.startScroll(getScrollX(),0,distance,0);
        myScroller.startScroll(getScrollX(),0,distance,0,300);
        invalidate();
    }

    @Override
    public void computeScroll() {
        if(myScroller.computeScrollOffset()){
            int newx=myScroller.getCurrX();
            scrollTo(newx,0);
            invalidate();
        }

    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean result=false;
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                firstx= (int) ev.getX();
                firsty= (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                int disx= (int) Math.abs(ev.getX()-firstx);
                int disy= (int) Math.abs(ev.getY()-firsty);
                if(disx>disy && disx>10){

                    result=true;

                }else{

                    result=false;

                }
                break;
            case MotionEvent.ACTION_UP:

                break;
        }
        return result;
    }

    public MyDingDanChangeListener getMyDingDanChangeListener() {
        return myDingDanChangeListener;
    }

    public void setMyDingDanChangeListener(MyDingDanChangeListener myDingDanChangeListener) {
        this.myDingDanChangeListener = myDingDanChangeListener;
    }

    public interface MyDingDanChangeListener{
        void moveToDest(int currid);
    }

}
