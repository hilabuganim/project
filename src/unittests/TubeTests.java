/**
 * 
 */
package unittests;

import static org.junit.Assert.*;


import org.junit.Test;

import geometries.Tube;
import primitives.Point3D;
import primitives.Ray;
import primitives.Vector;

/**
 * @author hilab
 *
 */
public class TubeTests {

	/**
	 * Test method for {@link Tube#getNormal(Point3D)}.
	 * A test that gets a point and expects to get the normal of that point
	 */
	@Test
	public void testGetNormal() {
		// ============ Equivalence Partitions Tests ==============
		Ray ray=new Ray(new Point3D(1,1,1), new Vector(0,0,1));
		double radius=2;
		Tube tube= new Tube(ray, radius);
		assertEquals("ERROR GetNormal() is not the right normal", tube.getNormal(new Point3D(1,0,0)), new Vector(0,-1,0).normalized());
	}

}
