package com.appyuken.frikiadospremium.gameModes;

import com.appyuken.frikiadospremium.R;
import com.appyuken.frikiadospremium.*;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.Window;

public class ChallengeActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_challenge);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.challenge, menu);
		return true;
	}

}
