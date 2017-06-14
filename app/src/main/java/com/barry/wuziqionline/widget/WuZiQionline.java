package com.barry.wuziqionline.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.barry.wuziqionline.R;
import com.barry.wuziqionline.domain.Room;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by Administrator on 2017/6/8.
 */
//f0c5a3
public class WuZiQionline extends View {
    public static final String TAG = "WuZiQi";
    private static final float TEXT_SIZE = 400;

    private int mPanelWidth;//棋盘的宽度
    private float mLineHeight;//每行的高度
    private int MAX_LINE = 14;//棋盘行数
    private int MAX_COUNT_IN_LINE = 5;

    private Paint mPaint = new Paint();

    private Bitmap mWhitePiece;
    private Bitmap mBlackPiece;

    private float ratioPiceOfLineHeight = 1f;

    private boolean mIsWhite;
    private ArrayList<Point> mWhiteArray = new ArrayList<>();
    private ArrayList<Point> mBlackArray = new ArrayList<>();

    private boolean mIsGameOver;
    private boolean mIsWhiteWinner;
    private Room room;
    private int who;
    int i;

    public void input(Room r,int status,int i) {
        this.mBlackArray = r.getBlackArray();
        this.mWhiteArray = r.getWhiteArray();
        this.mIsGameOver = r.isGameOver();
        this.mIsWhiteWinner = r.isWhiteWinner();
        this.room = r;
        this.who =status;
        this.i=i;
        invalidate();
    }


    public WuZiQionline(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mPaint.setColor(0xee000000);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStrokeWidth(5);
        mPaint.setStyle(Paint.Style.STROKE);

        mWhitePiece = BitmapFactory.decodeResource(getResources(), R.drawable.white);
        mBlackPiece = BitmapFactory.decodeResource(getResources(), R.drawable.black);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);

        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int width = Math.min(widthSize, heightSize);

        if (widthMode == MeasureSpec.UNSPECIFIED) {
            width = heightSize;

        } else if (heightMode == MeasureSpec.UNSPECIFIED) {
            width = widthSize;
        }
        setMeasuredDimension(width, width);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mPanelWidth = w;
        mLineHeight = mPanelWidth * 1.0f / MAX_LINE;

        int piceWidth = (int) (mLineHeight * ratioPiceOfLineHeight);
        mWhitePiece = Bitmap.createScaledBitmap(mWhitePiece, piceWidth, piceWidth, false);
        mBlackPiece = Bitmap.createScaledBitmap(mBlackPiece, piceWidth, piceWidth, false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setColor(0x44000000);
        paint.setTextSize(TEXT_SIZE);
        canvas.drawText(who==1 ? "白" : "黑", (mPanelWidth - TEXT_SIZE) / 2, (mPanelWidth) / 2, paint);
        drawBorad(canvas);

        drawPiece(canvas);

        checkGameOver();
    }

    private void checkGameOver() {
        boolean whitewin = checkFiveInLine(mWhiteArray);
        boolean blackwin = checkFiveInLine(mBlackArray);
        if (whitewin || blackwin) {
            mIsGameOver = true;
            mIsWhiteWinner = whitewin;

            String text = mIsWhiteWinner ? "白棋胜利" : "黑棋胜利";
            showAlert(text);
        }

    }

    private void showAlert(String text) {


        View inflate = LayoutInflater.from(getContext()).inflate(R.layout.alert_result, null);
        TextView result = (TextView) inflate.findViewById(R.id.result);
        result.setText(text);
        new AlertDialog.Builder(getContext(), R.style.dialog)
                .setView(inflate)
                .show();
    }

    private boolean checkFiveInLine(List<Point> points) {
        for (Point p : points) {
            int x = p.x;
            int y = p.y;

            boolean win = checkHorizontal(x, y, points);
            if (win) return true;
            win = checkVetical(x, y, points);
            if (win) return true;
            win = checkLeftDiagonal(x, y, points);
            if (win) return true;
            win = checkRightDiagonal(x, y, points);
            if (win) return true;
        }


        return false;
    }

    /**
     * 判断x,y位置的旗子，是否横向有五个
     *
     * @param x
     * @param y
     * @param points
     * @return
     */
    private boolean checkHorizontal(int x, int y, List<Point> points) {
        int count = 1;
        for (int i = 1; i < MAX_COUNT_IN_LINE; i++) {
            if (points.contains(new Point(x - i, y))) {
                count++;
            } else {
                break;
            }
        }
        for (int i = 1; i < MAX_COUNT_IN_LINE; i++) {
            if (points.contains(new Point(x + i, y))) {
                count++;
            } else {
                break;
            }
        }
        if (count == 5) {
            return true;
        }

        return false;
    }

    /**
     * 判断x,y位置的旗子，是否纵向有五个
     *
     * @param x
     * @param y
     * @param points
     * @return
     */
    private boolean checkVetical(int x, int y, List<Point> points) {
        int count = 1;
        for (int i = 1; i < MAX_COUNT_IN_LINE; i++) {
            if (points.contains(new Point(x, y - i))) {
                count++;
            } else {
                break;
            }
        }
        for (int i = 1; i < MAX_COUNT_IN_LINE; i++) {
            if (points.contains(new Point(x, y + i))) {
                count++;
            } else {
                break;
            }
        }
        if (count == 5) {
            return true;
        }

        return false;
    }

    /**
     * 判断x,y位置的旗子，是否左斜有五个
     *
     * @param x
     * @param y
     * @param points
     * @return
     */
    private boolean checkLeftDiagonal(int x, int y, List<Point> points) {
        int count = 1;
        for (int i = 1; i < MAX_COUNT_IN_LINE; i++) {
            if (points.contains(new Point(x - i, y + i))) {
                count++;
            } else {
                break;
            }
        }
        for (int i = 1; i < MAX_COUNT_IN_LINE; i++) {
            if (points.contains(new Point(x + i, y - i))) {
                count++;
            } else {
                break;
            }
        }
        if (count == 5) {
            return true;
        }

        return false;
    }

