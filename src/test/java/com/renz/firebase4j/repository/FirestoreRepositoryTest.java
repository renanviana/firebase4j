package com.renz.firebase4j.repository;

import static com.renz.firebase4j.repository.mother.DocumentMother.getDocument;
import static com.renz.firebase4j.repository.mother.DocumentMother.getDocumentWithNullId;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import com.renz.firebase4j.connection.FirebaseConnection;
import com.renz.firebase4j.firestore.FirestoreRepository;
import com.renz.firebase4j.repository.mother.Person;

@TestMethodOrder(OrderAnnotation.class)
public class FirestoreRepositoryTest {

	// Including your file path service account key (real account for tests) to execute unit tests
	private static final String SERVICE_ACCOUNT_KEY_PATH_TEST = "";

	private FirestoreRepository<Person> firestoreRep;
	private FirebaseConnection firebaseConn;

	@BeforeEach
	public void setup() throws IOException, URISyntaxException {
		this.firebaseConn = new FirebaseConnection();
		if (StringUtils.isNotBlank(SERVICE_ACCOUNT_KEY_PATH_TEST)) {
			this.firebaseConn.connect(SERVICE_ACCOUNT_KEY_PATH_TEST);
			this.firestoreRep = new FirestoreRepository<Person>(Person.class);
		}
	}
	
	@AfterEach
	public void after() throws IOException {
		if (StringUtils.isNotBlank(SERVICE_ACCOUNT_KEY_PATH_TEST)) {
			this.firebaseConn.close();
		}
	}

	@Test
	@Order(1)
	public void givenSaveDocument_whenSave_thenCreatedDocument() {
		if (StringUtils.isNotBlank(SERVICE_ACCOUNT_KEY_PATH_TEST)) {
			Person document = this.firestoreRep.save(getDocument());
			assertEquals(getDocument().getId(), document.getId());
		} else {
			assertTrue(Boolean.TRUE);
		}
	}
	
	@Test
	@Order(2)
	public void givenFindDocument_whenFindById_thenReadDocument() {
		if (StringUtils.isNotBlank(SERVICE_ACCOUNT_KEY_PATH_TEST)) {
			Person document = this.firestoreRep.findById(getDocument().getId());
			assertEquals(getDocument().getId(), document.getId());
		} else {
			assertTrue(Boolean.TRUE);
		}
	}
	
	@Test
	@Order(3)
	public void givenFindDocumentById_whenFindByFields_thenReadDocument() {
		if (StringUtils.isNotBlank(SERVICE_ACCOUNT_KEY_PATH_TEST)) {
			Map<String, Object> fields = new HashMap<>();
			fields.put("id", getDocument().getId());
			List<Person> documents = this.firestoreRep.findByFields(fields);
			assertTrue(documents.size() > 0);
		} else {
			assertTrue(Boolean.TRUE);
		}
	}
	
	@Test
	@Order(4)
	public void givenFindDocumentByIdAndName_whenFindByFields_thenReadDocument() {
		if (StringUtils.isNotBlank(SERVICE_ACCOUNT_KEY_PATH_TEST)) {
			Map<String, Object> fields = new HashMap<>();
			fields.put("id", getDocument().getId());
			fields.put("name", getDocument().getName());
			List<Person> documents = this.firestoreRep.findByFields(fields);
			assertTrue(documents.size() > 0);
		} else {
			assertTrue(Boolean.TRUE);
		}
	}
	
	@Test
	@Order(5)
	public void givenFindAllDocuments_whenFindAll_thenReadDocuments() {
		if (StringUtils.isNotBlank(SERVICE_ACCOUNT_KEY_PATH_TEST)) {
			List<Person> documents = this.firestoreRep.findAll();
			assertTrue(documents.size() > 0);
		} else {
			assertTrue(Boolean.TRUE);
		}
	}
	
	@Test
	@Order(6)
	public void givenSaveDocumentWithNullId_whenSave_thenCreatedDocument() throws Exception {
		if (StringUtils.isNotBlank(SERVICE_ACCOUNT_KEY_PATH_TEST)) {
			Person document = this.firestoreRep.save(getDocumentWithNullId());
			assertNotNull(document.getId());
		} else {
			assertTrue(Boolean.TRUE);
		}
	}
	
	@Test
	@Order(7)
	public void givenDeleteDocuments_whenDelete_thenDeleteDocuments() {
		if (StringUtils.isNotBlank(SERVICE_ACCOUNT_KEY_PATH_TEST)) {
			List<Person> documents = this.firestoreRep.findAll();
			documents.forEach(doc -> this.firestoreRep.delete(doc.getId()));
			List<Person> results = this.firestoreRep.findAll();
			assertTrue(results.size() == 0);
		} else {
			assertTrue(Boolean.TRUE);
		}
	}
	
}
