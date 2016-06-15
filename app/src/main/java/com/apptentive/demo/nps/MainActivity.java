package com.apptentive.demo.nps;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.RadioGroup;

import com.apptentive.demo.nps.widgets.ApptentiveNPSSeekBarItemsAdapter;
import com.apptentive.demo.nps.widgets.ApptentiveNPSWidgetListener;
import com.apptentive.demo.nps.widgets.ApptentiveNPSWidgetSeekBar;

public class MainActivity extends AppCompatActivity {

	private ApptentiveNPSWidgetSeekBar seekBarWidget, seekBarWidget_large, seekBarWidget_radio;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		seekBarWidget = (ApptentiveNPSWidgetSeekBar) findViewById(R.id.nps_seekbar);

		seekBarWidget.setAdapter(new ApptentiveNPSSeekBarItemsAdapter(getResources(), new int[] {
				R.drawable.apptentive_score_selector_overlay_solid,
				R.drawable.apptentive_score_selector_overlay_solid,
				R.drawable.apptentive_score_selector_overlay_solid,
				R.drawable.apptentive_score_selector_overlay_solid,
				R.drawable.apptentive_score_selector_overlay_solid,
				R.drawable.apptentive_score_selector_overlay_solid,
				R.drawable.apptentive_score_selector_overlay_solid,
				R.drawable.apptentive_score_selector_overlay_solid,
				R.drawable.apptentive_score_selector_overlay_solid,
				R.drawable.apptentive_score_selector_overlay_solid,
				R.drawable.apptentive_score_selector_overlay_solid}));

		seekBarWidget.setListener(new ApptentiveNPSWidgetListener() {
			@Override
			public void onScoreSelected(int position) {

			}

			@Override
			public void onTouched(int x, int y, int position, MotionEvent motionEvent) {
				Log.d("NPS", String.format("onTouched X:%d Y:%d pos:%d action:%d", x, y, position, motionEvent.getAction()));
			}
		});

		seekBarWidget_large = (ApptentiveNPSWidgetSeekBar) findViewById(R.id.nps_seekbar_2);

		seekBarWidget_large.setAdapter(new ApptentiveNPSSeekBarItemsAdapter(getResources(), new int[] {
				R.drawable.apptentive_score_selector_overlay,
				R.drawable.apptentive_score_selector_overlay,
				R.drawable.apptentive_score_selector_overlay,
				R.drawable.apptentive_score_selector_overlay,
				R.drawable.apptentive_score_selector_overlay,
				R.drawable.apptentive_score_selector_overlay,
				R.drawable.apptentive_score_selector_overlay,
				R.drawable.apptentive_score_selector_overlay,
				R.drawable.apptentive_score_selector_overlay,
				R.drawable.apptentive_score_selector_overlay,
				R.drawable.apptentive_score_selector_overlay}));

		seekBarWidget_large.setListener(new ApptentiveNPSWidgetListener() {
			@Override
			public void onScoreSelected(int position) {

			}

			@Override
			public void onTouched(int x, int y, int position, MotionEvent motionEvent) {
				Log.d("NPS", String.format("onTouched X:%d Y:%d pos:%d action:%d", x, y, position, motionEvent.getAction()));
			}
		});


		seekBarWidget_radio = (ApptentiveNPSWidgetSeekBar) findViewById(R.id.nps_seekbar_3);

		seekBarWidget_radio.setAdapter(new ApptentiveNPSSeekBarItemsAdapter(getResources(), new int[] {
				R.drawable.apptentive_score_selector_overlay_radio,
				R.drawable.apptentive_score_selector_overlay_radio,
				R.drawable.apptentive_score_selector_overlay_radio,
				R.drawable.apptentive_score_selector_overlay_radio,
				R.drawable.apptentive_score_selector_overlay_radio,
				R.drawable.apptentive_score_selector_overlay_radio,
				R.drawable.apptentive_score_selector_overlay_radio,
				R.drawable.apptentive_score_selector_overlay_radio,
				R.drawable.apptentive_score_selector_overlay_radio,
				R.drawable.apptentive_score_selector_overlay_radio,
				R.drawable.apptentive_score_selector_overlay_radio}));

		seekBarWidget_radio.setListener(new ApptentiveNPSWidgetListener() {
			@Override
			public void onScoreSelected(int position) {

			}

			@Override
			public void onTouched(int x, int y, int position, MotionEvent motionEvent) {
				Log.d("NPS", String.format("onTouched X:%d Y:%d pos:%d action:%d", x, y, position, motionEvent.getAction()));
			}
		});



	}
}
