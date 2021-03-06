package com.appyuken.frikiadospremium.menu;

import java.util.List;

import com.appyuken.frikiadospremium.R;
import com.appyuken.frikiadospremium.*;
import com.appyuken.frikiadospremium.classes.SoundPoolClass;
import com.appyuken.frikiadospremium.service.BackgroundAudiioService;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class CreditsActivity extends Activity {
	private TextView twitterLink;
	private TextView facebookLink;
	private TextView instagramLink;
	private boolean clickButton = false;
	private Intent playbackServiceIntent;
	private SoundPoolClass soundPool;
	private Tracker t;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_credits);
		t = ((Frikiados) getApplication())
				.getTracker(Frikiados.TrackerName.APP_TRACKER);
		// Set screen name.
		soundPool = new SoundPoolClass(getApplicationContext());
		GoogleAnalytics.getInstance(this).reportActivityStart(this);
		t.enableAdvertisingIdCollection(true);
		twitterLink = (TextView) findViewById(R.id.twitter);
		facebookLink = (TextView) findViewById(R.id.facebook);
		instagramLink = (TextView) findViewById(R.id.instagram);
		playbackServiceIntent = new Intent(this, BackgroundAudiioService.class);
		facebookLink.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// request your webservice here. Possible use of AsyncTask and
				// ProgressDialog
				// show the result here - dialog or Toast
				soundPool.playSelectionSound();
				final String urlFb = "fb://page/" + "629701493717602";
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setData(Uri.parse(urlFb));

				// If a Facebook app is installed, use it. Otherwise, launch
				// a browser
				final PackageManager packageManager = getPackageManager();
				List<ResolveInfo> list = packageManager.queryIntentActivities(
						intent, PackageManager.MATCH_DEFAULT_ONLY);
				if (list.size() == 0) {
					final String urlBrowser = "https://www.facebook.com/Frikiadosapp?fref=ts";
					intent.setData(Uri.parse(urlBrowser));
				}

				startActivity(intent);
			}

		});

		twitterLink.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// request your webservice here. Possible use of AsyncTask and
				// ProgressDialog
				// show the result here - dialog or Toast
				soundPool.playSelectionSound();
				PackageInfo info;
				try {
					info = getPackageManager().getPackageInfo(
							"com.twitter.android", 0);
					Intent intent = new Intent(Intent.ACTION_VIEW);
					if (info.applicationInfo.enabled)
						intent = new Intent(Intent.ACTION_VIEW, Uri
								.parse("twitter://user?screen_name=frikiadosgame"));
					else
						intent = new Intent(Intent.ACTION_VIEW, Uri
								.parse("https://twitter.com/#!/frikiadosgame"));
					startActivity(intent);
				} catch (NameNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		});

		instagramLink.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// request your webservice here. Possible use of AsyncTask and
				// ProgressDialog
				// show the result here - dialog or Toast
				soundPool.playSelectionSound();
				Uri uri = Uri.parse("http://instagram.com/_u/frikiadosgame");
			    Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);

			    likeIng.setPackage("com.instagram.android");

			    try {
			        startActivity(likeIng);
			    } catch (ActivityNotFoundException e) {
			        startActivity(new Intent(Intent.ACTION_VIEW,
			                Uri.parse("http://instagram.com/frikiadosgame")));
			    }
			}

		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.shop, menu);
		return true;
	}

	public void onStop() {
		if(!clickButton){
			stopService(playbackServiceIntent);
			}
		GoogleAnalytics.getInstance(this).reportActivityStop(this);
		super.onStop();
	}
	@Override
	public void onResume() {
		super.onResume();
		/*if (mPlayer == null) {
			playBackgroundMusic();
		}*/
		startService(playbackServiceIntent);
		clickButton = false;

	}
	public void onBackPressed() {
		clickButton = true;
		finish();
	}
}
