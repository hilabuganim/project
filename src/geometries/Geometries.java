package geometries;

import primitives.Point3D;
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

    @Override
    public List<Point3D> findIntsersections(Ray ray) {
        List<Point3D> result = new ArrayList<>();
        for (Intersectable intersectable: list) {
            List<Point3D> temp = intersectable.findIntsersections(ray);
            if(temp != null)
                result.addAll(temp);
        }

        if(result.size() > 0)
            return result;
        else
            return null;
    }
}
