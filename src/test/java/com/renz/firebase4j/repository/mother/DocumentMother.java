package com.renz.firebase4j.repository.mother;

public class DocumentMother {

	public static Person getDocument() {
		Person person = new Person();
		person.setName("Renz");
		person.setId("30355b20-a42a-44ff-a047-15ce7b53af11");
		return person;
	}

	public static Person getDocumentWithNullId() {
		return new Person();
	}

}