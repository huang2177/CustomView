package com.huang.practice1.process;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.huang.practice1.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Des: 自定义流程图
 * Created by huang on 2018/10/8 0008 11:50
 */
public class ProcessView1 extends View {
    //点击事件的时间差
    private static final int TOUCH_TIME = 1000;
    //上一次点击的时间
    private long preTime = 0L;
    //当前点击的时间
    private long currTime = 0L;

    //垂直方向item的个数
    private int verticalNum = 0;

    //水平方向item的个数 (与垂直方向一一对应)
    private String[] horizontalNum = null;

    //item的宽度
    private int itemWidth = 0;

    //item的高度
    private int itemHeight = 0;

    //divider的高度
    private int verticalSpace = 0;

    //item的颜色
    private int itemColor = Color.BLACK;

    //item的字体大小
    private float itemTextSize = 0f;

    //item水平间距
    private int horizontalSpace = 30;

    private float textHeight = 0f;

    private Paint paint = new Paint();

    private Rect lastItemRect;

    //用来保存每隔item的Rect，用于后面的点击事件判断
    private HashMap<Integer, List<Rect>> rects = new HashMap<>();

    private HashMap<Integer, List<String>> texts;
    private HashMap<Integer, List<Boolean>> isRecorder;

    private OnItemClickListener listener = null;

    public ProcessView1(Context context) {
        this(context, null);
    }

