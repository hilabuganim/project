package unittests;

import geometries.Geometries;
import geometries.Plane;
import geometries.Sphere;
import geometries.Triangle;
import org.junit.Test;
import primitives.Point3D;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class GeometriesTests {

    @Test
    public void testFindIntersections() {
        Geometries geometries = new Geometries();

        // TC01: Empty body collection
        Ray ray = new Ray(new Point3D(0, 0, 2), new Vector(0, 0, 0.5));
        assertNull("Geometries is empty", geometries.findIntsersections(ray));

        geometries.add(new Sphere(new Point3D(10, 0, 0), 1),
                new Triangle(new Point3D(1, 0, 1), new Point3D(1, 1, -1), new Point3D(1, -1, -1)),
                new Plane(new Point3D(-5, 0, 0), new Vector(1, 0, 0)));

        // TC02: No geometry is cut
        ray = new Ray(new Point3D(0, 5, 0), new Vector(1, 1, 2));
        assertNull("Geometries is cut", geometries.findIntsersections(ray));

        // TC02: Only one geometry is cut
        ray = new Ray(new Point3D(-4, 0, 0), new Vector(-1, 0, 0));
        List<Point3D> result = geometries.findIntsersections(ray);
        assertEquals("Wrong number of points", 1, result.size());

        // TC03: few geometries is cut, but not all of them
        ray = new Ray(new Point3D(0.5, 0, 0), new Vector(1, 0, 0));
        result = geometries.findIntsersections(ray);
        assertEquals("Wrong number of points", 3, result.size());

        // TC04: all geometries is cut
        ray = new Ray(new Point3D(-6, 0, 0), new Vector(1, 0, 0));
        result = geometries.findIntsersections(ray);
        assertEquals("Wrong number of points", 4, result.size());
    }
}
