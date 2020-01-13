package com.accele.gage.math;

public class AABB extends BoundingBox {
	
	public AABB(Vector3f position, Vector3f size) {
		super(position, size);
	}
	
	@Override
	public boolean intersects(BoundingBox other) {
		if (other instanceof OBB2)
			return toOBB().intersects(other);
		
		AABB b = other instanceof AABB ? ((AABB) other) : null;
		
		if (Math.abs(position.x - b.position.x) < size.x + b.size.x) {
			if (Math.abs(position.y - b.position.y) < size.y + b.size.y) {
				return true;
			}
		}

		return false;
	}
	
	public OBB2 toOBB() {
		return new OBB2(position, size, 0);
	}

}
