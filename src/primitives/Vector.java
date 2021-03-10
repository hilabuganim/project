package primitives;

public class Vector {
	Point3D head;
/**
 * Checks if the given vector is a zero vector and 
 * if so throws an exception if not set it
 * @param x
 * @param y
 * @param z
 */
	public Vector(Coordinate x, Coordinate y, Coordinate z) {
		Point3D point = new Point3D(x, y, z);
		if (point.equals(Point3D.ZERO))
			throw new IllegalArgumentException("zero vector");
		this.head = point;
	}
/**
 * Checks if the given vector is a zero vector and 
 * if so throws an exception if not set it
 * @param x
 * @param y
 * @param z
 */
	public Vector(double x, double y, double z) {
		Point3D point = new Point3D(x, y, z);
		if (point.equals(Point3D.ZERO))
			throw new IllegalArgumentException("zero vector");
		this.head = new Point3D(x, y, z);
	}

	public Vector(Point3D point) {
		this.head = point;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Vector other = (Vector) obj;
		if (head == null) {
			if (other.head != null)
				return false;
		} else if (!head.equals(other.head))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return head.toString();
	}
/**
 * Gets a vector normalized by a formula
 * @return Normalized vector
 */
	public Vector normalize() {
		this.head = scale(1 / length()).head;
		return this;
	}
/**
 * A function that use the previous function to normalize the vector
 * with the same direction
 * @return new vector
 */
	public Vector normalized() {
		return new Vector(head).normalize();
	}

	/**
	 * A function that add one vector to the other
	 * @param vector
	 * @return new vector
	 */
	public Vector add(Vector vector) {
		return new Vector(head.add(vector));
	}
/**
 * A function that subtract one vector from the other
 * @param vector
 * @return new vector
 */
	public Vector subtract(Vector vector) {
		return this.head.subtract(vector.head);
	}
/**
 * A function that doubles the vector in the scale
 * @param s
 * @return Vector after multiplication
 */
	public Vector scale(double s) {
		return new Vector(head.x.coord * s, head.y.coord * s, head.z.coord * s);
	}
/**
 * A function that makes a vector product by a formula
 * @param vector
 * @return new vector
 */
	public Vector crossProduct(Vector vector) {
		double Ax = this.head.x.coord;
		double Ay = this.head.y.coord;
		double Az = this.head.z.coord;

		double Bx = vector.head.x.coord;
		double By = vector.head.y.coord;
		double Bz = vector.head.z.coord;

		return new Vector(Ay * Bz - Az * By, Az * Bx - Ax * Bz, Ax * By - Ay * Bx);
	}
/**
 * A function that makes a scalar product of vectors
 * @param vector
 * @return new vector
 */
	public double dotProduct(Vector vector) {
		double Ax = this.head.x.coord;
		double Ay = this.head.y.coord;
		double Az = this.head.z.coord;

		double Bx = vector.head.x.coord;
		double By = vector.head.y.coord;
		double Bz = vector.head.z.coord;

		return (Ax * Bx + Ay * By + Az * Bz);
	}
/**
 * A function that calculate the vector length squared
 * @return double
 */
	public double lengthSquared() {
		return this.head.distanceSquarde(Point3D.ZERO);
	}
/**
 * A function that use the previous function to Calculate the vector length
 * by the function sqrt
 * @return double
 */
	public double length() {
		return Math.sqrt(lengthSquared());
	}

	public Point3D getHead() {
		return this.head;
	}

}
