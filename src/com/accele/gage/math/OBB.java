package com.accele.gage.math;

public class OBB extends BoundingBox {

	private float angle;
	
	public OBB(Vector3f position, Vector3f size, float angle) {
		super(position, size);
		this.angle = angle;
	}

	@Override
	public boolean intersects(BoundingBox other) {
		if (other instanceof AABB)
			return intersects(((AABB) other).toOBB());
		else if (!(other instanceof OBB))
			throw new IllegalArgumentException("OBBs can only be tested against other OBBs.");

		OBB otherOBB = (OBB) other;
		
		Vector3f[] vertices1 = getVertices();
		Vector3f[] vertices2 = otherOBB.getVertices();
		Vector3f[] axes1 = getAxes(vertices1);
		Vector3f[] axes2 = otherOBB.getAxes(vertices2);
		
		for (int i = 0; i < axes1.length; i++) {
			Vector3f axis = axes1[i];
			Projection p1 = project(vertices1, axis);
			Projection p2 = otherOBB.project(vertices2, axis);
			if (!p1.overlaps(p2))
				return false;
		}
		
		for (int i = 0; i < axes2.length; i++) {
			Vector3f axis = axes2[i];
			Projection p1 = project(vertices1, axis);
			Projection p2 = otherOBB.project(vertices2, axis);
			if (!p1.overlaps(p2))
				return false;
		}
		
		return true;
	}
	
	private Projection project(Vector3f[] vertices, Vector3f axis) {
		float min = axis.dot(vertices[0]);
		float max = min;
		for (int i = 1; i < vertices.length; i++) {
			float p = axis.dot(vertices[i]);
			if (p < min) {
				min = p;
			} else if (p > max) {
				max = p;
			}
		}
		return new Projection(min, max);
	}
	
	private Vector3f[] getAxes(Vector3f[] vertices) {
		Vector3f[] result = new Vector3f[vertices.length];
		for (int i = 0; i < result.length; i++) {
			Vector3f currentVertex = vertices[i];
			Vector3f nextVertex = vertices[i == result.length - 1 ? 0 : i];
			Vector3f edge = new Vector3f(currentVertex).sub(nextVertex);
			result[i] = new Vector3f(-edge.y, edge.x, 0).normalize();
		}
		return result;
	}
	
	private Vector3f[] getVertices() {
		return new Vector3f[] {
				rotateAround(new Vector3f(position).add(new Vector3f(-size.x, size.y, 0)), position, angle),
				rotateAround(new Vector3f(position).add(new Vector3f(size.x, size.y, 0)), position, angle),
				rotateAround(new Vector3f(position).add(new Vector3f(size.x, -size.y, 0)), position, angle),
				rotateAround(new Vector3f(position).add(new Vector3f(-size.x, -size.y, 0)), position, angle)
		};
	}
	
	private Vector3f rotateAround(Vector3f v, Vector3f origin, float angle) {
		Vector3f tmp = new Vector3f(v).sub(origin);
		Matrix4f rot = new Matrix4f().rotateZ(angle);
		Vector4f v4 = rot.mul(new Vector4f(tmp.x, tmp.y, tmp.z, 0));
		return new Vector3f(v4.x, v4.y, 0).add(origin);
	}

	public float getAngle() {
		return angle;
	}
	
	public void setAngle(float angle) {
		this.angle = angle;
	}
	
	private static class Projection {
		private float min;
		private float max;
		
		private Projection(float min, float max) {
			this.min = min;
			this.max = max;
		}
		
		private boolean overlaps(Projection other) {
			return max >= other.min && other.max >= min;
		}
	}
	
}
