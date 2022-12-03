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
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.FirebaseApp;
import com.google.firebase.cloud.FirestoreClient;
import com.google.gson.JsonSyntaxException;

/** 
 * Firestore repository services
 * 
 * @author Renan Viana
 * @author https://github.com/renanviana
 * @version 1.0.0
 * @since 1.0.0
 */
public class FirestoreRepository<T extends Document> extends FirestoreRepositoryAbs<T> {

	public FirestoreRepository(Class<?> clazz) {
		super(clazz);
	}

	/**
	 * Method used to save (create or update) document
	 * 
	 *  @return Document saved
	 */
	@Override
	public T save(T document) {
		if (document.getId() == null) {
			String id = create(document);
			document.setId(id);
		}
		ApiFuture<WriteResult> writeResultFuture = getDocumentRef(document.getId()).set(document);
		await(writeResultFuture);
		return document;
	}

	/**
	 * Method used to delete document by id
	 */
	@Override
	public void delete(String id) {
		ApiFuture<WriteResult> writeResultFuture = getDocumentRef(id).delete();
		await(writeResultFuture);
	}

	/**
	 * Method used to find document by id
	 * 
	 * @return Document
	 */
	@Override
	@SuppressWarnings("unchecked")
	public T findById(String id) {
		try {
			DocumentReference docRef = getDocumentRef(id);
			ApiFuture<DocumentSnapshot> docSnapFuture = docRef.get();
			await(docSnapFuture);
			DocumentSnapshot docSnap = docSnapFuture.get();
			return (T) docSnap.toObject(getDocumentClass());
		} catch (InterruptedException | ExecutionException | JsonSyntaxException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Found all documents in firestore
	 * 
	 * @return List of documents
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<T> findAll() {
		try {
			CollectionReference collectionRef = getCollectionRef();
			ApiFuture<QuerySnapshot> querySnapFuture = collectionRef.get();
			await(querySnapFuture);
			QuerySnapshot querySnap = querySnapFuture.get();
			return (List<T>) querySnap.toObjects(getDocumentClass());
		} catch (InterruptedException | ExecutionException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Found documents by fields filter
	 * 
	 * @return List of documents
	 */
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

	/**
	 * Method used to execute a query
	 * 
	 * @return List of documents
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<T> executeQuery(Query query) {
		try {
			ApiFuture<QuerySnapshot> querySnapFuture = query.get();
			await(querySnapFuture);
			QuerySnapshot querySnap = querySnapFuture.get();
			return (List<T>) querySnap.toObjects(getDocumentClass());
		} catch (InterruptedException | ExecutionException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Create a Collection Reference to Firestore
	 * 
	 * @return CollectionReference
	 */
	@Override
	public CollectionReference getCollectionRef() {
		Firestore db = FirestoreClient.getFirestore(FirebaseApp.getInstance());
		return db.collection(getDocumentClass().getSimpleName());
	}

	/**
	 * Create a Document Reference to Firestore
	 * 
	 * @return DocumentReference
	 */
	@Override
	public DocumentReference getDocumentRef(String id) {
		CollectionReference collectionRef = getCollectionRef();
		return collectionRef.document(id);
	}

	/**
	 * Method used to await while promise not done 
	 */
	@Override
	public ApiFuture<?> await(ApiFuture<?> future) {
		while (!future.isDone()) {};
		return future;
	}
	
	/**
	 * Method used to create document because not exists
	 * 
	 * @param document
	 * @return Return id of document
	 */
	private String create(T document) {
		try {
			CollectionReference collectionRef = getCollectionRef();
			ApiFuture<DocumentReference> docRefFuture = collectionRef.add(document);
			await(docRefFuture);
			return docRefFuture.get().getId();
		} catch (InterruptedException | ExecutionException e) {
			throw new RuntimeException(e);
		}
	}
}
