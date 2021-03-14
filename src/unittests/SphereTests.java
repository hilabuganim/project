/**
 * 
 */
package unittests;

import static org.junit.Assert.*;

import org.junit.Test;

import geometries.Sphere;
import primitives.Point3D;
import primitives.Vector;

/**
 * @author hilab
 *
 */
public class SphereTests {

	/**
	 * Test method for {@link geometries.Sphere#getNormal(primitives.Point3D)}.
	 */
	@Test
	public void testGetNormal() {
		Point3D point=new Point3D(1,1,1);
		double radius=5;
		Sphere sphere=new Sphere(point, radius);
		assertEquals("ERROR GetNormal() is not the right normal",sphere.getNormal(new Point3D(1,0,0)), new Vector(0,-1,-1).normalized());
	}

}
