package com.accele.gage.math;

public class OBB extends BoundingBox {

	private float angle;

	public OBB(Vector3f position, Vector3f size, float angle) {
		super(position, new Vector3f(size));
		//this.size.x *= 2;
		this.angle = angle;
	}

	@Override
	public boolean intersects(BoundingBox other) {
		if (other instanceof AABB)
			return intersects(((AABB) other).toOBB());
		else if (!(other instanceof OBB))
			throw new IllegalArgumentException("OBBs can only be tested against other OBBs.");

		OBB otherOBB = (OBB) other;

		Vector2f[] vertices1 = getVertices(position, size, angle);
		Vector2f[] vertices2 = getVertices(otherOBB.position, otherOBB.size, otherOBB.angle);
		
		System.out.println("Vertices1:");
		for (Vector2f v : vertices1)
			System.out.println(v.x + ", " + v.y);
		
		System.out.println("\n\nVertices2:");
		for (Vector2f v : vertices2)
			System.out.println(v.x + ", " + v.y);
		
		Vector2f[] axes = getPerpendicularAxes(vertices1, vertices2);

		// we need to find the minimal overlap and axis on which it happens
		float minOverlap = Float.POSITIVE_INFINITY;

		for (Vector2f axis : axes) {
			Vector2f proj1 = projectOnAxis(vertices1, axis);
			Vector2f proj2 = projectOnAxis(vertices2, axis);

			float overlap = getOverlapLength(proj1, proj2);
			if (overlap == 0.f) { // shapes are not overlapping
				return false;
			} else {
				if (overlap < minOverlap) {
					minOverlap = overlap;
				}
			}
		}

		return true;
	}

	private Vector2f getNormal(Vector2f v) {
		return new Vector2f(-v.y, v.x);
	}

	// Find minimum and maximum projections of each vertex on the axis
	private Vector2f projectOnAxis(Vector2f[] vertices, Vector2f axis) {
		float min = Float.POSITIVE_INFINITY;
		float max = Float.NEGATIVE_INFINITY;
		for (Vector2f vertex : vertices) {
			float projection = vertex.dot(axis);
			if (projection < min) {
				min = projection;
			}
			if (projection > max) {
				max = projection;
			}
		}
		return new Vector2f(min, max);
	}

	// a and b are ranges and it's assumed that a.x <= a.y and b.x <= b.y
	private boolean areOverlapping(Vector2f a, Vector2f b) {
		return a.x <= b.y && a.y >= b.x;
	}

	// a and b are ranges and it's assumed that a.x <= a.y and b.x <= b.y
	private float getOverlapLength(Vector2f a, Vector2f b) {
		if (!areOverlapping(a, b)) {
			return 0;
		}
		return Math.min(a.y, b.y) - Math.max(a.x, b.x);
	}

	Vector2f[] getVertices(Vector3f position, Vector3f size, float angle) {
		Vector2f[] vertices = new Vector2f[4];

		float xval = 1;
		float yval = 2;
		
		vertices[0] = new Vector2f(position.x + size.x / xval, position.y + size.y / yval);
		vertices[1] = new Vector2f(position.x + size.x / xval, position.y - size.y / yval);
		vertices[2] = new Vector2f(position.x - size.x / xval, position.y + size.y / yval);
		vertices[3] = new Vector2f(position.x - size.x / xval, position.y - size.y / yval);

		/*float cos = (float) Math.cos(angle);
		float sin = (float) Math.sin(angle);

		vertices[0] = new Vector2f(vertices[0].x * cos - vertices[0].y * sin, vertices[0].x * sin + vertices[0].y * cos);
		vertices[1] = new Vector2f(vertices[1].x * cos - vertices[1].y * sin, vertices[1].x * sin + vertices[1].y * cos);
		vertices[2] = new Vector2f(vertices[2].x * cos - vertices[2].y * sin, vertices[2].x * sin + vertices[2].y * cos);
		vertices[3] = new Vector2f(vertices[3].x * cos - vertices[3].y * sin, vertices[3].x * sin + vertices[3].y * cos);*/
		
		Matrix4f rotMatrix = new Matrix4f().rotateAround(new Quaternionf(new AxisAnglef(0, 0, 1, angle)), new Vector3f(position.x, position.y, position.z));
		
		Vector4f c0 = new Vector4f(vertices[0].x, vertices[0].y, 0, 1);
		Vector4f c1 = new Vector4f(vertices[1].x, vertices[1].y, 0, 1);
		Vector4f c2 = new Vector4f(vertices[2].x, vertices[2].y, 0, 1);
		Vector4f c3 = new Vector4f(vertices[3].x, vertices[3].y, 0, 1);
		
		c0 = rotMatrix.mul(c0);
		c0 = rotMatrix.mul(c1);
		c2 = rotMatrix.mul(c2);
		c3 = rotMatrix.mul(c3);
		
		vertices[0].set(c0.x, c0.y);
		vertices[1].set(c1.x, c1.y);
		vertices[2].set(c2.x, c2.y);
		vertices[3].set(c3.x, c3.y);

		return vertices;
	}

	Vector2f getPerpendicularAxis(Vector2f[] vertices, int index) {
		assert (index >= 0 && index < 4); // rect has 4 possible axes
		return getNormal(new Vector2f(vertices[index + 1]).sub(vertices[index]).normalize());
	}

	// axes for which we'll test stuff. Two for each box, because testing for
	// parallel axes isn't needed
	Vector2f[] getPerpendicularAxes(Vector2f[] vertices1, Vector2f[] vertices2) {
		Vector2f[] axes = new Vector2f[4];

		axes[0] = getPerpendicularAxis(vertices1, 0);
		axes[1] = getPerpendicularAxis(vertices1, 1);

		axes[2] = getPerpendicularAxis(vertices2, 0);
		axes[3] = getPerpendicularAxis(vertices2, 1);
		return axes;
	}

	public float getAngle() {
		return angle;
	}

}
