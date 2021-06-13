package KMeans;

import geometries.Intersectable;
import primitives.Point3D;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class KMeans {
    private static final int MAX_ITERATIONS = 100;
    private final List<Intersectable> shapes;
    private final List<Cluster> clusters;
    private final int numClusters;

    /**
     * A constructor who builds and calculates the groups by K-Means
     *
     * @param shapes      list of shapes to be divided into groups
     * @param numClusters the number of groups to divide
     */
    public KMeans(List<Intersectable> shapes, int numClusters) {
        this.numClusters = numClusters;
        this.shapes = shapes;
        this.clusters = new ArrayList<>();
        init();
        calculate();
    }

    /**
     * @return the clusters
     */
    public List<Cluster> getClusters() {
        return clusters;
    }

    /**
     * Initializes the process
     */
    public void init() {
        // create Clusters and set random centroids
        for (int i = 0; i < numClusters; i++) {
            Cluster cluster = new Cluster(i);
            cluster.setCentroid(shapes.get(new Random().nextInt(shapes.size() - 1)).getCenter());
            this.clusters.add(cluster);
        }
    }

    /**
     * The process to calculate the K Means, with iterating method.
     */
    public void calculate() {
        boolean finish = false;
        int iteration = 0;

        // add in new data, one at a time, recalculating centroids with each new one.
        while (!finish && iteration < MAX_ITERATIONS) {
            // clear cluster state
            clearClusters();
            List<Point3D> lastCentroids = getCentroids();

            // assign points to the closer cluster
            assignCluster();

            // calculate new centroids.
            calculateCentroids();
            iteration++;
            List<Point3D> currentCentroids = getCentroids();

            // calculates total distance between new and old Centroids
            double distance = 0;
            for (int i = 0; i < lastCentroids.size(); i++) {
                distance += lastCentroids.get(i).distance(currentCentroids.get(i));
            }

            // if everything is as before - finish
            if (distance == 0) {
                finish = true;
            }
        }
    }

    /**
     * Clear all the clusters from shapes
     */
    private void clearClusters() {
        for (Cluster cluster : clusters) {
            cluster.clear();
        }
    }

    /**
     * Get the list of all the centroids from all clusters
     *
     * @return the list of centroids Point3D
     */
    private List<Point3D> getCentroids() {
        List<Point3D> centroids = new ArrayList<>(numClusters);
        for (Cluster cluster : clusters) {
            Point3D point = cluster.getCentroid();
            centroids.add(point);
        }
        return centroids;
    }

    /**
     * Assign each one of the shapes into the closest cluster
     */
    private void assignCluster() {
        double max = Double.MAX_VALUE, min, distance;
        int cluster = 0;

        for (Intersectable intersectable : shapes) {
            min = max;
            for (int i = 0; i < numClusters; i++) {
                Cluster c = clusters.get(i);
                distance = intersectable.getCenter().distance(c.getCentroid());
                if (distance < min) {
                    min = distance;
                    cluster = i;
                }
            }
            clusters.get(cluster).addPoint(intersectable);
        }
    }

    /**
     * Recalculate the centroids points of each cluster
     */
    private void calculateCentroids() {
        for (Cluster cluster : clusters) {
            double sumX = 0;
            double sumY = 0;
            double sumZ = 0;
            List<Intersectable> listPoints = cluster.getGeometries();
            int n_points = listPoints.size();

            for (Intersectable intersectable : listPoints) {
                sumX += intersectable.getCenter().getX();
                sumY += intersectable.getCenter().getY();
                sumZ += intersectable.getCenter().getZ();
            }

            if (n_points > 0) {
                double newX = sumX / n_points;
                double newY = sumY / n_points;
                double newZ = sumZ / n_points;
                Point3D newCentroid = new Point3D(newX, newY, newZ);
                cluster.setCentroid(newCentroid);
            }
        }
    }
}