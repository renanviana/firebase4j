package com.renz.firebase4j.connection.impl;

import java.io.Closeable;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.api.client.util.PemReader;
import com.google.api.client.util.PemReader.Section;
import com.google.api.client.util.SecurityUtils;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.gson.Gson;
import com.renz.firebase4j.connection.FirebaseConnection;

public class FirebaseConnectionImpl implements FirebaseConnection, Closeable {

	private static final String PRIVATE_KEY = "private_key";
	private static final String TOKEN_URI = "token_uri";
	private static final String CLIENT_ID = "client_id";
	private static final String CLIENT_EMAIL = "client_email";
	private static final String PRIVATE_KEY_ID = "private_key_id";
	private static final String PROJECT_ID = "project_id";
	private static final String CLIENT_X509_CERT_URL = "client_x509_cert_url";
	
	private FirebaseApp firebaseApp;
	private Map<String, Object> serviceAccountKeyMap;
	
	@Override
	public FirebaseApp connect(String serviceAccountKeyPath) throws IOException, URISyntaxException {
		createFirebaseApp(new FileInputStream(serviceAccountKeyPath));
		return this.firebaseApp;
	}

	private void createFirebaseApp(InputStream serviceAccountKey) throws IOException, URISyntaxException {

		Reader reader = new InputStreamReader(serviceAccountKey, StandardCharsets.UTF_8);
		TypeReference<Map<String, Object>> type = new TypeReference<Map<String, Object>>() {};
		this.serviceAccountKeyMap = new Gson().fromJson(reader, type.getType());

		String projectId = getProjectId();
		String bucketUrl = projectId + ".appspot.com";
		String databaseUrl = "https://" + projectId + ".firebaseio.com";
		ServiceAccountCredentials credentials = getCredentials();
		String clientX509CertUrl = getClientX509CertUrl();
		
		FirebaseOptions options = FirebaseOptions.builder()
				.setProjectId(projectId)
				.setCredentials(credentials)
				.setStorageBucket(bucketUrl)
				.setDatabaseUrl(databaseUrl)
				.setServiceAccountId(clientX509CertUrl)
				.build();
		
		this.firebaseApp = FirebaseApp.initializeApp(options);
	}

	private ServiceAccountCredentials getCredentials() throws IOException, URISyntaxException {

		String privateKeyPkcs8 = getPrivateKey();
		PrivateKey privateKey = privateKeyFromPkcs8((String) privateKeyPkcs8);

		URI tokenServerUriFromCreds = new URI(getTokenURI());
		String clientId = getClientId();
		String clientEmail = getClientEmail();
		String privateKeyId = getPrivateKeyId();
		String projectId = getProjectId();
		
		return ServiceAccountCredentials.newBuilder()
				.setClientId(clientId)
				.setClientEmail(clientEmail)
				.setPrivateKey(privateKey)
				.setPrivateKeyId(privateKeyId)
				.setTokenServerUri(tokenServerUriFromCreds)
				.setProjectId(projectId)
				.build();
	}

	private PrivateKey privateKeyFromPkcs8(String privateKeyPkcs8) throws IOException {
		
		Reader reader = new StringReader(privateKeyPkcs8);
		Section section = PemReader.readFirstSectionAndClose(reader, "PRIVATE KEY");
		
		if (section == null) {
			throw new IOException("Invalid PKCS#8 data");
		}
		
		byte[] bytes = section.getBase64DecodedBytes();
		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(bytes);
		
		try {
			KeyFactory keyFactory = SecurityUtils.getRsaKeyFactory();
			return keyFactory.generatePrivate(keySpec);
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			throw new IOException("Unexpected exception reading PKCS#8 data", e);
		}
	}

	@Override
	public void close() throws IOException {
		this.firebaseApp.delete();
	}

	private String getPrivateKey() throws IOException {
		return getValue(PRIVATE_KEY);
	}

	private String getTokenURI() throws IOException {
		return getValue(TOKEN_URI);
	}

	private String getClientId() throws IOException {
		return getValue(CLIENT_ID);
	}

	private String getClientEmail() throws IOException {
		return getValue(CLIENT_EMAIL);
	}

	private String getPrivateKeyId() throws IOException {
		return getValue(PRIVATE_KEY_ID);
	}

	private String getProjectId() throws IOException {
		return getValue(PROJECT_ID);
	}
	
	private String getClientX509CertUrl() throws IOException {
		return getValue(CLIENT_X509_CERT_URL);
	}
	
	private String getValue(String name) throws IOException {
		Object value = this.serviceAccountKeyMap.get(name);
		validateAttributeFromJson(name, value);
		return (String) value;
	}
	
	private void validateAttributeFromJson(String name, Object value) throws IOException {
		if (value == null || value instanceof String) {
			throw new IOException("Error reading '" + name + "' service account credential from JSON");
		}
	}
}
