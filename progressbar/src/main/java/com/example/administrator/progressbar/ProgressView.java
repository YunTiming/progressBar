package com.example.administrator.progressbar;

import android.animation.AnimatorSet;
import android.animation.FloatEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.res.TypedArray;
import android.view.View;
import android.content.Context;
import android.graphics.Canvas;

import android.graphics.Paint;
import android.graphics.RectF;

import android.util.AttributeSet;

import java.text.DecimalFormat;


/**
 * Created by Administrator on 2016/5/19.
 */
public class ProgressView extends View {

    /**
     * 进度条最大值
     */
    private float maxCount = 100;
    /**
     * 进度条当前值
     */
    private float currentCount;
    /**
     * 画笔
     */
    private Paint mPaint;
    private int mWidth, mHeight;
    int progressColor;
    private float textSize;
    private float text;
    private String unit;//后面的单位

    private float progress;

    public ProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ProgressViewSetting);
        progressColor = ta.getColor(R.styleable.ProgressViewSetting_progressColor, 0xff6699FF);
        textSize = ta.getDimension(R.styleable.ProgressViewSetting_textSize, dipToPx(18));
        initView(context);
    }

    public ProgressView(Context context) {
        this(context, null);
        initView(context);
    }

    private void initView(Context context) {
        unit = "";
        progress = currentCount / maxCount;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        float strokeWidth = dipToPx(4);
        System.out.println("max=" + maxCount + "  current=" + currentCount);
        mPaint.setColor(0xffc0c0c0);
        mPaint.setStyle(Paint.Style.STROKE);//空心矩形框
        mPaint.setStrokeWidth(strokeWidth);
        RectF rectBg = new RectF(0, 0, mWidth, mHeight);
        canvas.drawRoundRect(rectBg, 0, 0, mPaint);


        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(progressColor);

        RectF rectProgressBg = new RectF(strokeWidth / 2, strokeWidth / 2, (mWidth - strokeWidth / 2) * progress, mHeight - strokeWidth / 2);
        canvas.drawRoundRect(rectProgressBg, 0, 0, mPaint);
        mPaint.setTextSize(textSize);
        DecimalFormat dcmFmt = new DecimalFormat("0.0");
        String format = dcmFmt.format(text);
        float textWidth = mPaint.measureText(format + unit);
        int y = (int) (-(mPaint.descent() + mPaint.ascent()) / 2);
        if (mWidth * progress + textWidth <= mWidth) {
            canvas.drawText(format + unit, mWidth * progress, y + mHeight / 2, mPaint);
        } else {
            mPaint.setColor(0Xffffffff);
            canvas.drawText(format + unit, mWidth * progress - textWidth-dipToPx(5), y + mHeight / 2, mPaint);
        }


    }


    private int dipToPx(int dip) {
        float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dip * scale + 0.5f * (dip >= 0 ? 1 : -1));
    }

    /**
     * 设置进度条后面的显示
     *
     * @param text
     */
    public void setText(float text) {
        this.text = text;
        invalidate();
    }

    public float getText() {
        return text;
    }


    /**
     * 设置显示的单位
     */

    public void setUnit(String Unit) {
        this.unit = Unit;
    }

    /***
     * 设置最大的进度值
     *
     * @param maxCount
     */
    public void setMaxCount(float maxCount) {
        this.maxCount = maxCount;
    }


    public float getMaxCount() {
        return maxCount;
    }


    private float lastCount;

    /***
     * 设置当前的进度值
     *
     * @param currentCount
     */
    public void setCurrentCount(float currentCount) {
        this.currentCount = currentCount > maxCount ? maxCount : currentCount;

        AnimatorSet set = new AnimatorSet();
        ValueAnimator animator = ObjectAnimator.ofFloat(this, "progress", lastProcess, this.currentCount / maxCount);
        animator.setEvaluator(new FloatEvaluator());
        ValueAnimator animator2 = ObjectAnimator.ofFloat(this, "text", lastCount,this.currentCount);
        animator2.setEvaluator(new FloatEvaluator());
        set.setDuration(1000);
        set.playTogether(animator, animator2);
        set.start();
        lastProcess = currentCount / maxCount;
        lastCount = currentCount;
    }

    public float getCurrentCount() {
        return currentCount;
    }

    private float lastProcess = 0f;

    public void setProgress(float progress) {
        this.progress = progress;
        invalidate();
    }

    public float getProgress() {
        return progress;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);
        if (widthSpecMode == MeasureSpec.EXACTLY || widthSpecMode == MeasureSpec.AT_MOST) {
            mWidth = widthSpecSize;
        } else {
            mWidth = 0;
        }
        if (heightSpecMode == MeasureSpec.AT_MOST || heightSpecMode == MeasureSpec.UNSPECIFIED) {
            mHeight = dipToPx(15);
        } else {
            mHeight = heightSpecSize;
        }
        setMeasuredDimension(mWidth, mHeight);
    }


}