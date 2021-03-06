/**
 *
 */
package unittests;

import elements.AmbientLight;
import elements.Camera;
import elements.PointLight;
import elements.SpotLight;
import geometries.Geometries;
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
        // Without Bounding Volume Hierarchy: 3 sec 761 ms
        // With Bounding Volume Hierarchy: 3 sec 680 ms
    	RayTracerBasic.RADIUS=0.01;
    	RayTracerBasic.NUM_OF_RAYS=0;
        Camera camera = new Camera(new Point3D(0, 0, 1000), new Vector(0, 0, -1), new Vector(0, 1, 0)) //
                .setViewPlaneSize(200, 200).setDistance(1000);

        scene.setAmbientLight(new AmbientLight(new Color(java.awt.Color.WHITE), 0.15));

        Triangle triangle1 = new Triangle(new Point3D(-400, 800, -12000), new Point3D(-400, 400, -12000), new Point3D(0, 600, -12000));
        Triangle triangle2 = new Triangle(new Point3D(400, 800, -12000), new Point3D(0, 600, -12000), new Point3D(400, 400, -12000));
        Triangle triangle3 = new Triangle(new Point3D(-400, 400, -12000), new Point3D(0, 600, -12000), new Point3D(0, 200, -12000));
        Triangle triangle4 = new Triangle(new Point3D(0, 600, -12000), new Point3D(0, 200, -12000), new Point3D(400, 400, -12000));
        Sphere sphere2=(Sphere) new Sphere(new Point3D(0, 700, -12000), 900).setEmmission(new Color(48, 45, 47)).setMaterial(new Material().setkT(0.9).setnShininess(60));; //big sphere
        Sphere sphere=(Sphere) new Sphere(new Point3D(450, -60, -5000), 100).setEmmission(new Color(0, 0, 100)).setMaterial(new Material().setkT(0.1).setnShininess(60));; //right sphere
        Sphere sphere5=(Sphere) new Sphere(new Point3D(430, -80, -3500), 70).setEmmission(new Color(184, 58, 217)).setMaterial(new Material().setkR(0.5).setnShininess(60));; //big purple sphere
        Sphere sphere1=(Sphere) new Sphere(new Point3D(-370, -100, -4000), 100).setEmmission(new Color(0, 0, 100)).setMaterial(new Material().setkR(0.5).setnShininess(60));; //left sphere
        Sphere sphere3=(Sphere) new Sphere(new Point3D(-350, -130, -3700), 50).setEmmission(new Color(68, 175, 217)).setMaterial(new Material().setkR(0.8).setnShininess(60));; //light blue sphere
        Sphere sphere4=(Sphere) new Sphere(new Point3D(-335, -150, -3500), 25).setEmmission(new Color(107, 37, 145)).setMaterial(new Material().setkR(0.5).setnShininess(60));; //small purple sphere
        Plane plane=(Plane) new Plane(new Point3D(0, -200, -5200), new Vector(0, 1, 0)).setMaterial(new Material().setkR(0.3).setkS(0.5).setnShininess(60));

        Color[] colors = {new Color(68, 175, 217), new Color(184, 58, 217), new Color(52, 45, 182), new Color(107, 37, 145)};

        Geometries geometries = new Geometries(triangle1, triangle2, triangle3, triangle4, plane, sphere, sphere1, sphere2, sphere3, sphere4, sphere5);
        geometries.buildHierarchy(3, 1);

        triangle1.setEmmission(colors[0]);
        triangle2.setEmmission(colors[1]);
        triangle3.setEmmission(colors[2]);
        triangle4.setEmmission(colors[3]);
        scene.geometries.add(geometries);

        scene.lights.add(new SpotLight(new Color(700, 400, 400), new Point3D(60, 50, 0), new Vector(0, 0, -1), 1, 4E-5, 2E-7));
        scene.lights.add(new SpotLight(new Color(700, 400, 400), new Point3D(-370, -100,-3500), new Vector(0, 0, -1), 1, 0.00002, 0.00005));
        scene.lights.add(new PointLight(new Color(700, 400, 400), new Point3D(0, 1000, -12000), 1, 4E-5, 2E-7)); //main light
        //scene.lights.add(new SpotLight(new Color(700, 400, 400), new Point3D(-370, -100,-3500), new Vector(0, 0, -1), 1, 0.00002, 0.00005));


        ImageWriter imageWriter = new ImageWriter("jctLogo", 600, 600);
        Render render = new Render() //
                .setImageWriter(imageWriter) //
                .setCamera(camera) //
                .setRayTracerBase(new RayTracerBasic(scene))
                .setMultithreading(0).setDebugPrint();

        render.renderImage();
        render.writeToImage();
   

    }
    
