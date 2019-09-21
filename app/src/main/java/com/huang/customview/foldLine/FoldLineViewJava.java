package com.huang.customview.foldLine;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static android.view.View.MeasureSpec.EXACTLY;
import static android.view.View.MeasureSpec.getMode;
import static android.view.View.MeasureSpec.getSize;
import static com.huang.customview.extended.ExtendedKt.dp2px;
import static com.huang.customview.extended.ExtendedKt.sp2px;
import static java.lang.System.in;

public class FoldLineViewJava extends View {
    private final String DEFAULT_LEVEL_TEXT = "好";

    private float cellWidth = 0f;

    private float cellHeight = 0f;
    private float textSize = sp2px(16, getContext().getResources());

    private float levelWidth;
    private float weekTextHeight;
    private int cellTopMargin = dp2px(25, getContext().getResources());// 单元格距离顶部文字的间距
    private int cellLeftMargin = dp2px(10, getContext().getResources()); // 单元格距离左边文字的间距
    private int radius = dp2px(5, getContext().getResources());

    private int cellRawNum = 3;
    private int cellColumnNum = 7;
    private int defaultHeight = dp2px(170, getContext().getResources());

    private Paint paint = new Paint();

    private List<String> levelList = Arrays.asList("好", "中", "差");
    private List<Pair<Float, Float>> dotsPosList = new ArrayList<>();
    // 需要绘制的点，例如<'好'，0> （0代表星期一）
    private List<Pair<String, Integer>> data = new ArrayList<>();


    public FoldLineViewJava(Context context) {
        this(context, null);
    }

    public FoldLineViewJava(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        paint.setAntiAlias(true);
        paint.setTextSize(textSize);
        paint.setStyle(Paint.Style.FILL);

        levelWidth = getTextWidth(DEFAULT_LEVEL_TEXT);
        weekTextHeight = getTextHeight(DEFAULT_LEVEL_TEXT);
    }

    public void setData(List<Pair<String, Integer>> data) {
        this.data = data;
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = (getMode(heightMeasureSpec) == EXACTLY)
                ? getSize(heightMeasureSpec)
                : defaultHeight;
        setMeasuredDimension(getSize(widthMeasureSpec), height);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        // 计算出单元格的宽高，以便后面绘制圆点
        cellWidth = (getWidth() - levelWidth * 2 - cellLeftMargin) / (cellColumnNum - 1);
        cellHeight = (getHeight() - weekTextHeight * 2 - cellTopMargin) / (cellRawNum - 1);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        drawCellAndText(canvas);
        drawDots(canvas);
        drawFoldLine(canvas);
    }

    /**
     * 绘制点之间的连线
     */
    private void drawFoldLine(Canvas canvas) {
        if (dotsPosList.size() < 2) return;
        paint.setColor(Color.WHITE);
        Collections.sort(dotsPosList, new Comparator<Pair<Float, Float>>() {
            @Override
            public int compare(Pair<Float, Float> o1, Pair<Float, Float> o2) {
                return (int) (o1.first - o2.first);
            }
        });

        float firstDotX = dotsPosList.get(0).first;
        float firstDotY = dotsPosList.get(0).second;

        for (int i = 0; i < dotsPosList.size(); i++) {
            if (i == 0) continue;
            Pair<Float, Float> dot = dotsPosList.get(i);
            canvas.drawLine(firstDotX, firstDotY, dot.first, dot.second, paint);
            firstDotX = dot.first;
            firstDotY = dot.second;
        }
    }

    /**
     * 绘制小圆点
     */
    private void drawDots(Canvas canvas) {
        dotsPosList.clear();
        paint.setColor(Color.RED);
        for (int i = 0; i < data.size(); i++) {
            Pair<String, Integer> pair = data.get(i);
            if (levelList.contains(pair.first) && pair.second >= 0 && pair.second <= cellColumnNum) {
                Pair<Float, Float> pos = getPosByIndex(pair);
                canvas.drawCircle(pos.first, pos.second, radius, paint);
                dotsPosList.add(pos);
            }
        }
    }

    /**
     * 绘制单元格 和 文字
     */
    private void drawCellAndText(Canvas canvas) {
        for (int i = 0; i < cellRawNum; i++) {
            float startX = levelWidth + cellLeftMargin;
            float startY = weekTextHeight + cellTopMargin + i * cellHeight;

            paint.setTextAlign(Paint.Align.LEFT);
            paint.setColor(Color.WHITE);
            canvas.drawText(levelList.get(i), 0f, startY + getCenteredY(), paint);
            paint.setColor(Color.GRAY);
            canvas.drawLine(startX, startY, getWidth() - levelWidth, startY, paint);
        }

        for (int j = 0; j < cellColumnNum; j++) {
            float startY = weekTextHeight + cellTopMargin;
            float startX = levelWidth + cellLeftMargin + j * cellWidth;

            paint.setTextAlign(Paint.Align.CENTER);
            paint.setColor(Color.WHITE);
            canvas.drawText(getWeekText(j), startX, getToppedY(), paint);
            paint.setColor(Color.GRAY);
            canvas.drawLine(startX, startY, startX, getHeight() - weekTextHeight, paint);
        }
    }

    private Pair<Float, Float> getPosByIndex(Pair<String, Integer> index) {
        float startX = levelWidth + cellLeftMargin;
        float startY = weekTextHeight + cellTopMargin;

        float cX = startX + index.second * cellWidth;
        float cY = startY + levelList.indexOf(index.first) * cellHeight;

        return Pair.create(cX, cY);
    }

    private Float getTextWidth(String text) {
        if (text.isEmpty()) return 0f;
        Rect rect = new Rect();
        paint.getTextBounds(text, 0, text.length(), rect);
        return (float) (rect.right - rect.left);
    }

    private Float getTextHeight(String text) {
        if (text.isEmpty()) return 0f;
        Rect rect = new Rect();
        paint.getTextBounds(text, 0, text.length(), rect);
        return (float) (rect.bottom - rect.top);
    }

    private Float getCenteredY() {
        return paint.getFontSpacing() / 2 - paint.getFontMetrics().bottom;
    }

    private Float getToppedY() {
        return -paint.getFontMetrics().ascent;
    }

    private String getWeekText(int i) {
        switch (i) {
            case 0:
                return "周一";
            case 1:
                return "周二";
            case 2:
                return "周三";
            case 3:
                return "周四";
            case 4:
                return "周五";
            case 5:
                return "周六";
            case 6:
                return "周日";
            default:
                return getWeekText(0);
        }
    }
}
