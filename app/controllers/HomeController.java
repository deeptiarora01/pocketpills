package controllers;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.inject.Singleton;

import model.FileDto;
import play.api.mvc.MultipartFormData.FilePart;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import service.ImageProcessor;
import service.UploadToS3;
import validation.ImageValidator;
import views.html.*;

/**
 * This class is the home controller used for index and upload page.
 */
@Singleton
public class HomeController extends Controller {

    private final play.data.FormFactory formFactory;
    
    private final UploadToS3 uploadToS3;
    
    private final ImageProcessor imageProcessor;
    
    private final ImageValidator imageValidator;
    
    @Inject
    public HomeController(play.data.FormFactory formFactory,UploadToS3 uploadToS3,ImageProcessor imageProcessor,ImageValidator imageValidator) {
        this.formFactory = formFactory;
        this.uploadToS3 = uploadToS3;
        this.imageProcessor = imageProcessor;
        this.imageValidator = imageValidator;
    }

    public Result index() {
        Form<FormData> form = formFactory.form(FormData.class);
        return ok(index.render(form));
    }

    public Result upload() throws IOException {
        final Http.MultipartFormData<File> formData = request().body().asMultipartFormData();
        final List<Http.MultipartFormData.FilePart<File>> fileParts = formData.getFiles();
        StringBuilder response = new StringBuilder();
        // Execute the code only if there is atleast one file selected by user
        if (!"".equals(fileParts.get(0).getFilename())) {
	        // For each file selected from frontend.
	        for(Http.MultipartFormData.FilePart<File> filePart:fileParts){
		        	
	        	if(!imageValidator.isValid(filePart)){
	        		response.append("Not a valid image : " +filePart.getFilename()+"\n");
	        		continue;
	        	}
	        	   // upload original file to S3.
		        	File file = filePart.getFile();
		        	FileDto fileDto = fileToFileDtoTransformer(file,filePart.getFilename(),filePart.getContentType(),"original");
		        	String path = file.getPath();
		        	response.append(	"URL for original image " + uploadToS3.upload(fileDto)+"\n");
					
					// resize smaller by 50% and then upload to S3
		          double percent = 0.5;
					File fileSmall = imageProcessor.resize(path, path+"small", percent);
					FileDto fileDtoSmall = fileToFileDtoTransformer(fileSmall,filePart.getFilename(),filePart.getContentType(),"small");
					response.append("URL for small image "+uploadToS3.upload(fileDtoSmall)+"\n");
					
					// resize bigger by 50% and the upload to S3
		          percent = 1.5;
		          File fileBig= imageProcessor.resize(path, path+"bigger", percent);
					FileDto fileDtoBig = fileToFileDtoTransformer(fileBig,filePart.getFilename(),filePart.getContentType(),"big");
					response.append("URL for big image "+uploadToS3.upload(fileDtoBig)+"\n");
	     
	        }
	        response.append("Thanks for using our application to upload files to S3");
	        return ok(response.toString());
        }else{
        	// here comes the validation
         //flash("error", "Missing file");
            return ok("There are no file to be uploaded");
        }
    }

    private FileDto fileToFileDtoTransformer(File file,String name,String contentType,String fileType) throws IOException{
    	FileDto fileDto = new FileDto();
    	fileDto.setContent(Files.readAllBytes(Paths.get(file.getAbsolutePath())));
		fileDto.setName(fileType+name);
		fileDto.setContentType(contentType);
		return fileDto;
    }
    
}
