package com.renz.firebase4j.connection;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.net.URISyntaxException;

import org.junit.jupiter.api.Test;

public class FirebaseConnectionTest {

	private static final String RESOURCE_PATH = "src/main/resources/test/";
	private static final String SERVICE_ACCOUNT_KEY_PATH_TEST_SUCCESS = RESOURCE_PATH + "test-success-firebase-adminsdk.json";
	private static final String SERVICE_ACCOUNT_KEY_PATH_TEST_INVALID_PKCS = RESOURCE_PATH + "test-invalid-pkcs-firebase-adminsdk.json";
	private static final String SERVICE_ACCOUNT_KEY_PATH_TEST_INVALID_READING_PKCS = RESOURCE_PATH + "test-invalid-reading-pkcs-firebase-adminsdk.json";
	private static final String SERVICE_ACCOUNT_KEY_PATH_TEST_READING_ATTRIBUTE_IS_NULL_FROM_JSON = RESOURCE_PATH + "test-reading-attribute-is-null-from-json-firebase-adminsdk.json";
	private static final String SERVICE_ACCOUNT_KEY_PATH_TEST_READING_ATTRIBUTE_NOT_STRING_FROM_JSON = RESOURCE_PATH + "test-reading-attribute-not-string-from-json-firebase-adminsdk.json";

	@Test
	public void givenValidConnection_whenConnect_thenCreatedConnection() throws IOException, URISyntaxException {
		FirebaseConnection firebaseConn = new FirebaseConnection();
		firebaseConn.connect(SERVICE_ACCOUNT_KEY_PATH_TEST_SUCCESS);
		firebaseConn.close();
	}

	@Test
	public void givenInvalidPKCS_whenConnect_thenIOExceptionInvalidPKCS() {

		Exception exception = assertThrows(IOException.class, () -> {
			try (FirebaseConnection firebaseConn = new FirebaseConnection()) {
				firebaseConn.connect(SERVICE_ACCOUNT_KEY_PATH_TEST_INVALID_PKCS);
			} catch (IOException e) {
				throw e;
			}
		});

		String expectedMessage = "Invalid PKCS#8 data";
		String actualMessage = exception.getMessage();

		assertTrue(actualMessage.contains(expectedMessage));
	}
	
	@Test
	public void givenInvalidReadingPKCS_whenConnect_thenIOExceptionInvalidReadingPKCS() {
		
		Exception exception = assertThrows(IOException.class, () -> {
			try (FirebaseConnection firebaseConn = new FirebaseConnection()) {
				firebaseConn.connect(SERVICE_ACCOUNT_KEY_PATH_TEST_INVALID_READING_PKCS);
			} catch (IOException e) {
				throw e;
			}
		});
		
		String expectedMessage = "Unexpected exception reading PKCS#8 data";
		String actualMessage = exception.getMessage();
		
		assertTrue(actualMessage.contains(expectedMessage));
	}
	
	@Test
	public void givenAttributeIsNullFromJson_whenConnect_thenIOExceptionReadingAttributeIsNullFromJSON() {
		
		Exception exception = assertThrows(IOException.class, () -> {
			try (FirebaseConnection firebaseConn = new FirebaseConnection()) {
				firebaseConn.connect(SERVICE_ACCOUNT_KEY_PATH_TEST_READING_ATTRIBUTE_IS_NULL_FROM_JSON);
			} catch (IOException e) {
				throw e;
			}
		});
		
		String expectedMessage = "Error reading 'project_id' service account credential from JSON";
		String actualMessage = exception.getMessage();
		
		assertTrue(actualMessage.contains(expectedMessage));
	}
	
	@Test
	public void givenAttributeNotStringFromJson_whenConnect_thenIOExceptionReadingAttributeNotStringFromJSON() {
		
		Exception exception = assertThrows(IOException.class, () -> {
			try (FirebaseConnection firebaseConn = new FirebaseConnection()) {
				firebaseConn.connect(SERVICE_ACCOUNT_KEY_PATH_TEST_READING_ATTRIBUTE_NOT_STRING_FROM_JSON);
			} catch (IOException e) {
				throw e;
			}
		});
		
		String expectedMessage = "Error reading 'project_id' service account credential from JSON";
		String actualMessage = exception.getMessage();
		
		assertTrue(actualMessage.contains(expectedMessage));
	}

}
