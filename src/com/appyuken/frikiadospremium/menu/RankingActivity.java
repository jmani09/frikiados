package com.appyuken.frikiadospremium.menu;

import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.appyuken.frikiadospremium.R;
import com.appyuken.frikiadospremium.Frikiados;
import com.appyuken.frikiadospremium.classes.ResultGetScore;
import com.appyuken.frikiadospremium.classes.Score;
import com.appyuken.frikiadospremium.classes.SessionManager;
import com.appyuken.frikiadospremium.classes.SoundPoolClass;
import com.appyuken.frikiadospremium.service.BackgroundAudiioService;
import com.appyuken.frikiadospremium.service.RestfulWebService;
import com.appyuken.imageloader.ImageLoader;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

public class RankingActivity extends Activity {
	ResultGetScore resultGetSocres;
	private ProgressDialog dialog = null;
	private List<Score> listOfBestScores;
	private AccountAdapter scoresAdapter;
	private ListView listView;
	private SessionManager sessionManager;
	private int resultScore;
	private boolean clickButton = false;
	private Intent playbackServiceIntent;
	private SoundPoolClass soundPool;
	private Tracker t;
	private ImageLoader imageLoader;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ranking);

		t = ((Frikiados) getApplication()).getTracker(Frikiados.TrackerName.APP_TRACKER);
		// Set screen name.
		GoogleAnalytics.getInstance(this).reportActivityStart(this);
		t.enableAdvertisingIdCollection(true);
		
		imageLoader = new ImageLoader(getApplicationContext(),String.valueOf((int)pxFromDp(70)),String.valueOf((int)pxFromDp(70)));
		sessionManager = new SessionManager(getApplicationContext());
		soundPool = new SoundPoolClass(getApplicationContext());
		playbackServiceIntent = new Intent(this, BackgroundAudiioService.class);

		listView = (ListView) findViewById(R.id.frkiadosRanking);
		System.out.println("Puntuación pendiente enviada: " + sessionManager.isBestScoreSendToServer());
		if(!sessionManager.isBestScoreSendToServer()&& sessionManager.isLoggedInFacebook()){
			if(sessionManager.getBestScore()>0)
			new UpdateScore().execute();
		}
		if(!sessionManager.isLoggedInFacebook()){
			showAlertFacebook();
		}
		
		new RefreshScores().execute();
	}

	private void showAlertFacebook() {
		// TODO Auto-generated method stub
		AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
		builder1.setTitle("Frikiados");
        builder1.setMessage("Vincula tu cuenta con Facebook para que tu puntuación quede registrada!!! ");
        builder1.setCancelable(true);
        builder1.setPositiveButton("Si",
                new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            	clickButton = true;
            	Intent profileActivity = new Intent(getBaseContext(),
						ProfileActivity.class);
				startActivity(profileActivity);
            	finish();
                dialog.cancel();
            }
        });
        builder1.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        AlertDialog alert11 = builder1.create();
        alert11.show();
	}

	class AccountAdapter extends ArrayAdapter<Score> {

		Context context;

		AccountAdapter(Context context, int resource, List<Score> objects) {
			super(context, resource, objects);
			this.context = context;
		}

		@SuppressLint("ViewHolder")
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			View row = View.inflate(context, R.layout.user_ranking_frikiados,
					null);

			// Recorrremos la lista de lidins para crear las filas del ListView
			if (position < listOfBestScores.size()) {
				// Procedemos a recuperar y mostrar todos los datos
				// pertenecientes al Lidin
				final TextView frikiadosNickName = (TextView) row
						.findViewById(R.id.frkiadosNickname);
				final TextView frikiadosUserScore = (TextView) row
						.findViewById(R.id.frikiadosPoints);
				final TextView frikiadosUserAnswerScore = (TextView) row
						.findViewById(R.id.frkiadosAnswer);
				final ImageView frikiadosImageRanking = (ImageView)row.findViewById(R.id.iconRanking);

				frikiadosNickName.setText(listOfBestScores.get(position)
						.getUserNickname());
				frikiadosUserScore.setText(listOfBestScores.get(position)
						.getUserScore());
				frikiadosUserAnswerScore.setText(listOfBestScores.get(position)
						.getUserAnswerScore());
				
				if(position==0){
					frikiadosImageRanking.setImageResource(R.drawable.trofeo_oro);
				}else if(position==1){
					frikiadosImageRanking.setImageResource(R.drawable.trofeo_plata);
				}else if(position==2){
					frikiadosImageRanking.setImageResource(R.drawable.trofeo_bronce);
				}else{
					//frikiadosImageRanking.setBackgroundResource(R.drawable.foto_perfil);
					System.out.println("id " + listOfBestScores.get(position).getIdFacebook());
					RestfulWebService.getCacheImage(listOfBestScores.get(position).getIdFacebook(), imageLoader, frikiadosImageRanking, getApplicationContext());
				}

			}
			return (row);
		}
	}

	public class RefreshScores extends AsyncTask<Void, String, Void> {

		@Override
		protected void onPreExecute() {
			dialog = new ProgressDialog(RankingActivity.this);
			dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			dialog.setCancelable(false);
			dialog.setMessage("Cargando Ranking! Por favor espere unos segundos...!");
			dialog.show();

		}

		@Override
		protected Void doInBackground(Void... unused) {
			resultGetSocres = RestfulWebService
					.getFrikiadosRanking(getApplicationContext());
			return (null);
		}

		@Override
		protected void onPostExecute(Void unused) {

			if (resultGetSocres != null) {
				switch (resultGetSocres.getError()) {
				case 0:
					// NO HAY ERROR
					try {
						dialog.dismiss();
						dialog = null;
						printScores(resultGetSocres);
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

	}

	private void printScores(ResultGetScore resultGetSocres) {
		// TODO Auto-generated method stub
		soundPool.playSelectionSound();
		if (resultGetSocres != null) {
			listOfBestScores = resultGetSocres.getScore();
			scoresAdapter = new AccountAdapter(getBaseContext(),
					R.layout.user_ranking_frikiados, listOfBestScores);
			listView.setAdapter(scoresAdapter);
			scoresAdapter.notifyDataSetChanged();
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.ranking, menu);
		return true;
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
			resultScore = RestfulWebService
					.sendScore(idUser, idFacebook,
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
	private float pxFromDp(float dp)
	{
	    return dp * this.getApplicationContext().getResources().getDisplayMetrics().density;
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
