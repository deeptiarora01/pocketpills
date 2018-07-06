package service;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.AnonymousAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;

import io.findify.s3mock.S3Mock;
import model.FileDto;


/**
 * Test class for UploadToS3Impl.
 * 
 * @author deeptiarora
 *
 */
public class UploadToS3ImplTest {
	
	 private AmazonS3Client client;
	 private UploadToS3Impl uploadToS3Impl;
	 S3Mock api;
	
	@Before
	public void setup() throws IOException{
		
		int free = new ServerSocket(0).getLocalPort();
	    api = new S3Mock.Builder().withPort(free).withInMemoryBackend().build();
	    api.start();
	            
	    /* AWS S3 client setup.
	     *  withPathStyleAccessEnabled(true) trick is required to overcome S3 default 
	     *  DNS-based bucket access scheme
	     *  resulting in attempts to connect to addresses like "bucketname.localhost"
	     *  which requires specific DNS setup.
	     */
	    EndpointConfiguration endpoint = new EndpointConfiguration("http://localhost:"+free, "us-west-2");
	    client = (AmazonS3Client) AmazonS3ClientBuilder
	      .standard()
	      .withPathStyleAccessEnabled(true)  
	      .withEndpointConfiguration(endpoint)
	      .withCredentials(new AWSStaticCredentialsProvider(new AnonymousAWSCredentials()))     
	      .build();
	    client.createBucket("testbucket");
	
	    uploadToS3Impl= new UploadToS3Impl();	

	}

	@Test
	public void testCreateFolder() {
		uploadToS3Impl.createFolder("testbucket", "folder", client);
		ObjectListing Objects = client.listObjects("testbucket");
		List<S3ObjectSummary> summaries= Objects.getObjectSummaries();
		assertEquals(summaries.size(), 1);
		assertEquals(summaries.get(0).getKey(), "folder/");
		
		byte[] content = {20,39,60};
		FileDto fileDto = new FileDto();
		fileDto.setContentType("jpg");
		fileDto.setContent(content);
		uploadToS3Impl.upload(fileDto ,"testbucket", "folder", client);
		
		
		uploadToS3Impl.deleteFolder("testbucket", "folder", client);
		Objects = client.listObjects("testbucket");
		summaries= Objects.getObjectSummaries();
		assertEquals(summaries.size(), 0);
		
		api.stop();
	}

}