    /**
     * 判断x,y位置的旗子，是否右斜有五个
     *
     * @param x
     * @param y
     * @param points
     * @return
     */
    private boolean checkRightDiagonal(int x, int y, List<Point> points) {
        int count = 1;
        for (int i = 1; i < MAX_COUNT_IN_LINE; i++) {
            if (points.contains(new Point(x - i, y - i))) {
                count++;
            } else {
                break;
            }
        }
        for (int i = 1; i < MAX_COUNT_IN_LINE; i++) {
            if (points.contains(new Point(x + i, y + i))) {
                count++;
            } else {
                break;
            }
        }
        if (count == 5) {
            return true;
        }

        return false;
    }

    private void drawPiece(Canvas canvas) {
        for (Point p : mWhiteArray) {
            canvas.drawBitmap(mWhitePiece, (p.x + (1 - ratioPiceOfLineHeight) / 2) * mLineHeight, (p.y + (1 - ratioPiceOfLineHeight) / 2) * mLineHeight, null);
        }
        for (Point p : mBlackArray) {
            canvas.drawBitmap(mBlackPiece, (p.x + (1 - ratioPiceOfLineHeight) / 2) * mLineHeight, (p.y + (1 - ratioPiceOfLineHeight) / 2) * mLineHeight, null);
        }

    }

    //绘制棋盘
    private void drawBorad(Canvas canvas) {
        int w = mPanelWidth;
        float lineHeight = mLineHeight;

        for (int i = 0; i < MAX_LINE; i++) {
            int startX = (int) (lineHeight / 2);//

            int endX = (int) (w - (lineHeight / 2));

            int y = (int) ((0.5 + i) * lineHeight);

            canvas.drawLine(startX, y, endX, y, mPaint);
            canvas.drawLine(y, startX, y, endX, mPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mIsGameOver) {
            return false;
        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
            int x = (int) event.getX();
            int y = (int) event.getY();

            Point p = getValiPonit(x, y);
            Log.i(TAG, "onTouchEvent: " + p);
            if (mWhiteArray.contains(p) || mBlackArray.contains(p)) {
                return false;
            }
            if (who ==1 || i==1) {
                mWhiteArray.add(p);
                Log.i(TAG, "onTouchEvent: who1");
                who =2;
                room.setWhiteArray(mWhiteArray);
            } else if(who==2 || i==2) {
                mBlackArray.add(p);
                Log.i(TAG, "onTouchEvent: who2");
                who =1;
                room.setBlackArray(mBlackArray);
            }
            room.setWhiteWinner(mIsWhiteWinner);
            room.setGameOver(mIsGameOver);
            room.setWho(who);
            room.update(room.getObjectId(), new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    if (e == null) {
                        Log.i("bmob", "更新成功");
                    } else {
                        Log.i("bmob", "更新失败：" + e.getMessage() + "," + e.getErrorCode());
                    }
                }
            });
            invalidate();

            return true;
        }

        return true;
    }

    private Point getValiPonit(int x, int y) {
        return new Point((int) (x / mLineHeight), (int) (y / mLineHeight));
    }

    private static final String INSTANCE = "instance";
    private static final String INSTANCE_GAME_OVER = "instance_game_over";
    private static final String INSTANCE_WHITE_ARRAY = "instance_white_array";
    private static final String INSTANCE_BLACK_ARRAY = "instance_black_array";
    private static final String INSTANCE_IsWhite = "instance_IsWhite";

    /**
     * 存自己的数据
     *
     * @return
     */
    @Override
    protected Parcelable onSaveInstanceState() {

        Bundle bundle = new Bundle();
        bundle.putParcelable(INSTANCE, super.onSaveInstanceState());
        bundle.putBoolean(INSTANCE_GAME_OVER, mIsGameOver);
        bundle.putParcelableArrayList(INSTANCE_WHITE_ARRAY, mWhiteArray);
        bundle.putParcelableArrayList(INSTANCE_BLACK_ARRAY, mBlackArray);
        bundle.putBoolean(INSTANCE_IsWhite, mIsWhite);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {

        //判断是否是自己存的数据
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            mIsGameOver = bundle.getBoolean(INSTANCE_GAME_OVER);
            mIsWhite = bundle.getBoolean(INSTANCE_IsWhite);
            mWhiteArray = bundle.getParcelableArrayList(INSTANCE_WHITE_ARRAY);
            mBlackArray = bundle.getParcelableArrayList(INSTANCE_BLACK_ARRAY);
            super.onRestoreInstanceState(bundle.getParcelable(INSTANCE));
            return;
        }
        super.onRestoreInstanceState(state);

    }

    public void start() {
        if (mBlackArray.size() != 0 || mWhiteArray.size() != 0) {
            new AlertDialog.Builder(getContext()).setTitle("提示").setMessage("确定开始新的一局?")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mWhiteArray.clear();
                            mBlackArray.clear();
                            mIsGameOver = false;
                            mIsWhiteWinner = false;
                            invalidate();

                        }
                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            })
                    .create().show();

        }

    }

    public void back() {
        if (mBlackArray.size() == 0 & mWhiteArray.size() == 0) {
            return;
        }
        if (mIsWhite) {
            mBlackArray.remove(mBlackArray.size() - 1);
        } else {
            mWhiteArray.remove(mWhiteArray.size() - 1);
        }
        mIsWhite = !mIsWhite;
        invalidate();
    }

}
