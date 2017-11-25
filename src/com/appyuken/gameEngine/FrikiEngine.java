package com.appyuken.gameEngine;

import java.util.ArrayList;

import android.content.Context;

import com.appyuken.frikiadospremium.classes.Question;
import com.appyuken.frikiadospremium.classes.SessionManager;
import com.appyuken.model.QuestionDAO;
import com.appyuken.model.ResumeLevel;

public class FrikiEngine {

	private QuestionDAO questionDAO;// Objeto con el que se descargan las
									// preguntas de la bbdd sqlite. falta crear
									// la clase!
	private ArrayList<Question> questionsArray;// Dónde se almacenarán las x
												// preguntas. falta crear la
												// clase!
	private ArrayList<Question> questionsArrayBoss;
	private int contadorInternoPreguntas;
	private int contadorInternoBoss;
	// private int numeroPreguntasPedidasPorModoJuego;
	static final String KEY_ITEM = "Level";
	static final String KEY_NUMBER = "Number";
	static final String KEY_STARS = "Stars";
	static final String KEY_UNLOCKED = "Unlocked";
	private static String DB_DIR = "/data/data/com.appyuken.frikiadospremium/databases";
	private SessionManager sessionManager;
	private int numberOfCurrentLives;
	private boolean userHasNoLives;
	private int frikiadosScore;

	// Constructor para cuando llamemos al motor desde OnePlayer, le pasamos el
	// mundo, el player y el número de preguntas que queremos (10 en este modo
	// siempre)
	public FrikiEngine(/* World w, Player p, */
	Context context, String category) {

		// numeroPreguntasPedidasPorModoJuego = 351;// exclusivamente 10
		// preguntas en este
		// modo, no se pueden
		// pedir ni más ni menos
		// que este número

		contadorInternoPreguntas = 0;// empezamos por la primera pregunta
		contadorInternoBoss = 0;
		if (category.equals("all")) {
			questionDAO = new QuestionDAO(context);
			questionsArray = questionDAO.obtenerPreguntas();// obtenemos
															// el
															// número
															// de
															// preguntas
															// que
															// nos
															// han
															// pedido!
			questionsArrayBoss = questionDAO.obtenerPreguntasBoss(20);
		} else {
			questionDAO = new QuestionDAO(context);
			questionsArray = questionDAO.obtenerPreguntasCategoryMode(category);
		}

	}

	public Question getQuestion(Context context)// Nos devolverá la siguiente
												// pregunta a
	// mostrar
	{
		Question question = new Question();
		if (contadorInternoPreguntas < questionsArray.size()
				&& !isUserWithoutLives(context)) {
			question = questionsArray.get(contadorInternoPreguntas);
			contadorInternoPreguntas++;
		}

		return question;
	}

	public Question getQuestionBoss(Context context, int i)// Nos devolverá la
	// siguiente pregunta a
	// mostrar
	{
		Question question = new Question();
		if (contadorInternoBoss < questionsArrayBoss.size()
				&& !isUserWithoutLives(context)) {
			question = questionsArrayBoss.get(i - 1);
			contadorInternoBoss++;
		}

		return question;
	}

	// Función que nos devolverá true si el juego ha terminado (contestando a
	// las 10 preguntas)
	public boolean isStageClear() {
		boolean finish = false;

		if (contadorInternoPreguntas >= questionsArray.size())
			finish = true;// Juego terminado

		return finish;
	}

	// Función que nos devolverá true si el jugador se ha quedado sin vidas
	public boolean isUserWithoutLives(Context context) {
		boolean userHasNoLives = false;

		// Comprobar vidas para dejar jugar o no
		/*
		 * sessionManager = new SessionManager(context); HashMap<String, String>
		 * hashmap = sessionManager.getUserDetails(); numberOfCurrentLives =
		 * Integer.parseInt(hashmap .get(SessionManager.KEY_NUMBER_LIFES)); if
		 * (numberOfCurrentLives <= 0) userHasNoLives = true;// Juego terminado
		 */

		return userHasNoLives;
	}

	public boolean userGameOver(Context context,
			ArrayList<ResumeLevel> resumeLevel, int newScore,
			int numOfQuestions, boolean gameOver, String categorySelection) {
		sessionManager = new SessionManager(context);

		/*
		 * HashMap<String, String> hashmap = sessionManager.getUserDetails();
		 * if(gameOver){ int numOfLifes = Integer.parseInt(hashmap
		 * .get(SessionManager.KEY_NUMBER_LIFES)); int newLifes = numOfLifes -
		 * 1; sessionManager.updateLifes(newLifes); }
		 */

		if (resumeLevel != null)
			questionDAO.saveTimesAppeared(resumeLevel);
		if (categorySelection.equals("Cómic")) {
			return sessionManager
					.updateBestScoreComic(newScore, numOfQuestions);
		} else if (categorySelection.endsWith("Manga")) {
			return sessionManager
					.updateBestScoreManga(newScore, numOfQuestions);
		} else if (categorySelection.equals("Series")) {
			return sessionManager.updateBestScoreSeries(newScore,
					numOfQuestions);
		} else if (categorySelection.equals("Películas")) {
			return sessionManager.updateBestScorePeliculas(newScore,
					numOfQuestions);
		} else if (categorySelection.equals("Videojuegos")) {
			return sessionManager.updateBestScoreVideojuegos(newScore,
					numOfQuestions);
		} else {

			return sessionManager.updateBestScore(newScore, numOfQuestions);
		}
	}

	public int calculateScoreQuestion(Question question2, double timeAnswer) {

		if (timeAnswer < 1) {
			timeAnswer = 1;
		}

		double scoreQuestion = Math.round((double) (500) / timeAnswer);
		if (scoreQuestion < 0) {
			scoreQuestion = 1;
		} else if (scoreQuestion > 500) {
			scoreQuestion = 500;
		}
		return (int) scoreQuestion;
	}

	public int questionOrBoss(int numQuestion) {
		int level;
		/*
		 * if (numQuestion == 25) { level = 1; } else if (numQuestion == 50) {
		 * level = 2; } else if (numQuestion == 100) { level = 3; } else if
		 * (numQuestion == 200) { level = 4; } else if (numQuestion == 350) {
		 * level = 5; } else { level = 0; }/* if (numQuestion == 2) { level = 1;
		 * } else if (numQuestion == 3) { level = 2; } else if (numQuestion ==
		 * 4) { level = 3; } else if (numQuestion == 5) { level = 4; } else if
		 * (numQuestion == 6) { level = 5; } else { level = 0; }
		 */
		if (numQuestion > questionsArray.size()) {
			level = 6;
		} else {
			level = 0;
		}
		return level;

	}
}