    public ProcessView1(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.ProcessView1);
            itemColor = typedArray.getColor(R.styleable.ProcessView1_itemColor, Color.BLACK);
            verticalNum = typedArray.getInt(R.styleable.ProcessView1_verticalNum, 0);
            itemTextSize = typedArray.getDimension(R.styleable.ProcessView1_itemTextSize, 16f);
            itemHeight = (int) typedArray.getDimension(R.styleable.ProcessView1_itemHeight, 30f);
            itemWidth = (int) typedArray.getDimension(R.styleable.ProcessView1_itemWidth, 150f);
            verticalSpace = (int) typedArray.getDimension(R.styleable.ProcessView1_itemVerticalSpace, 30f);
            horizontalSpace = (int) typedArray.getDimension(R.styleable.ProcessView1_itemHorizontalSpace, 30f);
            horizontalNum = typedArray.getString(R.styleable.ProcessView1_horizontalNum).split(",");
            typedArray.recycle();
        }
        paint.setAntiAlias(true);
        paint.setColor(itemColor);
        paint.setTextSize(itemTextSize);
        paint.setStyle(Paint.Style.STROKE);
        paint.setTextAlign(Paint.Align.CENTER);

        textHeight = paint.getFontMetrics().descent - paint.getFontMetrics().ascent + paint.getFontMetrics().leading;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measureSize(0, widthMeasureSpec), measureSize(1, heightMeasureSpec));
    }

    /**
     * 测量 子View宽高
     */
    private int measureSize(int type, int measureSpec) {
        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);
        int newSize = 0;
        if (mode == MeasureSpec.EXACTLY) {
            newSize = size;
            if (type == 0) {
                //horizontalSpace = (newSize - verticalNum * itemHeight) / (verticalNum - 1);
            } else {
                verticalSpace = (newSize - verticalNum * itemHeight) / (verticalNum - 1);
            }
        } else if (mode == MeasureSpec.AT_MOST) {
            if (type == 0) {
                //newSize = verticalNum * (itemHeight + verticalSpace);
            } else {
                newSize = verticalNum * (itemHeight + verticalSpace);
            }
        }
        return newSize;
    }

    @Override
    public void onDraw(Canvas canvas) {
        for (int vIndex = 0; vIndex < verticalNum; vIndex++) {
            List<Rect> list = new ArrayList<>();
            int hNum = Integer.parseInt(horizontalNum[vIndex]);
            // item实际宽度
            int tempWidth = (getWidth() - horizontalSpace * (hNum - 1)) / hNum;

            for (int hIndex = 0; hIndex < hNum; hIndex++) {
                Rect rectBorder = new Rect();
                String str = texts != null ? texts.get(vIndex).get(hIndex) : "----";
                rectBorder.top = vIndex * itemHeight + vIndex * verticalSpace;
                rectBorder.bottom = rectBorder.top + itemHeight;
                if (tempWidth >= itemWidth) {
                    // 距离两边的距离
                    int margin = (getWidth() - (itemWidth * hNum + (hNum - 1) * horizontalSpace)) / 2;
                    rectBorder.left = margin + hIndex * itemWidth + hIndex * horizontalSpace;
                    rectBorder.right = itemWidth + rectBorder.left;
                } else {
                    rectBorder.left = hIndex * tempWidth + hIndex * horizontalSpace;
                    rectBorder.right = tempWidth + rectBorder.left;
                }
                drawBackground(canvas, rectBorder, isRecorder.get(vIndex).get(hIndex));
                drawContent(canvas, rectBorder, str, isRecorder.get(vIndex).get(hIndex));
                drawLinkLine(canvas, rectBorder, hNum, vIndex);
                list.add(rectBorder);
            }
            rects.put(vIndex, list);
        }
    }

    /***
     * 画背景
     * @param isRecordColor
     */
    private void drawBackground(Canvas canvas, Rect rect, boolean isRecordColor) {
        paint.setColor(isRecordColor ? Color.RED : itemColor);
        canvas.drawRect(rect, paint);
    }

    /***
     * 画文字
     * @param isRecordColor
     */
    private void drawContent(Canvas canvas, Rect rect, String str, boolean isRecordColor) {
        paint.setColor(isRecordColor ? Color.GREEN : itemColor);
        canvas.drawText(str
                , (rect.right + rect.left) / 2f
                , (rect.bottom + rect.top) / 2f + textHeight / 2 - paint.getFontMetrics().descent
                , paint);

    }

    /***
     * 画连接线
     * @param rect     当前Item的Rect
     * @param currHNum 当前的水平方向 有多少个Item
     * @param vIndex   当前是垂直方向的第几个
     */
    private void drawLinkLine(Canvas canvas, Rect rect, int currHNum, int vIndex) {
        int stopX = getX(rect);
        int startX = getX(rect);
        paint.setColor(itemColor);

        if (vIndex < verticalNum - 1) {
            //画Item下面一半的垂直连接线
            canvas.drawLine(startX, rect.bottom, stopX, (rect.bottom + verticalSpace / 2f), paint);

            //画水平连接线
            if (currHNum > 1 && lastItemRect != null && lastItemRect.top == rect.top) {
                canvas.drawLine(getX(lastItemRect), (rect.bottom + verticalSpace / 2f), getX(rect), (rect.bottom + verticalSpace / 2f), paint);
            }
        }

        if (vIndex != 0) {
            //画Item上面一半的垂直连接线
            canvas.drawLine(startX, (rect.top - verticalSpace / 2f), stopX, rect.top, paint);

            //画水平连接线
            if (currHNum > 1 && lastItemRect != null && lastItemRect.top == rect.top) {
                canvas.drawLine(getX(lastItemRect), (rect.top - verticalSpace / 2f), getX(rect), (rect.top - verticalSpace / 2f), paint);
            }
        }
        lastItemRect = rect;
    }

    private int getX(Rect rect) {
        return (int) ((rect.left + rect.right) / 2f);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                preTime = System.currentTimeMillis();
                break;
            case MotionEvent.ACTION_UP:
                currTime = System.currentTimeMillis();
                Result result = isTouchPointInView(event);
                if (currTime - preTime < TOUCH_TIME && result.isTouchInView) {
                    listener.onItemClick(result.vPosition, result.hPosition, "");
                }
                break;
        }
        return true;
    }

    /**
     * 判断点击事件是否在某个Item上面
     */
    private Result isTouchPointInView(MotionEvent event) {
        if (rects.size() == 0) return new Result(false, 0, 0);

        int eventX = (int) event.getRawX();
        int eventY = (int) event.getY();

        for (int vIndex = 0; vIndex < rects.size(); vIndex++) {
            List<Rect> list = rects.get(vIndex);
            for (int hIndex = 0; hIndex < list.size(); hIndex++) {
                Rect rect = list.get(hIndex);
                if (rect.contains(eventX, eventY)) {
                    return new Result(true, vIndex, hIndex);
                }
            }
        }
        return new Result(false, 0, 0);
    }

    /***
     * 填充数据 设置进度
     * @param data
     * @param recorder
     */
    public void setData(HashMap<Integer, List<String>> data, HashMap<Integer, List<Boolean>> recorder) {
        this.texts = data;
        this.isRecorder = recorder;
        invalidate();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(int vPosition, int hPosition, String text);
    }

    class Result {
        int vPosition;
        int hPosition;
        boolean isTouchInView;

        Result(boolean isTouchInView, int vPosition, int hPosition) {
            this.vPosition = vPosition;
            this.hPosition = hPosition;
            this.isTouchInView = isTouchInView;
        }
    }
}
