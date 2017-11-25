package com.appyuken.frikiadospremium;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.appyuken.frikiadospremium.R;
import com.appyuken.frikiadospremium.classes.ResultGetQuestion;
import com.appyuken.frikiadospremium.classes.SessionManager;
import com.appyuken.frikiadospremium.classes.SoundPoolClass;
import com.appyuken.frikiadospremium.gameModes.CategoryModeActivity;
import com.appyuken.frikiadospremium.gameModes.OnePlayerActivity;
import com.appyuken.frikiadospremium.menu.CreditsActivity;
import com.appyuken.frikiadospremium.menu.ProfileActivity;
import com.appyuken.frikiadospremium.menu.RankingActivity;
import com.appyuken.frikiadospremium.service.BackgroundAudiioService;
import com.appyuken.frikiadospremium.service.RestfulWebService;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

@SuppressLint("ResourceAsColor")
public class MainActivity extends Activity {
	protected static final String TAG = "Main Activity";
	private Button btnOnePlayer;
	private Button btnCategoryMode;
	private Button btnVsAdvanced;
	private Button btnProfile;
	private Button btnRanking;
	private Button btnSettings;
	private Button btnShare;
	private ImageView logoFrikiados;
	private Animation moveDownButtons;
	private Animation moveDownLogo;
	private Animation moveUpButtons;
	private Animation fadeInLifes;
	private SessionManager sessionManager;
	private int numOfLifes;
	// private TextView textNumOfLifes;
	private Typeface font2;
	private Typeface font;
	private ProgressDialog dialog = null;
	private ResultGetQuestion result;
	private Tracker t;
	// private MediaPlayer mPlayer;
	private MediaPlayer mPlayerSelection;
	private Intent playbackServiceIntent;
	private boolean clickButton = false;
	private SoundPoolClass soundPool;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		t = ((Frikiados) getApplication())
				.getTracker(Frikiados.TrackerName.APP_TRACKER);
		t.enableAdvertisingIdCollection(true);
		// Set screen name.
		GoogleAnalytics.getInstance(this).reportActivityStart(this);

		sessionManager = new SessionManager(getBaseContext());
		soundPool = new SoundPoolClass(getApplicationContext());
		btnOnePlayer = (Button) findViewById(R.id.btnOnePlayer);
		btnCategoryMode = (Button) findViewById(R.id.btnCategoryMode);
		btnVsAdvanced = (Button) findViewById(R.id.btnVsAdvanced);
		btnProfile = (Button) findViewById(R.id.btnPerfil);
		// textNumOfLifes = (TextView) findViewById(R.id.numOfLifes);
		btnRanking = (Button) findViewById(R.id.btnRanking);
		btnShare = (Button) findViewById(R.id.btnShare);
		btnSettings = (Button) findViewById(R.id.btnSettings);
		logoFrikiados = (ImageView) findViewById(R.id.logoFrikiados);
		playbackServiceIntent = new Intent(this, BackgroundAudiioService.class);
		// mPlayer = MediaPlayer.create(MainActivity.this,
		// R.raw.music_frikiados);
		// imgLife = (ImageView) findViewById(R.id.imageLifes);

