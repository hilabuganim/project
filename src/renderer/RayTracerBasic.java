package renderer;

import java.util.List;

import elements.LightSource;
import geometries.Intersectable.GeoPoint;
import primitives.Color;
import primitives.Point3D;
import primitives.Ray;
import primitives.Util;
import primitives.Vector;
import scene.Scene;

public class RayTracerBasic extends RayTracerBase {
	private static final double DELTA = 0.1;

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
		List<GeoPoint> list = scene.geometries.findGeoIntersections(ray);
		if (list == null) {
			return scene.background;
		}
		GeoPoint point = ray.getClosestGeoPoint(list);
		return calcColor(point, ray);
	}

	/**
	 * Returns the color in a dot
	 * 
	 * @param point
	 * @return color
	 */
	public Color calcColor(GeoPoint point, Ray ray) {
		return scene.ambientLight.getIntensity().add(point.geometry.getEmmission()).add(calcLocalEffects(point, ray));
	}

	/**
	 * Calculate by scattering and shining some glossy surface at the same point for
	 * each of the lights
	 * 
	 * @param intersection
	 * @param ray
	 * @return color
	 */
	private Color calcLocalEffects(GeoPoint intersection, Ray ray) {
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
				if (unshaded(l, n, intersection, lightSource)) {
					Color lightIntensity = lightSource.getIntensity(intersection.point);
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
		Vector delta = n.scale(n.dotProduct(lightDirection) > 0 ? DELTA : -DELTA);
		Point3D point = geopoint.point.add(delta);
		Ray lightRay = new Ray(point, lightDirection);
		List<GeoPoint> intersections = scene.geometries.findGeoIntersections(lightRay);
		if (intersections == null)
			return true;
		double lightDistance = light.getDistance(geopoint.point);
		for (GeoPoint gp : intersections) {
			if (Util.alignZero(gp.point.distance(geopoint.point) - lightDistance) <= 0)
				return false;
		}
		return true;

	}
}
