package unittests;

import org.junit.Test;

import primitives.Color;
import renderer.ImageWriter;

public class ImageWriterTest {

	@Test
	public void writeImage() {
		ImageWriter image= new ImageWriter("matrix", 800, 500);
		for(int i = 0; i < 800; i++) {
			for(int j = 0; j < 500; j++)
			{
				if(i % 50 == 0 || j % 50 == 0)
					image.writePixel(i, j, Color.BLACK);
				else
					image.writePixel(i, j, new Color(0, 0, 255));
			}
		}
		image.writeToImage();
	}
}
