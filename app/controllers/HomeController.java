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
 * This class uses a custom body parser to change the upload type.
 */
@Singleton
public class HomeController extends Controller {

    private final play.data.FormFactory formFactory;
    
    private final UploadToS3Impl uploadToS3;
    
    private final ImageProcessorImpl imageProcessor;
    
    @Inject
    public HomeController(play.data.FormFactory formFactory,UploadToS3Impl uploadToS3,ImageProcessorImpl imageProcessor) {
        this.formFactory = formFactory;
        this.uploadToS3 = uploadToS3;
        this.imageProcessor = imageProcessor;
    }

    public Result index() {
        Form<FormData> form = formFactory.form(FormData.class);
        return ok(index.render(form));
    }

    /**
     * This method uses MyMultipartFormDataBodyParser as the body parser
     */
    public Result upload() throws IOException {
        final Http.MultipartFormData<File> formData = request().body().asMultipartFormData();
        final List<Http.MultipartFormData.FilePart<File>> fileParts = formData.getFiles();
        for(Http.MultipartFormData.FilePart<File> filePart:fileParts){
	        	FileDto fileDto = new FileDto();
	        	File file = filePart.getFile();
	        	String path = file.getPath();
				fileDto.setContent(Files.readAllBytes(Paths.get(file.getAbsolutePath())));
				fileDto.setName(filePart.getFilename());
				fileDto.setContentType(filePart.getContentType());
				fileDto.setPath(path);
				uploadToS3.upload(fileDto);

				//ImageProcessorImpl imageResizer = new ImageProcessorImpl();
				
				List<File> files = new ArrayList<>();
				// resize smaller by 50%
	            double percent = 0.5;
	            files.add(imageProcessor.resize(path, path+"small", percent));
	 
	            // resize bigger by 50%
	            percent = 1.5;
	            files.add(imageProcessor.resize(path, path+"bigger", percent));
				
				for(File file1:files){
					FileDto fileDto1 = new FileDto();
					fileDto1.setContent(Files.readAllBytes(Paths.get(file1.getAbsolutePath())));
					fileDto1.setName(file1.getName()+".jpg");
					fileDto1.setContentType(filePart.getContentType());
					fileDto1.setPath(file1.getPath());
					uploadToS3.upload(fileDto1);
				}
	        }
        
     //   final long data = operateOnTempFile(file);
        return ok("Files Uploaded to S3");
    }

    /*private long operateOnTempFile(File file) throws IOException {
        final long size = Files.size(file.toPath());
        Files.deleteIfExists(file.toPath());
        return size;
    }*/

}