		btnOnePlayer.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources()
				.getDimension(R.dimen.button_text));
		btnCategoryMode.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources()
				.getDimension(R.dimen.button_text));
		btnVsAdvanced.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources()
				.getDimension(R.dimen.button_text));

		// btnCategoryMode.setTextColor(Color.argb(50, 255, 255, 255));
		//btnVsAdvanced.setTextColor(Color.argb(50, 255, 255, 255));

		moveDownButtons = AnimationUtils.loadAnimation(getBaseContext(),
				R.anim.move_down_buttons);
		moveDownLogo = AnimationUtils.loadAnimation(getBaseContext(),
				R.anim.move_down_logo);
		moveUpButtons = AnimationUtils.loadAnimation(getBaseContext(),
				R.anim.move_up_buttons);
		fadeInLifes = AnimationUtils.loadAnimation(getBaseContext(),
				R.anim.fade_in);

		// playBackgroundMusic();
		// Comprobar vidas para dejar jugar o no
		/*
		 * HashMap<String, String> hashmap = sessionManager.getUserDetails();
		 * numOfLifes = Integer.parseInt(hashmap
		 * .get(SessionManager.KEY_NUMBER_LIFES)); textNumOfLifes.setText("x  "
		 * + numOfLifes);
		 */
		// Cargar Animación de entrada
		animMainAcitvity();

		// Cargar fuentes de texto
		font2 = Typeface.createFromAsset(getAssets(),
				"fonts/MYRIADPROSEMIBOLD.ttf");
		font = Typeface.createFromAsset(getAssets(), "fonts/MYRIADPROBOLD.ttf");
		// Asignar fuentes a los textos
		btnOnePlayer.setTypeface(font2);
		btnCategoryMode.setTypeface(font2);
		btnVsAdvanced.setTypeface(font2);

		btnOnePlayer.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				soundPool.playSelectionSound();
				if (sessionManager.isCreateBBDD()) {
					stopService(playbackServiceIntent);
					Intent onePlayerActivity = new Intent(getBaseContext(),
							OnePlayerActivity.class);
					startActivity(onePlayerActivity);
				} else {
					new DownloadQuestionsBBDD().execute();
				}
				// Google Analytics
				t.send(new HitBuilders.EventBuilder()
						.setCategory("Main Activity Events")
						.setAction("Pressed One Player Button").build());
			}
		});

		btnCategoryMode.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent categoryModeActivity = new Intent(getBaseContext(),
						CategoryModeActivity.class);
				startActivity(categoryModeActivity);

				// Google Analytics
				soundPool.playSelectionSound();
				t.send(new HitBuilders.EventBuilder()
						.setCategory("Main Activity Events")
						.setAction("Pressed Category Button").build());
			}
		});

		btnVsAdvanced.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String url = "http://rest.frikiados.com/frikiados/show_add_question";
				Intent i = new Intent(Intent.ACTION_VIEW);
				i.setData(Uri.parse(url));
				startActivity(i);
				// Google Analytics
				 soundPool.playSelectionSound();
				t.send(new HitBuilders.EventBuilder()
						.setCategory("Main Activity Events")
						.setAction("Pressed VS Advanced Button").build());
			}
		});

		btnProfile.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				soundPool.playSelectionSound();
				clickButton = true;
				Intent profileActivity = new Intent(getBaseContext(),
						ProfileActivity.class);
				startActivity(profileActivity);
				// Google Analytics
				t.send(new HitBuilders.EventBuilder()
						.setCategory("Main Activity Events")
						.setAction("Pressed Profile Button").build());

			}
		});

		/*
		 * btnFacebook.setOnClickListener(new View.OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { // TODO Auto-generated method
		 * stub
		 * 
		 * final String urlFb = "fb://page/" + "629701493717602"; Intent intent
		 * = new Intent(Intent.ACTION_VIEW); intent.setData(Uri.parse(urlFb));
		 * 
		 * // If a Facebook app is installed, use it. Otherwise, launch // a
		 * browser final PackageManager packageManager = getPackageManager();
		 * List<ResolveInfo> list = packageManager.queryIntentActivities(
		 * intent, PackageManager.MATCH_DEFAULT_ONLY); if (list.size() == 0) {
		 * final String urlBrowser =
		 * "https://www.facebook.com/Frikiadosapp?fref=ts";
		 * intent.setData(Uri.parse(urlBrowser)); }
		 * 
		 * startActivity(intent); }
		 * 
		 * });
		 */

		btnRanking.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) { // TODO Auto-generated method stub
				soundPool.playSelectionSound();
				clickButton = true;
				Intent rankingActivity = new Intent(getBaseContext(),
						RankingActivity.class);
				startActivity(rankingActivity);
				// Google Analytics
				t.send(new HitBuilders.EventBuilder()
						.setCategory("Main Activity Events")
						.setAction("Pressed Ranking Button").build());
			}
		});

		btnSettings.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				soundPool.playSelectionSound();
				// Google Analytics
				t.send(new HitBuilders.EventBuilder()
						.setCategory("Main Activity Events")
						.setAction("Pressed Settings Button").build());
				showSettings();
			}
		});
		btnShare.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				soundPool.playSelectionSound();
				Intent sharingIntent = new Intent(
						android.content.Intent.ACTION_SEND);
				sharingIntent.setType("text/plain");
				String shareBody = "Ya está disponible Frikiados Trivial Friki "
						+ "https://play.google.com/store/apps/details?id=com.appyuken.frikiadospremium";

				PackageManager pm = getPackageManager();
				List<ResolveInfo> activityList = pm.queryIntentActivities(
						sharingIntent, 0);
				for (final ResolveInfo app : activityList) {
					Log.i(TAG, "app.actinfo.name: " + app.activityInfo.name);
					// if((app.activityInfo.name).contains("facebook")) {
					if ("com.facebook.katana.ShareLinkActivity"
							.equals(app.activityInfo.name)) {

						sharingIntent
								.putExtra(android.content.Intent.EXTRA_TEXT,
										"https://play.google.com/store/apps/details?id=com.appyuken.frikiadospremium");
						startActivity(Intent.createChooser(sharingIntent,
								"Share idea"));
						break;
					} else {
						sharingIntent.putExtra(
								android.content.Intent.EXTRA_SUBJECT,
								"Frikiados");
						sharingIntent.putExtra(
								android.content.Intent.EXTRA_TEXT, shareBody);
						startActivity(Intent.createChooser(sharingIntent,
								"Share"));
						break;
					}
				}

				// Google Analytics
				t.send(new HitBuilders.EventBuilder()
						.setCategory("Main Activity Events")
						.setAction("Pressed Share Button").build());
			}
		});
	}

	private void playBackgroundMusic() {
		startService(playbackServiceIntent);
	}

	private void animMainAcitvity() {
		// TODO Auto-generated method stub

		btnVsAdvanced.setVisibility(View.VISIBLE);
		btnVsAdvanced.startAnimation(moveDownButtons);
		btnCategoryMode.setVisibility(View.VISIBLE);
		btnCategoryMode.startAnimation(moveDownButtons);
		btnOnePlayer.setVisibility(View.VISIBLE);
		btnOnePlayer.startAnimation(moveDownButtons);

		final Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				// Do something after 100ms
				btnProfile.setVisibility(View.VISIBLE);
				btnProfile.startAnimation(moveUpButtons);
				logoFrikiados.setVisibility(View.VISIBLE);
				logoFrikiados.startAnimation(moveDownLogo);
				btnSettings.setVisibility(View.VISIBLE);
				btnSettings.startAnimation(moveUpButtons);
				btnShare.setVisibility(View.VISIBLE);
				btnShare.startAnimation(moveUpButtons);
				btnRanking.setVisibility(View.VISIBLE);
				btnRanking.startAnimation(moveUpButtons);
			}
		}, 400);

		/*
		 * final Handler handler2 = new Handler(); handler2.postDelayed(new
		 * Runnable() {
		 * 
		 * @Override public void run() { // Do something after 100ms
		 * imgLife.setVisibility(View.VISIBLE);
		 * imgLife.startAnimation(fadeInLifes);
		 * textNumOfLifes.setVisibility(View.VISIBLE);
		 * textNumOfLifes.startAnimation(fadeInLifes); } }, 1400);
		 */

	}

	public void showSettings() {
		// TODO Auto-generated method stub
		final Dialog dialog = new Dialog(MainActivity.this,
				android.R.style.Theme_Translucent_NoTitleBar);
		dialog.setContentView(R.layout.custom_dialog_settings);
		dialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(R.color.opacity));
		final LinearLayout dialog2 = (LinearLayout) dialog
				.findViewById(R.id.linearOpacity);
		dialog2.setVisibility(LinearLayout.VISIBLE);
		Animation animation = AnimationUtils
				.loadAnimation(this, R.anim.fade_in);
		animation.setDuration(500);
		dialog2.setAnimation(animation);
		animation.start();
		dialog.setCancelable(false);

		Button btnCredits = (Button) dialog.findViewById(R.id.btnCredits);
		final Button btnMusic = (Button) dialog.findViewById(R.id.btnMusic);
		final Button btnSoundEffects = (Button) dialog
				.findViewById(R.id.btnSoundEffects);
		Button btnClose = (Button) dialog.findViewById(R.id.butnClose);
		Button btnOtherApps = (Button) dialog.findViewById(R.id.btnOtherApps);
		btnOtherApps.setTypeface(font);
		btnCredits.setTypeface(font);

		if (!sessionManager.isMusicEnabled()) {
			btnMusic.setBackgroundResource(R.drawable.music_off);
		}

		if (!sessionManager.isSoundEfectsEnabled()) {
			btnSoundEffects.setBackgroundResource(R.drawable.sound_off);
		}

		btnClose.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				soundPool.playSelectionSound();
				// Do something after 100ms
				dialog.dismiss();

			}
		});

		btnOtherApps.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				soundPool.playSelectionSound();
				try {
					startActivity(new Intent(Intent.ACTION_VIEW, Uri
							.parse("market://search?q=appyuken&hl=es")));
				} catch (android.content.ActivityNotFoundException anfe) {
					startActivity(new Intent(
							Intent.ACTION_VIEW,
							Uri.parse("https://play.google.com/store/search?q=appyuken&hl=es")));
				}
				// Google Analytics
				t.send(new HitBuilders.EventBuilder()
						.setCategory("Main Activity Events")
						.setAction("Pressed Other Apss Button").build());
				// Do something after 100ms
				dialog.dismiss();

			}
		});

		btnCredits.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				soundPool.playSelectionSound();
				clickButton = true;
				Intent creditsActivity = new Intent(getBaseContext(),
						CreditsActivity.class);
				startActivity(creditsActivity);
				// Google Analytics
				t.send(new HitBuilders.EventBuilder()
						.setCategory("Main Activity Events")
						.setAction("Pressed Credits Button").build());
				dialog.dismiss();
			}
		});

		btnMusic.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				soundPool.playSelectionSound();
				if (sessionManager.isMusicEnabled()) {
					sessionManager.setMusicDisable();
					btnMusic.setBackgroundResource(R.drawable.music_off);
					enableDisableMusic(false);
				} else {
					sessionManager.setMusicEnable();
					btnMusic.setBackgroundResource(R.drawable.music_on);
					enableDisableMusic(true);
				}
				// Google Analytics
				t.send(new HitBuilders.EventBuilder()
						.setCategory("Main Activity Events")
						.setAction("Pressed Music Button").build());
			}
		});

		btnSoundEffects.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				soundPool.playSelectionSound();
				if (sessionManager.isSoundEfectsEnabled()) {
					sessionManager.setSoundEfectsDisable();
					btnSoundEffects.setBackgroundResource(R.drawable.sound_off);
					enableDisableSoundEffects(false);
				} else {
					sessionManager.setSoundEfectsEnable();
					btnSoundEffects.setBackgroundResource(R.drawable.sound_on);
					enableDisableSoundEffects(true);
				}
				// Google Analytics
				t.send(new HitBuilders.EventBuilder()
						.setCategory("Main Activity Events")
						.setAction("Pressed Sound Effects Button").build());
			}

		});

		dialog.show();
	}

	protected void enableDisableSoundEffects(boolean soundEnabled) {
		// TODO Auto-generated method stub
		System.out.println("Sonido: " + soundEnabled);

	}

	protected void enableDisableMusic(boolean musicEnabled) {
		// TODO Auto-generated method stub
		if (musicEnabled) {
			// playBackgroundMusic();
			startService(playbackServiceIntent);
		} else {
			stopService(playbackServiceIntent);
			/*
			 * if (mPlayer != null) { if (mPlayer.isPlaying()) { mPlayer.stop();
			 * } }
			 */
		}
		System.out.println("Musica: " + musicEnabled);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onPause() {

		/*
		 * if (mPlayer != null) { mPlayer.release(); mPlayer = null; }
		 */
		super.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();

		clickButton = false;
		playBackgroundMusic();

		// Comprobar vidas para dejar jugar o no
		/*
		 * HashMap<String, String> hashmap = sessionManager.getUserDetails();
		 * numOfLifes = Integer.parseInt(hashmap
		 * .get(SessionManager.KEY_NUMBER_LIFES)); textNumOfLifes.setText("x  "
		 * + numOfLifes);
		 */
	}

	@Override
	public void onDestroy() {

		super.onDestroy();
	}

	public void onStop() {
		if (!clickButton) {
			stopService(playbackServiceIntent);
		}
		GoogleAnalytics.getInstance(this).reportActivityStop(this);
		super.onStop();
	}

	public void onBackPressed() {
		final Dialog dialog = new Dialog(MainActivity.this,
				android.R.style.Theme_Translucent_NoTitleBar);
		dialog.setContentView(R.layout.custom_dialog_exit);
		dialog.setCancelable(false);
		final Button btnExit = (Button) dialog.findViewById(R.id.btnExit);
		final Button btnCancel = (Button) dialog.findViewById(R.id.btnContinue);
		TextView titleExit = (TextView) dialog.findViewById(R.id.titleExit);
		TextView textExit = (TextView) dialog.findViewById(R.id.textExit);
		titleExit.setTypeface(font2);
		textExit.setTypeface(font2);
		titleExit.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources()
				.getDimension(R.dimen.exit_title));
		textExit.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources()
				.getDimension(R.dimen.exit_text));

		btnExit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				soundPool.playSelectionSound();
				dialog.dismiss();
				moveTaskToBack(true);
			}
		});

		btnCancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				soundPool.playSelectionSound();
				dialog.dismiss();
			}
		});
		dialog.show();
	}

	public class DownloadQuestionsBBDD extends AsyncTask<Void, String, Void> {

		@Override
		protected void onPreExecute() {
			dialog = new ProgressDialog(MainActivity.this);
			dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			dialog.setCancelable(false);
			dialog.setMessage("Descargando BBDD. Por favor espere unos segundos...!");
			dialog.show();

		}

		@Override
		protected Void doInBackground(Void... unused) {
			result = RestfulWebService.getDownloadModifiedQuestionsIfNeeded(
					getApplicationContext(), sessionManager.getToken());
			return (null);
		}

		@Override
		protected void onPostExecute(Void unused) {

			switch (result.getError()) {
			case 0:
				// NO HAY ERROR
				try {
					dialog.dismiss();
					dialog = null;
					Intent onePlayerActivity = new Intent(getBaseContext(),
							OnePlayerActivity.class);
					startActivity(onePlayerActivity);
					// goToTarget = false;
				} catch (Exception e) {
					// nothing
				}
				break;

			case 1:
				try {
					dialog.dismiss();
					dialog = null;
					showAlertDialogError();
					// goToTarget = false;
				} catch (Exception e) {
					// nothing
				}
				// ERROR WEBSERVICE
				System.out.println("Error WebService");
				break;

			case 2:
				// ERROREXCEPTION
				try {
					dialog.dismiss();
					dialog = null;
					showAlertDialogError();
					// goToTarget = false;
				} catch (Exception e) {
					// nothing
				}
				System.out.println("Error Exception");

				break;

			default:
				break;
			}

		}

	}

	public void showAlertDialogError() {
		// TODO Auto-generated method stub
		Context context = getApplicationContext();
		CharSequence text = "Error de conexión con el Servidor. Por favor pruebe más tarde!";
		int duration = Toast.LENGTH_SHORT;
		Toast toast = Toast.makeText(context, text, duration);
		toast.show();
	}

}
