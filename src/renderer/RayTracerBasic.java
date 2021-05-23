package renderer;

import java.util.List;

import elements.LightSource;
import geometries.Intersectable.GeoPoint;
import primitives.Color;
import primitives.Material;
import primitives.Point3D;
import primitives.Ray;
import primitives.Util;
import primitives.Vector;
import scene.Scene;

public class RayTracerBasic extends RayTracerBase {
	private static final int MAX_CALC_COLOR_LEVEL = 10;
	private static final double MIN_CALC_COLOR_K = 0.001;

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
     * 
     * @param geopoint
     * @param ray
     * @param level
     * @param k
     * @return
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

	private boolean unshaded(Vector l, Vector n, GeoPoint geopoint, LightSource light) {
		Vector lightDirection = l.scale(-1); // from point to light source
		Ray lightRay = new Ray(geopoint.point, lightDirection, n);
		List<GeoPoint> intersections = scene.geometries.findGeoIntersections(lightRay);
		if (intersections == null)
			return true;
		double lightDistance = light.getDistance(geopoint.point);
		for (GeoPoint gp : intersections) {
			if (Util.alignZero(gp.point.distance(geopoint.point) - lightDistance) <= 0
					&& gp.geometry.getMaterial().kT == 0)
				return false;
		}
		return true;
	}

	private Ray constructingReflectedRay(Vector n, Vector v, GeoPoint geopoint) {
		Vector r = v.subtract(n.scale(n.dotProduct(v) * 2));
		return new Ray(geopoint.point, r, n);
	}

	private Ray constructingRefractedRay(Vector n, Vector v, GeoPoint geopoint) {
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

	private double transparency(LightSource ls, Vector l, Vector n, GeoPoint geoPoint) {
		Vector lightDirection = l.scale(-1); // from point to light source
		Ray lightRay = new Ray(geoPoint.point, lightDirection, n);
		List<GeoPoint> intersections = scene.geometries.findGeoIntersections(lightRay);
		if (intersections == null)
			return 1.0;
		double ktr = 1.0;
		double lightDistance = ls.getDistance(geoPoint.point);
		for (GeoPoint gp : intersections) {
			if (Util.alignZero(gp.point.distance(geoPoint.point) - lightDistance) <= 0) {
				ktr *= gp.geometry.getMaterial().kT;
				if (ktr < MIN_CALC_COLOR_K)
					return 0.0;
			}
		}
		return ktr;
	}
}
