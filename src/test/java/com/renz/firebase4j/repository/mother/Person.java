package com.renz.firebase4j.repository.mother;

import com.renz.firebase4j.firestore.Document;

public class Person extends Document {

	public Person() {
	}

	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
