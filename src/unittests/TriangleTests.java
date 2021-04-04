/**
 * 
 */
package unittests;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import geometries.Triangle;
import primitives.Point3D;
import primitives.Ray;
import primitives.Vector;

/**
 * @author hilab
 *
 */
public class TriangleTests {

	/**
	 * Test method for {@link geometries.Polygon#getNormal(primitives.Point3D)}.
	 * Creates a triangle by three points and checks what the vector 
	 * at a given point expects to get the required vector
	 */
	@Test
	public void testGetNormal() {
		Point3D point1=new Point3D(10,0,0);
		Point3D point2=new Point3D(0,10,0);
		Point3D point3=new Point3D(0,0,10);
		
		Triangle triangle=new Triangle(point1, point2, point3);
		assertEquals("ERROR GetNormal() is not the right normal", triangle.getNormal(point1), new Vector(10, 10, 10).normalize());
	}
	
	/**
	 * Test method for {@link geometries.Triangle#findIntersections(primitives.Ray)}.
	 */
	@Test
	public void testFindIntersections() {
		Triangle triangle = new Triangle(new Point3D(0, 3, 0), new Point3D(0, 0, 0), new Point3D(3, 0, 0));

		// ============ Equivalence Partitions Tests ==============

		// TC01: Ray's line Inside polygon/triangle
		Ray ray = new Ray(new Point3D(1, 1, -1), new Vector(0, 0, 1));
		List<Point3D> result = triangle.findIntsersections(ray);
		Point3D p1 = new Point3D(1, 1, 0);
		assertEquals("Wrong number of points", 1, result.size());
		assertEquals("Ray crosses triangle", List.of(p1), result);

		ray = new Ray(new Point3D(0.9, 0.5, -1), new Vector(0, 0, 1));
		result = triangle.findIntsersections(ray);
		p1 = new Point3D(0.9, 0.5, 0);
		assertEquals("Wrong number of points", 1, result.size());
		assertEquals("Ray crosses triangle", List.of(p1), result);

		// TC02: Ray's line Outside against edge
		ray = new Ray(new Point3D(-1, 2, -1), new Vector(0, 0, 1));
		assertNull("Ray's line outside against edge", triangle.findIntsersections(ray));

		// TC03: Ray's line Outside against vertex
		ray = new Ray(new Point3D(-1, -2, -1), new Vector(0, 0, 1));
		assertNull("Ray's line outside against vertex", triangle.findIntsersections(ray));


		// =============== Boundary Values Tests ==================

		// TC04: Ray's line On edge
		ray = new Ray(new Point3D(1, 0, -1), new Vector(0, 0, 1));
		assertNull("Ray's line outside against vertex", triangle.findIntsersections(ray));

		// TC05: Ray's line In vertex
		ray = new Ray(new Point3D(3, 0, -1), new Vector(0, 0, 1));
		assertNull("Ray's line outside against vertex", triangle.findIntsersections(ray));

		// TC06: Ray's line On edge's continuation
		ray = new Ray(new Point3D(-1, 0, -1), new Vector(0, 0, 1));
		assertNull("Ray's line outside against vertex", triangle.findIntsersections(ray));
	}

}
