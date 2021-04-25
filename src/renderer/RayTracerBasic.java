package renderer;

import java.util.List;

import primitives.Color;
import primitives.Point3D;
import primitives.Ray;
import scene.Scene;

public class RayTracerBasic extends RayTracerBase {

	public RayTracerBasic(Scene scene) {
		super(scene);
	}

	@Override
	/**
	 * Receives a ray and returns the color in which it should paint the pixel
	 * @param ray
	 * @return color
	 */
	public Color traceRay(Ray ray) {
		List<Point3D> list=scene.geometries.findIntsersections(ray);
		if(list==null) {
			return scene.background;
		}
		Point3D point=ray.findClosestPoint(list);
		return calcColor(point);
	}
	/**
	 * Returns the color in a dot
	 * @param point
	 * @return color
	 */
	public Color calcColor(Point3D point) {
		return scene.ambientLight.getIntensity();
	}

}
