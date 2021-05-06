/**
 * 
 */
package geometries;

import java.util.List;
import java.util.stream.Collectors;

import primitives.Point3D;
import primitives.Ray;

/**
 * @author hilab
 *
 */
public interface Intersectable {
	default List<Point3D> findIntsersections(Ray ray) {
	    var geoList = findGeoIntersections(ray);
	    return geoList == null ? null
	                           : geoList.stream().map(gp -> gp.point).collect(Collectors.toList());
	}

	List<GeoPoint> findGeoIntersections(Ray ray);
	/**
	 * A class that represents pairs of geometry and dot
	 */
	public static class GeoPoint {
	    public Geometry geometry;
	    public Point3D point;
		public GeoPoint(Geometry geometry, Point3D point) {
			super();
			this.geometry = geometry;
			this.point = point;
		}
		
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			GeoPoint other = (GeoPoint) obj;
			if (geometry == null) {
				if (other.geometry != null)
					return false;
			} else if (!geometry.equals(other.geometry))
				return false;
			if (point == null) {
				if (other.point != null)
					return false;
			} else if (!point.equals(other.point))
				return false;
			return true;
		}
	}
	

}
