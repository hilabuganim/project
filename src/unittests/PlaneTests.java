/**
 * 
 */
package unittests;

import static org.junit.Assert.*;

import org.junit.Test;

import geometries.Plane;
import primitives.Point3D;
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
        assertEquals("ERROR GetNormalPoint3D() is not the right normal ",plane.getNormal(new Point3D(1, 0, 0)), new Vector(0, 0, 1));
		
	}

}
