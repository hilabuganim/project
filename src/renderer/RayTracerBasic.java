package renderer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import elements.LightSource;
import geometries.Intersectable.GeoPoint;
import primitives.Color;
import primitives.Material;
import primitives.Ray;
import primitives.Util;
import primitives.Vector;
import scene.Scene;

public class RayTracerBasic extends RayTracerBase {
	private static final int MAX_CALC_COLOR_LEVEL = 10;
	private static final double MIN_CALC_COLOR_K = 0.001;
	public static int NUM_OF_RAYS = 0;
	public static double RADIUS = 0.1;



	public RayTracerBasic(Scene scene) {
		super(scene);
	}

	@Override
	/**
	 * Receives a ray and returns the color in which it should paint the pixel
	 * 
	 * @param ray
	 * @return color
	 */
	public Color traceRay(Ray ray) {
		GeoPoint closestPoint = findClosestIntersection(ray);
		return closestPoint == null ? Color.BLACK : calcColor(closestPoint, ray);
	}

	/**
	 * Returns the color in a dot
	 * 
	 * @param point
	 * @return color
	 */
	public Color calcColor(GeoPoint point, Ray ray, int level, double k) {
		Color color = point.geometry.getEmmission();
		color = color.add(calcLocalEffects(point, ray, k));
		return 1 == level ? color : color.add(calcGlobalEffects(point, ray, level, k));
	}
    /**
     * effects the closes geometries 
     * @param geopoint
     * @param ray
     * @param level
     * @param k
     * @return color of the closes geometry
     */
	private Color calcGlobalEffects(GeoPoint geopoint, Ray ray, int level, double k) {
		Color color = Color.BLACK;
		Vector n = geopoint.geometry.getNormal(geopoint.point);
		Material material = geopoint.geometry.getMaterial();
		double kr = material.kR, kkr = k * kr;
		if (kkr > MIN_CALC_COLOR_K) {
			Ray reflectedRay = constructingReflectedRay(n, ray.getDir(), geopoint);
			GeoPoint reflectedPoint = findClosestIntersection(reflectedRay);
			if (reflectedPoint != null)
				color = color.add(calcColor(reflectedPoint, reflectedRay, level - 1, kkr).scale(kr));
		}
		double kt = material.kT, kkt = k * kt;
		if (kkt > MIN_CALC_COLOR_K) {
			Ray refractedRay = constructingRefractedRay(n, ray.getDir(), geopoint);
			GeoPoint refractedPoint = findClosestIntersection(refractedRay);
			if (refractedPoint != null)
				color = color.add(calcColor(refractedPoint, refractedRay, level - 1, kkt).scale(kt));
		}
		return color;

	}

	/**
	 * Calculate by scattering and shining some glossy surface at the same point for
	 * each of the lights
	 * 
	 * @param intersection
	 * @param ray
	 * @return color
	 */
	private Color calcLocalEffects(GeoPoint intersection, Ray ray, double k) {
		Vector v = ray.getDir();
		Vector n = intersection.geometry.getNormal(intersection.point);
		double nv = Util.alignZero(n.dotProduct(v));
		if (nv == 0)
			return Color.BLACK;
		int nShininess = intersection.geometry.getMaterial().nShininess;
		double kd = intersection.geometry.getMaterial().kD, ks = intersection.geometry.getMaterial().kS;
		Color color = Color.BLACK;
		for (LightSource lightSource : scene.lights) {
			Vector l = lightSource.getL(intersection.point);
			double nl = Util.alignZero(n.normalize().dotProduct(l.normalize()));
			if (nl * nv > 0) { // sign(nl) == sing(nv)
				double ktr = transparency(lightSource, l, n, intersection);
				if (ktr * k > MIN_CALC_COLOR_K) {
					Color lightIntensity = lightSource.getIntensity(intersection.point).scale(ktr);
					color = color.add(calcDiffusive(kd, nl, lightIntensity),
							calcSpecular(ks, l, n, v, nShininess, lightIntensity));
				}
			}
		}
		return color;
	}

	/**
	 * Calculates the reflection of light
	 * 
	 * @param ks
	 * @param l
	 * @param n
	 * @param v
	 * @param nShininess
	 * @param lightIntensity
	 * @return color
	 */
	private Color calcSpecular(double ks, Vector l, Vector n, Vector v, int nShininess, Color lightIntensity) {
		Vector r = l.subtract(n.scale(l.dotProduct(n) * 2));
		double vr = Math.max(0, v.scale(-1).dotProduct(r));
		double a = Math.pow(vr, nShininess) * ks;
		return lightIntensity.scale(a);
	}

