package geometries;

import primitives.Ray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Geometries implements Intersectable {
    private List<Intersectable> list;

    public Geometries() {
        this.list = new ArrayList<>();
    }

    public Geometries(Intersectable... geometries) {
        this.list = Arrays.asList(geometries);
    }

    public void add(Intersectable... geometries) {
        this.list.addAll(Arrays.asList(geometries));
    }
/**
 * Returns all cuts with the shapes and also the name of the shape
 */
    @Override
	public List<GeoPoint> findGeoIntersections(Ray ray) {
    	List<GeoPoint> result = new ArrayList<>();
        for (Intersectable intersectable: list) {
            List<GeoPoint> temp = intersectable.findGeoIntersections(ray);
            if(temp != null)
                result.addAll(temp);
        }

        if(result.size() > 0)
            return result;
        else
            return null;
	}

}
