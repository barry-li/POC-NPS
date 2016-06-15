package com.apptentive.demo.nps.widgets;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.apptentive.demo.nps.R;

public class ApptentiveNPSWidgetSeekBar extends View {

	protected static final int[] STATE_NORMAL = new int[] {};
	protected static final int[] STATE_SELECTED = new int[] { android.R.attr.state_selected };
	protected static final int[] STATE_PRESSED = new int[] { android.R.attr.state_pressed };

	protected int[] itemState = STATE_SELECTED;

	protected boolean isFirstDraw = true;
	protected boolean doUpdateFromPosition = false;
	protected boolean doDrawLabel = false;

	protected Drawable widgetBackgroundDrawable;
	protected RectF widgetBackgroundPaddingRect;

	protected int itemTintColor;

	protected int currentX, currentY;
	protected int centerY;
	protected int labelOffset;
	protected float highlightEnlargeFactor = 1.0f;
	protected int itemHalfWidth, itemHalfHeight;
	protected int[][] anchors;
	protected int currentItem = -1;

	private static final String SUPER_INSTANCE_STATE = "saved_instance_state_parcelable";
	private static final String STATE_SAVED_ITEM = "state_current_item";

	protected ApptentiveNPSSeekBarItemsAdapter npsItemsAdapter;
	protected ApptentiveNPSWidgetListener npsSelectionListener;

	public ApptentiveNPSWidgetSeekBar(Context context) {
		super(context);
		init(null, 0);
	}

	public ApptentiveNPSWidgetSeekBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(attrs, 0);
	}

	public ApptentiveNPSWidgetSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(attrs, defStyleAttr);
	}

	protected void init(AttributeSet attrs, int defStyleAttr) {
		widgetBackgroundPaddingRect = new RectF();
		if (attrs != null) {
			TypedArray a = getContext().obtainStyledAttributes(
					attrs, R.styleable.ApptentiveNPSWidgetSeekBar, defStyleAttr, 0);

			widgetBackgroundPaddingRect.left = a.getDimension(R.styleable.ApptentiveNPSWidgetSeekBar_base_margin_left, 0.0f);
			widgetBackgroundPaddingRect.top = a.getDimension(R.styleable.ApptentiveNPSWidgetSeekBar_base_margin_top, 0.0f);
			widgetBackgroundPaddingRect.right = a.getDimension(R.styleable.ApptentiveNPSWidgetSeekBar_base_margin_right, 0.0f);
			widgetBackgroundPaddingRect.bottom = a.getDimension(R.styleable.ApptentiveNPSWidgetSeekBar_base_margin_bottom, 0.0f);

			itemHalfWidth = (int) (a.getDimension(R.styleable.ApptentiveNPSWidgetSeekBar_item_width, 0.0f) / 2.0f);
			itemHalfHeight = (int) (a.getDimension(R.styleable.ApptentiveNPSWidgetSeekBar_item_height, 0.0f) / 2.0f);

			widgetBackgroundDrawable = a.getDrawable(R.styleable.ApptentiveNPSWidgetSeekBar_base_background);

			itemTintColor = a.getColor(R.styleable.ApptentiveNPSWidgetSeekBar_item_tint, 0);

			doDrawLabel = a.getBoolean(R.styleable.ApptentiveNPSWidgetSeekBar_draw_label, false);

            labelOffset = (int) (a.getDimension(R.styleable.ApptentiveNPSWidgetSeekBar_label_offset, 0.0f));

			highlightEnlargeFactor = a.getFraction(R.styleable.ApptentiveNPSWidgetSeekBar_highlight_factor, 1, 1, 1.0f);
			a.recycle();
		}
	}

	private void configureBounds() {
		Rect rect = new Rect((int) widgetBackgroundPaddingRect.left,
				(int) widgetBackgroundPaddingRect.top,
				(int) (getWidth() - widgetBackgroundPaddingRect.right),
				(int) (getHeight() - widgetBackgroundPaddingRect.bottom));
		if (widgetBackgroundDrawable != null) {
			widgetBackgroundDrawable.setBounds(rect);
		}

		centerY = getHeight() / 2;

		int count = getCount();
		int widthBase = rect.width() / count;
		int widthHalf = widthBase / 2;

		itemHalfWidth = Math.min(widthHalf, itemHalfWidth);

		anchors = new int[count][2];
		for (int i = 0, j = 1; i < count; i++, j++) {
			anchors[i][0] = widthBase * j - widthHalf + rect.left;
			anchors[i][1] = centerY;
		}

		if (currentItem >= 0) {
			currentX = anchors[currentItem][0];
			currentY = anchors[currentItem][1];
		}
	}

	@Override
	protected void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		setFirstDraw(true);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		boolean bSelected = false;
		if (isFirstDraw) {
			configureBounds();
		}
		if (widgetBackgroundDrawable != null) {
			widgetBackgroundDrawable.draw(canvas);
		}
		if (isInEditMode()) {
			return;
		}

		Drawable itemOff;
		Drawable itemOn = null;
		StateListDrawable stateListDrawable;
		stateListDrawable = npsItemsAdapter.getItem(currentItem);

		if (doUpdateFromPosition && currentItem >= 0)  {
			bSelected = true;
			doUpdateFromPosition = false;
			currentX = anchors[currentItem][0];
			currentY = anchors[currentItem][1];
			stateListDrawable = npsItemsAdapter.getItem(currentItem);
			Log.d("NPS", String.format("onDraw true currentItem: %d", currentItem));
		}
		if (stateListDrawable != null) {
			stateListDrawable.setState(itemState);
			itemOn = stateListDrawable.getCurrent();
		}
		int count = getCount();
		Paint labelPaint = new Paint();
		Rect labelBounds = new Rect();
		String label;
		labelPaint.setTextSize(30);
        labelPaint.setStyle(Paint.Style.STROKE);
		labelPaint.setColor(Color.DKGRAY);
		labelPaint.setAntiAlias(true);
		for (int i = 0; i < count; i++) {
			if (doDrawLabel && (i != currentItem || !bSelected || labelOffset != 0)) {
				label = String.valueOf(i);
				// computes minimal bounds where label will fit
				labelPaint.getTextBounds(label, 0, label.length(), labelBounds);
				int labelHalfWidth = labelBounds.width() / 2;
				int labelHalfHeight = labelBounds.height() / 2;
				canvas.drawText(label, anchors[i][0] - labelHalfWidth, anchors[i][1] + labelHalfHeight - labelOffset, labelPaint);
			}

			if (i != currentItem || !bSelected  || labelOffset != 0) {
				stateListDrawable = npsItemsAdapter.getItem(i);
				stateListDrawable.setState(STATE_NORMAL);
				itemOff = stateListDrawable.getCurrent();
				itemOff.setBounds(
						anchors[i][0] - itemHalfWidth,
						anchors[i][1] - itemHalfHeight,
						anchors[i][0] + itemHalfWidth,
						anchors[i][1] + itemHalfHeight);
				if (itemTintColor != 0) {
					itemOff.setColorFilter(itemTintColor, PorterDuff.Mode.SRC_ATOP);
				}
				itemOff.draw(canvas);
			}
		}

		if (itemOn != null) {
			if (bSelected && labelOffset != 0) {
				itemOn.setBounds(
						currentX - (int) (0.3 * itemHalfWidth),
						currentY - (int) (0.3 * itemHalfHeight),
						currentX + (int) (0.3 * itemHalfWidth),
						currentY + (int) (0.3 * itemHalfHeight));
			} else {
				itemOn.setBounds(
						currentX - (int) (highlightEnlargeFactor * itemHalfWidth),
						currentY - (int) (highlightEnlargeFactor * itemHalfHeight),
						currentX + (int) (highlightEnlargeFactor * itemHalfWidth),
						currentY + (int) (highlightEnlargeFactor * itemHalfHeight));
			}
			itemOn.draw(canvas);
			if (doDrawLabel && labelOffset == 0) {
				label = String.valueOf(currentItem);
				labelPaint.getTextBounds(label, 0, label.length(), labelBounds);
				int labelHalfWidth = labelBounds.width() / 2;
				int labelHalfHeight = labelBounds.height() / 2;
				labelPaint.setTextSize(30 * highlightEnlargeFactor);
				canvas.drawText(label, currentX - (int) (highlightEnlargeFactor * labelHalfWidth), currentY + (int) (highlightEnlargeFactor * labelHalfHeight) - labelOffset, labelPaint);
			}
		}

		setFirstDraw(false);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		currentX = getNormalizedX(event);
		currentY = centerY;
		int action = event.getAction();
		doUpdateFromPosition = (action == MotionEvent.ACTION_UP);
		itemState = action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL ? STATE_SELECTED : STATE_PRESSED;
		invalidate();

		if (npsSelectionListener != null) {
			npsSelectionListener.onTouched(currentX, currentY, currentItem, event);
		}

		switch (action) {
			case MotionEvent.ACTION_DOWN:
				updateCurrentItem();
				return true;
			case MotionEvent.ACTION_UP:
				return true;
			default:
				updateCurrentItem();
		}
		return super.onTouchEvent(event);
	}

	@Override
	protected Parcelable onSaveInstanceState() {

		// Create a bundle to put super parcelable in
		Bundle bundle = new Bundle();
		bundle.putParcelable(SUPER_INSTANCE_STATE, super.onSaveInstanceState());
		// save current item in the bundle;
		bundle.putInt(STATE_SAVED_ITEM, currentItem);
		// return the bundle
		return bundle;
	}

	@Override
	protected void onRestoreInstanceState(Parcelable state) {
		// We know state is a Bundle:
		Bundle bundle = (Bundle) state;

		setPosition(bundle.getInt(STATE_SAVED_ITEM));
		// get super parcelable back out of the bundle and pass it to
		// super.onRestoreInstanceState(Parcelable)
		state = bundle.getParcelable(SUPER_INSTANCE_STATE);
		super.onRestoreInstanceState(state);
	}

	protected int getNormalizedX(MotionEvent event) {
		return Math.min(Math.max((int) event.getX(), itemHalfWidth), getWidth() - itemHalfWidth);
	}

	protected int getNormalizedY(MotionEvent event) {
		return Math.min(Math.max((int) event.getY(), itemHalfHeight), getHeight() - itemHalfHeight);
	}

	protected int getCount() {
		return isInEditMode() ? 3 : npsItemsAdapter.getCount();
	}

	public void setAdapter(ApptentiveNPSSeekBarItemsAdapter adapter) {
		npsItemsAdapter = adapter;
	}

	public void setFirstDraw(boolean firstDraw) {
		isFirstDraw = firstDraw;
	}

	public void setListener(ApptentiveNPSWidgetListener listener) {
		npsSelectionListener = listener;
	}

	public void setPosition(int position) {
		if (position < 0) {
			return;
		}

		position = position >= npsItemsAdapter.getCount() ? npsItemsAdapter.getCount() - 1 : position;
		currentItem = position;
		doUpdateFromPosition = true;
		invalidate();
	}

	public int getCurrentX() {
		return currentX;
	}

	public int getCurrentY() {
		return currentY;
	}

	public int getCurrentItem() {
		return currentItem;
	}

	protected void setCurrentItem(int theItem) {
		if (currentItem != theItem && npsSelectionListener != null) {
			npsSelectionListener.onScoreSelected(theItem);
		}
		Log.d("NPS", String.format("setCurrentItem currentItem: %d", theItem));
		currentItem = theItem;
	}

	// From the touch point, find the nearest score position, and set as current
	private void updateCurrentItem() {
		int distance;
		int minIndex = 0;
		int minDistance = Integer.MAX_VALUE;
		int count = getCount();
		for (int i = 0; i < count; i++) {
			distance = Math.abs(anchors[i][0] - currentX);
			if (minDistance > distance) {
				minIndex = i;
				minDistance = distance;
			}
		}
		Log.d("NPS", String.format("onDraw false currentItem: %d", currentItem));
		setCurrentItem(minIndex);
	}
}
