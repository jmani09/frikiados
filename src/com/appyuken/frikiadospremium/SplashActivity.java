package com.appyuken.frikiadospremium;

import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.appyuken.frikiadospremium.R;
import com.appyuken.frikiadospremium.classes.ResultGetQuestion;
import com.appyuken.frikiadospremium.classes.SessionManager;
import com.appyuken.frikiadospremium.service.RestfulWebService;

public class SplashActivity extends Activity {

	private SessionManager sessionManager;
	private ImageView logoFrikiados;
	private ImageView logoSpoilers;
	private TextView textSpoiler;
	private TextView loadingQuestions;
	private Animation fadeInLoading;
	private Animation blinkLoanding;
	public static ProgressBar progressBar;
	private ResultGetQuestion result;
	private int progressStatus = 0;
	private Handler handler = new Handler();
	private int resultScore;
	private MediaPlayer mPlayer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

		sessionManager = new SessionManager(getBaseContext());
		textSpoiler = (TextView) findViewById(R.id.textSpoiler);
		loadingQuestions = (TextView) findViewById(R.id.textLoadingBBDD);
		logoFrikiados = (ImageView) findViewById(R.id.logoFrikiados);
		logoSpoilers = (ImageView) findViewById(R.id.logoSpoilers);
		fadeInLoading = AnimationUtils.loadAnimation(getBaseContext(),
				R.anim.fade_in_loading);
		blinkLoanding = AnimationUtils.loadAnimation(getBaseContext(),
				R.anim.blink);
		progressBar = (ProgressBar) findViewById(R.id.progressbar2);

		logoFrikiados.setVisibility(View.VISIBLE);
		logoFrikiados.startAnimation(fadeInLoading);
		
		logoSpoilers.setVisibility(View.VISIBLE);
		logoSpoilers.startAnimation(blinkLoanding);
		
		textSpoiler.setVisibility(View.VISIBLE);
		loadingQuestions.setVisibility(View.VISIBLE);
		loadingQuestions.startAnimation(blinkLoanding);

		/*HashMap<String, String> hashmap = sessionManager.getUserDetails();
		int numOfLifes = Integer.parseInt(hashmap
				.get(SessionManager.KEY_NUMBER_LIFES));

		Context context = getApplicationContext();
		CharSequence text = "Number of life: " + numOfLifes;
		int duration = Toast.LENGTH_SHORT;

		Toast toast = Toast.makeText(context, text, duration);
		toast.show();*/
		sessionManager.setMusicPosition(0);

		if (!sessionManager.isBestScoreSendToServer()
				&& sessionManager.isLoggedInFacebook()) {
			if (sessionManager.getBestScore() > 0)
				new UpdateScore().execute();
		}

