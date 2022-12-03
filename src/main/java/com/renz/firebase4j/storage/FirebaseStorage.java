package com.renz.firebase4j.storage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import com.google.cloud.storage.Acl;
import com.google.cloud.storage.Acl.User;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.firebase.FirebaseApp;
import com.google.firebase.cloud.StorageClient;

/** 
 * Firebase storage services
 * 
 * @author Renan Viana
 * @author https://github.com/renanviana
 * @version 1.0.0
 * @since 1.0.0
 */
public class FirebaseStorage {

	/**
	 * Method used to upload files
	 * 
	 * @param file
	 * @return Return Media Link to access file uploaded
	 */
	public String uploadFile(File file) {
		Blob blob = createBlob(file);
		blob.createAcl(Acl.of(User.ofAllUsers(), Acl.Role.READER));
		return blob.getMediaLink();
	}

	private Bucket createBucket() {
		return StorageClient.getInstance(FirebaseApp.getInstance()).bucket();
	}

	private Blob createBlob(File file) {
		try {
			return createBucket().create(file.getName(), new FileInputStream(file),
					"application/" + getExtension(file));
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	private String getExtension(File file) {
		return file.getName().substring(file.getName().indexOf(".")).substring(1);
	}

}
