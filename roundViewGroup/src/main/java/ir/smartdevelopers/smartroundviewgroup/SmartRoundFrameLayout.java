package ir.smartdevelopers.smartroundviewgroup;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;


public class SmartRoundFrameLayout extends FrameLayout {
    private RoundDrawable mSmartRoundDrawable;
    private ColorStateList mBackgroundColor= ColorStateList.valueOf(Color.WHITE);

    private float mShadowTopOffset,mShadowLeftOffset,mShadowRightOffset,mShadowBottomOffset;
    private boolean mShowShadow;
    private float mShadowRadius;
    private int mShadowColor;
    private float mShadowDx,mShadowDy;




    public SmartRoundFrameLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    public SmartRoundFrameLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SmartRoundFrameLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs){

//        setWillNotDraw(false);

        mSmartRoundDrawable=new RoundDrawable();
        if (attrs!=null){
            TypedArray typedArray=context.obtainStyledAttributes(attrs, R.styleable.SmartRoundFrameLayout);
            mBackgroundColor=typedArray.getColorStateList(R.styleable.SmartRoundFrameLayout_backgroundColor);
            if (mBackgroundColor==null){
                mBackgroundColor= ColorStateList.valueOf(Color.WHITE);
            }
            float radius=typedArray.getDimension(R.styleable.SmartRoundFrameLayout_android_radius,-1);
            if (radius!=-1){
                mSmartRoundDrawable.setRadius(radius,radius,radius,radius);
            }else {
                float topLeftRadius=typedArray.getDimension(R.styleable.SmartRoundFrameLayout_android_topLeftRadius,30f);
                float topRightRadius=typedArray.getDimension(R.styleable.SmartRoundFrameLayout_android_topRightRadius,30f);
                float bottomRightRadius=typedArray.getDimension(R.styleable.SmartRoundFrameLayout_android_bottomRightRadius,30f);
                float bottomLeftRadius=typedArray.getDimension(R.styleable.SmartRoundFrameLayout_android_bottomLeftRadius,30f);
                mSmartRoundDrawable.setRadius(topLeftRadius, topRightRadius, bottomRightRadius, bottomLeftRadius);
            }
           mShowShadow=typedArray.getBoolean(R.styleable.SmartRoundFrameLayout_showShadow,false);
           if (mShowShadow){
               mShadowTopOffset=typedArray.getDimension(R.styleable.SmartRoundFrameLayout_topShadowOffset,4);
               mShadowBottomOffset=typedArray.getDimension(R.styleable.SmartRoundFrameLayout_bottomShadowOffset,4);
               mShadowRightOffset=typedArray.getDimension(R.styleable.SmartRoundFrameLayout_rightShadowOffset,4);
               mShadowLeftOffset=typedArray.getDimension(R.styleable.SmartRoundFrameLayout_leftShadowOffset,4);
               mShadowRadius=typedArray.getFloat(R.styleable.SmartRoundFrameLayout_android_shadowRadius,4);
               mShadowDx=typedArray.getFloat(R.styleable.SmartRoundFrameLayout_android_shadowDx,0);
               mShadowDy=typedArray.getFloat(R.styleable.SmartRoundFrameLayout_android_shadowDy,0);
               mShadowColor=typedArray.getColor(R.styleable.SmartRoundFrameLayout_android_shadowColor,
                       Color.argb(128, 0, 0, 0));
           }
           int gradientStartColor=typedArray.getColor(R.styleable.SmartRoundFrameLayout_gradientStartColor,-1);
           int gradientEndColor=typedArray.getColor(R.styleable.SmartRoundFrameLayout_gradientEndColor,-1);
           float gradientAngle=typedArray.getFloat(R.styleable.SmartRoundFrameLayout_gradientAngle,0);
           if (gradientEndColor!=-1 && gradientStartColor!=-1){
               mSmartRoundDrawable.setGradientColor(gradientStartColor,gradientEndColor,gradientAngle);
           }

        typedArray.recycle();
        }

        if (mShowShadow){
            setPadding((int) mShadowLeftOffset, (int) mShadowTopOffset,
                    (int) mShadowRightOffset, (int) mShadowBottomOffset);
            mSmartRoundDrawable.mShadowBottomOffset=mShadowBottomOffset;
            mSmartRoundDrawable.mShadowLeftOffset=mShadowLeftOffset;
            mSmartRoundDrawable.mShadowTopOffset=mShadowTopOffset;
            mSmartRoundDrawable.mShadowRightOffset=mShadowRightOffset;
            mSmartRoundDrawable.mShadowRadius=mShadowRadius;
            mSmartRoundDrawable.mShadowDx=mShadowDx;
            mSmartRoundDrawable.mShadowDy=mShadowDy;
            mSmartRoundDrawable.mShowShadow=mShowShadow;
            mSmartRoundDrawable.mShadowColor=mShadowColor;


            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P){
                setLayerType(LAYER_TYPE_SOFTWARE,null);
            }


        }

        mSmartRoundDrawable.setColor(mBackgroundColor);
        setBackground(mSmartRoundDrawable);
    }
    public void setGradientColor(int startColor,int endColor,float angle){
        mSmartRoundDrawable.setGradientColor(startColor,endColor,angle);

    }

//    @Override
//    public void setPadding(int left, int top, int right, int bottom) {
//        super.setPadding((int) (left+mShadowLeftOffset), (int) (top+mShadowTopOffset),
//                (int) (right+mShadowRightOffset), (int) (bottom+mShadowBottomOffset));
//    }

    public void setRadius(float topLeft, float topRight, float bottomRight, float bottomLeft){
        mSmartRoundDrawable.setRadius(topLeft,topRight,bottomRight,bottomLeft);
//        setRadius(topLeft,topRight,bottomRight,bottomLeft,true);
    }



    public int getBackgroundColor() {
        return mBackgroundColor.getDefaultColor();
    }

    @Override
    public void setBackgroundColor(int backgroundColor) {
        mBackgroundColor = ColorStateList.valueOf(backgroundColor);
        mSmartRoundDrawable.setColor(mBackgroundColor);
    }

    public void setBackgroundColor(ColorStateList backgroundColor) {
        mBackgroundColor = backgroundColor;
    }
}
