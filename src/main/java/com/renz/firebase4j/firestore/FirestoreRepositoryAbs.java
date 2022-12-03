package com.renz.firebase4j.firestore;

import java.util.List;
import java.util.Map;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Query;

public abstract class FirestoreRepositoryAbs<T extends Document> {

	private Class<?> clazz;
	
	protected FirestoreRepositoryAbs(Class<?> clazz) {
		this.clazz = clazz;
	}
	
	protected Class<?> getDocumentClass() {
		return this.clazz;
	}
	
	public abstract T save(T entity);

	public abstract void delete(String id);

	public abstract T findById(String id);

	public abstract List<T> findAll();

	public abstract List<T> findByFields(Map<String, Object> fields);

	public abstract List<T> executeQuery(Query query);
	
	public abstract CollectionReference getCollectionRef();
	
	public abstract DocumentReference getDocumentRef(String id);
	
	public abstract ApiFuture<?> await(ApiFuture<?> future);
}
