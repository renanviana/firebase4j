package com.renz.firebase4j.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import javax.servlet.http.Part;

import org.springframework.stereotype.Component;

import com.google.cloud.storage.Acl;
import com.google.cloud.storage.Acl.User;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.firebase.FirebaseApp;
import com.google.firebase.cloud.StorageClient;

@Component
public class FirebaseStorage {

	private SimpleDateFormat isoFormat;
	
	public FirebaseStorage() {
		this.isoFormat = new SimpleDateFormat("dd-MM-yyyy-HH-mm-ss");
		this.isoFormat.setTimeZone(TimeZone.getTimeZone("GMT-3"));
	}
	
	public String uploadFile(String fileName, Part filePart, FirebaseApp app) throws IOException {
		Bucket bucket = StorageClient.getInstance(app).bucket();
		String dtString = isoFormat.format(new Date());
		String extension = fileName.substring(fileName.indexOf("."));
		fileName = fileName.substring(0, fileName.indexOf("."));
		fileName = fileName + "-" + dtString + extension;
		Blob blob = bucket.create(fileName, filePart.getInputStream(), filePart.getContentType());
		blob.createAcl(Acl.of(User.ofAllUsers(), Acl.Role.READER));
		return blob.getMediaLink();
	}

	public String upload(File file, FirebaseApp app) throws IOException {
		Bucket bucket = StorageClient.getInstance(app).bucket();
		String dtString = isoFormat.format(new Date());
		String extension = file.getName().substring(file.getName().indexOf("."));
		String fileName = file.getName().substring(0, file.getName().indexOf("."));
		fileName = fileName + "-" + dtString + extension;
		Blob blob = bucket.create(fileName, new FileInputStream(file), "application/" + extension.substring(1));
		blob.createAcl(Acl.of(User.ofAllUsers(), Acl.Role.READER));
		return blob.getMediaLink();
	}
	
	public String uploadPDF(String fileName, InputStream input, FirebaseApp app) throws IOException {
		Bucket bucket = StorageClient.getInstance(app).bucket();
		String dtString = isoFormat.format(new Date());
		String extension = fileName.substring(fileName.indexOf("."));
		fileName = fileName.substring(0, fileName.indexOf("."));
		fileName = fileName + "-" + dtString + extension;
		Blob blob = bucket.create(fileName, input, "application/pdf");
		blob.createAcl(Acl.of(User.ofAllUsers(), Acl.Role.READER));
		return blob.getMediaLink();
	}
}
