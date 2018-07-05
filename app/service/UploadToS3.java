package service;

import com.amazonaws.services.s3.AmazonS3;

import model.FileDto;

public interface UploadToS3 {
	
	public void upload(FileDto fileDto);
	
	public void createFolder(String bucketName, String folderName, AmazonS3 client);
	
	public void deleteFolder(String bucketName, String folderName, AmazonS3 client);

}
