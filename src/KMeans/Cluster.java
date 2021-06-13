package KMeans;

import geometries.Intersectable;
import primitives.Point3D;

import java.util.ArrayList;
import java.util.List;

public class Cluster {
    public List<Intersectable> geometries;
    public Point3D centroid;
    public int id;

    public Cluster(int id) {
        this.id = id;
        this.geometries = new ArrayList<>();
        this.centroid = null;
    }

    public List<Intersectable> getGeometries() {
        return geometries;
    }

    public void setGeometries(List<Intersectable> geometries) {
        this.geometries = geometries;
    }

    public void addPoint(Intersectable point) {
        geometries.add(point);
    }

    public Point3D getCentroid() {
        return centroid;
    }

    public void setCentroid(Point3D centroid) {
        this.centroid = centroid;
    }

    public void clear() {
        geometries.clear();
    }
}