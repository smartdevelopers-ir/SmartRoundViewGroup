package ir.smartdevelopers.smartroundviewgroup;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.util.Arrays;

public class SmartRoundClippingLayout extends FrameLayout {
    private Path mClipPath;
    private RectF mRectF;
    private float[] mRadius;
    public SmartRoundClippingLayout(@NonNull Context context) {
        super(context);
        init(context, null);
    }

    public SmartRoundClippingLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public SmartRoundClippingLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SmartRoundClippingLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }
    private void init(@NonNull Context context, @Nullable AttributeSet attrs){
        mClipPath =new Path();
        mRectF=new RectF();
        mRadius=new float[]{16,16,16,16,16,16,16,16};
        if (attrs!=null){
            TypedArray typedArray=context.obtainStyledAttributes(attrs, R.styleable.SmartRoundClippingLayout);
            float radius=typedArray.getDimension(R.styleable.SmartRoundClippingLayout_android_radius,-1);
            if (radius!=-1){
                setRadiusInternal(radius,false);
            }else {
                float topRightRadius=typedArray.getDimension(R.styleable.SmartRoundClippingLayout_android_topRightRadius,16);
                float topLeftRadius=typedArray.getDimension(R.styleable.SmartRoundClippingLayout_android_topLeftRadius,16);
                float bottomLeftRadius=typedArray.getDimension(R.styleable.SmartRoundClippingLayout_android_bottomLeftRadius,16);
                float bottomRightRadius=typedArray.getDimension(R.styleable.SmartRoundClippingLayout_android_bottomRightRadius,16);
                setRadiusInternal(topLeftRadius,topRightRadius,bottomRightRadius,bottomLeftRadius,false);
            }

            typedArray.recycle();
        }
    }

//    @Override
//    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
//        super.onSizeChanged(w, h, oldw, oldh);
//        mClipPath.reset();
//        mRectF.set(0,0,w,h);
//        mClipPath.addRoundRect(mRectF,mRadius, Path.Direction.CW);
//        mClipPath.close();
//    }
    public void setRadius(float radius){
        setRadiusInternal(radius,true);
    }
    public void setRadius(float topLeft, float topRight, float bottomRight, float bottomLeft){
        setRadiusInternal(topLeft,topRight,bottomRight,bottomLeft,true);
    }

    public float[] getRadius() {
        return new float[]{mRadius[0],mRadius[2],mRadius[4],mRadius[6]};
    }

    private void setRadiusInternal(float radius, boolean invalidate){
        setRadiusInternal(radius,radius,radius,radius,invalidate);
    }
    private void setRadiusInternal(float topLeft, float topRight, float bottomRight, float bottomLeft, boolean b){
        float[] radius={topLeft,topLeft,topRight,topRight,bottomRight,bottomRight,bottomLeft,bottomLeft};
        if (Arrays.equals(mRadius,radius)){
            return;
        }
        mRadius[0]=mRadius[1]=topLeft;
        mRadius[2]=mRadius[3]=topRight;
        mRadius[4]=mRadius[5]=bottomRight;
        mRadius[6]=mRadius[7]=bottomLeft;
        invalidate();
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        int save=canvas.save();
        mClipPath.rewind();
        mRectF.set(0,0,getMeasuredWidth(),getMeasuredHeight());
        mClipPath.addRoundRect(mRectF,mRadius, Path.Direction.CW);
        canvas.clipPath(mClipPath);
        super.dispatchDraw(canvas);
        canvas.restoreToCount(save);
    }
}
