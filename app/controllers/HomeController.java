package controllers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import model.FileDto;
import model.Size;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import service.ImageProcessor;
import service.ImageProcessorImpl;
import service.UploadToS3;
import service.UploadToS3Impl;

import views.html.*;
/**
 * This class is the home controller used for index and upload page.
 */
@Singleton
public class HomeController extends Controller {

    private final play.data.FormFactory formFactory;
    
    private final UploadToS3 uploadToS3;
    
    private final ImageProcessor imageProcessor;
    
    @Inject
    public HomeController(play.data.FormFactory formFactory,UploadToS3 uploadToS3,ImageProcessor imageProcessor) {
        this.formFactory = formFactory;
        this.uploadToS3 = uploadToS3;
        this.imageProcessor = imageProcessor;
    }

    public Result index() {
        Form<FormData> form = formFactory.form(FormData.class);
        return ok(index.render(form));
    }

    public Result upload() throws IOException {
        final Http.MultipartFormData<File> formData = request().body().asMultipartFormData();
        final List<Http.MultipartFormData.FilePart<File>> fileParts = formData.getFiles();
        
        // For each file selected from frontend.
        for(Http.MultipartFormData.FilePart<File> filePart:fileParts){
	        	
        	   // upload original file to S3.
	        	File file = filePart.getFile();
	        	FileDto fileDto = fileToFileDtoTransformer(file,filePart.getFilename(),filePart.getContentType(),"original");
	        	String path = file.getPath();
				uploadToS3.upload(fileDto);
				
				// resize smaller by 50% and then upload to S3
	          double percent = 0.5;
				File fileSmall = imageProcessor.resize(path, path+"small", percent);
				FileDto fileDtoSmall = fileToFileDtoTransformer(fileSmall,filePart.getFilename(),filePart.getContentType(),"small");
				uploadToS3.upload(fileDtoSmall);
				
				// resize bigger by 50% and the upload to S3
	          percent = 1.5;
	          File fileBig= imageProcessor.resize(path, path+"bigger", percent);
				FileDto fileDtoBig = fileToFileDtoTransformer(fileBig,filePart.getFilename(),filePart.getContentType(),"big");
				uploadToS3.upload(fileDtoBig);
     
        }
        return ok("Files Uploaded to S3");
    }

    private FileDto fileToFileDtoTransformer(File file,String name,String contentType,String fileType) throws IOException{
    	FileDto fileDto = new FileDto();
    	fileDto.setContent(Files.readAllBytes(Paths.get(file.getAbsolutePath())));
		fileDto.setName(fileType+name);
		fileDto.setContentType(contentType);
		return fileDto;
    }
    
    private boolean validateFile(){
    	
    	return false;
    }
    
    /*private long operateOnTempFile(File file) throws IOException {
        final long size = Files.size(file.toPath());
        Files.deleteIfExists(file.toPath());
        return size;
    }*/

}
