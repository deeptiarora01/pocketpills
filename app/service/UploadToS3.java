package service;

import com.amazonaws.services.s3.AmazonS3;
import com.google.inject.ImplementedBy;

import model.FileDto;

/**
 * Interface to perform action in S3 i.e creating and deleting folders and uploading files to S3. 
 * 
 * @author deeptiarora
 *
 */
@ImplementedBy(UploadToS3Impl.class)
public interface UploadToS3 {
	
	/**
	 * Method to upload file to S3.
	 * 
	 * @param fileDto
	 * @return String
	 */
	public String upload(FileDto fileDto);
	
	/**
	 * The method to create an empty folder.
	 * 
	 * @param bucketName
	 * @param folderName
	 * @param client
	 */
	public void createFolder(String bucketName, String folderName, AmazonS3 client);
	
	/**
	 * The method to delete a folder and all files inside it.
	 * 
	 * @param bucketName
	 * @param folderName
	 * @param client
	 */
	public void deleteFolder(String bucketName, String folderName, AmazonS3 client);

}