//    @Test
//    public void test1() {
//        Scene scene = new Scene("Tst soft shadow");
//        Sphere sphere = new Sphere(new Point3D(0.0, 0, -5000), 350);
//        sphere.setMaterial(new Material().setkD(1).setkS(1).setnShininess(20));
//        sphere.setEmmission(new Color(0, 0, 100));
//        scene.geometries.add(sphere);
//
//        sphere = new Sphere(new Point3D(-800, 0, -5000), 350);
//        sphere.setEmmission(new Color(100, 0, 5));
//        scene.geometries.add(sphere);
//
//        sphere = new Sphere(new Point3D(800, 0, -5000), 350);
//        sphere.setEmmission(new Color(28, 100, 0));
//        scene.geometries.add(sphere);
//
//
//        scene.lights.add(new SpotLight(new Color(17, 17, 17), new Point3D(0, 1000, -4500), new Vector(-2, -2, -3), 0, 0.000001, 0.0000005));
//
//        Plane plane = new Plane(new Point3D(0, -400, 0), new Vector(0, 1, 0));
//        plane.setEmmission(new Color(0, 0, 0));
//        plane.setMaterial(new Material().setkR(0.4).setkS(0));
//        scene.geometries.add(plane);
//
//            scene.lights.add(new SpotLight(new Color(171, 171, 171), new Point3D(0, 1000, -4000), new Vector(-2, -2, -3), 0, 0.000001, 0.0000005));
//
//            ImageWriter imageWriter = new ImageWriter("soft shadow", 500, 500);
//
//
//            Render render = new Render() //
//                    .setImageWriter(imageWriter) //
//                    .setCamera((new Camera(new Point3D(0, 0, 1000), new Vector(-1, 0, 0), new Vector(0, 0, -1)).setDistance(1000).setViewPlaneSize(500, 500))) //
//                    .setRayTracerBase(new RayTracerBasic(scene))
//                    .setMultithreading(0);
//
//            render.renderImage();
//            render.writeToImage();
//
//    }
    @Test
    public void softShadow() {
    	RayTracerBasic.RADIUS=0.03;
    	RayTracerBasic.NUM_OF_RAYS=50;
        Camera camera = new Camera(new Point3D(0, 0, 1000), new Vector(0, 0, -1), new Vector(0, 1, 0)) //
                .setViewPlaneSize(200, 200).setDistance(1000);

        double pos1 = 50, pos2 = -35;

        scene.geometries.add(

                new Sphere(new Point3D(50, pos2 + 30, 0), 30)
                        .setEmmission(new Color(java.awt.Color.BLUE))
                        .setMaterial(new Material().setkD(0.8).setkS(1).setkR(0.2).setnShininess(30)),
                new Sphere(new Point3D(-50, pos2+10, 0), 10)
                        .setEmmission(new Color(java.awt.Color.RED))
                        .setMaterial(new Material().setkD(0.8).setkS(1).setkR(0.2).setnShininess(30)),
                new Sphere(new Point3D(-10, pos2 + 20, 0), 20)
                        .setEmmission(new Color(0, 100, 0))
                        .setMaterial(new Material().setkD(0.8).setkS(1).setkR(0.2).setnShininess(30)),

                new Plane(new Point3D(0, pos2, -50), new Vector(0, 1, 0))
                        .setEmmission(new Color(50, 50, 50))
                        .setMaterial(new Material().setkD(0.2).setkS(0.2).setnShininess(30).setkR(0.2))
        );

        scene.lights.add(new PointLight(new Color(300, 300, 300), new Point3D(0, pos1, -200), 1, 0.00005, 0.000005));

        ImageWriter imageWriter = new ImageWriter("temp", 600, 600);
        Render render = new Render()
                .setImageWriter(imageWriter)
                .setCamera(camera)
                .setRayTracerBase(new RayTracerBasic(scene));

        render.renderImage();
        render.writeToImage();
    }


}
