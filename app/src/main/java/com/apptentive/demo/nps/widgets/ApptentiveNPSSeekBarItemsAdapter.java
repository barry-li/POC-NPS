package com.apptentive.demo.nps.widgets;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;

public class ApptentiveNPSSeekBarItemsAdapter {

	protected StateListDrawable[] npsItems;

	public ApptentiveNPSSeekBarItemsAdapter(Resources resources, int[] items) {
		int size = items.length;
		npsItems = new StateListDrawable[size];
		Drawable drawable;
		for (int i = 0; i < size; i++) {
			drawable = resources.getDrawable(items[i]);
			if (drawable instanceof StateListDrawable) {
				npsItems[i] = (StateListDrawable) drawable;
			} else {
				npsItems[i] = new StateListDrawable();
				npsItems[i].addState(new int[] {}, drawable);
			}
		}
	}

	public int getCount() {
		return npsItems.length;
	}

	public StateListDrawable getItem(int position) {
		if (position < 0 || position >= npsItems.length) {
			return null;
		}
		return npsItems[position];
	}

}
