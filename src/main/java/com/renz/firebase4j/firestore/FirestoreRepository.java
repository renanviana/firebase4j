package com.renz.firebase4j.firestore;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.FirebaseApp;
import com.google.firebase.cloud.FirestoreClient;
import com.google.gson.JsonSyntaxException;

public class FirestoreRepository<T extends Document> extends FirestoreRepositoryAbs<T> {

	public FirestoreRepository(Class<?> clazz) {
		super(clazz);
	}

	@Override
	public T save(T entity) {
		if (entity.getId() == null) {
			String id = create(entity);
			entity.setId(id);
		}
		getDocumentRef(entity.getId()).set(entity);
		return entity;
	}
	
	@Override
	public void delete(String id) {
		getDocumentRef(id).delete();	
	}

	@Override
	@SuppressWarnings("unchecked")
	public T findById(String id) {
		try {
			DocumentReference docRef = getDocumentRef(id);
			ApiFuture<DocumentSnapshot> docSnapFuture = docRef.get();
			await(docSnapFuture);
			DocumentSnapshot docSnap = docSnapFuture.get();
			return (T) docSnap.toObject(getEntityClass());
		} catch (InterruptedException | ExecutionException | JsonSyntaxException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<T> findAll() {
		try {
			CollectionReference collectionRef = getCollectionRef();
			ApiFuture<QuerySnapshot> querySnapFuture = collectionRef.get();
			await(querySnapFuture);
			QuerySnapshot querySnap = querySnapFuture.get();
			return (List<T>) querySnap.toObjects(getEntityClass());
		} catch (InterruptedException | ExecutionException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public List<T> findByFields(Map<String, Object> fields) {
		try {
			CollectionReference collectionRef = getCollectionRef();
			Query query = null;
			for (String field : fields.keySet()) {
				Object value = fields.get(field);
				if (query == null) {
					query = collectionRef.whereEqualTo(field, value);
				} else {
					query = query.whereEqualTo(field, value);
				}
			}
			return executeQuery(query);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<T> executeQuery(Query query) {
		try {
			ApiFuture<QuerySnapshot> querySnapFuture = query.get();
			await(querySnapFuture);
			QuerySnapshot querySnap = querySnapFuture.get();
			return (List<T>) querySnap.toObjects(getEntityClass());
		} catch (InterruptedException | ExecutionException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public CollectionReference getCollectionRef() {
		Firestore db = FirestoreClient.getFirestore(FirebaseApp.getInstance());
		return db.collection(getEntityClass().getSimpleName());
	}

	@Override
	public DocumentReference getDocumentRef(String id) {
		CollectionReference collectionRef = getCollectionRef();
		return collectionRef.document(id);
	}

	@Override
	public ApiFuture<?> await(ApiFuture<?> future) {
		while (!future.isDone()) {};
		return future;
	}
	
	private String create(T entity) {
		try {
			CollectionReference collectionRef = getCollectionRef();
			ApiFuture<DocumentReference> docRefFuture = collectionRef.add(entity);
			await(docRefFuture);
			return docRefFuture.get().getId();
		} catch (InterruptedException | ExecutionException e) {
			throw new RuntimeException(e);
		}
	}
}
