package unittests;

import static org.junit.Assert.*;

import java.util.List;

import geometries.*;
import org.junit.Test;

import elements.Camera;
import geometries.Plane;
import geometries.Sphere;
import geometries.Triangle;
import primitives.Point3D;
import primitives.Ray;
import primitives.Vector;

public class ViewPlaneTests {
    final int WIDTH = 3;
    final int HEIGHT = 3;

    /**
     * A function that returns the amount of intersections between the shape and the camera
     * @param camera The camera with which you want to check the intersection
     * @param intersectable The shape with which you want to test the intersection
     * @return Number of intersections
     */
    private int getNumberOfIntersections(Camera camera, Intersectable intersectable) {
        int intersections = 0;
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                Ray ray = camera.constructRayThroughPixel(WIDTH, HEIGHT, j, i);
                List<Point3D> intersectionsList = intersectable.findIntsersections(ray);
                if (intersectionsList != null)
                    intersections += intersectionsList.size();
            }
        }
        return intersections;
    }

    @Test
    public void PlaneIntersectionPointsTest() {
        // First test case, 9 intersection points
        Camera camera = new Camera(new Point3D(0.0, 0.0, 0.0), new Vector(0.0, 0.0, -1.0), new Vector(0.0, 1.0, 0.0))
                .setDistance(1).setViewPlaneSize(WIDTH, HEIGHT);
        Plane plane = new Plane(new Point3D(0, 0, -3), new Vector(0.0, 0.0, -1.0));
        assertEquals(9, getNumberOfIntersections(camera, plane));

        // Second test case, 9 intersection points
        plane = new Plane(new Point3D(0, 0, -3), new Vector(0.0, 0.5, -1.0));
        assertEquals(9, getNumberOfIntersections(camera, plane));

        // Third test case, 6 intersection points
        plane = new Plane(new Point3D(0, 0, -3), new Vector(0.0, 1, -1.0));
        assertEquals(6, getNumberOfIntersections(camera, plane));
    }


    /**
     * Sphere test
     */
    @Test
    public void SphereIntersectionPointsTest() {
        // First test case, 2 intersection points
        Camera camera = new Camera(new Point3D(0.0, 0.0, 0.0), new Vector(0.0, 0.0, -1.0), new Vector(0.0, 1.0, 0.0))
                .setDistance(1).setViewPlaneSize(WIDTH, HEIGHT);
        Sphere sphere = new Sphere(new Point3D(0, 0, -3), 1);
        assertEquals(2, getNumberOfIntersections(camera, sphere));

        // Second test case, 18 intersection points
        camera = new Camera(new Point3D(0.0, 0.0, 0.5), new Vector(0.0, 0.0, -1.0), new Vector(0.0, 1.0, 0.0))
                .setDistance(1).setViewPlaneSize(WIDTH, HEIGHT);
        sphere = new Sphere(new Point3D(0, 0, -2.5), 2.5);
        assertEquals(18, getNumberOfIntersections(camera, sphere));

        // Third test case, 9 intersection points
        sphere = new Sphere(new Point3D(0, 0, 0), 4);
        assertEquals(9, getNumberOfIntersections(camera, sphere));

        // Fourth test case, 0 intersection points
        sphere = new Sphere(new Point3D(0, 0, 1), 0.5);
        assertEquals(0, getNumberOfIntersections(camera, sphere));
    }


    // *** Triangle test **
    @Test
    public void TriangleIntersectionPointsTest() {
        // First test case, 1 intersection points
        Camera camera = new Camera(new Point3D(0.0, 0.0, 0.0), new Vector(0.0, 0.0, -1.0), new Vector(0.0, 1.0, 0.0))
                .setDistance(1).setViewPlaneSize(WIDTH, HEIGHT);
        Triangle triangle = new Triangle(new Point3D(0, 1, -2), new Point3D(1, -1, -2), new Point3D(-1, -1, -2));
        assertEquals(1, getNumberOfIntersections(camera, triangle));

        // Second test case, 2 intersection points
        triangle = new Triangle(new Point3D(0, 20, -2), new Point3D(1, -1, -2), new Point3D(-1, -1, -2));
        assertEquals(2, getNumberOfIntersections(camera, triangle));
    }


}
