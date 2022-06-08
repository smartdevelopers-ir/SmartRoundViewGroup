package ir.smartdevelopers.smartroundviewgroup;

import android.content.res.ColorStateList;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RoundDrawable extends Drawable {
    private final Paint mBackgroundPaint;
    private final Paint mShadowPaint;
    private final Path mPath;
    private RectF rectF;
    private RectF mShadowRect;
    float mShadowTopOffset, mShadowLeftOffset, mShadowRightOffset, mShadowBottomOffset;
    boolean mShowShadow;
    float mShadowRadius;
    int mShadowColor;
    float mShadowDx, mShadowDy;
    private ColorStateList mBackgroundColor;
    private int mCurrentColor;
    private int mGradientStartColor = 0, mGradientEndColor = 0;
    private float mGradientAngle = 0;


    private final float[] corners = {
            30, 30, // top left
            30, 30, // top right
            0, 0, // bottom right
            0, 0 // bottom left
    };

    @Override
    public boolean isStateful() {
        return true;
    }

    @Override
    protected boolean onStateChange(int[] state) {
        int stateColor = mBackgroundColor.getColorForState(state, mBackgroundColor.getDefaultColor());
        updateColor(stateColor);
        return true;
    }

    public void setGradientColor(int startColor, int endColor, float angle) {
        mGradientStartColor = startColor;
        mGradientEndColor = endColor;
        mGradientAngle = angle;
        invalidateSelf();
    }

    public RoundDrawable() {
        mBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBackgroundPaint.setColor(Color.BLACK);
        mBackgroundPaint.setStyle(Paint.Style.FILL);

        mShadowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mShadowPaint.setStyle(Paint.Style.FILL);
        mShadowPaint.setColor(Color.argb(128, 0, 0, 0));

        mPath = new Path();
        rectF = new RectF();
        mShadowRect = new RectF();


    }

    private float[][] getAxis(RectF rectF, float angle) {
        float[][] axis = new float[2][2];
        float squareLength = rectF.right - rectF.left;
        float squareHeight = rectF.bottom - rectF.top;
        List<float[]> angles = new ArrayList<>();
        /*squareRadius=(sqrt(length^2+length^2))/2*/
        float squareRadius = (float) (Math.sqrt(Math.pow(squareLength, 2) + Math.pow(squareHeight, 2)) / 2);
        /*find right side part angle*/
        /*A=cos-1 ( (b^2 + c^2 - a^2 ) / 2bc )*/
        float part1Angel = (float) Math.toDegrees(Math.acos(((Math.pow(squareRadius, 2) * 2) - Math.pow(squareHeight, 2)) / (2 * squareRadius * squareRadius)));
        float part2Angel = (380 - part1Angel * 2) / 2;
        angles.add(new float[]{360 - (part1Angel / 2), part1Angel / 2});
        angles.add(new float[]{angles.get(0)[1], angles.get(0)[1] + part2Angel});
        angles.add(new float[]{angles.get(1)[1], angles.get(1)[1] + part1Angel});
        angles.add(new float[]{angles.get(2)[1], angles.get(2)[1] + part2Angel});
        float part = 360 - (Math.abs(360 - angle) % 360);

        float xStart = 0;
        float xEnd;
        float yStart = 0;
        float yEnd;
        float triangleAngle;
        float squareRadiusAngle;
        float circleRadius;
        if (part >= angles.get(0)[0] || part < angles.get(0)[1]) {
            xStart = rectF.right;


            if (part >= angles.get(0)[0] || part < 0) {
                triangleAngle = part - angles.get(0)[0];
            } else {
                triangleAngle = part;
            }
            yStart = calculateTriangleEdgeLength(squareRadius, triangleAngle);


        } else if (part >= angles.get(1)[0] && part < angles.get(1)[1]) {
            triangleAngle = part - angles.get(1)[0];
            xStart = calculateTriangleEdgeLength(squareRadius, triangleAngle);
            yStart = rectF.bottom;
        } else if (part >= angles.get(2)[0] && part < angles.get(2)[1]) {
            triangleAngle = part - angles.get(2)[0];
            xStart = rectF.left;
            yStart = calculateTriangleEdgeLength(squareRadius, triangleAngle);

        } else if (part >= angles.get(3)[0] && part < angles.get(3)[1]) {
            triangleAngle = part - angles.get(3)[0];
            yStart = rectF.top;
            xStart = calculateTriangleEdgeLength(squareRadius, triangleAngle);
        }

        xEnd = rectF.right - xStart;
        yEnd = rectF.bottom - yStart;
        axis[0][0] = xStart;
        axis[0][1] = yStart;
        axis[1][0] = xEnd;
        axis[1][1] = yEnd;
        return axis;
    }

    private float calculateTriangleEdgeLength(float squareRadius, float angle) {
        float squareRadiusAngle = 180 - 45 - angle;
        float circleRadius = (float) (squareRadius / Math.sin(Math.toRadians(squareRadiusAngle)) / 2);
        return (float) (Math.sin(Math.toRadians(angle)) * 2 * circleRadius);
    }

    public void setRadius(float topLeft, float topRight, float bottomRight, float bottomLeft) {
        float[] radius = {topLeft, topLeft, topRight, topRight, bottomRight, bottomRight, bottomLeft, bottomLeft};
        if (Arrays.equals(corners, radius)) {
            return;
        }
        corners[0] = topLeft;
        corners[1] = topLeft;
        corners[2] = topRight;
        corners[3] = topRight;
        corners[4] = bottomRight;
        corners[5] = bottomRight;
        corners[6] = bottomLeft;
        corners[7] = bottomLeft;
        invalidateSelf();
    }

    public void setColor(ColorStateList color) {
        mBackgroundColor = color;
        updateColor(mBackgroundColor.getDefaultColor());
    }

    private void updateColor(int color) {
        if (mBackgroundPaint.getColor() == color) {
            return;
        }
        mBackgroundPaint.setColor(color);
        invalidateSelf();
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        Rect bound = getBounds();
        rectF.set(mShadowLeftOffset, mShadowTopOffset,
                bound.right - mShadowRightOffset,
                bound.bottom - mShadowBottomOffset);

        if (mGradientEndColor != 0 && mGradientStartColor != 0) {
            float[][] axis = getAxis(rectF, mGradientAngle);
            mBackgroundPaint.setShader(new LinearGradient(axis[0][0], axis[0][1], axis[1][0], axis[1][1],
//            mBackgroundPaint.setShader(new LinearGradient(100,0,100,200,
                    mGradientStartColor, mGradientEndColor, Shader.TileMode.CLAMP));
        }

        if (mShowShadow) {

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
                mShadowRect.set(mShadowLeftOffset + mShadowDx,
                        mShadowTopOffset + mShadowDy,
                        bound.right - mShadowRightOffset + mShadowDx,
                        bound.bottom - mShadowBottomOffset + mShadowDy);
                mPath.reset();
                mPath.addRoundRect(mShadowRect, corners, Path.Direction.CW);
                mShadowPaint.setColor(mShadowColor);
//            mShadowPaint.setShadowLayer(mShadowRadius,mShadowDx,mShadowDy
//            ,mShadowColor);
                mShadowPaint.setMaskFilter(new BlurMaskFilter(mShadowRadius, BlurMaskFilter.Blur.NORMAL));
            } else {
                mPath.reset();
                mPath.addRoundRect(rectF, corners, Path.Direction.CW);
                mShadowPaint.setColor(mShadowColor);
                mShadowPaint.setShadowLayer(mShadowRadius, mShadowDx, mShadowDy
                        , mShadowColor);
                mShadowPaint.setMaskFilter(new BlurMaskFilter(1, BlurMaskFilter.Blur.NORMAL));
            }
            canvas.drawPath(mPath, mShadowPaint);
        }
        mPath.reset();
        mPath.addRoundRect(rectF, corners, Path.Direction.CW);
        canvas.drawPath(mPath, mBackgroundPaint);
    }

    @Override
    public void setAlpha(int alpha) {

        mBackgroundPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
        mBackgroundPaint.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }
}
