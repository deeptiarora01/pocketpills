package service;

import static org.junit.Assert.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import javax.imageio.ImageIO;

import org.junit.Before;
import org.junit.Test;

import model.Size;


public class ImageProcessorImplTest {
	
	ImageProcessorImpl imageProcessor;
	File inputFile;
	File outputFile;
	
	@Before
	public void setup(){
		imageProcessor = new ImageProcessorImpl();
		inputFile = new File("test\\resources\\ArtTutorGridPic.jpg");
		outputFile = new File("test\\resources\\output\\ArtTutorGridPic.jpg");
	}
	
	@Test
	public void testConvertToSizes() throws IOException {
		final int scaledWidth = 150;
		final int scaledHeight = 150;
		Size size = new Size();
		size.setHeight(scaledHeight);
		size.setWidth(scaledWidth);
		List<Size> sizes =new ArrayList<>();
		sizes.add(size);
		List<File> files =imageProcessor.convertToSizes(sizes, outputFile.getAbsolutePath(), outputFile.getAbsolutePath());
		File file =files.get(0);
		BufferedImage outputImage = ImageIO.read(file);
		assertEquals(scaledHeight,outputImage.getHeight());
		assertEquals(scaledWidth,outputImage.getWidth());
	}

	@Test
	public void testResizeStringStringWithScaledValues() throws IOException {
		final int scaledWidth = 100;
		final int scaledHeight = 100; 
      String inputImagePath = inputFile.getAbsolutePath();
      String outputImagePath = outputFile.getAbsolutePath();
		File file = imageProcessor.resize(inputImagePath, outputImagePath, scaledWidth, scaledHeight);
		BufferedImage inputImage = ImageIO.read(file);
		assertEquals(scaledHeight,inputImage.getHeight());
		assertEquals(scaledWidth,inputImage.getWidth());
	}

	@Test
	public void testResizeWithPercent() throws IOException {
		double percent = 0.5;
	   String inputImagePath = inputFile.getAbsolutePath();
	   String outputImagePath = outputFile.getAbsolutePath();
		File file = imageProcessor.resize(inputImagePath, outputImagePath, percent);
		BufferedImage outputImage = ImageIO.read(file);
		BufferedImage inputImage = ImageIO.read(new File(inputImagePath));
		assertEquals(outputImage.getHeight(),inputImage.getHeight()*percent,1);
		assertEquals(outputImage.getWidth(),inputImage.getWidth()*percent,1);
	}

}
