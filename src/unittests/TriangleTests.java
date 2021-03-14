/**
 * 
 */
package unittests;

import static org.junit.Assert.*;

import org.junit.Test;

import geometries.Triangle;
import primitives.Point3D;
import primitives.Vector;

/**
 * @author hilab
 *
 */
public class TriangleTests {

	/**
	 * Test method for {@link geometries.Polygon#getNormal(primitives.Point3D)}.
	 */
	@Test
	public void testGetNormal() {
		Point3D point1=new Point3D(10,0,0);
		Point3D point2=new Point3D(0,10,0);
		Point3D point3=new Point3D(0,0,10);
		
		Triangle triangle=new Triangle(point1, point2, point3);
		assertEquals("ERROR GetNormal() is not the right normal", triangle.getNormal(point1), new Vector(0,0,100));
	}

}
