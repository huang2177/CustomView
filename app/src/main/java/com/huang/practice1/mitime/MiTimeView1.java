package com.huang.practice1.mitime;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.util.Calendar;

/**
 * Des: 仿小米时间控件
 * Created by hs on 2018/7/30 0030 10:25
 */
public class MiTimeView1 extends View {

    private Paint mArcPaint;
    private Paint mTextPaint;
    private Paint mNamePaint;
    private Paint mHourPaint;
    private Paint mSecondPaint;
    private Paint mPaintMinute;
    private Paint mPaintTriangle;
    private Paint mProgressPaint;
    private Paint mProgressBgPaint;
    private Paint mCenterBallPaint;

    private int mWidth, mHeight;
    private int mCenterX, mCenterY;
    private int paddingOut = 30;
    private float mInnerRadius;

    private float mSecondDegress, mMinuteDegress, mHourDegress, mSecondMillsDegress;

    public MiTimeView1(Context context) {
        this(context, null);
    }

    public MiTimeView1(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MiTimeView1(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        int color = Color.parseColor("#ffffff");

        mArcPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mArcPaint.setColor(color);
        mArcPaint.setStrokeWidth(2);
        mArcPaint.setStyle(Paint.Style.STROKE);

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(color);
        mTextPaint.setTextSize(32);
        mTextPaint.setTypeface(Typeface.DEFAULT_BOLD);
        mTextPaint.setTextAlign(Paint.Align.CENTER);

        mNamePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mNamePaint.setColor(color);
        mNamePaint.setTextSize(66);
        mNamePaint.setTypeface(Typeface.MONOSPACE);
        mNamePaint.setTextAlign(Paint.Align.CENTER);

        mProgressBgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mProgressBgPaint.setColor(Color.parseColor("#4Cffffff"));
        mProgressBgPaint.setStrokeWidth(4);
        mProgressBgPaint.setStyle(Paint.Style.STROKE);

        mProgressPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mProgressPaint.setStrokeWidth(4);
        mProgressPaint.setStyle(Paint.Style.STROKE);

        mInnerRadius = 24f;
        mCenterBallPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCenterBallPaint.setColor(color);
        mCenterBallPaint.setStrokeWidth(8);
        mCenterBallPaint.setStyle(Paint.Style.STROKE);

        mHourPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mHourPaint.setColor(Color.parseColor("#ffffff"));
        mHourPaint.setStyle(Paint.Style.FILL);

        mPaintMinute = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintMinute.setColor(Color.parseColor("#ffffff"));
        mPaintMinute.setStrokeWidth(6);
        mPaintMinute.setStrokeCap(Paint.Cap.ROUND);
        mPaintMinute.setStyle(Paint.Style.STROKE);

        mSecondPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mSecondPaint.setColor(Color.parseColor("#ffffff"));
        mSecondPaint.setStyle(Paint.Style.FILL);

        mPaintTriangle = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintTriangle.setColor(color);
        mPaintTriangle.setStyle(Paint.Style.FILL);

        calculateDegree();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measureSpec(widthMeasureSpec), measureSpec(heightMeasureSpec));
    }

    private int measureSpec(int measureSpec) {
        int mode = MeasureSpec.getMode(measureSpec);
        if (mode == MeasureSpec.EXACTLY) {
            return View.MeasureSpec.getSize(measureSpec);
        } else {
            return 600;
        }
    }

    /**
     * 该方法被调用的时候表示已经测量完成
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = w;
        mCenterX = w / 2;
        mCenterY = h / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate((float) mCenterX, (float) mCenterY);
        drawArcCircle(canvas);
        drawOutText(canvas);
        //drawName(canvas);
        drawCalibrationLine(canvas);
        drawHour(canvas);
        drawMinute(canvas);
        drawSecond(canvas);
        drawCenterBall(canvas);
    }

    /**
     * 画秒针
     *
     * @param canvas
     */
    private void drawSecond(Canvas canvas) {
        canvas.save();
        canvas.rotate(mSecondDegress);
        Path path = new Path();
        path.moveTo(0f, -(mHeight * 3 / 8) + 20);
        path.lineTo(-20, -(mHeight * 3 / 8) + 40);
        path.lineTo(20, -(mHeight * 3 / 8) + 40);
        path.close();
        canvas.drawPath(path, mPaintTriangle);
        canvas.restore();

        //绘制渐变刻度
        for (int i = 0; i <= 90; i++) {
            if (i % 6 == 0) {
                canvas.save();
                mProgressPaint.setARGB((int) (255 - 2.7 * i), 255, 255, 255);
                //这里的先减去90°，是为了旋转到开始角度，因为开始角度是y轴的负方向
                canvas.rotate(((mSecondDegress - 90 - i)));
                float stopX = (float) (mHeight / 2 * 0.7) + 20;
                canvas.drawLine((float) (mHeight / 2 * 0.7), 0f, stopX, 0f, mProgressPaint);
                canvas.restore();
            }
        }
    }

