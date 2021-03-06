package com.appyuken.frikiadospremium.service;

public class RestfulWeb {

	/**
	 * Constante con la información del Servidor.
	 */
	protected final static String HTTP_REST_FRIKIADOS = "http://rest.frikiados.com/api/";

	/**
	 * Tag inicial de todas las llamadas, para saber si la llamada ha sido
	 * correcta.
	 */
	protected static final String TAG_STATUS = "status";

	/**
	 * Tag data, contiene una array con los datos en formato json.
	 */
	protected static final String TAG_DATA_PROFILE = "profile";

	/**
	 * Tag id de usuario
	 */
	protected static final String TAG_ID_USER = "IdUser";

	/**
	 * Tag con la información del sexo del usuario
	 */
	protected static final String TAG_GENDER = "Sex";

	/**
	 * Tag con la información del email del usuario
	 */
	protected static final String TAG_EMAIL = "Email";
	/**
	 * Tag con la información del user name.
	 */
	protected static final String TAG_NICKNAME = "Nickname";

	/**
	 * Tag id del lidin
	 */
	protected static final String TAG_USER_BIRTHDAY = "Birthday";

	/**
	 * Tag que informa de la fecha de creación del lidin
	 */
	protected static final String TAG_FECHA_CREACION = "dateCreated";

	/**
	 * Tag que informa del ID Facebook
	 */
	protected static final String TAG_ID_FACEBOOK = "IdFacebookUser";
	/**
	 * Tag que informa del MESSAGE en caso de error
	 */
	protected static final String TAG_MESSAGE = "message";
	/**
	 * Tag nickname para ranking
	 */
	protected static final String TAG_SCORE_NICKNAME = "nickname";
	/**
	 * Tag score para ranking
	 */
	protected static final String TAG_SCORE_BEST_SCORE = "score";
	/**
	 * Tag date creation para ranking
	 */
	protected static final String TAG_SCORE_DATE_CREATION = "DateCreation";
	/**
	 * Tag date creation para ranking
	 */
	protected static final String TAG_ID_FACEBOOK_USER = "idFacebookUser";
	/**
	 * Tag data para recibir información del servidor
	 */
	protected static final String TAG_DATA = "data";
	/**
	 * Tag data para recibir información del servidor
	 */
	protected static final String TAG_QUESTIONS = "questions";

	protected static final String TAG_ID_QUESTION = "IdQuestion";
	protected static final String TAG_TEXT_QUESTION = "QuestionText";
	protected static final String TAG_ANSWER_ONE_QUESTION = "Answer1";
	protected static final String TAG_ANSWER_TWO_QUESTION = "Answer2";
	protected static final String TAG_ANSWER_THREE_QUESTION = "Answer3";
	protected static final String TAG_CORRECT_ANSWER_QUESTION = "CorrectAnswer";
	protected static final String TAG_GENRE_QUESTION = "Genre";
	protected static final String TAG_DIFFICULTY_QUESTION = "Difficulty";
	protected static final String TAG_TIMES_APPEARED_QUESTION = "TimesAppeared";
	protected static final String TAG_LAST_UPDATE_QUESTION = "LastUpdate";
	
	protected static final String TAG_TOKEN = "token";
	protected static final String TAG_DATA_SCORE = "score";
	protected static final String TAG_SCORE = "Score";
	protected static final String TAG_ANSWERED_QUESTIONS = "AnsweredQuestions";
	protected static final String TAG_IS_VALID_NICKNAME = "isValidNickname";

}
