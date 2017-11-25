package com.appyuken.frikiadospremium.gameModes;

import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appyuken.frikiadospremium.Frikiados;
import com.appyuken.frikiadospremium.MainActivity;
import com.appyuken.frikiadospremium.R;
import com.appyuken.frikiadospremium.classes.Question;
import com.appyuken.frikiadospremium.classes.SessionManager;
import com.appyuken.frikiadospremium.classes.SoundPoolClass;
import com.appyuken.frikiadospremium.service.RestfulWebService;
import com.appyuken.gameEngine.FrikiEngine;
import com.appyuken.model.ResumeLevel;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

public class CategoryModeActivity extends Activity {

	private Button firstAnswer;
	private Button secondAnswer;
	private Button thirdAnswer;
	private Button textTimer;
	private TextView textQuestion;
	private TextView textNumQuestion;
	private String categorySelection;
	private Animation animMove;
	private Animation animMove2;
	private Animation animMove3;
	private Animation animBounce;
	private Animation animZoomIn;
	private FrikiEngine frikiEngine;
	private int correctAnswer;
	private int numberOfCorrectQuestion = 0;
	private int numberOfQuestion = 0;
	private long timeFrikiChrono = 60;
	private long pastTime;
	private final long interval = 1 * 1000;
	private CountDownTimer FrikiChrono;
	private long startTime;
	private boolean FrikiChronoIsStart = false;
	private Question question;
	private ArrayList<ResumeLevel> resumeLevel;
	private static final String MY_AD_UNIT_ID = "ca-app-pub-9585973118353015/2939825487";
	private int frikiadosScore;
	private boolean levelBoss = false;
	private int frikiadosScoreBoss;
	private int frikiadosTotalScore;
	private Button btnBoss;
	private Typeface font;
	private Typeface font2;
	private Typeface font3;
	private double timeToAnswer;
	private int numberOfBoss;
	private SessionManager sessionManager;
	private int result;
	private ProgressDialog dialog = null;
	private boolean finalBoss = false;
	private static final String AD_UNIT_ID = "ca-app-pub-9585973118353015/9578664681";
	private TextView userScore;
	private ImageView categoryAnswer;
	private boolean correctOrFail = true;
	private boolean firstQuestionLoad = true;
	private Tracker t;
	private MediaPlayer mPlayerSelection;
	private ImageView imgLogo;
	private ImageView imgJefeComic;
	private ImageView imgHumoOne;
	private ImageView imgHumoTwo;
	private ImageView imgRayo;
	private TextView txtBoss;
	private int x;
	private MediaPlayer mPlayerBoss;
	SoundPool soundPoolFail;
	HashMap<Integer, Integer> soundPoolMapFail;
	int soundIDFail = 1;
	SoundPool soundPoolCorrect;
	HashMap<Integer, Integer> soundPoolMapCorrect;
	int soundIDCorrect = 1;
	AudioManager audioManager;
	private SoundPoolClass soundPool;
	private boolean gameCompleted = false;

	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_one_player);

		t = ((Frikiados) getApplication())
				.getTracker(Frikiados.TrackerName.APP_TRACKER);
		// Set screen name.
		GoogleAnalytics.getInstance(this).reportActivityStart(this);
		t.enableAdvertisingIdCollection(true);

		soundPoolFail = new SoundPool(4, AudioManager.STREAM_MUSIC, 100);
		soundPoolMapFail = new HashMap<Integer, Integer>();
		soundPoolMapFail.put(soundIDFail,
				soundPoolFail.load(this, R.raw.fail, 1));

		soundPoolCorrect = new SoundPool(4, AudioManager.STREAM_MUSIC, 100);
		soundPoolMapCorrect = new HashMap<Integer, Integer>();
		soundPoolMapCorrect.put(soundIDCorrect,
				soundPoolCorrect.load(this, R.raw.ok2, 1));
		audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

		firstAnswer = (Button) findViewById(R.id.firstAnswer);
		secondAnswer = (Button) findViewById(R.id.secondAnswer);
		thirdAnswer = (Button) findViewById(R.id.thirdAnswer);
		textQuestion = (TextView) findViewById(R.id.textQuestion);
		textNumQuestion = (TextView) findViewById(R.id.numQuestion);
		textTimer = (Button) findViewById(R.id.textTimer);
		userScore = (TextView) findViewById(R.id.userScore);
		categoryAnswer = (ImageView) findViewById(R.id.categoryAnswer);

		sessionManager = new SessionManager(getBaseContext());
		soundPool = new SoundPoolClass(getBaseContext());

		font = Typeface.createFromAsset(getAssets(), "fonts/MYRIADPROBOLD.ttf");
		textQuestion.setTypeface(font);

		font2 = Typeface.createFromAsset(getAssets(),
				"fonts/MYRIADPROSEMIBOLD.ttf");
		font3 = Typeface.createFromAsset(getAssets(), "fonts/Minecraftia.ttf");
		firstAnswer.setTypeface(font2);
		secondAnswer.setTypeface(font2);
		thirdAnswer.setTypeface(font2);
		textTimer.setTypeface(font3);
		userScore.setTypeface(font3);
		textTimer.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources()
				.getDimension(R.dimen.radar_text));
		firstAnswer.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources()
				.getDimension(R.dimen.answer));
		secondAnswer.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources()
				.getDimension(R.dimen.answer));
		thirdAnswer.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources()
				.getDimension(R.dimen.answer));
		textQuestion.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources()
				.getDimension(R.dimen.question));

		animMove = AnimationUtils.loadAnimation(getApplicationContext(),
				R.anim.move);
		animMove2 = AnimationUtils.loadAnimation(getApplicationContext(),
				R.anim.move);
		animMove3 = AnimationUtils.loadAnimation(getApplicationContext(),
				R.anim.move);
		animBounce = AnimationUtils.loadAnimation(getApplicationContext(),
				R.anim.bounce);
		animZoomIn = AnimationUtils.loadAnimation(getApplicationContext(),
				R.anim.zoom_in);

		resumeLevel = new ArrayList<ResumeLevel>();

		firstAnswer.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				thirdAnswer.setClickable(false);
				secondAnswer.setClickable(false);
				firstAnswer.setClickable(false);
				// TODO Auto-generated method stub
				if (correctAnswer == 1) {
					soundPool.playCorrectSound();
					tickCorrectAnswer("firstAnswer");
				} else {
					soundPool.playIncorrectSound();
					tickIncorrectAnswer("firstAnswer");
				}
			}
		});

		secondAnswer.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				thirdAnswer.setClickable(false);
				secondAnswer.setClickable(false);
				firstAnswer.setClickable(false);
				if (correctAnswer == 2) {
					soundPool.playCorrectSound();
					tickCorrectAnswer("secondAnswer");
				} else {
					soundPool.playIncorrectSound();
					tickIncorrectAnswer("secondAnswer");
				}
			}
		});
		thirdAnswer.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				thirdAnswer.setClickable(false);
				secondAnswer.setClickable(false);
				firstAnswer.setClickable(false);
				// TODO Auto-generated method stub
				if (correctAnswer == 3) {
					soundPool.playCorrectSound();
					tickCorrectAnswer("thirdAnswer");
				} else {
					soundPool.playIncorrectSound();
					tickIncorrectAnswer("thirdAnswer");
				}
			}
		});

		// startGame();
		chooseCategory();

	}

	private void chooseCategory() {
		// TODO Auto-generated method stub
		final Dialog dialog = new Dialog(CategoryModeActivity.this,
				android.R.style.Theme_Translucent_NoTitleBar);
		dialog.setContentView(R.layout.custom_dialog_choose_category);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(R.color.opacity));
		final LinearLayout dialog2   = (LinearLayout) dialog.findViewById(R.id.linearOpacity);
	    dialog2.setVisibility(LinearLayout.VISIBLE);
		Animation animation   =    AnimationUtils.loadAnimation(this, R.anim.fade_in);
	    animation.setDuration(500);
	    dialog2.setAnimation(animation);
	    animation.start();
		dialog.setCancelable(false);
	    
		final Button btnComic = (Button) dialog.findViewById(R.id.btnComic);
		final Button btnManga = (Button) dialog.findViewById(R.id.btnManga);
		final Button btnSerie = (Button) dialog.findViewById(R.id.btnSeries);
		final Button btnPelicula = (Button) dialog
				.findViewById(R.id.btnPeliculas);
		final Button btnJuego = (Button) dialog.findViewById(R.id.btnJuego);
		final Button btnClose = (Button) dialog.findViewById(R.id.btnClose);
		
		btnComic.setTypeface(font);
		btnManga.setTypeface(font);
		btnSerie.setTypeface(font);
		btnPelicula.setTypeface(font);
		btnJuego.setTypeface(font);
		
		/*TextView titleChooseCategory = (TextView) dialog
				.findViewById(R.id.titleChooseCategory);
		titleChooseCategory.setTypeface(font2);
		titleChooseCategory.setTextSize(TypedValue.COMPLEX_UNIT_PX,
				getResources().getDimension(R.dimen.exit_title));*/
		btnClose.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				soundPool.playSelectionSound();
						// Do something after 100ms
						dialog.dismiss();
						finish();
			}
		});
		
		
		btnComic.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				t.send(new HitBuilders.EventBuilder()
				.setCategory("Category Activity Events")
				.setAction("Pressed Comic Button").build());
				soundPool.playSelectionSound();
				categorySelection = "Cómic";
				frikiEngine = new FrikiEngine(getBaseContext(),
						categorySelection);
				startGame();
				dialog.dismiss();

			}
		});

		btnManga.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				soundPool.playSelectionSound();
				t.send(new HitBuilders.EventBuilder()
				.setCategory("Category Activity Events")
				.setAction("Pressed Manga Button").build());
				categorySelection = "Manga";
				frikiEngine = new FrikiEngine(getBaseContext(),
						categorySelection);
				startGame();
				dialog.dismiss();
			}
		});
		btnSerie.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				t.send(new HitBuilders.EventBuilder()
				.setCategory("Category Activity Events")
				.setAction("Pressed TV Button").build());
				soundPool.playSelectionSound();
				categorySelection = "Series";
				frikiEngine = new FrikiEngine(getBaseContext(),
						categorySelection);
				startGame();
				dialog.dismiss();

			}
		});

		btnPelicula.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				t.send(new HitBuilders.EventBuilder()
				.setCategory("Category Activity Events")
				.setAction("Pressed Film Button").build());
				soundPool.playSelectionSound();
				categorySelection = "Películas";
				frikiEngine = new FrikiEngine(getBaseContext(),
						categorySelection);
				startGame();
				dialog.dismiss();
			}
		});
		btnJuego.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				t.send(new HitBuilders.EventBuilder()
				.setCategory("Category Activity Events")
				.setAction("Pressed VideoGame Button").build());
				soundPool.playSelectionSound();
				categorySelection = "Videojuegos";
				frikiEngine = new FrikiEngine(getBaseContext(),
						categorySelection);
				startGame();
				dialog.dismiss();

			}
		});
		dialog.show();
	}

	protected void tickIncorrectAnswer(String button) {
		// TODO Auto-generated method stub

		if (button.equals("firstAnswer")) {
			firstAnswer
					.setBackgroundResource(R.drawable.background_button_answer_incorrect);
		} else if (button.equals("secondAnswer")) {
			secondAnswer
					.setBackgroundResource(R.drawable.background_button_answer_incorrect);
		} else if (button.equals("thirdAnswer")) {
			thirdAnswer
					.setBackgroundResource(R.drawable.background_button_answer_incorrect);
		}
		correctOrFail = false;
		saveIncorrectAnswerQuestion(question);
		// gameFinish(true);
		final Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				// Do something after 100ms
				if (finalBoss) {
					loadNextQuestion(true);
				} else {
					loadNextQuestion(false);
				}
			}
		}, 1000);

	}

	protected void tickCorrectAnswer(String button) {
		// TODO Auto-generated method stub
		if (button.equals("firstAnswer")) {
			firstAnswer
					.setBackgroundResource(R.drawable.background_button_answer_correct);
		} else if (button.equals("secondAnswer")) {
			secondAnswer
					.setBackgroundResource(R.drawable.background_button_answer_correct);
		} else if (button.equals("thirdAnswer")) {
			thirdAnswer
					.setBackgroundResource(R.drawable.background_button_answer_correct);
		}
		correctOrFail = true;
		saveCorrectAnswerQuestion(question);
		System.out.println("Tiempo de respuesta: " + timeToAnswer);
		// calculateScoreQuestion(question, timeToAnswer);
		
		frikiadosScore = (int) (frikiadosScore + frikiEngine
				.calculateScoreQuestion(question, timeToAnswer));
		userScore.setText(Integer.toString(frikiadosScore) + "pts.");
		
		final Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				// Do something after 100ms
				if (finalBoss) {
					loadNextQuestion(true);
				} else {
					loadNextQuestion(false);
				}
			}
		}, 1000);

	}

	private void startGame() {
		// TODO Auto-generated method stub
		loadNextQuestion(false);

	}

	protected void loadNextQuestion(boolean finalBossQuestion) {
		// TODO Auto-generated method stub
		if (!finalBossQuestion) {
			if (!frikiEngine.isUserWithoutLives(getApplicationContext())) {
				if (FrikiChronoIsStart) {
					FrikiChrono.cancel();
					FrikiChrono = null;
				}
				numberOfQuestion++;
				if (frikiEngine.questionOrBoss(numberOfQuestion) == 0) {
					if (correctOrFail) {
						if (firstQuestionLoad) {
							firstQuestionLoad = false;
						} else {
							numberOfCorrectQuestion++;
						}
					}
					hideQuestion();
					// textNumQuestion.setText("Pregunta " + numberOfQuestion);
					startQuestionAnimations();
					startNumQuestionAnimation();
					loadQuestionToShow();
				} else if (frikiEngine.questionOrBoss(numberOfQuestion) == 6) {
					if (correctOrFail) {
						if (firstQuestionLoad) {
							firstQuestionLoad = false;
						} else {
							numberOfCorrectQuestion++;
						}
					}
					gameCompleted = true;
					gameFinish(false);
				} else {
					if (correctOrFail) {
						if (firstQuestionLoad) {
							firstQuestionLoad = false;
						} else {
							numberOfCorrectQuestion++;
						}
					}
					loadBossLevel(frikiEngine.questionOrBoss(numberOfQuestion));
				}

			} else {
				// returnToMain();
			}
		} else {
			if (correctOrFail) {
				if (firstQuestionLoad) {
					firstQuestionLoad = false;
				} else {
					numberOfCorrectQuestion++;
				}
			}
			gameFinish(false);
		}

	}

	private void loadQuestionToShow() {
		question = new Question();
		question = frikiEngine.getQuestion(getApplicationContext());
		if (question.getGenre().equals("Cómic")) {
			categoryAnswer
					.setBackgroundResource(R.drawable.icono_categoria_comic);
		} else if (question.getGenre().equals("Manga")) {
			categoryAnswer
					.setBackgroundResource(R.drawable.icono_categoria_manga);
		} else if (question.getGenre().equals("Videojuegos")) {
			categoryAnswer
					.setBackgroundResource(R.drawable.icono_categoria_juego);
		} else if (question.getGenre().equals("Series")) {
			categoryAnswer
					.setBackgroundResource(R.drawable.icono_categoria_serie);
		} else if (question.getGenre().equals("Películas")) {
			categoryAnswer
					.setBackgroundResource(R.drawable.icono_categoria_peliculas);
		}
		textQuestion.setText(question.getQuestionText());
		firstAnswer.setText(question.getAnswer1());
		firstAnswer.setBackgroundResource(R.drawable.background_button_answer);
		secondAnswer.setText(question.getAnswer2());
		secondAnswer.setBackgroundResource(R.drawable.background_button_answer);
		thirdAnswer.setText(question.getAnswer3());
		thirdAnswer.setBackgroundResource(R.drawable.background_button_answer);
		correctAnswer = question.getCorrectAnswer();
		/*
		 * textQuestion.setText(
		 * "¿En cuántas ocasiones aparece la hija del director, Peter Jackson, durante la trilogía de 'El Señor de los Anillos' haciendo pequeños cameos?"
		 * ); firstAnswer.setText("Sus huesos son de adamantium");
		 * secondAnswer.setText("Leer la mente al tocar a alguien");
		 * thirdAnswer.setText("Leer la mente a distancia");
		 */
	}

	private void loadQuestionBossToShow(int i) {
		question = new Question();
		question = frikiEngine.getQuestionBoss(getApplicationContext(), i);
		if (question.getGenre().equals("Cómic")) {
			categoryAnswer
					.setBackgroundResource(R.drawable.icono_categoria_comic);
		} else if (question.getGenre().equals("Manga")) {
			categoryAnswer
					.setBackgroundResource(R.drawable.icono_categoria_manga);
		} else if (question.getGenre().equals("Videojuegos")) {
			categoryAnswer
					.setBackgroundResource(R.drawable.icono_categoria_juego);
		} else if (question.getGenre().equals("Series")) {
			categoryAnswer
					.setBackgroundResource(R.drawable.icono_categoria_serie);
		} else if (question.getGenre().equals("Películas")) {
			categoryAnswer
					.setBackgroundResource(R.drawable.icono_categoria_peliculas);
		}
		textQuestion.setText(question.getQuestionText());
		firstAnswer.setText(question.getAnswer1());
		firstAnswer.setBackgroundResource(R.drawable.background_button_answer);
		secondAnswer.setText(question.getAnswer2());
		secondAnswer.setBackgroundResource(R.drawable.background_button_answer);
		thirdAnswer.setText(question.getAnswer3());
		thirdAnswer.setBackgroundResource(R.drawable.background_button_answer);
		correctAnswer = question.getCorrectAnswer();
	}

	/*
	 * private void returnToMain() { // TODO Auto-generated method stub final
	 * Handler handler = new Handler(); handler.postDelayed(new Runnable() {
	 * 
	 * @Override public void run() { // Do something after 100ms Intent
	 * activityMain = new Intent(getBaseContext(), MainActivity.class);
	 * startActivity(activityMain); } }, 700); }
	 */

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	private void loadBossLevel(final int i) {
		// TODO Auto-generated method stub

		levelBoss = true;
		final Dialog dialog = new Dialog(CategoryModeActivity.this,
				android.R.style.Theme_Translucent_NoTitleBar);
		dialog.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
		dialog.setCancelable(false);
		x = 0;
		switch (i) {
		case 1:
			dialog.setContentView(R.layout.custom_boss_comic);
			imgLogo = (ImageView) dialog.findViewById(R.id.logoJefe);
			imgJefeComic = (ImageView) dialog.findViewById(R.id.imgJefe);
			imgHumoOne = (ImageView) dialog.findViewById(R.id.imgHumoOne);
			imgHumoTwo = (ImageView) dialog.findViewById(R.id.imgHumoTwo);
			imgRayo = (ImageView) dialog.findViewById(R.id.imgRayo);
			txtBoss = (TextView) dialog.findViewById(R.id.textJefeComic);
			txtBoss.setTypeface(font);
			// textNumQuestion.setText("Comic");

			break;
		case 2:
			dialog.setContentView(R.layout.custom_boss_manga);
			imgLogo = (ImageView) dialog.findViewById(R.id.logoJefe);
			imgJefeComic = (ImageView) dialog.findViewById(R.id.imgJefe);
			imgHumoOne = (ImageView) dialog.findViewById(R.id.imgHumoOne);
			imgHumoTwo = (ImageView) dialog.findViewById(R.id.imgHumoTwo);
			imgRayo = (ImageView) dialog.findViewById(R.id.imgRayo);
			txtBoss = (TextView) dialog.findViewById(R.id.textJefeComic);
			txtBoss.setTypeface(font);
			// textNumQuestion.setText("Manga");

			break;
		case 3:
			dialog.setContentView(R.layout.custom_boss_movies);
			imgLogo = (ImageView) dialog.findViewById(R.id.logoJefe);
			imgJefeComic = (ImageView) dialog.findViewById(R.id.imgJefe);
			imgHumoOne = (ImageView) dialog.findViewById(R.id.imgHumoOne);
			imgHumoTwo = (ImageView) dialog.findViewById(R.id.imgHumoTwo);
			imgRayo = (ImageView) dialog.findViewById(R.id.imgRayo);
			txtBoss = (TextView) dialog.findViewById(R.id.textJefeComic);
			txtBoss.setTypeface(font);
			// textNumQuestion.setText("Boss Movies");

			break;
		case 4:
			dialog.setContentView(R.layout.custom_boss_series);
			imgLogo = (ImageView) dialog.findViewById(R.id.logoJefe);
			imgJefeComic = (ImageView) dialog.findViewById(R.id.imgJefe);
			imgHumoOne = (ImageView) dialog.findViewById(R.id.imgHumoOne);
			imgHumoTwo = (ImageView) dialog.findViewById(R.id.imgHumoTwo);
			imgRayo = (ImageView) dialog.findViewById(R.id.imgRayo);
			txtBoss = (TextView) dialog.findViewById(R.id.textJefeComic);
			txtBoss.setTypeface(font);
			// textNumQuestion.setText("Series");

			break;
		case 5:
			dialog.setContentView(R.layout.custom_boss_videogames);
			imgLogo = (ImageView) dialog.findViewById(R.id.logoJefe);
			imgJefeComic = (ImageView) dialog.findViewById(R.id.imgJefe);
			imgHumoOne = (ImageView) dialog.findViewById(R.id.imgHumoOne);
			imgHumoTwo = (ImageView) dialog.findViewById(R.id.imgHumoTwo);
			imgRayo = (ImageView) dialog.findViewById(R.id.imgRayo);
			txtBoss = (TextView) dialog.findViewById(R.id.textJefeComic);
			txtBoss.setTypeface(font);
			// textNumQuestion.setText("VideoGames");
			finalBoss = true;

			break;
		}
		btnBoss = (Button) dialog.findViewById(R.id.btnBoss);
		final Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				// Do something after 100ms
				soundPool.playInroBossSound();
				imgJefeComic.setVisibility(View.VISIBLE);
				imgJefeComic.startAnimation(animZoomIn);
				imgHumoOne.setVisibility(View.VISIBLE);
				imgHumoOne.startAnimation(animZoomIn);
				imgHumoTwo.setVisibility(View.VISIBLE);
				imgHumoTwo.startAnimation(animZoomIn);
				imgLogo.setVisibility(View.VISIBLE);
				imgLogo.startAnimation(animZoomIn);
				imgRayo.setVisibility(View.VISIBLE);
				imgRayo.startAnimation(animZoomIn);
				txtBoss.setVisibility(View.VISIBLE);
				txtBoss.startAnimation(animBounce);
				btnBoss.setVisibility(View.VISIBLE);
				btnBoss.startAnimation(animZoomIn);
			}
		}, 700);

		/*
		 * timer = new Timer(); timer.schedule(new TimerTask() {
		 * 
		 * @Override public void run() { runOnUiThread(new Runnable() {
		 * 
		 * public void run() { System.out.println("Entra en timer"); if(x==0){
		 * imgRayo.setImageResource(R.drawable.rayo_2); x=1; } else{
		 * imgRayo.setImageResource(R.drawable.rayo_1); x=0; } } }); } },
		 * System.currentTimeMillis(), 1000);
		 */
		final Handler handler2 = new Handler();
		final Runnable runnable = new Runnable() {
			int i = 0;

			public void run() {
				if (x == 0) {
					imgRayo.setBackgroundResource(R.drawable.rayo_2);
					x = 1;
				} else {
					imgRayo.setBackgroundResource(R.drawable.rayo_1);
					x = 0;
				}
				handler2.postDelayed(this, 500); // for interval...
			}
		};
		handler2.postDelayed(runnable, 2000); // for initial delay..

		btnBoss.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				handler2.removeCallbacks(runnable);
				hideQuestion();
				// textNumQuestion.setText("Pregunta " + numberOfQuestion);
				startQuestionAnimations();
				startNumQuestionAnimation();
				loadQuestionBossToShow(i);
				dialog.dismiss();
			}
		});
		dialog.show();
		/*
		 * startQuestionAnimations(); startNumQuestionAnimation();
		 * loadQuestionBossToShow(i);
		 */
	}

	private void gameFinish(boolean b) {
		// TODO Auto-generated method stub
		if (FrikiChronoIsStart) {
			if (FrikiChrono != null) {
				FrikiChrono.cancel();
				FrikiChrono = null;
			}
		}

		frikiadosTotalScore = frikiadosScore + frikiadosScoreBoss;
		frikiEngine.userGameOver(getApplicationContext(), resumeLevel,
				frikiadosTotalScore, numberOfCorrectQuestion, b,
				categorySelection);
		/*
		 * sendScoreToServer(frikiEngine.userGameOver(getApplicationContext(),
		 * resumeLevel, frikiadosTotalScore, numberOfCorrectQuestion, b),
		 * numberOfCorrectQuestion); /* if(FrikiChrono!=null){
		 * FrikiChrono.cancel(); }
		 */
		if (!isFinishing()) {

			final Dialog dialog = new Dialog(CategoryModeActivity.this,
					android.R.style.Theme_Translucent_NoTitleBar);
			dialog.setContentView(R.layout.custom_dialog_finishgame);
			dialog.getWindow().setBackgroundDrawable(
					new ColorDrawable(R.color.opacity));
			if (gameCompleted) {
				System.out.println("Game Completed");
				ImageView titlePopUp = (ImageView) dialog
						.findViewById(R.id.textPopUp);
				titlePopUp.setBackgroundResource(R.drawable.juego_completado);
			}

			final LinearLayout dialog2 = (LinearLayout) dialog
					.findViewById(R.id.linearOpacity);
			dialog2.setVisibility(LinearLayout.VISIBLE);
			Animation animation = AnimationUtils.loadAnimation(this,
					R.anim.fade_in);
			final Animation animation2 = AnimationUtils.loadAnimation(this,
					R.anim.fade_out);
			animation.setDuration(500);
			dialog2.setAnimation(animation);
			animation.start();
			dialog.setCancelable(false);

			Button btnMenu = (Button) dialog.findViewById(R.id.btnMenu);
			Button btnTryAgain = (Button) dialog.findViewById(R.id.btnTryAgain);
			TextView txtScore = (TextView) dialog
					.findViewById(R.id.textScorePointsAnswers);
			TextView txtBestScore = (TextView) dialog
					.findViewById(R.id.textBestScorePointsAnswers);
			TextView txtScoreTitle = (TextView) dialog
					.findViewById(R.id.textScore);
			TextView txtBestScoreTitle = (TextView) dialog
					.findViewById(R.id.textBestScore);

			txtScore.setTypeface(font3);
			txtBestScore.setTypeface(font3);
			txtScoreTitle.setTypeface(font);
			txtBestScoreTitle.setTypeface(font);

			txtScore.setText(frikiadosTotalScore + "pts / "
					+ numberOfCorrectQuestion + "?");
			if (categorySelection.equals("Cómic")) {
				txtBestScore.setText(sessionManager.getBestScoreComic()
						+ "pts / " + sessionManager.getAnsweredQuestionsComic()
						+ "?");
			} else if (categorySelection.endsWith("Manga")) {
				txtBestScore.setText(sessionManager.getBestScoreManga()
						+ "pts / " + sessionManager.getAnsweredQuestionsManga()
						+ "?");
			} else if (categorySelection.equals("Series")) {
				txtBestScore.setText(sessionManager.getBestScoreSeries()
						+ "pts / "
						+ sessionManager.getAnsweredQuestionsSeries() + "?");
			} else if (categorySelection.equals("Películas")) {
				txtBestScore.setText(sessionManager.getBestScorePeliculas()
						+ "pts / "
						+ sessionManager.getAnsweredQuestionsPeliculas() + "?");
			} else if (categorySelection.equals("Videojuegos")) {
				txtBestScore.setText(sessionManager.getBestScoreVideojuegos()
						+ "pts / "
						+ sessionManager.getAnsweredQuestionsVideojuegos()
						+ "?");
			}
			/*
			 * txtBestScore.setText(sessionManager.getBestScore() + "pts / " +
			 * sessionManager.getAnsweredQuestions() + "?");
			 */

			btnMenu.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					animation2.setDuration(500);
					dialog2.setAnimation(animation2);
					animation2.start();

					final Handler handler = new Handler();
					handler.postDelayed(new Runnable() {
						@Override
						public void run() {
							// Do something after 100ms
							dialog.dismiss();
							Intent activityMain = new Intent(getBaseContext(),
									MainActivity.class);
							startActivity(activityMain);
						}
					}, 600);

				}
			});

			btnTryAgain.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// postWall("Estoy Compartiendo un Lidin");
					animation2.setDuration(500);
					dialog2.setAnimation(animation2);
					animation2.start();

					final Handler handler = new Handler();
					handler.postDelayed(new Runnable() {
						@Override
						public void run() {
							// Do something after 100ms
							dialog.dismiss();
							finish();
							startActivity(getIntent());
						}
					}, 600);
				}
			});

			dialog.show();
		}
	}

	private void sendScoreToServer(boolean bestScore, int numberOfQuestion2) {
		// TODO Auto-generated method stub
		System.out.println("Actualizar puntuación: " + bestScore);
		System.out.println("Logueado: " + sessionManager.isLoggedInFacebook());
		if (sessionManager.isLoggedInFacebook() && bestScore == true) {
			System.out.println("Enviar puntuación al servidor");
			new UpdateScore().execute();
		} else {
			if (!sessionManager.isBestScoreSendToServer()
					&& sessionManager.isLoggedInFacebook()) {
				if (sessionManager.getBestScore() > 0)
					new UpdateScore().execute();
				System.out.println("Puntuación pendiente enviada: "
						+ sessionManager.isBestScoreSendToServer());
			}
			System.out
					.println("No se envía la puntuación porque no se ha logueado o no se ha superado la mejor puntuación");
		}

	}

	protected void hideQuestion() {
		// TODO Auto-generated method stub
		firstAnswer.setVisibility(View.INVISIBLE);
		secondAnswer.setVisibility(View.INVISIBLE);
		thirdAnswer.setVisibility(View.INVISIBLE);
		textQuestion.setVisibility(View.INVISIBLE);
		categoryAnswer.setVisibility(View.VISIBLE);
	}

	private void startQuestionAnimations() {
		System.out.println("Cargando animaciones pregunta");
		firstAnswer.setVisibility(View.VISIBLE);
		textQuestion.setVisibility(View.VISIBLE);
		categoryAnswer.setVisibility(View.VISIBLE);
		/*
		 * TranslateAnimation slide = new TranslateAnimation(0, 0, 1000,0 );
		 * slide.setDuration(1000); slide.setFillAfter(true);
		 * firstAnswer.startAnimation(slide);
		 */

		firstAnswer.startAnimation(animMove);
		textQuestion.startAnimation(animBounce);
		categoryAnswer.startAnimation(animBounce);

		// Secuencia de animación de carga de las respuestas
		animMove.setAnimationListener(new AnimationListener() {
			public void onAnimationStart(Animation animation) {
			}

			public void onAnimationRepeat(Animation animation) {
			}

			public void onAnimationEnd(Animation animation) {
				firstAnswer.clearAnimation();
				secondAnswer.setVisibility(View.VISIBLE);
				secondAnswer.startAnimation(animMove2);
				// text2.setVisibility(View.VISIBLE);

			}
		});

		animMove2.setAnimationListener(new AnimationListener() {
			public void onAnimationStart(Animation animation) {
			}

			public void onAnimationRepeat(Animation animation) {
			}

			public void onAnimationEnd(Animation animation) {
				secondAnswer.clearAnimation();
				thirdAnswer.setVisibility(View.VISIBLE);
				thirdAnswer.startAnimation(animMove3);
				// text2.setVisibility(View.VISIBLE);

			}
		});

		animMove3.setAnimationListener(new AnimationListener() {
			public void onAnimationStart(Animation animation) {
			}

			public void onAnimationRepeat(Animation animation) {
			}

			public void onAnimationEnd(Animation animation) {
				thirdAnswer.clearAnimation();
				// text2.setVisibility(View.VISIBLE);

				thirdAnswer.setClickable(true);
				secondAnswer.setClickable(true);
				firstAnswer.setClickable(true);

			}
		});
		startFrikiChrono();
	}

	protected void startFrikiChrono() {
		// TODO Auto-generated method stub

		if (correctOrFail) {
			pastTime = timeFrikiChrono + 10;
			if (pastTime > 60) {
				pastTime = 60;
			}
			textTimer.setText(String.valueOf(pastTime));
			startTime = (long) (pastTime) * 1000;
		} else {
			pastTime = timeFrikiChrono - 3;
			if (pastTime <= 0) {
				startTime = (long) (0);
				textTimer.setText(String.valueOf("0"));
			} else {
				startTime = (long) (pastTime) * 1000;
				textTimer.setText(String.valueOf(pastTime));
			}
		}

		if (pastTime <= 0) {
			FrikiChronoIsStart = false;
			gameFinish(true);
		} else {
			FrikiChrono = new FrikiChrono(startTime, interval);
			final Handler handler = new Handler();
			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					// Do something after 100ms

					FrikiChrono.start();
				}
			}, 1500);
		}
	}

	private void startNumQuestionAnimation() {
		// TODO Auto-generated method stub
		textNumQuestion.setVisibility(View.VISIBLE);
		textNumQuestion.startAnimation(animZoomIn);
		textNumQuestion.setText("Pregunta " + numberOfQuestion);

	}

	public class FrikiChrono extends CountDownTimer {
		public FrikiChrono(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
			// TODO Auto-generated constructor stub
			textTimer.setTextColor(getResources().getColor(R.color.white));
			System.out.print("entra chrono");
			FrikiChronoIsStart = true;
		}

		@Override
		public void onFinish() {
			textTimer.setTextColor(getResources().getColor(R.color.white));
			textTimer.setText("");// Vaciamos contador
			gameFinish(true);
			FrikiChronoIsStart = false;
		}

		@Override
		public void onTick(long millisUntilFinished) {
			textTimer.setText("" + millisUntilFinished / 1000);
			timeToAnswer = pastTime
					- Math.round((double) (millisUntilFinished / 1000));
			timeFrikiChrono = Math.round((double) (millisUntilFinished / 1000));
			System.out.println(millisUntilFinished / 1000);

			if (millisUntilFinished / 1000 == 5) {
				textTimer.setTextColor(getResources().getColor(R.color.red));
			}
		}
	}

	protected void saveCorrectAnswerQuestion(Question question2) {
		// TODO Auto-generated method stub
		ResumeLevel resumeLeve = new ResumeLevel();
		resumeLeve.setQuestionId(question2.getQuestionId());
		resumeLeve.setGenre(question2.getGenre());
		resumeLeve.setDifficulty(question2.getDifficulty());
		resumeLeve.setTimesAppeared(question.getTimesAppeared());
		resumeLeve.setGoodQuestion(true);
		resumeLevel.add(resumeLeve);

	}

	/*
	 * private void calculateScoreQuestion(Question question2, double
	 * timeAnswer) {
	 * 
	 * if (timeAnswer < 1) { timeAnswer = 1; }
	 * 
	 * if (levelBoss) { levelBoss = false; numberOfBoss++; double scoreBoss =
	 * Math.round((double) (1000 * numberOfBoss) / timeAnswer); if (scoreBoss <
	 * 0) { scoreBoss = 1; } else if (scoreBoss > 5000) { scoreBoss = 5000; }
	 * frikiadosScoreBoss = (int) (frikiadosScoreBoss + scoreBoss); //
	 * bossDefeated++; System.out.println("Score boss"); } else { double
	 * scoreAnswer = Math .round((double) (10 * numberOfQuestion * question2
	 * .getDifficulty()) / timeAnswer); if (scoreAnswer < 0) { scoreAnswer = 1;
	 * } else if (scoreAnswer > 500) { scoreAnswer = 500; } frikiadosScore =
	 * (int) (frikiadosScore + scoreAnswer); } int scoreTextview =
	 * frikiadosScore + frikiadosScoreBoss;
	 * userScore.setText(Integer.toString(scoreTextview) + "pts."); }
	 */

	protected void saveIncorrectAnswerQuestion(Question question2) {
		// TODO Auto-generated method stub
		ResumeLevel resumeLeve = new ResumeLevel();
		resumeLeve.setQuestionId(question2.getQuestionId());
		resumeLeve.setGenre(question2.getGenre());
		resumeLeve.setDifficulty(question2.getDifficulty());
		resumeLeve.setTimesAppeared(question.getTimesAppeared());
		resumeLeve.setGoodQuestion(false);
		resumeLevel.add(resumeLeve);
	}

	public void onBackPressed() {
		final Dialog dialog = new Dialog(CategoryModeActivity.this,
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
				soundSelection();
				FrikiChrono.cancel();
				finish();
			}
		});

		btnCancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				soundSelection();
				dialog.dismiss();
			}
		});
		dialog.show();

	}

	public class UpdateScore extends AsyncTask<Void, String, Void> {

		@Override
		protected void onPreExecute() {
			dialog = new ProgressDialog(CategoryModeActivity.this);
			dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			dialog.setCancelable(false);
			dialog.setMessage("Cargando Ranking! Por favor espere unos segundos...!");
			dialog.show();

		}

		@Override
		protected Void doInBackground(Void... unused) {
			HashMap<String, String> hashmap = sessionManager.getUserDetails();
			String idFacebook = hashmap.get(SessionManager.KEY_FB_USER_ID);
			String idUser = hashmap.get(SessionManager.KEY_ID_USER_FRIKIADOS);
			result = RestfulWebService.sendScore(idUser, idFacebook,
					Integer.toString(sessionManager.getBestScore()),
					Integer.toString(sessionManager.getAnsweredQuestions()),
					getApplicationContext());
			return (null);
		}

		@Override
		protected void onPostExecute(Void unused) {

			switch (result) {
			case 0:
				// NO HAY ERROR
				try {
					dialog.dismiss();
					dialog = null;
					sessionManager.setBestScoreSendToServer(true);
					// goToTarget = false;
				} catch (Exception e) {
					// nothing
				}
				break;

			case 1:
				try {
					dialog.dismiss();
					dialog = null;
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
					dialog.dismiss();
					dialog = null;
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

	protected void soundSelection() {
		// TODO Auto-generated method stub
		sessionManager = new SessionManager(getBaseContext());
		if (sessionManager.isSoundEfectsEnabled()) {
			mPlayerSelection = MediaPlayer.create(CategoryModeActivity.this,
					R.raw.select_pop);
			if (!mPlayerSelection.isPlaying()) {
				mPlayerSelection.start();
			}
		}
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.one_player, menu);
		return true;
	}

}
