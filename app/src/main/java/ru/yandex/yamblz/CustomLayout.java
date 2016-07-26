package ru.yandex.yamblz;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

public class CustomLayout extends ViewGroup {


    public CustomLayout(Context context) {
        super(context);
    }

    public CustomLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CustomLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int childCount = getChildCount();

        View childWithMatchParent = null;

        int maxChildHeight = 0;
        int filledWidth = 0;

        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);

            if (child.getVisibility() == GONE) {
                continue;
            }

            LayoutParams childLayoutParams = child.getLayoutParams();

            if (childLayoutParams.width == LayoutParams.MATCH_PARENT) {
                if (childWithMatchParent == null) {
                    childWithMatchParent = child;
                } else {
                    throw new RuntimeException("Более одного элемента с MATCH_PARENT");
                }
            } else {
                int widthSpec = MeasureSpec.makeMeasureSpec(childLayoutParams.width, MeasureSpec.EXACTLY);
                int heightSpec = MeasureSpec.makeMeasureSpec(childLayoutParams.height, MeasureSpec.EXACTLY);
                measureChild(child, widthSpec, heightSpec);

                filledWidth += child.getMeasuredWidth();
                if (child.getMeasuredHeight() > maxChildHeight) {
                    maxChildHeight = child.getMeasuredHeight();
                }
            }
        }

        if (childWithMatchParent != null) {
            int childWithMatchParentWidth = MeasureSpec.getSize(widthMeasureSpec) - filledWidth;
            LayoutParams childLayoutParams = childWithMatchParent.getLayoutParams();

            measureChild(childWithMatchParent,
                    MeasureSpec.makeMeasureSpec(childWithMatchParentWidth, MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec(childLayoutParams.height, MeasureSpec.EXACTLY));
        }

        setMeasuredDimension(resolveSize(filledWidth, widthMeasureSpec), resolveSize(maxChildHeight, heightMeasureSpec));
    }



    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int childCount = getChildCount();
        int currentLeftPosition = left;
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            int rightPosition = currentLeftPosition + child.getMeasuredWidth();
            int bottomPosition = bottom + child.getMeasuredHeight();
            child.layout(currentLeftPosition, top, rightPosition, bottomPosition);
            currentLeftPosition = rightPosition;
        }
    }

}
