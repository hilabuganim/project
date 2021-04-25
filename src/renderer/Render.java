package renderer;

import java.util.MissingResourceException;

import elements.Camera;
import primitives.Color;
import primitives.Ray;
import scene.Scene;

public class Render {
	private ImageWriter imageWriter;
	private Scene scene;
	private Camera camera;
	private RayTracerBase rayTracerBase;
	
	public Render setImageWriter(ImageWriter imageWriter) {
		this.imageWriter = imageWriter;
		return this;
	}
	
	public Render setScene(Scene scene) {
		this.scene = scene;
		return this;
	}
	
	public Render setCamera(Camera camera) {
		this.camera = camera;
		return this;
	}
	
	public Render setRayTracerBase(RayTracerBase rayTracerBase) {
		this.rayTracerBase = rayTracerBase;
		return this;
	}
	/**
	 * if one of the fields is zero
	 */
	public void renderImage() {
		if(imageWriter == null) 
			throw new MissingResourceException("imageWriter is null", "ImageWriter", "imageWriter");
		
		if(scene == null)
			throw new MissingResourceException("scene is null", "Scene", "scene");
		
		if(camera == null)
			throw new MissingResourceException("camera is null", "Camera", "camera");	
		
		if(rayTracerBase == null)
			throw new MissingResourceException("rayTracerBase is null", "RayTracerBase", "rayTracerBase");
		/**
		 * Goes through a pixel matrix and calculates for each pixel its color according to the ray coming out of it
		 */
		for(int i = 0; i < imageWriter.getNx(); i++)
			for(int j = 0; j < imageWriter.getNy(); j++) {
			Ray ray=camera.constructRayThroughPixel(imageWriter.getNx(), imageWriter.getNy(), j, i);
			Color color=rayTracerBase.traceRay(ray);
			imageWriter.writePixel(i, j, color);
	}
		
	}
	/**
	 * Draws the grid
	 * @param interval- size of the grid
	 * @param color
	 */
	public void printGrid(int interval, Color color) { 
		if(imageWriter == null) //There is no data so it is impossible to build an image
			throw new MissingResourceException("imageWriter is null", "ImageWriter", "imageWriter");
		//Draws the grid
		for(int i = 0; i < imageWriter.getNx(); i++)
			for(int j = 0; j < imageWriter.getNy(); j++)
				if(i % interval == 0 || j % interval == 0)
					imageWriter.writePixel(i, j, color);
	}
	/**
	 * save the image as file
	 */
	public void writeToImage() {
		if(imageWriter == null) 
			throw new MissingResourceException("imageWriter is null", "ImageWriter", "imageWriter");
		
		imageWriter.writeToImage();
	}

}