	/**
	 * Calculates the diffusive of light
	 * 
	 * @param kd
	 * @param nl
	 * @param lightIntensity
	 * @return color
	 */
	private Color calcDiffusive(double kd, double nl, Color lightIntensity) {
		nl = Math.abs(nl);
		return lightIntensity.scale(nl * kd);
	}

//	private boolean unshaded(Vector l, Vector n, GeoPoint geopoint, LightSource light) {
//		Vector lightDirection = l.scale(-1); // from point to light source
//		Ray lightRay = new Ray(geopoint.point, lightDirection, n);
//		List<GeoPoint> intersections = scene.geometries.findGeoIntersections(lightRay);
//		if (intersections == null)
//			return true;
//		double lightDistance = light.getDistance(geopoint.point);
//		for (GeoPoint gp : intersections) {
//			if (Util.alignZero(gp.point.distance(geopoint.point) - lightDistance) <= 0
//					&& gp.geometry.getMaterial().kT == 0)
//				return false;
//		}
//		return true;
//	}

	private Ray constructingReflectedRay(Vector n, Vector v, GeoPoint geopoint) { //ray return back-hishtakfut
		Vector r = v.subtract(n.scale(n.dotProduct(v) * 2));
		return new Ray(geopoint.point, r, n);
	}

	private Ray constructingRefractedRay(Vector n, Vector v, GeoPoint geopoint) { //ray pass throw the geometry-shkifut
		return new Ray(geopoint.point, v, n);
	}

	private GeoPoint findClosestIntersection(Ray ray) {
		List<GeoPoint> list = scene.geometries.findGeoIntersections(ray);
		if (list == null) {
			return null;
		}
		return ray.getClosestGeoPoint(list);
	}

	private Color calcColor(GeoPoint gp, Ray ray) {
		return calcColor(gp, ray, MAX_CALC_COLOR_LEVEL, 1).add(scene.ambientLight.getIntensity());
	}
	/**
	 * Calculate the new vector by a random number between radius and minus radius
	 * @param vector
	 * @param radius
	 * @return new vector
	 */
	private static Vector randomVector(Vector vector, double radius) {
	    double x = vector.getHead().x.coord + ThreadLocalRandom.current().nextDouble(-radius, radius);
	    double y = vector.getHead().y.coord + ThreadLocalRandom.current().nextDouble(-radius, radius);
	    double z = vector.getHead().z.coord + ThreadLocalRandom.current().nextDouble(-radius, radius);
	    return new Vector(x, y, z);
	}
/**
 * Creates a new ray by a starting point p0, and also receives a direction vector and radius 
 * and calculates the random vector according to this
 * @param ray
 * @return list of rays
 */

	public static List<Ray> splitRay(Ray ray) {
	    List<Ray> result = new ArrayList<>();
	    result.add(ray);
	    for (int i = 0; i < NUM_OF_RAYS; i++) {
	        result.add(new Ray(ray.getP0(), randomVector(ray.getDir(), RADIUS)));
	    }
	    return result;
	}

//	private double transparency(LightSource ls, Vector l, Vector n, GeoPoint geoPoint) {
//		Vector lightDirection = l.scale(-1); // from point to light source
//		Ray lightRay = new Ray(geoPoint.point, lightDirection, n);
//		List<GeoPoint> intersections = scene.geometries.findGeoIntersections(lightRay);
//		if (intersections == null)
//			return 1.0;
//		double ktr = 1.0;
//		double lightDistance = ls.getDistance(geoPoint.point);
//		for (GeoPoint gp : intersections) {
//			if (Util.alignZero(gp.point.distance(geoPoint.point) - lightDistance) <= 0) {
//				ktr *= gp.geometry.getMaterial().kT;
//				if (ktr < MIN_CALC_COLOR_K)
//					return 0.0;
//			}
//		}
//		return ktr;
//	}
	
	private double transparency(Ray ray, GeoPoint geopoint, LightSource light) {
	    // calculate the distance between the light source and the point
	    double lightDistance = light.getDistance(geopoint.point);

	    // check if there is geometries between the light and the point
	    var intersections = scene.geometries.findGeoIntersections(ray);
	    if (intersections == null) return 1.0;

	    // if there is, calculate the amount of transparency of all of them
	    double ktr = 1.0;
	    for (GeoPoint gp : intersections) {
	        if (Util.alignZero(gp.point.distance(geopoint.point) - lightDistance) <= 0) {
	            ktr *= gp.geometry.getMaterial().kT;
	            if (ktr < MIN_CALC_COLOR_K) return 0.0;
	        }
	    }
	    return ktr;
	}
/**
 * Calculates the main ray and sends it to splitray function that creates a list of rays 
 * and summarizes the transparency of each ray and then returns their average to divide by the size of the list.
 * @param light
 * @param l
 * @param n
 * @param geopoint
 * @return rays transparency average to divide by the size of the list.
 */
	private double transparency(LightSource light, Vector l, Vector n, GeoPoint geopoint) {
	    Vector lightDirection = l.scale(-1);
	    Ray lightRay = new Ray(geopoint.point, lightDirection, n);
	    List<Ray> cone = splitRay(lightRay);
	    double transparency = 0;
	    for (Ray ray : cone) {
	        transparency += transparency(ray, geopoint, light);
	    }
	    return Util.alignZero(transparency / cone.size());
	}
}
