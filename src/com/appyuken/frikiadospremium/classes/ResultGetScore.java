package com.appyuken.frikiadospremium.classes;

import java.util.List;

public class ResultGetScore {

	
	private Boolean newScore;
	private Integer numScores;
	private int error;
	private List<Score> score;

	public Boolean getNewScores() {
		return newScore;
	}

	public void setNewScores(Boolean newScore) {
		this.newScore = newScore;
	}

	public Integer getNumScores() {
		return numScores;
	}

	public void setNumScores(Integer numScores) {
		this.numScores = numScores;
	}

	public List<Score> getScore() {
		return score;
	}

	public void setScore(List<Score> score) {
		this.score = score;
	}
	public int getError() {
		return error;
	}

	public void setError(int error) {
		this.error = error;
	}

}
