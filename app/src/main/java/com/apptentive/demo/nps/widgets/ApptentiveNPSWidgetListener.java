package com.apptentive.demo.nps.widgets;

import android.view.MotionEvent;

public interface ApptentiveNPSWidgetListener {
	void onScoreSelected(int position);
	void onTouched(int x, int y, int position, MotionEvent motionEvent);
}