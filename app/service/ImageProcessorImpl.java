package service;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import model.Size;

public class ImageProcessorImpl implements ImageProcessor{
	
	public List<File> convertToSizes ( List<Size> sizes , String imagePath ,String outputImagePath) throws IOException{
		List<File> files = new ArrayList<>();
		for(Size size: sizes){
			File file =resize(imagePath, outputImagePath ,size.getWidth(),size.getHeight());
			files.add(file);
		}
		
		return files;
	}
	
	@Override
    public File resize(String inputImagePath,
            String outputImagePath, int scaledWidth, int scaledHeight)
            throws IOException {
        // reads input image
        File inputFile = new File(inputImagePath);
        BufferedImage inputImage = ImageIO.read(inputFile);
 
        // creates output image
        BufferedImage outputImage = new BufferedImage(scaledWidth,
                scaledHeight, inputImage.getType());
 
        // scales the input image to the output image
        Graphics2D g2d = outputImage.createGraphics();
        g2d.drawImage(inputImage, 0, 0, scaledWidth, scaledHeight, null);
        g2d.dispose();
 
        
      //  return outputImage;
        // extracts extension of output file
      /*  String formatName = outputImagePath.substring(outputImagePath
                .lastIndexOf(".") + 1);*/
 
        File file = new File(outputImagePath);
        // writes to output file
        ImageIO.write(outputImage, "jpg", file);
        
        return file;
    }
 
	@Override
    public File resize(String inputImagePath,
            String outputImagePath, double percent) throws IOException {
        File inputFile = new File(inputImagePath);
        BufferedImage inputImage = ImageIO.read(inputFile);
        int scaledWidth = (int) (inputImage.getWidth() * percent);
        int scaledHeight = (int) (inputImage.getHeight() * percent);
        return resize(inputImagePath, outputImagePath, scaledWidth, scaledHeight);
    }
}
