/**
 * 
 */
package unittests;
import primitives.*;
import static org.junit.Assert.*;

import org.junit.Test;

/**
 * @author hilab
 *
 */
public class VectorTests {
	 Vector v1 = new Vector(1, 2, 3);
     Vector v2 = new Vector(-2, -4, -6);
     Vector v3 = new Vector(0, 3, -2);

	/**
	 * Test method for {@link primitives.Vector#normalize()}.
	 * The test checks if a vector is equal to the same normal vector
	 * The test checks if the vector after normalization is equal to the unit vector
	 */
	@Test
	public void testNormalize() {
		// ============ Equivalence Partitions Tests ==============
		Vector v = new Vector(1, 2, 3);
        Vector vCopy = new Vector(v.getHead());
        Vector vCopyNormalize = vCopy.normalize();
        assertTrue("ERROR: normalize() function creates a new vector", vCopy == vCopyNormalize);
        assertEquals("ERROR: normalize() result is not a unit vector", vCopyNormalize.length(), 1, 0.00001);
	}

	/**
	 * Test method for {@link primitives.Vector#normalized()}.
	 * The test checks if the normalized vector is a new vector
       and expect to get them equal then give false
       The test checks if the vector u is equal to the unit vector
	 */
	@Test
	public void testNormalized() {
		Vector v = new Vector(1, 2, 3);
		Vector u = v.normalized();
		assertFalse("ERROR: normalizated() function does not create a new vector", u == v);
		assertEquals(u.length(), 1, 0.00001);
	}

	/**
	 * Test method for {@link primitives.Vector#add(primitives.Vector)}.
	 * A test that checks if adding vectors returns a result as required
	 * excepted to get zero point
	 */
	@Test
	public void testAdd() {
		Point3D p1 = new Point3D(1, 2, 3);
		assertEquals("ERROR: Point + Vector does not work correctly", p1.add(new Vector(-1, -2, -3)), Point3D.ZERO);
	}

	/**
	 * Test method for {@link primitives.Vector#subtract(primitives.Vector)}.
	 *A test that gets a point and calculates the subtraction between two vectors
	 and checks if its correct
	 */
	@Test
	public void testSubtract() {
		Point3D p1 = new Point3D(1, 2, 3);
		assertEquals("ERROR: Point - Point does not work correctly", new Point3D(2, 3, 4).subtract(p1), new Vector(1, 1, 1));
	}

	/**
	 * Test method for {@link primitives.Vector#scale(double)}.
	 * The vector recipient multiplies it by a scalar 
	 * and checks if the result is indeed as expected
	 */
	@Test
	public void testScale() {
		Vector v1 = new Vector(1, 1, 1);
        assertEquals(new Vector(2, 2, 2), v1.scale(2));
        Vector v4 = new Vector(1, 2, 3);
        assertEquals(new Vector(10, 20, 30), v4.scale(10));
	}

	/**
	 * Test method for {@link primitives.Vector#crossProduct(primitives.Vector)}.
	 * Vector multiplication between vectors Expects to get the length of a new vector
	 */
	@Test
	public void testCrossProduct() {
	        Vector v1 = new Vector(1, 2, 3);
	        Vector v2 = new Vector(-2, -4, -6);

	        // ============ Equivalence Partitions Tests ==============
	        Vector v3 = new Vector(0, 3, -2);
	        Vector vr = v1.crossProduct(v3);

	        // Test that length of cross-product is proper (orthogonal vectors taken for simplicity)
	        assertEquals("crossProduct() wrong result length", v1.length() * v3.length(), vr.length(), 0.00001);

	        // Test cross-product result orthogonality to its operands
	        assertTrue("crossProduct() result is not orthogonal to 1st operand", vr.dotProduct(v1) == 0);
	        assertTrue("crossProduct() result is not orthogonal to 2nd operand", vr.dotProduct(v3) == 0);

	        // =============== Boundary Values Tests ==================
	        // test zero vector from cross-productof co-lined vectors
	        try {
	            v1.crossProduct(v2);
	            fail("crossProduct() for parallel vectors does not throw an exception");
	        } catch (Exception e) {}
	    }

	/**
	 * Test method for {@link primitives.Vector#dotProduct(primitives.Vector)}.
	 * A scalar product between vectors expects to get a zero result
	 * Scalar multiplication between vectors expects to get the required result
	 */
	@Test
	public void testDotProduct() {
	assertEquals("ERROR: dotProduct() for orthogonal vectors is not zero", v1.dotProduct(v3), 0, 0.00001);
	assertEquals("ERROR: dotProduct() wrong value", v1.dotProduct(v2), -28, 0.00001);
	}

	/**
	 * Test method for {@link primitives.Vector#lengthSquared()}.
	 * A square-length function between vectors expects to get the required result
	 */
	@Test
	public void testLengthSquared() {
		assertEquals("ERROR: lengthSquared() wrong value", v1.lengthSquared(), 14, 0.00001);
	}

	/**
	 * Test method for {@link primitives.Vector#length()}.
	 * The length function between vectors expects to get a result as required
	 */
	@Test
	public void testLength() {
		Vector v = new Vector(0, 3, 4);
		assertEquals("ERROR: length() wrong value", v.length(), 5, 0.00001);
	}

}
