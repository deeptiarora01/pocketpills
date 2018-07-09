package validation;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import javax.imageio.ImageIO;

import play.mvc.Http;

public class ImageValidator implements Validator<Http.MultipartFormData.FilePart<File>>{

	
	@Override
	public boolean isValid(Http.MultipartFormData.FilePart<File> filePart) throws IOException {
		final long size = Files.size(filePart.getFile().toPath());
		//The image cannot be more than 8MB of size.
    	if(size>8000000){
    		return false;
    	}
    	
    	//The image type can be either png or jpg.
    	if(!("image/jpeg".equals(filePart.getContentType())||"image/png".equals(filePart.getContentType()))){
        	return false;
      }
    	
    	//The image dimensions cannot be more than 2048 x 2048
    	BufferedImage inputImage = ImageIO.read(filePart.getFile());
      int width = inputImage.getWidth();
      int height = inputImage.getHeight();
      if(width>2048 || height > 2048){
        	return false;
      }
    	return true;
	}

}
