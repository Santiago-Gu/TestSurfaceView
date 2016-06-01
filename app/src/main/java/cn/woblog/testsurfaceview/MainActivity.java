package cn.woblog.testsurfaceview;

import android.annotation.TargetApi;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener {
    public static final String TAG = "TAG";
    private SurfaceView sv;
    private SurfaceView sv_mini;
    private SurfaceHolder sfh;
    private SurfaceHolder holder;
    private RelativeLayout rl;
    private GestureDetector.SimpleOnGestureListener simpleOnGestureListener;
    private GestureDetector gestureDetector;
    private int topMargin;
    private int leftMargin;
    private int topTitleHeight;
    private int lastX;
    private int lastY;
    private int screenWidth;
    private int screenHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DisplayMetrics dm = getResources().getDisplayMetrics();
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels - 50;
getSupportActionBar().hide();
        sv = (SurfaceView) findViewById(R.id.sv);
        rl = (RelativeLayout) findViewById(R.id.rl);


        simpleOnGestureListener = new GestureDetector.SimpleOnGestureListener(){


            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {


                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) rl.getLayoutParams();

                rl.setX(rl.getX()+distanceX);
                rl.setY(rl.getY()+distanceY);
                Log.d(TAG,"x,y"+distanceX+","+distanceY+","+layoutParams);
                return super.onScroll(e1, e2, distanceX, distanceY);
            }
        };
        gestureDetector = new GestureDetector(this, simpleOnGestureListener);
        rl.setOnTouchListener(this);

        sfh = sv.getHolder();
        //对 surfaceView 进行操作
        sfh.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                Canvas c = sfh.lockCanvas(new Rect(0, 0, 600, 600));
                //2.开画
                Paint  p =new Paint();
                p.setColor(Color.RED);
                Rect aa  =  new Rect(0,0,600,600);
                c.drawRect(aa, p);
                //3. 解锁画布   更新提交屏幕显示内容
                sfh.unlockCanvasAndPost(c);
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

            }
        });// 自动运行surfaceCreated以及surfaceChanged

        sv_mini = (SurfaceView) findViewById(R.id.sv_mini);

//        sv.setZOrderOnTop(false);

        //这两个方法差不多，设置了就会浮现到顶部，但是，后面的看不见，要像下面设置为透明
        sv_mini.setZOrderOnTop(true);
        sv_mini.setZOrderMediaOverlay(true);

        holder = sv_mini.getHolder();

        holder.setFormat(PixelFormat.TRANSPARENT);
        sfh.setFormat(PixelFormat.TRANSPARENT);

        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                Canvas c = holder.lockCanvas(new Rect(0, 0, 400, 400));
                //2.开画
                Paint  p =new Paint();
                p.setColor(Color.BLUE);
                Rect aa  =  new Rect(0,0,400,400);
                c.drawRect(aa, p);
                //3. 解锁画布   更新提交屏幕显示内容
                holder.unlockCanvasAndPost(c);
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

            }
        });
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int ea=event.getAction();
        switch(ea){
            case MotionEvent.ACTION_DOWN:
                lastX = (int) event.getRawX();
                lastY = (int) event.getRawY();
                break;
            /**
             * layout(l,t,r,b)
             * l  Left position, relative to parent
             t  Top position, relative to parent
             r  Right position, relative to parent
             b  Bottom position, relative to parent
             * */
            case MotionEvent.ACTION_MOVE:
                int dx =(int)event.getRawX() - lastX;
                int dy =(int)event.getRawY() - lastY;

                int left = v.getLeft() + dx;
                int top = v.getTop() + dy;
                int right = v.getRight() + dx;
                int bottom = v.getBottom() + dy;

                if(left < 0){
                    left = 0;
                    right = left + v.getWidth();
                }

                if(right > screenWidth){
                    right = screenWidth;
                    left = right - v.getWidth();
                }

                if(top < 0){
                    top = 0;
                    bottom = top + v.getHeight();
                }

                if(bottom > screenHeight){
                    bottom = screenHeight;
                    top = bottom - v.getHeight();
                }

                v.layout(left, top, right, bottom);

                Log.i("", "position：" + left +", " + top + ", " + right + ", " + bottom);

                lastX = (int) event.getRawX();
                lastY = (int) event.getRawY();

                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return true;
    }


}
