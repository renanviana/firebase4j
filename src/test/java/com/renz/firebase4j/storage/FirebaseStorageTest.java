package com.renz.firebase4j.storage;


import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.renz.firebase4j.connection.FirebaseConnection;

public class FirebaseStorageTest {

	// Including your file path service account key (real account for tests) to execute unit tests
	private static final String SERVICE_ACCOUNT_KEY_PATH_TEST = "";
	
	private FirebaseConnection firebaseConn;
	
	@BeforeEach
	public void setup() throws IOException, URISyntaxException {
		this.firebaseConn = new FirebaseConnection();
		if (StringUtils.isNotBlank(SERVICE_ACCOUNT_KEY_PATH_TEST)) {
			this.firebaseConn.connect(SERVICE_ACCOUNT_KEY_PATH_TEST);
		}
		
	}
	
	@AfterEach
	public void after() throws IOException {
		if (StringUtils.isNotBlank(SERVICE_ACCOUNT_KEY_PATH_TEST)) {
			this.firebaseConn.close();
		}
	}
	
	@Test
	public void givenSaveDocument_whenSave_thenCreatedDocument() {
		if (StringUtils.isNotBlank(SERVICE_ACCOUNT_KEY_PATH_TEST)) {
			String mediaLink = new FirebaseStorage().uploadFile(new File("src/main/resources/application.properties"));
			assertNotNull(mediaLink);
		} else {
			assertTrue(Boolean.TRUE);
		}
	}
	
}
