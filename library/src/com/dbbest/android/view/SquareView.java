package com.dbbest.android.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

public class SquareView extends ImageView {
	public SquareView(Context context) {
		super(context);
	}

	public SquareView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public SquareView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int width = MeasureSpec.getSize(widthMeasureSpec);
		int height = MeasureSpec.getSize(heightMeasureSpec);
		
		int target = width > height ? heightMeasureSpec : widthMeasureSpec;
		
		super.onMeasure(target, target);
	}

}
