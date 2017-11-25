package com.appyuken.frikiadospremium.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.StrictMode;
import android.util.Log;
import android.widget.ImageView;

import com.appyuken.frikiadospremium.SplashActivity;
import com.appyuken.frikiadospremium.classes.Question;
import com.appyuken.frikiadospremium.classes.ResultGetQuestion;
import com.appyuken.frikiadospremium.classes.ResultGetScore;
import com.appyuken.frikiadospremium.classes.Score;
import com.appyuken.frikiadospremium.classes.SessionManager;
import com.appyuken.imageloader.ImageLoader;
import com.appyuken.model.QuestionDAO;

/**
 * Clase que proporciona la logica de negocio para interactuar con el servidor.
 * 
 * @version 1.0, 8/1/14
 * @author AppYuken
 */
@SuppressLint("SimpleDateFormat")
public class RestfulWebService extends RestfulWeb {

	// Session Manager Class (propia clase de la app)
	public static SessionManager sessionManagerFrikiados;

	private static int TIME_OUT_CONNEXION = 0; // 12 segundos
	private static int TIME_OUT_SOCKET = 60000; // 12 segundos
	private static String idUserFrikiados;
	private static String idUserFacebook;
	private static String userNickname;
	private static String userEmail;
	private static String userBirthday;
	private static String userGender;
	private static int userScore = 0;
	private static int userAnsweredQuestions = 0;

	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@SuppressLint("NewApi")
	public static int getLoginFacebook(String idFacebook, String nickName,
			String email, String birthday, String gender,
			Context applicationContext) {
		// Session Manager
		sessionManagerFrikiados = new SessionManager(applicationContext);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);
		String userBirthday2 ="";
		if(!birthday.isEmpty()&&birthday!=null){
		StringTokenizer tokens = new StringTokenizer(birthday, "/");
		String first = tokens.nextToken();// this will contain "Fruit"
		String second = tokens.nextToken();
		String third = tokens.nextToken();
		userBirthday2 = third + "/" + second + "/" + first;
		}

		// Inicializao variables
		int conexion = 1;

		// HttpClient httpclient = new DefaultHttpClient();
		// Prepare a request object

		System.out.println("idFacebook: " + idFacebook);
		HttpGet httpget = new HttpGet(HTTP_REST_FRIKIADOS
				+ "loginFrikiados?idFacebookUser=" + idFacebook + "&nickname="
				+ nickName + "&email=" + email + "&birthday=" + userBirthday2
				+ "&sex=" + gender);

		HttpParams httpParameters = new BasicHttpParams();
		// Set the timeout in milliseconds until a connection is established.
		// The default value is zero, that means the timeout is not used.
		HttpConnectionParams.setConnectionTimeout(httpParameters,
				TIME_OUT_CONNEXION);
		// Set the default socket timeout (SO_TIMEOUT)
		// in milliseconds which is the timeout for waiting for data.
		HttpConnectionParams.setSoTimeout(httpParameters, TIME_OUT_SOCKET);

		DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);

		try {
			// Execute the request
			HttpResponse response = httpClient.execute(httpget);
			// Get the response entity
			HttpEntity entity = response.getEntity();
			JSONObject jsonObj = getJSONFromInputStream(entity.getContent());

			String status = jsonObj.getString(TAG_STATUS);

			if (status.equals(SessionManager.STATUS_ERROR)) {
				String message = jsonObj.getString(TAG_MESSAGE);
				/**
				 * if(message.equals("Invalid API Key.")) {
				 * //sessionManagerLidinyu.closeSession(); //conexion=9; }
				 **/

				Log.e("getLoginFacebook: Error webService", message);
				conexion = 3;
			} else {
				conexion = 0;
				System.out.println("Login correcto");
				JSONObject dataObj = jsonObj.getJSONObject(TAG_DATA_PROFILE);
				idUserFacebook = new String(dataObj.getString(TAG_ID_FACEBOOK)
						.getBytes("ISO-8859-1"), "UTF-8");
				idUserFrikiados = new String(dataObj.getString(TAG_ID_USER)
						.getBytes("ISO-8859-1"), "UTF-8");
				if(!dataObj.isNull(TAG_USER_BIRTHDAY)){
					userBirthday = new String(dataObj.getString(TAG_USER_BIRTHDAY)
							.getBytes("ISO-8859-1"), "UTF-8");
				}
				if(!dataObj.isNull(TAG_GENDER)){
					if(dataObj.getString(TAG_GENDER).equals("m")){
					userGender = "male";
				} else if (dataObj.getString(TAG_GENDER).equals("f")) {
					userGender = "female";
				}
				}
				if(!dataObj.isNull(TAG_EMAIL)){
					userEmail = new String(dataObj.getString(TAG_EMAIL).getBytes(
							"ISO-8859-1"), "UTF-8");
				}
			
			
				/*if(dataObj.getString(TAG_USER_BIRTHDAY)!=null){
				userBirthday = new String(dataObj.getString(TAG_USER_BIRTHDAY)
						.getBytes("ISO-8859-1"), "UTF-8");
				}*/
				/*if (dataObj.getString(TAG_GENDER).equals("m")) {
					userGender = "male";
				} else if (dataObj.getString(TAG_GENDER).equals("f")) {
					userGender = "female";
				}*/
				/*userEmail = new String(dataObj.getString(TAG_EMAIL).getBytes(
						"ISO-8859-1"), "UTF-8");*/
				userNickname = new String(dataObj.getString(TAG_NICKNAME)
						.getBytes("ISO-8859-1"), "UTF-8");

				// GUARDAMOS EN SESSION LOS DATOS
				sessionManagerFrikiados.createLoginSessionFrikiados(
						idUserFacebook, userBirthday, userGender, userEmail,
						idUserFrikiados, userNickname);

				Log.e("getLoginFacebook: data response", dataObj.toString());
				JSONObject dataObj2 = jsonObj.getJSONObject(TAG_DATA_SCORE);
				if (!dataObj2.isNull(TAG_SCORE)) {
					Log.e("getLoginFacebook: data response2",
							dataObj2.toString());
					userAnsweredQuestions = Integer.parseInt(new String(
							dataObj2.getString(TAG_ANSWERED_QUESTIONS)
									.getBytes("ISO-8859-1"), "UTF-8"));
					userScore = Integer.parseInt(new String(dataObj2.getString(
							TAG_SCORE).getBytes("ISO-8859-1"), "UTF-8"));
				}
				if (!sessionManagerFrikiados.updateBestScore(userScore,
						userAnsweredQuestions)) {
					HashMap<String, String> hashmap = sessionManagerFrikiados
							.getUserDetails();
					if (userScore == sessionManagerFrikiados.getBestScore()
							&& userAnsweredQuestions == sessionManagerFrikiados
									.getAnsweredQuestions()) {
						System.out.println("Puntuación igual no se envía");

					} else {
						idUserFacebook = hashmap
								.get(SessionManager.KEY_FB_USER_ID);
						idUserFrikiados = hashmap
								.get(SessionManager.KEY_ID_USER_FRIKIADOS);
						RestfulWebService.sendScore(idUserFrikiados,
								idUserFacebook, Integer
										.toString(sessionManagerFrikiados
												.getBestScore()), Integer
										.toString(sessionManagerFrikiados
												.getAnsweredQuestions()),
								applicationContext);
					}
				} else {
					System.out.println("Puntuación del server mejor");
				}
			}
		} catch (JSONException e) {
			Log.e("getLogin:Error JSONException", ""+ e);
			//conexion = 2;
		} catch (ClientProtocolException e) {
			Log.e("getLogin:Error ClientProtocolException", ""+ e);
			conexion = 2;
		} catch (ConnectTimeoutException e) {
			Log.e("getLogin:Error ConnectTimeoutException", ""+ e);
			conexion = 2;
		} catch (IOException e) {
			Log.e("getLogin:Error IOException", ""+ e);
			conexion = 2;
		} catch (Exception e) {
			Log.e("Error data null", "No mensaje");
			conexion = 2;
		}

		// Informamos de como ha ido el login TRUE|FALSE
		return conexion;
	}

	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@SuppressLint("NewApi")
	public static int updateUserProfile(String idUser, String idFacebook,
			String email, String nickname, Context applicationContext) {
		// Session Manager
		sessionManagerFrikiados = new SessionManager(applicationContext);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);

		// Inicializao variables
		int conexion = 1;

		// HttpClient httpclient = new DefaultHttpClient();
		// Prepare a request object

		HttpGet httpget = new HttpGet(HTTP_REST_FRIKIADOS
				+ "updateProfile?idFrikiados=" + idUser + "&idFacebookUser="
				+ idFacebook + "&email=" + email + "&nickname=" + nickname);

		HttpParams httpParameters = new BasicHttpParams();
		// Set the timeout in milliseconds until a connection is established.
		// The default value is zero, that means the timeout is not used.
		HttpConnectionParams.setConnectionTimeout(httpParameters,
				TIME_OUT_CONNEXION);
		// Set the default socket timeout (SO_TIMEOUT)
		// in milliseconds which is the timeout for waiting for data.
		HttpConnectionParams.setSoTimeout(httpParameters, TIME_OUT_SOCKET);

		DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);

		try {
			// Execute the request
			HttpResponse response = httpClient.execute(httpget);
			// Get the response entity
			HttpEntity entity = response.getEntity();
			JSONObject jsonObj = getJSONFromInputStream(entity.getContent());

			String status = jsonObj.getString(TAG_STATUS);

			if (status.equals(SessionManager.STATUS_ERROR)) {
				String message = jsonObj.getString(TAG_MESSAGE);
				/**
				 * if(message.equals("Invalid API Key.")) {
				 * //sessionManagerLidinyu.closeSession(); //conexion=9; }
				 **/

				Log.e("updateUserData: Error webService", message);
				conexion = 3;
			} else {
				conexion = 0;
				System.out.println("Update correcto");
				// GUARDAMOS EN SESSION LOS DATOS
				sessionManagerFrikiados.updateProfileFrikiados(nickname, email);
			}
		} catch (JSONException e) {
			Log.e("updateUserData:Error JSONException", ""+ e);
			conexion = 2;
		} catch (ClientProtocolException e) {
			Log.e("updateUserData:Error ClientProtocolException", ""+ e);
			conexion = 2;
		} catch (ConnectTimeoutException e) {
			Log.e("updateUserData:Error ConnectTimeoutException", ""+ e);
			conexion = 2;
		} catch (IOException e) {
			Log.e("updateUserData:Error IOException", ""+ e);
			conexion = 2;
		} catch (Exception e) {
			Log.e("Error data null", "No mensaje");
			conexion = 2;
		}

		// Informamos de como ha ido el login TRUE|FALSE
		return conexion;
	}

	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@SuppressLint("NewApi")
	public static int sendScore(String idUser, String idFacebook,
			String userScore, String numOfAnswerQuestions,
			Context applicationContext) {
		// Session Manager
		sessionManagerFrikiados = new SessionManager(applicationContext);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);

		// Inicializao variables
		int conexion = 1;

		// HttpClient httpclient = new DefaultHttpClient();
		// Prepare a request object

		HttpGet httpget = new HttpGet(HTTP_REST_FRIKIADOS
				+ "sendScore?idFrikiados=" + idUser + "&idFacebookUser="
				+ idFacebook + "&score=" + userScore + "&answeredQuestions="
				+ numOfAnswerQuestions);

		HttpParams httpParameters = new BasicHttpParams();
		// Set the timeout in milliseconds until a connection is established.
		// The default value is zero, that means the timeout is not used.
		HttpConnectionParams.setConnectionTimeout(httpParameters,
				TIME_OUT_CONNEXION);
		// Set the default socket timeout (SO_TIMEOUT)
		// in milliseconds which is the timeout for waiting for data.
		HttpConnectionParams.setSoTimeout(httpParameters, TIME_OUT_SOCKET);

		DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);

		try {
			// Execute the request
			HttpResponse response = httpClient.execute(httpget);
			// Get the response entity
			HttpEntity entity = response.getEntity();
			JSONObject jsonObj = getJSONFromInputStream(entity.getContent());

			String status = jsonObj.getString(TAG_STATUS);

			if (status.equals(SessionManager.STATUS_ERROR)) {
				String message = jsonObj.getString(TAG_MESSAGE);
				/**
				 * if(message.equals("Invalid API Key.")) {
				 * //sessionManagerLidinyu.closeSession(); //conexion=9; }
				 **/

				Log.e("sendScore: Error webService", message);
			} else {
				System.out.println("Update correcto");
				conexion = 0;
				sessionManagerFrikiados.setBestScoreSendToServer(true);
				// GUARDAMOS EN SESSION LOS DATOS
				sessionManagerFrikiados.updateBestScore(
						Integer.parseInt(userScore),
						Integer.parseInt(numOfAnswerQuestions));
			}
		} catch (JSONException e) {
			Log.e("sendScore:Error JSONException", ""+ e);
			conexion = 2;
		} catch (ClientProtocolException e) {
			Log.e("sendScore:Error ClientProtocolException", ""+ e);
			conexion = 2;
		} catch (ConnectTimeoutException e) {
			Log.e("sendScore:Error ConnectTimeoutException", ""+ e);
			conexion = 2;
		} catch (IOException e) {
			Log.e("sendScore:Error IOException", ""+ e);
			conexion = 2;
		} catch (Exception e) {
			Log.e("Error data null", "No mensaje");
			conexion = 2;
		}

		// Informamos de como ha ido el login TRUE|FALSE
		return conexion;
	}

	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@SuppressLint("NewApi")
	public static ResultGetScore getFrikiadosRanking(Context applicationContext) {
		ResultGetScore result = new ResultGetScore();
		// Session Manager
		sessionManagerFrikiados = new SessionManager(applicationContext);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);

		// Inicializao variables
		result.setError(1);

		// HttpClient httpclient = new DefaultHttpClient();
		// Prepare a request object

		HttpGet httpget = new HttpGet(HTTP_REST_FRIKIADOS
				+ "getFrikiadosRanking");

		HttpParams httpParameters = new BasicHttpParams();
		// Set the timeout in milliseconds until a connection is established.
		// The default value is zero, that means the timeout is not used.
		HttpConnectionParams.setConnectionTimeout(httpParameters,
				TIME_OUT_CONNEXION);
		// Set the default socket timeout (SO_TIMEOUT)
		// in milliseconds which is the timeout for waiting for data.
		HttpConnectionParams.setSoTimeout(httpParameters, TIME_OUT_SOCKET);

		DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);

		try {
			// Execute the request
			HttpResponse response = httpClient.execute(httpget);
			// Get the response entity
			HttpEntity entity = response.getEntity();
			JSONObject jsonObj = getJSONFromInputStream(entity.getContent());

			String status = jsonObj.getString(TAG_STATUS);

			if (status.equals(SessionManager.STATUS_ERROR)) {
				String message = jsonObj.getString(TAG_MESSAGE);
				/**
				 * if(message.equals("Invalid API Key.")) {
				 * //sessionManagerLidinyu.closeSession(); //conexion=9; }
				 **/

				Log.e("getRankingFrikiados: Error webService", message);
			} else {
				System.out.println("Correct get data ranking");

				// GUARDAMOS EN SESSION LOS DATOS
				if (jsonObj.getString(TAG_STATUS).equals(
						SessionManager.STATUS_OK)) {
					JSONArray messageArray = jsonObj.getJSONArray(TAG_DATA);

					if (messageArray != null) {
						List<Score> scores = new ArrayList<Score>();

						for (int i = 0; i < messageArray.length(); i++) {
							Score sco = new Score();
							sco.setUserNickName(new String(messageArray
									.getJSONObject(i)
									.getString(TAG_SCORE_NICKNAME)
									.getBytes("ISO-8859-1"), "UTF-8"));
							sco.setUserScore(new String(messageArray
									.getJSONObject(i)
									.getString(TAG_SCORE_BEST_SCORE)
									.getBytes("ISO-8859-1"), "UTF-8"));
							sco.setUserAnswerScore(new String(messageArray
									.getJSONObject(i)
									.getString(TAG_ANSWERED_QUESTIONS)
									.getBytes("ISO-8859-1"), "UTF-8"));
							sco.setDateCreation(new String(messageArray
									.getJSONObject(i)
									.getString(TAG_SCORE_DATE_CREATION)
									.getBytes("ISO-8859-1"), "UTF-8"));
							sco.setIdFacebook(new String(messageArray
									.getJSONObject(i)
									.getString(TAG_ID_FACEBOOK_USER)
									.getBytes("ISO-8859-1"), "UTF-8"));
							scores.add(sco);
							System.out.println("Ranking: " + sco.toString());
						}
						result.setError(0);
						result.setScore(scores);
					}
				}
			}
		} catch (JSONException e) {
			Log.e("getRankingFrikiados:Error JSONException", ""+ e);
			result.setError(2);
		} catch (ClientProtocolException e) {
			Log.e("getRankingFrikiados:Error ClientProtocolException", ""+ e);
			result.setError(2);
		} catch (ConnectTimeoutException e) {
			Log.e("getRankingFrikiados:Error ConnectTimeoutException", ""+ e);
			result.setError(2);
		} catch (IOException e) {
			Log.e("getRankingFrikiados:Error IOException", ""+ e);
			result.setError(2);
		} catch (Exception e) {
			Log.e("Error data null", "No mensaje");
			result.setError(2);
		}

		// Informamos de como ha ido el login TRUE|FALSE
		return result;
	}

	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@SuppressLint("NewApi")
	public static ResultGetQuestion getDownloadModifiedQuestionsIfNeeded(
			Context applicationContext, String token) {
		ResultGetQuestion result = new ResultGetQuestion();
		// Session Manager
		sessionManagerFrikiados = new SessionManager(applicationContext);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);

		System.out.println("ultimo update " + sessionManagerFrikiados.getLastUpdate());
		// Inicializao variables
		result.setError(1);

		// HttpClient httpclient = new DefaultHttpClient();
		// Prepare a request object

		HttpGet httpget = new HttpGet(HTTP_REST_FRIKIADOS
				+ "downloadModifiedQuestionsIfNeeded?token=" + token
				+ "&lastUpdate=" + sessionManagerFrikiados.getLastUpdate());

		HttpParams httpParameters = new BasicHttpParams();
		// Set the timeout in milliseconds until a connection is established.
		// The default value is zero, that means the timeout is not used.
		HttpConnectionParams.setConnectionTimeout(httpParameters,
				TIME_OUT_CONNEXION);
		// Set the default socket timeout (SO_TIMEOUT)
		// in milliseconds which is the timeout for waiting for data.
		HttpConnectionParams.setSoTimeout(httpParameters, TIME_OUT_SOCKET);

		DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);

		try {
			// Execute the request
			HttpResponse response = httpClient.execute(httpget);
			// Get the response entity
			HttpEntity entity = response.getEntity();
			JSONObject jsonObj = getJSONFromInputStream(entity.getContent());

			String status = jsonObj.getString(TAG_STATUS);

			if (status.equals(SessionManager.STATUS_ERROR)) {
				String message = jsonObj.getString(TAG_MESSAGE);
				/**
				 * if(message.equals("Invalid API Key.")) {
				 * //sessionManagerLidinyu.closeSession(); //conexion=9; }
				 **/

				Log.e("getDownloadQuestions: Error webService", message);
			} else {
				System.out.println("Correct get data ranking");

				// GUARDAMOS EN SESSION LOS DATOS
				if (jsonObj.getString(TAG_STATUS).equals(
						SessionManager.STATUS_OK)) {
					JSONArray messageArray = jsonObj
							.getJSONArray(TAG_QUESTIONS);
					System.out.println("string questions: " + messageArray);

					if (messageArray != null) {
						List<Question> questions = new ArrayList<Question>();

						for (int i = 0; i < messageArray.length(); i++) {
							Question ques = new Question();
							ques.setQuestionId(Integer.parseInt(new String(
									messageArray.getJSONObject(i)
											.getString(TAG_ID_QUESTION)
											.getBytes("ISO-8859-1"), "UTF-8")));
							ques.setQuestionText(new String(messageArray
									.getJSONObject(i)
									.getString(TAG_TEXT_QUESTION)
									.getBytes("ISO-8859-1"), "UTF-8"));
							ques.setAnswer1(new String(messageArray
									.getJSONObject(i)
									.getString(TAG_ANSWER_ONE_QUESTION)
									.getBytes("ISO-8859-1"), "UTF-8"));
							ques.setAnswer2(new String(messageArray
									.getJSONObject(i)
									.getString(TAG_ANSWER_TWO_QUESTION)
									.getBytes("ISO-8859-1"), "UTF-8"));
							ques.setAnswer3(new String(messageArray
									.getJSONObject(i)
									.getString(TAG_ANSWER_THREE_QUESTION)
									.getBytes("ISO-8859-1"), "UTF-8"));
							ques.setCorrectAnswer(Integer
									.parseInt(new String(
											messageArray
													.getJSONObject(i)
													.getString(
															TAG_CORRECT_ANSWER_QUESTION)
													.getBytes("ISO-8859-1"),
											"UTF-8")));
							ques.setDifficulty(Integer.parseInt(new String(
									messageArray.getJSONObject(i)
											.getString(TAG_DIFFICULTY_QUESTION)
											.getBytes("ISO-8859-1"), "UTF-8")));
							ques.setGenre(new String(messageArray
									.getJSONObject(i)
									.getString(TAG_GENRE_QUESTION)
									.getBytes("ISO-8859-1"), "UTF-8"));
							ques.setLastUpdate(new String(messageArray
									.getJSONObject(i)
									.getString(TAG_LAST_UPDATE_QUESTION)
									.getBytes("ISO-8859-1"), "UTF-8"));
							ques.setTimesAppeared(Integer
									.parseInt(new String(
											messageArray
													.getJSONObject(i)
													.getString(
															TAG_TIMES_APPEARED_QUESTION)
													.getBytes("ISO-8859-1"),
											"UTF-8")));

							questions.add(ques);
							/*if (i == 100) {
								SplashActivity.progressBarProgress(10);
							} else if (i == 200) {
								SplashActivity.progressBarProgress(20);
							} else if (i == 300) {
								SplashActivity.progressBarProgress(30);
							} else if (i == 400) {
								SplashActivity.progressBarProgress(40);
							} else if (i == 500) {
								SplashActivity.progressBarProgress(50);
							} else if (i == 600) {
								SplashActivity.progressBarProgress(60);
							} else if (i == 700) {
								SplashActivity.progressBarProgress(70);
							} else if (i == 800) {
								SplashActivity.progressBarProgress(80);
							} else if (i == 900) {
								SplashActivity.progressBarProgress(90);
							} else if (i == 1000) {
								SplashActivity.progressBarProgress(100);
							}*/
						}

						result.setError(0);
						QuestionDAO listQuestion = new QuestionDAO(
								applicationContext);
						listQuestion.addQuestion(questions, applicationContext);
						sessionManagerFrikiados.saveLastUpdate(getDayData()
								+ "%20" + getHourData());
					}
					result.setError(3);
					System.out.println("Array de preguntas vacia");
				}
			}
		} catch (JSONException e) {
			Log.e("getDownloadQuestions:Error JSONException", ""+ e);
			result.setError(2);
		} catch (ClientProtocolException e) {
			Log.e("getDownloadQuestions:Error ClientProtocolException", ""+ e);
			result.setError(2);
		} catch (ConnectTimeoutException e) {
			Log.e("getDownloadQuestions:Error ConnectTimeoutException", ""+ e);
			result.setError(2);
		} catch (IOException e) {
			Log.e("getDownloadQuestions:Error IOException",""+ e);
			result.setError(2);
		} catch (Exception e) {
			Log.e("Error data null", "No mensaje");
			result.setError(2);
		}

		// Informamos de como ha ido el login TRUE|FALSE
		return result;
	}

	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@SuppressLint("NewApi")
	public static int getFirstToken(Context applicationContext) {
		// Session Manager
		sessionManagerFrikiados = new SessionManager(applicationContext);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);

		// Inicializao variables
		int conexion = 1;

		// HttpClient httpclient = new DefaultHttpClient();
		// Prepare a request object

		HttpGet httpget = new HttpGet(HTTP_REST_FRIKIADOS + "getFirstToken");

		HttpParams httpParameters = new BasicHttpParams();
		// Set the timeout in milliseconds until a connection is established.
		// The default value is zero, that means the timeout is not used.
		HttpConnectionParams.setConnectionTimeout(httpParameters,
				TIME_OUT_CONNEXION);
		// Set the default socket timeout (SO_TIMEOUT)
		// in milliseconds which is the timeout for waiting for data.
		HttpConnectionParams.setSoTimeout(httpParameters, TIME_OUT_SOCKET);

		DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);

		try {
			// Execute the request
			HttpResponse response = httpClient.execute(httpget);
			// Get the response entity
			HttpEntity entity = response.getEntity();
			JSONObject jsonObj = getJSONFromInputStream(entity.getContent());

			String status = jsonObj.getString(TAG_STATUS);

			if (status.equals(SessionManager.STATUS_ERROR)) {
				String message = jsonObj.getString(TAG_MESSAGE);
				/**
				 * if(message.equals("Invalid API Key.")) {
				 * //sessionManagerLidinyu.closeSession(); //conexion=9; }
				 **/

				Log.e("getLoginFacebook: Error webService", message);
			} else {
				System.out.println("Update correcto");
				conexion = 0;
				if (jsonObj.getString(TAG_STATUS).equals(
						SessionManager.STATUS_OK)) {
					String token = new String(jsonObj.getString(TAG_TOKEN)
							.getBytes("ISO-8859-1"), "UTF-8");
					sessionManagerFrikiados.saveToken(token);
				}
			}
		} catch (JSONException e) {
			Log.e("getLogin:Error JSONException", ""+ e);
			conexion = 2;
		} catch (ClientProtocolException e) {
			Log.e("getLogin:Error ClientProtocolException", ""+ e);
			conexion = 2;
		} catch (ConnectTimeoutException e) {
			Log.e("getLogin:Error ConnectTimeoutException", ""+ e);
			conexion = 2;
		} catch (IOException e) {
			Log.e("getLogin:Error IOException", ""+ e);
			conexion = 2;
		} catch (Exception e) {
			Log.e("Error data null", "No mensaje");
			conexion = 2;
		}

		// Informamos de como ha ido el login TRUE|FALSE
		return conexion;
	}
	public static String isValidNickName(Context applicationContext , String nickName) {
		// Session Manager
		sessionManagerFrikiados = new SessionManager(applicationContext);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);

		// Inicializao variables
		String conexion = "1";

		// HttpClient httpclient = new DefaultHttpClient();
		// Prepare a request object

		HttpGet httpget = new HttpGet(HTTP_REST_FRIKIADOS
				+ "isValidNickname?nickname=" + nickName);

		HttpParams httpParameters = new BasicHttpParams();
		// Set the timeout in milliseconds until a connection is established.
		// The default value is zero, that means the timeout is not used.
		HttpConnectionParams.setConnectionTimeout(httpParameters,
				TIME_OUT_CONNEXION);
		// Set the default socket timeout (SO_TIMEOUT)
		// in milliseconds which is the timeout for waiting for data.
		HttpConnectionParams.setSoTimeout(httpParameters, TIME_OUT_SOCKET);

		DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);

		try {
			// Execute the request
			HttpResponse response = httpClient.execute(httpget);
			// Get the response entity
			HttpEntity entity = response.getEntity();
			JSONObject jsonObj = getJSONFromInputStream(entity.getContent());

			String status = jsonObj.getString(TAG_STATUS);

			if (status.equals(SessionManager.STATUS_ERROR)) {
				String message = jsonObj.getString(TAG_MESSAGE);
				/**
				 * if(message.equals("Invalid API Key.")) {
				 * //sessionManagerLidinyu.closeSession(); //conexion=9; }
				 **/

				Log.e("isValidNickName: Error webService", message);
			} else {
				System.out.println("Update correcto");
				if (jsonObj.getString(TAG_STATUS).equals(
						SessionManager.STATUS_OK)) {
					conexion = new String(jsonObj.getString(TAG_IS_VALID_NICKNAME)
							.getBytes("ISO-8859-1"), "UTF-8");
				}
			}
		} catch (JSONException e) {
			Log.e("isValidNickName:Error JSONException", ""+ e);
			conexion = "2";
		} catch (ClientProtocolException e) {
			Log.e("isValidNickName:Error ClientProtocolException", ""+ e);
			conexion = "2";
		} catch (ConnectTimeoutException e) {
			Log.e("isValidNickName:Error ConnectTimeoutException", ""+ e);
			conexion = "2";
		} catch (IOException e) {
			Log.e("isValidNickName:Error IOException", ""+ e);
			conexion = "2";
		} catch (Exception e) {
			Log.e("Error data null", "No mensaje");
			conexion = "2";
		}

		// Informamos de como ha ido el login TRUE|FALSE
		return conexion;
	}
	public static int getCacheImage(String userImageURL, ImageLoader imageLoader, ImageView imageView, Context context) 
	{
		int result = 0;//No error
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);

		try 
		{
			URL url = new URL("https://graph.facebook.com/" + userImageURL
					+ "/picture?type=large");
			
			if(imageLoader != null & url != null)
			{
				imageLoader.DisplayImage(url.toString(), imageView, context);
			}	
			else
			{
				result = 5;//Error en url o imageLoader
			}
		} 
		catch (IOException e) 
		{
			Log.e("getCacheImage: IOException", e.getMessage());
			result = 4; //Excepción
		} 
		catch (Exception e)
		{
			Log.e("getCacheImage: Exception", e.getMessage());
			result = 4; //Excepción
		}
		
		
		return result;
	}
	
	
	private static JSONObject getJSONFromInputStream(InputStream is) {
		JSONObject jObj = null;
		String json = null;

		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}

			is.close();
			json = sb.toString();

			jObj = new JSONObject(json);
		} catch (JSONException e) {
			Log.e("JSON getJSONFromInputStream",
					"JSONException " + e.toString());
		} catch (Exception e) {
			Log.e("Error getJSONFromInputStream", "Exception " + e.toString());
		}

		return jObj;
	}

	private static String getDayData() {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

		// Get the date today using Calendar object.
		Date today = Calendar.getInstance().getTime();
		// Using DateFormat format method we can create a string
		// representation of a date with the defined format.
		String reportDate = df.format(today);

		// Print what date is today!
		System.out.println("Report Date: " + reportDate);
		return reportDate;
	}

	private static String getHourData() {
		DateFormat df = new SimpleDateFormat("hh:mm:ss");

		// Get the date today using Calendar object.
		Date today = Calendar.getInstance().getTime();
		// Using DateFormat format method we can create a string
		// representation of a date with the defined format.
		String reportDate = df.format(today);

		// Print what date is today!
		System.out.println("Report Date: " + reportDate);
		return reportDate;
	}

}