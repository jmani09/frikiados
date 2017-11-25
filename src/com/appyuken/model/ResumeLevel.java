package com.appyuken.model;

public class ResumeLevel {
	
	private int questionId;
    private String genre;
    private int timesAppeared;
    private int difficulty;
    private boolean goodQuestion;
    
    //Constructor por defecto
    public ResumeLevel(){}

    //Constructor sobrecargado
	public ResumeLevel(int questionId, int timesAppeared,
			int difficulty, boolean goodQuestion) 
	{
		super();
		this.questionId = questionId;
		this.genre = genre;
		this.timesAppeared = timesAppeared;
		this.difficulty = difficulty;
		this.goodQuestion = goodQuestion;
	}
	
	public int getQuestionId() {
		return questionId;
	}

	public void setQuestionId(int questionId) {
		this.questionId = questionId;
	}


	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public int getTimesAppeared() {
		return timesAppeared;
	}

	public void setTimesAppeared(int timesAppeared) {
		this.timesAppeared = timesAppeared;
	}

	public int getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(int difficulty) {
		this.difficulty = difficulty;
	}
	public boolean getGoodQuestion() {
		return goodQuestion;
	}

	public void setGoodQuestion(boolean goodQuestion) {
		this.goodQuestion = goodQuestion;
	}

}
