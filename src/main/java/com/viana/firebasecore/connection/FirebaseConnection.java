package com.viana.firebasecore.connection;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.json.JSONObject;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.viana.requestintegrate.company.CompanyIntegrate;
import com.viana.user.bean.CompanyClient;

public class FirebaseConnection {

	private FirebaseConnection() {
	}

	public static FirebaseApp initializeApp(String pathServiceAccountKey, String bucketUrl, String databaseUrl)
			throws FileNotFoundException, IOException {
		try {
			FirebaseApp.getInstance().delete();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		FirebaseOptions options = new FirebaseOptions.Builder()
				.setCredentials(GoogleCredentials.fromStream(new FileInputStream(pathServiceAccountKey)))
				.setStorageBucket(bucketUrl).setDatabaseUrl(databaseUrl).build();
		return FirebaseApp.initializeApp(options);
	}

	public static FirebaseApp initializeApp(InputStream serviceAccountKey, String bucketUrl, String databaseUrl)
			throws FileNotFoundException, IOException {
		try {
			FirebaseApp.getInstance().delete();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		FirebaseOptions options = new FirebaseOptions.Builder()
				.setCredentials(GoogleCredentials.fromStream(serviceAccountKey)).setStorageBucket(bucketUrl)
				.setDatabaseUrl(databaseUrl).build();
		return FirebaseApp.initializeApp(options);
	}

	public static FirebaseApp initializeAppFromUniqueConnection(String name, InputStream serviceAccountKey,
			String bucketUrl, String databaseUrl) throws FileNotFoundException, IOException {
		for (FirebaseApp app : FirebaseApp.getApps()) {
			if (name.equals(app.getName())) {
				return app;
			}
		}
		FirebaseOptions options = new FirebaseOptions.Builder()
				.setCredentials(GoogleCredentials.fromStream(serviceAccountKey)).setStorageBucket(bucketUrl)
				.setDatabaseUrl(databaseUrl).build();
		
		return tratament(options, name);
	}

	private static FirebaseApp tratament(FirebaseOptions options, String name) {
		FirebaseApp app = null;
		try {
			app = FirebaseApp.initializeApp(options, name);
		} catch (Exception e ) {
			System.out.println(e.getMessage());
			if (e.getMessage().contains("already exists")) {
				app = FirebaseApp.getInstance(name);
			}
		}
		return app;
	}
	
	public static FirebaseApp initializeAppFromUniqueConnection(String name, InputStream serviceAccountKey,
			String databaseUrl) throws FileNotFoundException, IOException {
		for (FirebaseApp app : FirebaseApp.getApps()) {
			if (name.equals(app.getName())) {
				return app;
			}
		}
		FirebaseOptions options = new FirebaseOptions.Builder()
				.setCredentials(GoogleCredentials.fromStream(serviceAccountKey)).build();
		return tratament(options, name);
	}

	public static FirebaseApp initializeApp(String pathServiceAccountKey, String databaseUrl)
			throws FileNotFoundException, IOException {
		try {
			FirebaseApp.getInstance().delete();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		FirebaseOptions options = new FirebaseOptions.Builder()
				.setCredentials(GoogleCredentials.fromStream(new FileInputStream(pathServiceAccountKey)))
				.setDatabaseUrl(databaseUrl).build();
		return FirebaseApp.initializeApp(options);
	}

	public static FirebaseApp openConnection(CompanyIntegrate companyIntegrate, String idCompany,
			Map<String, FirebaseApp> connection) {
		for (FirebaseApp app : FirebaseApp.getApps()) {
			if (idCompany.equals(app.getName())) {
				return app;
			}
		}
		CompanyClient company = companyIntegrate.getCompany(idCompany);
		if (company != null) {
			Map<String, Object> serviceAccountKey = company.getServiceAccountKey();
			String projectId = (String) serviceAccountKey.get("project_id");
			String bucketUrl = projectId + ".appspot.com";
			String databaseUrl = "https://" + projectId + ".firebaseio.com";
			String json = new JSONObject(serviceAccountKey).toString();
			InputStream inputStream = new ByteArrayInputStream(json.getBytes());
			try {
				FirebaseApp app = initializeAppFromUniqueConnection(idCompany, inputStream, bucketUrl, databaseUrl);
				connection.put(idCompany, app);
				return app;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}
