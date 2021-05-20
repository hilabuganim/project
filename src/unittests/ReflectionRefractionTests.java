/**
 *
 */
package unittests;

import elements.AmbientLight;
import elements.Camera;
import elements.PointLight;
import elements.SpotLight;
import geometries.Plane;
import geometries.Sphere;
import geometries.Triangle;
import org.junit.Test;
import primitives.Color;
import primitives.Material;
import primitives.Point3D;
import primitives.Vector;
import renderer.ImageWriter;
import renderer.RayTracerBasic;
import renderer.Render;
import scene.Scene;

/**
 * Tests for reflection and transparency functionality, test for partial shadows
 * (with transparency)
 *
 * @author dzilb
 */
public class ReflectionRefractionTests {
    private Scene scene = new Scene("Test scene");

    /**
     * Produce a picture of a sphere lighted by a spot light
     */
    @Test
    public void twoSpheres() {
        Camera camera = new Camera(new Point3D(0, 0, 1000), new Vector(0, 0, -1), new Vector(0, 1, 0)) //
                .setViewPlaneSize(150, 150).setDistance(1000);

        scene.geometries.add( //
                new Sphere(new Point3D(0, 0, -50), 50) //
                        .setEmmission(new Color(java.awt.Color.BLUE)) //
                        .setMaterial(new Material().setkD(0.4).setkS(0.3).setnShininess(100).setkT(0.3)),
                new Sphere(new Point3D(0, 0, -50), 25) //
                        .setEmmission(new Color(java.awt.Color.RED)) //
                        .setMaterial(new Material().setkD(0.5).setkS(0.5).setnShininess(100)));
        scene.lights.add( //
                new SpotLight(new Color(1000, 600, 0), new Point3D(-100, -100, 500), new Vector(-1, -1, -2), 1, 0.0004, 0.0000006));

        Render render = new Render() //
                .setImageWriter(new ImageWriter("refractionTwoSpheres", 500, 500)) //
                .setCamera(camera) //
                .setRayTracerBase(new RayTracerBasic(scene));
        render.renderImage();
        render.writeToImage();
    }

    /**
     * Produce a picture of a sphere lighted by a spot light
     */
    @Test
    public void twoSpheresOnMirrors() {
        Camera camera = new Camera(new Point3D(0, 0, 10000), new Vector(0, 0, -1), new Vector(0, 1, 0)) //
                .setViewPlaneSize(2500, 2500).setDistance(10000); //

        scene.setAmbientLight(new AmbientLight(new Color(255, 255, 255), 0.1));

        scene.geometries.add( //
                new Sphere(new Point3D(-950, -900, -1000), 400) //
                        .setEmmission(new Color(0, 0, 100)) //
                        .setMaterial(new Material().setkD(0.25).setkS(0.25).setnShininess(20).setkT(0.5)),
                new Sphere(new Point3D(-950, -900, -1000), 200) //
                        .setEmmission(new Color(100, 20, 20)) //
                        .setMaterial(new Material().setkD(0.25).setkS(0.25).setnShininess(20)),
                new Triangle(new Point3D(1500, -1500, -1500), new Point3D(-1500, 1500, -1500),
                        new Point3D(670, 670, 3000)) //
                        .setEmmission(new Color(20, 20, 20)) //
                        .setMaterial(new Material().setkR(1)),
                new Triangle(new Point3D(1500, -1500, -1500), new Point3D(-1500, 1500, -1500),
                        new Point3D(-1500, -1500, -2000)) //
                        .setEmmission(new Color(20, 20, 20)) //
                        .setMaterial(new Material().setkR(0.5)));

        scene.lights.add(new SpotLight(new Color(1020, 400, 400), new Point3D(-750, -750, -150), new Vector(-1, -1, -4), 1, 0.00001, 0.000005));

        ImageWriter imageWriter = new ImageWriter("reflectionTwoSpheresMirrored", 500, 500);
        Render render = new Render() //
                .setImageWriter(imageWriter) //
                .setCamera(camera) //
                .setRayTracerBase(new RayTracerBasic(scene));

        render.renderImage();
        render.writeToImage();
    }

