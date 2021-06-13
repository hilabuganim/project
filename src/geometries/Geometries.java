package geometries;

import KMeans.Cluster;
import KMeans.KMeans;
import primitives.Point3D;
import primitives.Ray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Geometries extends Intersectable {
    private List<Intersectable> list;

    public Geometries() {
        this.list = new ArrayList<>();
        this.box = createBox();
    }

    public Geometries(Intersectable... geometries) {
        this.list = Arrays.asList(geometries);
        this.box = createBox();
    }

    public void add(Intersectable... geometries) {
        this.list.addAll(Arrays.asList(geometries));
        this.box = createBox();
    }

    /**
     * Split the list of shapes into a hierarchy by K-Means
     *
     * @param clusters The maximum number of groups in each layer in the hierarchy tree
     * @param depth    Maximum hierarchy tree depth
     */
    public void buildHierarchy(int clusters, int depth) {
        KMeans kMeans = new KMeans(this.list, clusters);
        this.list = new ArrayList<>();
        for (Cluster cluster : kMeans.getClusters()) {
            if (cluster.getGeometries().size() > 0) {
                Geometries geo = new Geometries();
                for (Intersectable intersectable : cluster.getGeometries())
                    geo.add(intersectable);
                this.list.add(geo);
            }
        }
        if (depth > 1)
            buildHierarchy(clusters, --depth);
    }

    @Override
    public Point3D getCenter() {
        return new Point3D(box.x0 + box.x1 / 2, box.y0 + box.y1 / 2, box.z0 + box.z1 / 2);
    }

    /**
     * Returns all cuts with the shapes and also the name of the shape
     */
    @Override
    public List<GeoPoint> findGeoIntersections(Ray ray) {
        if (!box.intersectionBox(ray))
            return null;

        List<GeoPoint> result = new ArrayList<>();
        for (Intersectable intersectable : list) {
            List<GeoPoint> temp = intersectable.findGeoIntersections(ray);
            if (temp != null)
                result.addAll(temp);
        }

        if (result.size() > 0)
            return result;
        else
            return null;
    }

    private Box createBox() {
        double x1 = Double.NEGATIVE_INFINITY;
        double x0 = Double.POSITIVE_INFINITY;
        double y1 = Double.NEGATIVE_INFINITY;
        double y0 = Double.POSITIVE_INFINITY;
        double z1 = Double.NEGATIVE_INFINITY;
        double z0 = Double.POSITIVE_INFINITY;
        for (Intersectable geo : list) {
            if (geo.box.getX0() < x0) x0 = geo.box.getX0();
            if (geo.box.getX1() > x1) x1 = geo.box.getX1();
            if (geo.box.getY0() < y0) y0 = geo.box.getY0();
            if (geo.box.getY1() > y1) y1 = geo.box.getY1();
            if (geo.box.getZ0() < z0) z0 = geo.box.getZ0();
            if (geo.box.getZ1() > z1) z1 = geo.box.getZ1();
        }
        return new Box(x0, x1, y0, y1, z0, z1);
    }

}