    /**
     * 画分针
     *
     * @param canvas
     */
    private void drawMinute(Canvas canvas) {
        canvas.save();
        canvas.rotate(mMinuteDegress);
        canvas.drawLine(0f, -(mInnerRadius / 2 + 4), 0f, -(mHeight / 3) + 25, mPaintMinute);
        canvas.restore();
    }

    /**
     * 画时针
     *
     * @param canvas
     */
    private void drawHour(Canvas canvas) {
        Path path = new Path();
        path.moveTo(-mInnerRadius / 4, -(mInnerRadius / 2 + 8) / 2);
        path.lineTo(mInnerRadius / 4, -(mInnerRadius / 2 + 8) / 2);
        path.lineTo(mInnerRadius / 8, -(mHeight / 4));
        path.lineTo(-mInnerRadius / 8, -(mHeight / 4));
        path.close();
        canvas.save();
        canvas.rotate(mHourDegress);
        canvas.drawPath(path, mHourPaint);
        canvas.restore();
    }

    /**
     * 画中心小圆
     *
     * @param canvas
     */
    private void drawCenterBall(Canvas canvas) {
        canvas.drawCircle(0f, 0f, mInnerRadius / 2, mCenterBallPaint);
    }

    /**
     * 绘制刻度线
     * desc:每个2°绘制一个刻度，也就是有180个刻度
     *
     * @param canvas
     */
    private void drawCalibrationLine(Canvas canvas) {
        for (int i = 0; i < 360; i++) {
            if (i % 6 == 0) {
                canvas.save();
                canvas.rotate((float) i);
                float stopX;
                if (i % 30 == 0) {
                    stopX = (float) (mHeight / 2 * 0.7) + 30;
                    mProgressBgPaint.setColor(Color.parseColor("#ffffff"));
                } else {
                    stopX = (float) (mHeight / 2 * 0.7) + 20;
                    mProgressBgPaint.setColor(Color.parseColor("#4Cffffff"));
                }
                canvas.drawLine((float) (mHeight / 2 * 0.7), 0f, stopX, 0f, mProgressBgPaint);
                canvas.restore();
            }
        }
    }

    private void drawName(Canvas canvas) {
        canvas.drawText("erva", 0f, -(mHeight / 7), mNamePaint);
    }

    /**
     * 画外层文字（3,6,9,12）
     *
     * @param canvas
     */
    private void drawOutText(Canvas canvas) {
        float textRadius = (mHeight - paddingOut) / 2;

        Paint.FontMetrics fm = mTextPaint.getFontMetrics();
        float textHeight = (fm.descent - fm.ascent) / 2 - fm.descent;

        canvas.drawText("3", textRadius, (float) textHeight, mTextPaint);
        canvas.drawText("9", -textRadius, (float) textHeight, mTextPaint);

        canvas.drawText("6", 0f, textRadius + textHeight, mTextPaint);
        canvas.drawText("12", 0f, -textRadius + textHeight, mTextPaint);
    }


    /**
     * 画外层圆弧（5-85,95-175,185 -265,275-355）
     *
     * @param canvas
     * @return
     */
    private void drawArcCircle(Canvas canvas) {
        RectF rect = new RectF(-(mHeight - paddingOut) / 2
                , -(mHeight - paddingOut) / 2
                , (mHeight - paddingOut) / 2
                , (mHeight - paddingOut) / 2);
        canvas.drawArc(rect, 5f, 80f, false, mArcPaint);
        canvas.drawArc(rect, 95f, 80f, false, mArcPaint);
        canvas.drawArc(rect, 185f, 80f, false, mArcPaint);
        canvas.drawArc(rect, 275f, 80f, false, mArcPaint);
    }


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        startTime();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopTime();
    }

    private Runnable mRunnable = () -> {
        calculateDegree();
        invalidate();
        startTime();
    };

    /**
     * 计算转动的角度
     */
    private void calculateDegree() {
        Calendar mCalendar = Calendar.getInstance();
        mCalendar.setTimeInMillis(System.currentTimeMillis());
        int minute = mCalendar.get(Calendar.MINUTE);
        int secondMills = mCalendar.get(Calendar.MILLISECOND);
        int second = mCalendar.get(Calendar.SECOND);
        int hour = mCalendar.get(Calendar.HOUR);
        mHourDegress = hour * 30 + (minute / 60) * 360;
        mMinuteDegress = minute * 6;
        mSecondMillsDegress = second * 6 + secondMills * 0.006f;
        mSecondDegress = second * 6;
        float mills = secondMills * 0.006f;

        //因为是每2°旋转一个刻度，所以这里要根据毫秒值来进行计算
        if (mills >= 2 && mills < 4) {
            mSecondDegress += 6;
        } else if (mills >= 4 && mills < 6) {
            mSecondDegress += 12;
        }
    }

    public void startTime() {
        postDelayed(mRunnable, 1000);
    }


    public void stopTime() {
        removeCallbacks(mRunnable);
    }
}
