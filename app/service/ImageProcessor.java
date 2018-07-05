package service;

import java.io.File;
import java.io.IOException;

import com.google.inject.ImplementedBy;

/**
 * Interface for image processing. 
 * 
 * @author deeptiarora
 *
 */
@ImplementedBy(ImageProcessorImpl.class)
public interface ImageProcessor {
	
	/**
     * Resizes an image to a absolute width and height (the image may not be
     * proportional)
     * @param inputImagePath Path of the original image
     * @param outputImagePath Path to save the resized image
     * @param scaledWidth absolute width in pixels
     * @param scaledHeight absolute height in pixels
     * @throws IOException
     */
	public File resize(String inputImagePath,
            String outputImagePath, int scaledWidth, int scaledHeight)
            throws IOException;
	
	  /**
     * Resizes an image by a percentage of original size (proportional).
     * @param inputImagePath Path of the original image
     * @param outputImagePath Path to save the resized image
     * @param percent a double number specifies percentage of the output image
     * over the input image.
     * @throws IOException
     */
	public File resize(String inputImagePath,
            String outputImagePath, double percent) throws IOException; 

}
