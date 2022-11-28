package com.renz.firebase4j.repository.impl;

import static com.renz.firebase4j.util.ReferenceUtil.await;
import static com.renz.firebase4j.util.ReferenceUtil.getCollectionRef;

import java.util.concurrent.ExecutionException;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.FirebaseApp;
import com.renz.firebase4j.repository.FirebaseRepository;

import org.springframework.stereotype.Component;

@Component
public class FirebaseRepositoryImpl<T> implements FirebaseRepository<T> {

    @Override
    public T save(T entity) {
        CollectionReference collectionRef = getCollectionRef(entity.getClass().getSimpleName(),
                FirebaseApp.getInstance());
        ApiFuture<WriteResult> writeResult = await(collectionRef.document().create(entity));
        try {
            System.out.println("TESTEEEEE: " + writeResult.get().toString());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

}
