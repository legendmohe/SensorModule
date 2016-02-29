package vendor.ui;

import android.content.Context;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Administrator on 2016/1/27.
 */
public class StepCountView extends View {

    public static final String TAG = "StepCountView";

    private int mCenterX;
    private int mCenterY;
    private float mTouchX;
    private float mTouchY;
    private float mCanvasRotateX = 0;
    private float mCanvasRotateY = 0;
    private float mCanvasMaxRotateDegree = 50;
    private Matrix mMatrix = new Matrix();
    private Camera mCamera = new Camera();
    private Paint mPaint;
    private Path mPath;

    public StepCountView(Context context) {
        super(context);
    }

    public StepCountView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCanvasMaxRotateDegree = 20;
        mPath = new Path();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        mCenterX = getWidth() / 2;
        mCenterY = getHeight() / 2;

        rotateCanvas(canvas);
        mPaint.setColor(Color.WHITE);
        mPaint.setStrokeWidth(2);

        drawText(canvas);
        drawArc(canvas);
    }

    private void drawText(Canvas canvas) {
        mPaint.setTextSize(80);
        mPaint.setTextAlign(Paint.Align.CENTER);
        Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
        float baseY = getHeight()/2 + (fontMetrics.descent - fontMetrics.ascent)/2 - fontMetrics.bottom;
        canvas.drawText("7687 Steps", mCenterX, baseY, mPaint);
    }

    private void drawArc(Canvas canvas) {
        canvas.save();
        int fill_present = 80;
        int radius = Math.max(getWidth(), getHeight())/2 - 20;
        for (int i = 0; i < 120; i++) {
            if (i < 120*0.8)
                mPaint.setStrokeWidth(6);
            else
                mPaint.setStrokeWidth(2);
            mPaint.setAlpha(255);
            canvas.drawLine(mCenterX, mCenterX - (radius - 20), mCenterX, mCenterX - radius, mPaint);
            canvas.rotate(3,mCenterX,mCenterY);
        }
        canvas.restore();
    }

    private void rotateCanvas(Canvas canvas) {
        mMatrix.reset();
        mCamera.save();
        mCamera.rotateX(mCanvasRotateX);
        mCamera.rotateY(mCanvasRotateY);
        mCamera.getMatrix(mMatrix);
        mCamera.restore();
        mMatrix.preTranslate(-mCenterX, -mCenterY);
        mMatrix.postTranslate(mCenterX, mCenterY);

        canvas.concat(mMatrix);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        mTouchX = x;
        mTouchY = y;

        int action = event.getActionMasked();
//        Log.d(TAG, "action:" + action);
        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                getParent().requestDisallowInterceptTouchEvent(true);
                rotateCanvasWhenMove(x, y);
                return true;
            }
            case MotionEvent.ACTION_MOVE: {
                rotateCanvasWhenMove(x, y);
                invalidate();
                return true;
            }
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
            {
                getParent().requestDisallowInterceptTouchEvent(false);
                mCanvasRotateY = 0;
                mCanvasRotateX = 0;
                invalidate();
                return true;
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        boolean result = super.dispatchTouchEvent(event);
//        Log.d(TAG, "dispatchTouchEvent:" + result);
        return result;
    }

    private void rotateCanvasWhenMove(float x, float y) {
        float dx = x - mCenterX;
        float dy = y - mCenterY;

        float percentX = dx / mCenterX;
        float percentY = dy / mCenterY;

        if (percentX > 1f) {
            percentX = 1f;
        } else if (percentX < -1f) {
            percentX = -1f;
        }
        if (percentY > 1f) {
            percentY = 1f;
        } else if (percentY < -1f) {
            percentY = -1f;
        }

        mCanvasRotateY = mCanvasMaxRotateDegree * percentX;
        mCanvasRotateX = -(mCanvasMaxRotateDegree * percentY);
    }


}


