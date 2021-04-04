/**
 * 
 */
package unittests;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import geometries.Plane;
import primitives.Point3D;
import primitives.Ray;
import primitives.Vector;

/**
 * @author hilab
 *
 */
public class PlaneTests {

	/**
	 * Test method for {@link geometries.Plane#getNormal(primitives.Point3D)}.
	 */
	@Test
	public void testGetNormalPoint3D() {
		Point3D point=new Point3D(1,1,1);
		Vector vector=new Vector(1,0,0);
		Plane plane=new Plane(point, vector);
		assertEquals("ERROR GetNormalPoint3D() is not the right normal ",plane.getNormal(point), new Vector(1,0,0));
		
        plane = new Plane(new Point3D(1,0,0), new Point3D(0,1,0), new Point3D(0,0,1));
        assertEquals("ERROR GetNormalPoint3D() is not the right normal ",plane.getNormal(new Point3D(1, 0, 0)), new Vector(1, 1, 1).normalize());
		
	}
	
	@Test
	public void testFindIntersections() {
		Plane plane = new Plane(new Point3D(0, 0, 1), new Vector(0, 0, 1));
		
		// ============ Equivalence Partitions Tests ==============

		// TC01: Ray's intersect the plane
		Ray ray = new Ray(new Point3D(0, 0, -1), new Vector(0, 1, 0.5));
		Point3D p1 = new Point3D(0, 4, 1);
		List<Point3D> result = plane.findIntsersections(ray);
		assertEquals("Wrong number of points", 1, result.size());
		assertEquals("Ray crosses plane", List.of(p1), result);
		
		// TC02: Ray's does not intersect the plane
		ray = new Ray(new Point3D(0, 0, 2), new Vector(0, 0, 0.5));
		assertNull("Ray's line out of plane", plane.findIntsersections(ray));
		
		
		// =============== Boundary Values Tests ==================

		// **** Group: Ray's paralall to the plane
		// TC03: Ray included in the plane (0 points)
		ray = new Ray(new Point3D(0, 0, 10), new Vector(1, 0, 0));
		assertNull("Ray's line out of plane", plane.findIntsersections(ray));
		
		// TC04: Ray not included in the plane (0 points)
		ray = new Ray(new Point3D(2, 0, 0), new Vector(1, 0, 0));
		assertNull("Ray's line out of plane", plane.findIntsersections(ray));
		
		
		// **** Group: Ray's orthogonal to the plane
		// TC05: Ray orthogonal before the plane (1 points)
		ray = new Ray(new Point3D(0, 0, -5), new Vector(0, 0, 1));
		result = plane.findIntsersections(ray);
		p1 = new Point3D(0, 0, 1);
		assertEquals("Wrong number of points", 1, result.size());
		assertEquals("Ray crosses plane", List.of(p1), result);
		
		// TC06: Ray orthogonal after the plane (0 points)
		ray = new Ray(new Point3D(0, 0, 5), new Vector(0, 0, 1));
		assertNull("Ray's line out of plane", plane.findIntsersections(ray));

		// TC07: Ray orthogonal in the plane (0 points)
		ray = new Ray(new Point3D(0, 4, 1), new Vector(0, 0, 2));
		assertNull("Ray's line out of plane", plane.findIntsersections(ray));
		
		
		// TC08: Ray is no orthogonal or parelall to the plane (0 points)
		ray = new Ray(new Point3D(0, 0, 10), new Vector(1, 1, 0));
		assertNull("Ray's line out of plane", plane.findIntsersections(ray));
		
		// TC09: Ray is no orthogonal or parelall to the plane (0 points)
		ray = new Ray(new Point3D(0, 0, 2), new Vector(1, 1, 0));
		assertNull("Ray's line out of plane", plane.findIntsersections(ray));
	}


}
