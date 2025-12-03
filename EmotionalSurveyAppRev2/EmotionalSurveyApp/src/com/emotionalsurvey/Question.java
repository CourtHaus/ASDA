package com.emotionalsurvey;

public class Question {
	private int id;
	private String imagePath;

	public Question(int id, String imagePath) {
		this.id = id;
		this.imagePath = imagePath;
	}

	public int getId() {
		return id;
	}

	public String getImagePath() {
		return imagePath;
	}
}
