package service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.activation.MimetypesFileTypeMap;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3ObjectSummary;

import model.FileDto;

public class UploadToS3Impl implements UploadToS3{
	
	private static final String SUFFIX = "/";
	
	@Override
	public void upload(FileDto fileDto){
		// credentials object identifying user for authentication
				AWSCredentials credentials = new BasicAWSCredentials(
						"AKIAIOYV6CGT7H3L46KA", 
						"d4rYemFy66ZMyKQ3R9OkkCOHyjnNFqzvw8yFPZIn");
				
				// create a client connection based on credentials
				AmazonS3Client s3client = new AmazonS3Client(credentials);
				
				// create bucket - name must be unique for all S3 users
				String bucketName = "pocketpillsassignment";
				
				// create folder into bucket
				String folderName = "testfolder";
				createFolder(bucketName, folderName, s3client);
				
				// upload file to folder and set it to public
				String fileName = folderName +SUFFIX+fileDto.getName();
				
				// Declare meta data of the file.
				ObjectMetadata metaData = new ObjectMetadata();
				MimetypesFileTypeMap mimeTypesMap = new MimetypesFileTypeMap();
				String mimeType = mimeTypesMap.getContentType(fileDto.getContentType());
				metaData.setContentLength(fileDto.getContent().length);
				metaData.setContentType(mimeType);
				
				// Put the file in the S3 bucket.
				PutObjectResult putObjectResult=null;
				try (InputStream input = new ByteArrayInputStream(fileDto.getContent())) {
					PutObjectRequest putObjectRequest = new PutObjectRequest(
							bucketName, fileName, input,metaData)
							.withCannedAcl(CannedAccessControlList.PublicRead);
					putObjectResult = s3client.putObject(putObjectRequest);
					
					String url = s3client.getResourceUrl(bucketName, fileName);
					System.out.println(url);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	}
	
	@Override
	public void createFolder(String bucketName, String folderName, AmazonS3 client) {
		// create meta-data for your folder and set content-length to 0
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentLength(0);
		// create empty content
		InputStream emptyContent = new ByteArrayInputStream(new byte[0]);
		// create a PutObjectRequest passing the folder name suffixed by /
		PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName,
				folderName + SUFFIX, emptyContent, metadata);
		// send request to S3 to create folder
		client.putObject(putObjectRequest);
	}
	
	@Override
	public void deleteFolder(String bucketName, String folderName, AmazonS3 client) {
		List<S3ObjectSummary> fileList = client.listObjects(bucketName, folderName).getObjectSummaries();
		for (S3ObjectSummary file : fileList) {
			client.deleteObject(bucketName, file.getKey());
		}
		//client.deleteObject(bucketName, folderName);
	}

}