		if (!sessionManager.isCreateToken()) {
			new DownloadToken().execute();
		} else {
			 simulatedLoading();
			new DownloadQuestionsIfNeeded().execute();
		}

	}

	private class DownloadToken extends AsyncTask<Integer, Integer, Integer> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected Integer doInBackground(Integer... urls) {
			return RestfulWebService.getFirstToken(getApplicationContext());

		}

		@Override
		protected void onPostExecute(Integer resultToken) {

			switch (resultToken) {
			case 0:
				// NO HAY ERROR
				try {
					new DownloadQuestionsIfNeeded().execute();
					// goToTarget = false;
				} catch (Exception e) {
					// nothing
				}
				break;

			case 1:
				try {
					if (!sessionManager.isCreateBBDD()) {
						showAlertDialogError();
					} else {
						//simulatedLoading();
					}
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
					if (!sessionManager.isCreateBBDD()) {
						showAlertDialogError();
					} else {
						//simulatedLoading();
					}
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

	private class DownloadQuestionsIfNeeded extends
			AsyncTask<Void, Integer, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... urls) {
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
					
					SplashActivity.this.finish();
					Intent mainActivity = new Intent(getBaseContext(),
							MainActivity.class);
					startActivity(mainActivity);
					// goToTarget = false;
				} catch (Exception e) {
					// nothing
				}
				break;

			case 1:
				try {
					if (!sessionManager.isCreateBBDD()) {
						showAlertDialogError();
					} else {
						simulatedLoading();
					}
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
					if (!sessionManager.isCreateBBDD()) {
						showAlertDialogError();
					} else {
						simulatedLoading();
					}
					// goToTarget = false;
				} catch (Exception e) {
					// nothing
				}
				System.out.println("Error Exception");

				break;

			case 3:
				try {
					if (!sessionManager.isCreateBBDD()) {
						showAlertDialogError();
					} else {
						simulatedLoading();
					}
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.splash, menu);
		return true;
	}

	public void showAlertDialogError() {
		// TODO Auto-generated method stub
		if (!sessionManager.isCreateBBDD()) {
			AlertDialog.Builder builder1 = new AlertDialog.Builder(
					SplashActivity.this);
			builder1.setTitle("Error Connection");
			builder1.setMessage("Internet Connection Failed");
			builder1.setCancelable(true);
			builder1.setPositiveButton("Try again",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							if (!sessionManager.isCreateToken()) {
								new DownloadToken().execute();
							} else {
								// simulatedLoading();
								new DownloadQuestionsIfNeeded().execute();
							}
							dialog.cancel();
						}
					});

			AlertDialog alert11 = builder1.create();
			alert11.show();
		} else {
			simulatedLoading();
		}
	}

	private void simulatedLoading() {
		// TODO Auto-generated method stub
		new Thread(new Runnable() {
			public void run() {
				while (progressStatus < 100) {
					progressStatus += 5;
					// Update the progress bar and display the
					// current value in the text view
					handler.post(new Runnable() {
						public void run() {
							progressBar.setProgress(progressStatus);
						}
					});
					try {
						// Sleep for 200 milliseconds.
						// Just to display the progress slowly
						Thread.sleep(200);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					if (progressStatus >= 100) {

						// sleep 2 seconds, so that you can see the 100%
						Intent mainActivity = new Intent(getBaseContext(),
								MainActivity.class);
						startActivity(mainActivity);
						SplashActivity.this.finish();
					}
				}
			}
		}).start();
	}

	public class UpdateScore extends AsyncTask<Void, String, Void> {

		@Override
		protected void onPreExecute() {

		}

		@Override
		protected Void doInBackground(Void... unused) {
			HashMap<String, String> hashmap = sessionManager.getUserDetails();
			String idFacebook = hashmap.get(SessionManager.KEY_FB_USER_ID);
			String idUser = hashmap.get(SessionManager.KEY_ID_USER_FRIKIADOS);
			resultScore = RestfulWebService.sendScore(idUser, idFacebook,
					Integer.toString(sessionManager.getBestScore()),
					Integer.toString(sessionManager.getAnsweredQuestions()),
					getApplicationContext());
			return (null);
		}

		@Override
		protected void onPostExecute(Void unused) {

			switch (resultScore) {
			case 0:
				// NO HAY ERROR
				try {
					sessionManager.setBestScoreSendToServer(true);
					// goToTarget = false;
				} catch (Exception e) {
					// nothing
				}
				break;

			case 1:
				try {
					sessionManager.setBestScoreSendToServer(false);
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
					sessionManager.setBestScoreSendToServer(false);
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
	private void playBackgroundMusic() {
		System.out.println("Aquí entra musica habilitada:"
				+ sessionManager.isSoundEfectsEnabled());
		sessionManager = new SessionManager(getBaseContext());
		if (sessionManager.isMusicEnabled()) {
			mPlayer = MediaPlayer.create(SplashActivity.this, R.raw.intro_frikiados);
			if (!mPlayer.isPlaying()) {
				mPlayer.start();
			}
		}
	}

	protected void onResume() {
		super.onResume();
		if (mPlayer == null) {
			playBackgroundMusic();
		}
		if (!sessionManager.isCreateToken()) {
			new DownloadToken().execute();
		} else {
			 simulatedLoading();
			new DownloadQuestionsIfNeeded().execute();
		}
	}

	public static void progressBarProgress(int progress) {
		// TODO Auto-generated method stub
		progressBar.setProgress(progress);
		System.out.println("Entra en progress");
	}

}
