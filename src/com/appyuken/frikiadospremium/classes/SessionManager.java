package com.appyuken.frikiadospremium.classes;

import java.util.HashMap;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SessionManager {
	// Shared Preferences
	SharedPreferences pref;

	// Editor for Shared preferences
	Editor editor;

	// Context
	Context _context;

	// Shared pref mode
	int PRIVATE_MODE = 0;

	// Sharedpref file name
	private static final String IS_BBDD_CREATED = "IsBBDDCreated";
	private static final String PREF_NAME = "AndroidHivePref";
	// All Shared Preferences Keys
	private static final String IS_LOGINFACEBOOK = "IsLoggedInFacebook";

	/******************************* LOGIN FRIKIADOS ***************************/
	// User name (make variable public to access from outside)
	public static final String KEY_NICKNAME = "nickname";
	// Email address (make variable public to access from outside)
	public static final String KEY_EMAIL = "email";
	// User sex
	public static final String KEY_GENDER = "gender";
	// User name (make variable public to access from outside)
	public static final String KEY_ID_USER_FRIKIADOS = "idUserFrikiados";
	// User lifes game
	public static final String KEY_NUMBER_LIFES = "numberOfLifes";
	// User birthday
	public static final String KEY_USER_BIRTHDAY = "birthday";
	// User name (make variable public to access from outside)
	public static final String TOKEN = "communicationKey";
	// Key verify if token create
	private static final String IS_TOKEN_CREATE = "IsTokenCreated";
	// User best score
	public static final String KEY_BEST_SCORE = "bestScore";
	// User best score
	public static final String KEY_BEST_SCORE_MANGA = "bestScoreManga";
	// User best score
	public static final String KEY_BEST_SCORE_COMIC = "bestScoreComic";
	// User best score
	public static final String KEY_BEST_SCORE_SERIES = "bestScoreSeries";
	// User best score
	public static final String KEY_BEST_SCORE_PELICULAS = "bestScorePeliculas";
	// User best score
	public static final String KEY_BEST_SCORE_VIDEOJUEGOS = "bestScoreVideojuegos";
	// User num of answer questions all categories
	public static final String KEY_NUM_OF_ANSWER_QUESTION = "numOfAnswerQuestions";
	// User num of answer questions manga
	public static final String KEY_NUM_OF_ANSWER_QUESTION_COMIC = "numOfAnswerQuestionsComic";
	// User num of answer questions comic
	public static final String KEY_NUM_OF_ANSWER_QUESTION_MANGA = "numOfAnswerQuestionsManga";
	// User num of answer questions series
	public static final String KEY_NUM_OF_ANSWER_QUESTION_PELICULAS = "numOfAnswerQuestionsPeliculas";
	// User num of answer questions peliculas
	public static final String KEY_NUM_OF_ANSWER_QUESTION_SERIES = "numOfAnswerQuestionsSeries";
	// User num of answer questions videojuegos
	public static final String KEY_NUM_OF_ANSWER_QUESTION_VIDEOJUEGOS = "numOfAnswerQuestionsVideojuegos";
	// User LastUpdate
	public static final String KEY_LAST_UPDATE = "LastUpdate";
	/***********************************************************************/
	/***********************************************************************/

	/******************************* FACEBOOK ********************************/
	// id user in Facebook (make variable public to access from outside)
	public static final String KEY_FB_USER_ID = "fbUserId";

	public static final String STATUS_ERROR = "error";
	public static final Object STATUS_OK = "ok";
	/******************************* SOUND PREFERENCES ********************************/
	public static final String IS_SOUND_EFFECTS_ENABLED = "activeSounds";
	public static final String IS_MUSIC_ENABLED = "soundActive";
	public static final String CURRENT_AUDIO_POSITION = "currentPos";
	/******************************* LIFES PREFERENCES ********************************/
	public static final String IS_COUNT_DOWN_TIMER_ENABLED = "timerEnabled";
	public static final String DATE_OF_LASTIMER = "soundActive";
	/******************************* SEND SCORE ********************************/
	public static final String IS_BEST_SCORE_SEND_TO_SERVER = "sendBestScore";
	public static final String IS_FIRST_TIME_APP = "firstTime";
	/******************************* SERVICE SOUND ********************************/
	public static final String PLAY_MUSIC = "playMusic";
	public static final String PLAY_CORRECT_SOUND = "playCorrectSound";
	public static final String PLAY_FAIL_SOUND = "playFailSound";
	public static final String PLAY_SELECTION_SOUND = "playSelectionSound";
	public static final String PLAY_BOSS_SOUND = "playBossSound";

	/***********************************************************************/
	/***********************************************************************/

	// Constructor
	public SessionManager(Context context) {
		this._context = context;
		pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE); // Private_mode
																		// means
																		// that
																		// only
																		// the
																		// application
																		// creating
																		// the
																		// shared
																		// preference
																		// can
																		// read/write
																		// the
																		// preference
		editor = pref.edit();
	}

	/**
	 * Get stored session data
	 * */
	public HashMap<String, String> getUserDetails() {
		// HashMap pares de nombre propiedad, valor propiedad
		HashMap<String, String> user = new HashMap<String, String>();

		// user name
		user.put(KEY_NICKNAME, pref.getString(KEY_NICKNAME, null));
		// user email
		user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));
		// id user frikiados
		user.put(KEY_ID_USER_FRIKIADOS,
				pref.getString(KEY_ID_USER_FRIKIADOS, null));
		// number of lifes
		user.put(KEY_NUMBER_LIFES, pref.getString(KEY_NUMBER_LIFES, "5"));
		// user id on facebook
		user.put(KEY_FB_USER_ID, pref.getString(KEY_FB_USER_ID, null));
		// user gender
		user.put(KEY_GENDER, pref.getString(KEY_GENDER, "male"));
		// user birthday
		user.put(KEY_USER_BIRTHDAY, pref.getString(KEY_USER_BIRTHDAY, null));
		// user best socre
		user.put(KEY_BEST_SCORE, pref.getString(KEY_BEST_SCORE, "0"));
		// user best socre
		user.put(KEY_BEST_SCORE_MANGA,
				pref.getString(KEY_BEST_SCORE_MANGA, "0"));
		// user best socre
		user.put(KEY_BEST_SCORE_COMIC,
				pref.getString(KEY_BEST_SCORE_COMIC, "0"));
		// user best socre
		user.put(KEY_BEST_SCORE_PELICULAS,
				pref.getString(KEY_BEST_SCORE_PELICULAS, "0"));
		// user best socre
		user.put(KEY_BEST_SCORE_SERIES,
				pref.getString(KEY_BEST_SCORE_SERIES, "0"));
		// user best socre
		user.put(KEY_BEST_SCORE_VIDEOJUEGOS,
				pref.getString(KEY_BEST_SCORE_VIDEOJUEGOS, "0"));
		// user num of answer questions
		user.put(KEY_NUM_OF_ANSWER_QUESTION,
				pref.getString(KEY_NUM_OF_ANSWER_QUESTION, "0"));
		// user token
		user.put(TOKEN, pref.getString(TOKEN, null));
		// user LastUpdate
		user.put(KEY_LAST_UPDATE, pref.getString(KEY_LAST_UPDATE, "1"));
		// return user
		return user;
	}

	/**
	 * Clear session facebook details
	 * */
	public void logoutUserFacebook() {
		// Clearing all data from Shared Preferences
		editor.putBoolean(IS_LOGINFACEBOOK, false);
		editor.putString(KEY_EMAIL, "");
		editor.putString(KEY_ID_USER_FRIKIADOS, "");
		editor.putString(KEY_FB_USER_ID, "");
		editor.putString(KEY_NICKNAME, "");
		editor.putString(KEY_USER_BIRTHDAY, "");
		editor.putString(KEY_GENDER, "");
		editor.commit();
	}

	public long getMusicPosition() {
		return pref.getLong(CURRENT_AUDIO_POSITION, 0);
		// Si está la variable está vacia devuelve false por defecto
	}

	public void setMusicPosition(int currentPos) {
		// TODO Auto-generated method stub
		editor.putLong(CURRENT_AUDIO_POSITION, currentPos);
		editor.commit();
	}

	public boolean isBestScoreSendToServer() {
		return pref.getBoolean(IS_BEST_SCORE_SEND_TO_SERVER, true);
		// Si está la variable está vacia devuelve false por defecto
	}

	public void setBestScoreSendToServer(boolean scoreBolean) {
		// TODO Auto-generated method stub
		editor.putBoolean(IS_BEST_SCORE_SEND_TO_SERVER, scoreBolean);
		editor.commit();
	}

	public boolean isFirstTimeApp() {
		return pref.getBoolean(IS_FIRST_TIME_APP, true);
		// Si está la variable está vacia devuelve false por defecto
	}

	public void setFirstTimeApp(boolean firstTime) {
		// TODO Auto-generated method stub
		editor.putBoolean(IS_FIRST_TIME_APP, firstTime);
		editor.commit();
	}

	public boolean isCountDownTimerLifesEnabled() {
		return pref.getBoolean(IS_COUNT_DOWN_TIMER_ENABLED, false);
		// Si está la variable está vacia devuelve false por defecto
	}

	public void setCountDownTimerLifesEnable() {
		// TODO Auto-generated method stub
		editor.putBoolean(IS_COUNT_DOWN_TIMER_ENABLED, true);
		editor.commit();
	}

	public void setCountDownTimerLifesDisable() {
		// TODO Auto-generated method stub
		editor.putBoolean(IS_COUNT_DOWN_TIMER_ENABLED, false);
		editor.commit();
	}

	public void setDateLastGenerationLifes(String dateLastGenerationLifes) {
		// TODO Auto-generated method stub
		editor.putString(DATE_OF_LASTIMER, dateLastGenerationLifes);
		editor.commit();

	}

	public String geetDateLastGenerationLifes() {
		return pref.getString(DATE_OF_LASTIMER, "0");
		// Si está la variable está vacia devuelve false por defecto
	}

	public boolean isCreateBBDD() {
		return pref.getBoolean(IS_BBDD_CREATED, false);// Si está vacia la
														// variable
		// devuelve false por defecto
	}

	public boolean isSoundEfectsEnabled() {
		return pref.getBoolean(IS_SOUND_EFFECTS_ENABLED, true);// Si está vacia
																// la
		// variable
		// devuelve false por defecto
	}

	public void setSoundEfectsEnable() {
		// TODO Auto-generated method stub
		editor.putBoolean(IS_SOUND_EFFECTS_ENABLED, true);
		editor.commit();

	}

	public void setSoundEfectsDisable() {
		// TODO Auto-generated method stub
		editor.putBoolean(IS_SOUND_EFFECTS_ENABLED, false);
		editor.commit();

	}

	public boolean isMusicEnabled() {
		return pref.getBoolean(IS_MUSIC_ENABLED, true);// Si está vacia la
														// variable
		// devuelve false por defecto
	}

	public void setMusicEnable() {
		// TODO Auto-generated method stub
		editor.putBoolean(IS_MUSIC_ENABLED, true);
		editor.commit();

	}

	public void setMusicDisable() {
		// TODO Auto-generated method stub
		editor.putBoolean(IS_MUSIC_ENABLED, false);
		editor.commit();

	}

	public boolean isLoggedInFacebook() {
		return pref.getBoolean(IS_LOGINFACEBOOK, false);// Si está vacia la
														// variable
		// devuelve false por defecto
	}

	public String getNumberOfLifes() {
		return pref.getString(KEY_NUMBER_LIFES, "");
	}

	public void createBBDD() {
		// Storing create bbdd files value as TRUE
		editor.putBoolean(IS_BBDD_CREATED, true);//
		editor.commit();
	}

	/**
	 * Create login session for Facebook Login
	 * 
	 * @param email
	 * @param fbSex
	 * */
	public void createLoginSessionFrikiados(String fbId, String fbBirthday,
			String fbGender, String email, String frikiadosId,
			String userNickname) {
		// Storing login value as TRUE
		editor.putBoolean(IS_LOGINFACEBOOK, true);

		// Storing facebook user id in pref
		editor.putString(KEY_FB_USER_ID, fbId);

		// Storing facebook birthday name in pref
		editor.putString(KEY_USER_BIRTHDAY, fbBirthday);
		// Storing facebook email in pref
		editor.putString(KEY_EMAIL, email);
		// Storing facebook gender in pref
		editor.putString(KEY_GENDER, fbGender);
		// Storing id frikaidos in pref
		editor.putString(KEY_ID_USER_FRIKIADOS, frikiadosId);
		// Storing id frikaidos in pref
		editor.putString(KEY_NICKNAME, userNickname);

		// commit changes
		editor.commit();

	}

	public void updateProfileFrikiados(String userNickname, String userEmail) {
		// Storing new values for user profile

		// Storing new email in pref
		editor.putString(KEY_EMAIL, userEmail);
		// Storing new nickname in pref
		editor.putString(KEY_NICKNAME, userNickname);
		// commit changes
		editor.commit();
	}

	// Save bestScore all categories
	public boolean updateBestScore(int newScore, int numOfQuestions) {
		// Update bestScore only score is best than last
		int lastScore = Integer.parseInt(pref.getString(KEY_BEST_SCORE, "0"));
		if (newScore > lastScore) {
			// update bestScore
			System.out.println("puntuación mejor");
			editor.putString(KEY_BEST_SCORE, Integer.toString(newScore));
			editor.putString(KEY_NUM_OF_ANSWER_QUESTION,
					Integer.toString(numOfQuestions));
			// comit changes
			editor.commit();
			return true;
		} else {
			System.out.println("puntuación igual o peor");
			return false;
		}
	}

	// Get bestScore all categories
	public int getBestScore() {
		return Integer.parseInt(pref.getString(KEY_BEST_SCORE, "0"));
	}

	// Get coorect answered questions of all categories
	public int getAnsweredQuestions() {
		return Integer
				.parseInt(pref.getString(KEY_NUM_OF_ANSWER_QUESTION, "0"));
	}

	// Save bestScore comic categories
	public boolean updateBestScoreComic(int newScore, int numOfQuestions) {
		// Update bestScore only score is best than last
		int lastScore = Integer.parseInt(pref.getString(KEY_BEST_SCORE_COMIC,
				"0"));
		if (newScore > lastScore) {
			// update bestScore
			System.out.println("puntuación mejor");
			editor.putString(KEY_BEST_SCORE_COMIC, Integer.toString(newScore));
			editor.putString(KEY_NUM_OF_ANSWER_QUESTION_COMIC,
					Integer.toString(numOfQuestions));
			// comit changes
			editor.commit();
			return true;
		} else {
			System.out.println("puntuación igual o peor");
			return false;
		}
	}

	// Get bestScore comic categories
	public int getBestScoreComic() {
		return Integer.parseInt(pref.getString(KEY_BEST_SCORE_COMIC, "0"));
	}

	// Get coorect answered questions of comic categories
	public int getAnsweredQuestionsComic() {
		return Integer.parseInt(pref.getString(
				KEY_NUM_OF_ANSWER_QUESTION_COMIC, "0"));
	}

	// Save bestScore manga categories
	public boolean updateBestScoreManga(int newScore, int numOfQuestions) {
		// Update bestScore only score is best than last
		int lastScore = Integer.parseInt(pref.getString(KEY_BEST_SCORE_MANGA,
				"0"));
		if (newScore > lastScore) {
			// update bestScore
			System.out.println("puntuación mejor");
			editor.putString(KEY_BEST_SCORE_MANGA, Integer.toString(newScore));
			editor.putString(KEY_NUM_OF_ANSWER_QUESTION_MANGA,
					Integer.toString(numOfQuestions));
			// comit changes
			editor.commit();
			return true;
		} else {
			System.out.println("puntuación igual o peor");
			return false;
		}
	}

	// Get bestScore manga categories
	public int getBestScoreManga() {
		return Integer.parseInt(pref.getString(KEY_BEST_SCORE_MANGA, "0"));
	}

	// Get coorect answered questions of all categories
	public int getAnsweredQuestionsManga() {
		return Integer.parseInt(pref.getString(
				KEY_NUM_OF_ANSWER_QUESTION_MANGA, "0"));
	}

	// Save bestScore series categories
	public boolean updateBestScoreSeries(int newScore, int numOfQuestions) {
		// Update bestScore only score is best than last
		int lastScore = Integer.parseInt(pref.getString(KEY_BEST_SCORE_SERIES,
				"0"));
		if (newScore > lastScore) {
			// update bestScore
			System.out.println("puntuación mejor");
			editor.putString(KEY_BEST_SCORE_SERIES, Integer.toString(newScore));
			editor.putString(KEY_NUM_OF_ANSWER_QUESTION_SERIES,
					Integer.toString(numOfQuestions));
			// comit changes
			editor.commit();
			return true;
		} else {
			System.out.println("puntuación igual o peor");
			return false;
		}
	}

	// Get bestScore series categories
	public int getBestScoreSeries() {
		return Integer.parseInt(pref.getString(KEY_BEST_SCORE_SERIES, "0"));
	}

	// Get coorect answered questions of all categories
	public int getAnsweredQuestionsSeries() {
		return Integer.parseInt(pref.getString(
				KEY_NUM_OF_ANSWER_QUESTION_SERIES, "0"));
	}

	// Save bestScore peliculas categories
	public boolean updateBestScorePeliculas(int newScore, int numOfQuestions) {
		// Update bestScore only score is best than last
		int lastScore = Integer.parseInt(pref.getString(
				KEY_BEST_SCORE_PELICULAS, "0"));
		if (newScore > lastScore) {
			// update bestScore
			System.out.println("puntuación mejor");
			editor.putString(KEY_BEST_SCORE_PELICULAS,
					Integer.toString(newScore));
			editor.putString(KEY_NUM_OF_ANSWER_QUESTION_PELICULAS,
					Integer.toString(numOfQuestions));
			// comit changes
			editor.commit();
			return true;
		} else {
			System.out.println("puntuación igual o peor");
			return false;
		}
	}

	// Get bestScore all categories
	public int getBestScorePeliculas() {
		return Integer.parseInt(pref.getString(KEY_BEST_SCORE_PELICULAS, "0"));
	}

	// Get coorect answered questions of all categories
	public int getAnsweredQuestionsPeliculas() {
		return Integer.parseInt(pref.getString(
				KEY_NUM_OF_ANSWER_QUESTION_PELICULAS, "0"));
	}

	// Save bestScore videojuegos categories
	public boolean updateBestScoreVideojuegos(int newScore, int numOfQuestions) {
		// Update bestScore only score is best than last
		int lastScore = Integer.parseInt(pref.getString(
				KEY_BEST_SCORE_VIDEOJUEGOS, "0"));
		if (newScore > lastScore) {
			// update bestScore
			System.out.println("puntuación mejor");
			editor.putString(KEY_BEST_SCORE_VIDEOJUEGOS,
					Integer.toString(newScore));
			editor.putString(KEY_NUM_OF_ANSWER_QUESTION_VIDEOJUEGOS,
					Integer.toString(numOfQuestions));
			// comit changes
			editor.commit();
			return true;
		} else {
			System.out.println("puntuación igual o peor");
			return false;
		}
	}

	// Get bestScore videojuegos categories
	public int getBestScoreVideojuegos() {
		return Integer
				.parseInt(pref.getString(KEY_BEST_SCORE_VIDEOJUEGOS, "0"));
	}

	// Get coorect answered questions of all categories
	public int getAnsweredQuestionsVideojuegos() {
		return Integer.parseInt(pref.getString(
				KEY_NUM_OF_ANSWER_QUESTION_VIDEOJUEGOS, "0"));
	}

	/*
	 * public void updateLifes(int newLifes) { // TODO Auto-generated method
	 * stub editor.putString(KEY_NUMBER_LIFES, String.valueOf(newLifes));
	 * editor.commit();
	 * 
	 * }
	 */

	public void saveToken(String token) {
		// TODO Auto-generated method stub
		editor.putString(TOKEN, token);
		editor.putBoolean(IS_TOKEN_CREATE, true);
		editor.commit();
	}

	public String getToken() {
		return pref.getString(TOKEN, "2c71dc888bc98f836d871e85e37feff9");
	}

	public boolean isCreateToken() {
		return pref.getBoolean(IS_TOKEN_CREATE, false);// Si está vacia la
														// variable
		// devuelve false por defecto
	}

	public void saveLastUpdate(String lastUpdate) {
		// TODO Auto-generated method stub
		editor.putString(KEY_LAST_UPDATE, lastUpdate);
		editor.commit();
	}

	public String getLastUpdate() {
		return pref.getString(KEY_LAST_UPDATE, "1");
	}
}
