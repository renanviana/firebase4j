package com.viana.firebasecore.core;

import java.util.List;
import java.util.Map;

import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.firebase.FirebaseApp;

public interface FirebaseInterface {

	public void save(String collectionName, Object pojo, FirebaseApp app);

	public void save(String collectionName, Map<String, Object> fields, FirebaseApp app);

	public void update(String collectionName, String id, Object pojo, FirebaseApp app);

	public void update(String collectionName, String id, Map<String, Object> fields, FirebaseApp app);

	public void delete(String collectionName, String id, FirebaseApp app);

	public List<QueryDocumentSnapshot> findAll(String collectionName, FirebaseApp app);

	public DocumentSnapshot findById(String collectionName, String id, FirebaseApp app);

	public List<QueryDocumentSnapshot> findByField(String collectionName, String field, String value, FirebaseApp app);
}
