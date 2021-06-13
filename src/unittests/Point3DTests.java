/**
 * 
 */
package unittests;
import primitives.Point3D;
import primitives.Vector;

import static org.junit.Assert.*;


import org.junit.Test;

/**
 * @author hilab
 *
 */
public class Point3DTests {

	/**
	 * Test method for {@link Point3D#add(Vector)}.
	 */
	@Test
	public void testAdd() {
		Point3D point1= new Point3D(1,2,3);
		Point3D point2=Point3D.ZERO;
		assertEquals("ERROR add() result is not the correct point", point2.add(new Vector(point1)), point1);
		
	}

	/**
	 * Test method for {@link Point3D#subtract(Point3D)}.
	 */
	@Test
	public void testSubtract() {
		Point3D point1= new Point3D(1,2,3);
		Point3D point2=Point3D.ZERO;
		assertEquals("ERROR Subtract() result is not the correct point", point2.subtract(point1), new Vector(-1, -2, -3));
		
		
	}

	/**
	 * Test method for {@link Point3D#distanceSquarde(Point3D)}.
	 */
	@Test
	public void testDistanceSquarde() {
		Point3D point1= new Point3D(1,1,1);
		Point3D point2= new Point3D(2,2,2);
		assertEquals("ERROR DistanceSquarde() result is not the correct number", point2.distanceSquarde(point1), 3 , 0.00001);
		
	}

	/**
	 * Test method for {@link Point3D#distance(Point3D)}.
	 */
	@Test
	public void testDistance() {
		Point3D point1= new Point3D(1,1,1);
		Point3D point2= new Point3D(2,2,2);
		assertEquals("ERROR Distance() result is not the correct number", point2.distance(point1), Math.sqrt(3) , 0.00001);
		
	}

}
