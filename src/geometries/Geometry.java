package geometries;

import primitives.Color;
import primitives.Material;
import primitives.Point3D;
import primitives.Vector;

public abstract class Geometry extends Intersectable {
	public abstract Vector getNormal(Point3D point);

	protected Color emmission = Color.BLACK;
	protected Material material=new Material(); //Retains details of the material from which the shape is made

	public Material getMaterial() { 
		return material;
	}

	public Geometry setMaterial(Material material) {
		this.material = material;
		return this;
	}

	public Color getEmmission() {
		return emmission;
	}

	public Geometry setEmmission(Color emmission) {
		this.emmission = emmission;
		return this;
	}
}
