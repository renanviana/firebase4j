package com.viana.firebasecore.core;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.FirebaseApp;
import com.google.firebase.cloud.FirestoreClient;

public abstract class FirebaseAbstract {

	public CollectionReference getCollectionRef(String collectionName, FirebaseApp app) {
		Firestore db = FirestoreClient.getFirestore(app);
		return db.collection(collectionName);
	}

	public DocumentReference getDocumentRef(String collectionName, String id, FirebaseApp app) {
		CollectionReference collectionRef = getCollectionRef(collectionName, app);
		return collectionRef.document(id);
	}
	
	public void await(ApiFuture<WriteResult> writeResult) {
		while(!writeResult.isDone()) {};
	}
}