    /**
     * Produce a picture of a two triangles lighted by a spot light with a partially
     * transparent Sphere producing partial shadow
     */
    @Test
    public void trianglesTransparentSphere() {
        Camera camera = new Camera(new Point3D(0, 0, 1000), new Vector(0, 0, -1), new Vector(0, 1, 0)) //
                .setViewPlaneSize(200, 200).setDistance(1000);

        scene.setAmbientLight(new AmbientLight(new Color(java.awt.Color.WHITE), 0.15));

        scene.geometries.add( //
                new Triangle(new Point3D(-150, -150, -115), new Point3D(150, -150, -135), new Point3D(75, 75, -150)) //
                        .setMaterial(new Material().setkD(0.5).setkS(0.5).setnShininess(60)), //
                new Triangle(new Point3D(-150, -150, -115), new Point3D(-70, 70, -140), new Point3D(75, 75, -150)) //
                        .setMaterial(new Material().setkD(0.5).setkS(0.5).setnShininess(60)), //
                new Sphere(new Point3D(60, 50, -50), 30) //
                        .setEmmission(new Color(java.awt.Color.BLUE)) //
                        .setMaterial(new Material().setkD(0.2).setkS(0.2).setnShininess(30).setkT(0.6)));

        scene.lights.add(new SpotLight(new Color(700, 400, 400), new Point3D(60, 50, 0), new Vector(0, 0, -1), 1, 4E-5, 2E-7));

        ImageWriter imageWriter = new ImageWriter("refractionShadow", 600, 600);
        Render render = new Render() //
                .setImageWriter(imageWriter) //
                .setCamera(camera) //
                .setRayTracerBase(new RayTracerBasic(scene));

        render.renderImage();
        render.writeToImage();
    }
    @Test
    public void jctLogo() {
        Camera camera = new Camera(new Point3D(0, 0, 1000), new Vector(0, 0, -1), new Vector(0, 1, 0)) //
                .setViewPlaneSize(200, 200).setDistance(1000);

        scene.setAmbientLight(new AmbientLight(new Color(java.awt.Color.WHITE), 0.15));
        Triangle triangle1 = new Triangle(new Point3D(-400, 400, -8000), new Point3D(-400, 0, -8000), new Point3D(0, 200, -8000));
        Triangle triangle2 = new Triangle(new Point3D(400, 400, -8000), new Point3D(0, 200, -8000), new Point3D(400, 0, -8000));
        Triangle triangle3 = new Triangle(new Point3D(-400, 0, -8000), new Point3D(0, 200, -8000), new Point3D(0, -200, -8000));
        Triangle triangle4 = new Triangle(new Point3D(0, 200, -8000), new Point3D(0, -200, -8000), new Point3D(400, 0, -8000));
        Plane plane=(Plane) new Plane(new Point3D(0, -200, -5200), new Vector(0, 1, 0)).setMaterial(new Material().setkR(0.3).setkS(0.5).setnShininess(60));
        Sphere sphere=(Sphere) new Sphere(new Point3D(470, -100, -5000), 100).setEmmission(new Color(0, 0, 100)).setMaterial(new Material().setkR(0.9).setkT(0.1).setnShininess(60));;
        Sphere sphere1=(Sphere) new Sphere(new Point3D(-370, -100, -4000), 100).setEmmission(new Color(0, 0, 100)).setMaterial(new Material().setkT(1).setkR(0.5).setnShininess(60));;
        Color[] colors = {new Color(68, 175, 217), new Color(184, 58, 217), new Color(52, 45, 182), new Color(107, 37, 145)};

        triangle1.setEmmission(colors[0]);
        triangle2.setEmmission(colors[1]);
        triangle3.setEmmission(colors[2]);
        triangle4.setEmmission(colors[3]);
        scene.geometries.add(triangle1, triangle2, triangle3, triangle4, plane, sphere, sphere1); //
                

        scene.lights.add(new SpotLight(new Color(700, 400, 400), new Point3D(60, 50, 0), new Vector(0, 0, -1), 1, 4E-5, 2E-7));
        //scene.lights.add(new PointLight(new Color(700, 400, 400), new Point3D(400, 200,-4000), 1, 4E-5, 2E-7));
        scene.lights.add(new PointLight(new Color(700, 400, 400), new Point3D(-100, 100, -7000), 1, 4E-5, 2E-7));


        ImageWriter imageWriter = new ImageWriter("jctLogo", 600, 600);
        Render render = new Render() //
                .setImageWriter(imageWriter) //
                .setCamera(camera) //
                .setRayTracerBase(new RayTracerBasic(scene));

        render.renderImage();
        render.writeToImage();
   

    }
}
