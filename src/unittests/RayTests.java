package unittests;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import primitives.Point3D;
import primitives.Ray;
import primitives.Vector;

public class RayTests {
	@Test
	public void findClosestPointTest() {
		Ray ray=new Ray(Point3D.ZERO, new Vector(1,1,1));
		List<Point3D> list=new ArrayList<Point3D>();
		
		// ============ Equivalence Partitions Tests ==============
		
		// TC01: Point at the center of the list
		list.add(new Point3D(1,2,0));
	    list.add(new Point3D(1,0,0));
	    list.add(new Point3D(1,2,3));
	    assertEquals("the point is not the central point", new Point3D(1,0,0), ray.findClosestPoint(list));
	    
	    
	    // =============== Boundary Values Tests ==================
	    
	    // TC2: The list is empty
	    list.clear();
	    assertEquals("the list is not empty", null, ray.findClosestPoint(list));
	    
	    // TC3:  Point at the begging of the list
	    list.clear();
	    list.add(new Point3D(1,0,0));
		list.add(new Point3D(1,2,0));
	    list.add(new Point3D(1,2,3));
	    assertEquals("the point is not the first point", new Point3D(1,0,0), ray.findClosestPoint(list));
	    
	    
	    // TC4:  Point at the end of the list
	    list.clear();
		list.add(new Point3D(1,2,0));
	    list.add(new Point3D(1,2,3));
	    list.add(new Point3D(1,0,0));
	    assertEquals("the point is not the last point", new Point3D(1,0,0), ray.findClosestPoint(list));
	}
}
