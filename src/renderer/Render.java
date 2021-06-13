package renderer;

import java.util.List;
import java.util.MissingResourceException;
import geometries.Intersectable.GeoPoint;

import elements.Camera;
import primitives.Color;
import primitives.Ray;
import scene.Scene;

public class Render {
	private ImageWriter imageWriter;
	private Camera camera;
	private RayTracerBase rayTracerBase;

	public Render setImageWriter(ImageWriter imageWriter) {
		this.imageWriter = imageWriter;
		return this;
	}

	// public Render setScene(Scene scene) {
	// return this;
	// }

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
		if (imageWriter == null)
			throw new MissingResourceException("imageWriter is null", "ImageWriter", "imageWriter");

		if (camera == null)
			throw new MissingResourceException("camera is null", "Camera", "camera");

		if (rayTracerBase == null)
			throw new MissingResourceException("rayTracerBase is null", "RayTracerBase", "rayTracerBase");
		/**
		 * Goes through a pixel matrix and calculates for each pixel its color according
		 * to the ray coming out of it
		 */
		final Pixel thePixel = new Pixel(imageWriter.getNy(), imageWriter.getNx()); // Main pixel management object
		Thread[] threads = new Thread[_threads];
		for (int i = _threads - 1; i >= 0; --i) { // create all threads
			threads[i] = new Thread(() -> {
				Pixel pixel = new Pixel(); // Auxiliary thread's pixel object
				while (thePixel.nextPixel(pixel)) {
					Ray rays = camera.constructRayThroughPixel(imageWriter.getNx(), imageWriter.getNy(), pixel.col,
							pixel.row);
					Color color = rayTracerBase.traceRay(rays);
					imageWriter.writePixel(pixel.col, pixel.row, color);
				}
			});
		}
		for (Thread thread : threads)
			thread.start(); // Start all the threads
		// Wait for all threads to finish
		for (Thread thread : threads)
			try {
				thread.join();
			} catch (Exception e) {
			}
		if (print)
			System.out.printf("\r100%%\n"); // Print 100%
	}

	/**
	 * Draws the grid
	 * 
	 * @param interval- size of the grid
	 * @param color
	 */
	public void printGrid(int interval, Color color) {
		if (imageWriter == null) // There is no data so it is impossible to build an image
			throw new MissingResourceException("imageWriter is null", "ImageWriter", "imageWriter");
		// Draws the grid
		for (int i = 0; i < imageWriter.getNx(); i++)
			for (int j = 0; j < imageWriter.getNy(); j++)
				if (i % interval == 0 || j % interval == 0)
					imageWriter.writePixel(i, j, color);
	}

	/**
	 * save the image as file
	 */
	public void writeToImage() {
		if (imageWriter == null)
			throw new MissingResourceException("imageWriter is null", "ImageWriter", "imageWriter");

		imageWriter.writeToImage();
	}

	private int threadsNumber = 1;
	private final int SPARE_THREADS = 2; // Spare threads if trying to use all the cores
	private boolean print = false; // printing progress percentage
	private int _threads = 1;

	/**
	 * Set multithreading <br>
	 * - if the parameter is 0 - number of coress less SPARE (2) is taken
	 * 
	 * @param threads number of threads
	 * @return the Render object itself
	 */
	public Render setMultithreading(int threads) {
		if (threads < 0)
			throw new IllegalArgumentException("Multithreading must be 0 or higher");
		if (threads != 0)
			_threads = threads;
		else {
			int cores = Runtime.getRuntime().availableProcessors() - SPARE_THREADS;
			_threads = cores <= 2 ? 1 : cores;
		}
		return this;
	}

	/**
	 * Set debug printing on
	 * 
	 * @return the Render object itself
	 */
	public Render setDebugPrint() {
		print = true;
		return this;
	}

	/**
	 * Pixel is an internal helper class whose objects are associated with a Render
	 * object that they are generated in scope of. It is used for multithreading in
	 * the Renderer and for follow up its progress.<br/>
	 * There is a main follow up object and several secondary objects - one in each
	 * thread.
	 */
	private class Pixel {
		private long maxRows = 0; // Ny
		private long maxCols = 0; // Nx
		private long pixels = 0; // Total number of pixels: Nx*Ny
		public volatile int row = 0; // Last processed row
		public volatile int col = -1; // Last processed column
		private long counter = 0; // Total number of pixels processed
		private int percents = 0; // Percent of pixels processed
		private long nextCounter = 0; // Next amount of processed pixels for percent progress

		/**
		 * The constructor for initializing the main follow up Pixel object
		 * 
		 * @param maxRows the amount of pixel rows
		 * @param maxCols the amount of pixel columns
		 */
		public Pixel(int maxRows, int maxCols) {
			this.maxRows = maxRows;
			this.maxCols = maxCols;
			pixels = maxRows * maxCols;
			nextCounter = pixels / 100;
			if (Render.this.print)
				System.out.printf("\r %02d%%", percents);
		}

		/**
		 * Default constructor for secondary Pixel objects
		 */
		public Pixel() {
		}

		/**
		 * Public function for getting next pixel number into secondary Pixel object.
		 * The function prints also progress percentage in the console window.
		 * 
		 * @param target target secondary Pixel object to copy the row/column of the
		 *               next pixel
		 * @return true if the work still in progress, -1 if it's done
		 */
		public boolean nextPixel(Pixel target) {
			int percents = nextP(target);
			if (print && percents > 0)
				System.out.printf("\r %02d%%", percents);
			if (percents >= 0)
				return true;
			if (print)
				System.out.printf("\r %02d%%", 100);
			return false;
		}

		/**
		 * Internal function for thread-safe manipulating of main follow up Pixel object
		 * - this function is critical section for all the threads, and main Pixel
		 * object data is the shared data of this critical section.<br/>
		 * The function provides next pixel number each call.
		 * 
		 * @param target target secondary Pixel object to copy the row/column of the
		 *               next pixel
		 * @return the progress percentage for follow up: if it is 0 - nothing to print,
		 *         if it is -1 - the task is finished, any other value - the progress
		 *         percentage (only when it changes)
		 */
		private synchronized int nextP(Pixel target) {
			++col;
			++counter;
			if (col < maxCols) {
				target.row = this.row;
				target.col = this.col;
				if (print && counter == nextCounter) {
					++percents;
					nextCounter = pixels * (percents + 1) / 100;
					return percents;
				}
				return 0;
			}
			++row;
			if (row < maxRows) {
				col = 0;
				if (print && counter == nextCounter) {
					++percents;
					nextCounter = pixels * (percents + 1) / 100;
					return percents;
				}
				return 0;
			}
			return -1;
		}
	}

}
