package service;

import java.io.File;
import java.io.IOException;

public interface ImageProcessor {
	
	public File resize(String inputImagePath,
            String outputImagePath, int scaledWidth, int scaledHeight)
            throws IOException;
	
	public File resize(String inputImagePath,
            String outputImagePath, double percent) throws IOException; 

}
