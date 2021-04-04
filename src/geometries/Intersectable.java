/**
 * 
 */
package geometries;

import java.util.List;

import primitives.Point3D;
import primitives.Ray;

/**
 * @author hilab
 *
 */
public interface Intersectable {
	List<Point3D> findIntsersections(Ray ray);

}
