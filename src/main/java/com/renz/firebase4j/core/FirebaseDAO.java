package com.renz.firebase4j.core;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.springframework.stereotype.Component;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.FirebaseApp;

@Component
public class FirebaseDAO extends FirebaseAbstract implements FirebaseInterface {

	@Override
	public void save(String collectionName, Object pojo, FirebaseApp app) {
		CollectionReference collectionRef = getCollectionRef(collectionName, app);
		await(collectionRef.document().create(pojo));
	}

	@Override
	public void save(String collectionName, Map<String, Object> fields, FirebaseApp app) {
		CollectionReference collectionRef = getCollectionRef(collectionName, app);
		await(collectionRef.document().create(fields));
	}

	@Override
	public void update(String collectionName, String id, Object pojo, FirebaseApp app) {
		DocumentReference documentRef = getDocumentRef(collectionName, id, app);
		await(documentRef.set(pojo));
	}

	@Override
	public void update(String collectionName, String id, Map<String, Object> fields, FirebaseApp app) {
		DocumentReference documentRef = getDocumentRef(collectionName, id, app);
		await(documentRef.set(fields));
	}

	@Override
	public void delete(String collectionName, String id, FirebaseApp app) {
		DocumentReference documentRef = getDocumentRef(collectionName, id, app);
		await(documentRef.delete());
	}

	@Override
	public List<QueryDocumentSnapshot> findAll(String collectionName, FirebaseApp app) {
		List<QueryDocumentSnapshot> documents = null;
		CollectionReference collectionRef = getCollectionRef(collectionName, app);
		ApiFuture<QuerySnapshot> future = collectionRef.get();
		try {
			QuerySnapshot snap = future.get();
			documents = snap.getDocuments();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		return documents;
	}

	@Override
	public DocumentSnapshot findById(String collectionName, String id, FirebaseApp app) {
		DocumentSnapshot snap = null;
		DocumentReference documentRef = getDocumentRef(collectionName, id, app);
		ApiFuture<DocumentSnapshot> future = documentRef.get();
		try {
			snap = future.get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		return snap;
	}

	@Override
	public List<QueryDocumentSnapshot> findByField(String collectionName, String field, String value, FirebaseApp app) {
		List<QueryDocumentSnapshot> documents = null;
		CollectionReference collectionRef = getCollectionRef(collectionName, app);
		try {
			Query query = collectionRef.whereEqualTo(field, value);
			ApiFuture<QuerySnapshot> future = query.get();
			documents = future.get().getDocuments();
		} catch (InterruptedException | ExecutionException e1) {
			e1.printStackTrace();
		}
		return documents;
	}
}
