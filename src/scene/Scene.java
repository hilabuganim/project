package scene;

import java.util.LinkedList;
import java.util.List;

import elements.AmbientLight;
import elements.LightSource;
import geometries.Geometries;
import primitives.Color;

public class Scene {
	public List<LightSource> lights;
	public String name;
	public Color background = Color.BLACK;
	public AmbientLight ambientLight;
	public Geometries geometries;
	public Scene(String name) {
		super();
		this.name = name;
		this.geometries=new Geometries();
		this.lights=new LinkedList<LightSource>();
		this.ambientLight = new AmbientLight(Color.BLACK, 0);
	}

	public Scene setLights(List<LightSource> lights) {
		this.lights = lights;
		return this;
	}

	public Scene setBackground(Color background) {
		this.background = background;
		return this;
	}
	public Scene setAmbientLight(AmbientLight ambientLight) {
		this.ambientLight = ambientLight;
		return this;
	}
	public Scene setGeometries(Geometries geometries) {
		this.geometries = geometries;
		return this;
	}
}

