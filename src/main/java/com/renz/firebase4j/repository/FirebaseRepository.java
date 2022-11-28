package com.renz.firebase4j.repository;

public interface FirebaseRepository<T> {
    
    T save(T pojo);

}
