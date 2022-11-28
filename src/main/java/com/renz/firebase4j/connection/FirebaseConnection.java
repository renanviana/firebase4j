package com.renz.firebase4j.connection;

import java.io.IOException;
import java.net.URISyntaxException;

import com.google.firebase.FirebaseApp;

public interface FirebaseConnection {
    
    FirebaseApp connect(String serviceAccountKeyPath) throws IOException, URISyntaxException;

}
