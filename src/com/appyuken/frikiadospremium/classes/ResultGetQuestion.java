package com.appyuken.frikiadospremium.classes;

import java.util.List;

public class ResultGetQuestion {
	private Boolean newQuestion;
	private Integer numQuestions;
	private int error;
	private List<Question> question;

	public Boolean getNewQuestion() {
		return newQuestion;
	}

	public void setNewQestions(Boolean newQuestion) {
		this.newQuestion = newQuestion;
	}

	public Integer getNumQuestions() {
		return numQuestions;
	}

	public void setNumQuestions(Integer numQuestions) {
		this.numQuestions = numQuestions;
	}

	public List<Question> getScore() {
		return question;
	}

	public void setQuestion(List<Question> question) {
		this.question = question;
	}
	public int getError() {
		return error;
	}

	public void setError(int error) {
		this.error = error;
	}

}
