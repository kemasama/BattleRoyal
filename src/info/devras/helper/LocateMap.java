package info.devras.helper;

import org.bukkit.Location;

/*
 * Getting Correct Location (Using HashTable)
 */

public class LocateMap {
	public LocateMap(Location l) {
		this.x = l.getBlockX();
		this.y = l.getBlockY();
		this.z = l.getBlockZ();
	}

	private int x;
	private int y;
	private int z;

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getZ() {
		return z;
	}

	@Override
	public int hashCode() {
		int result = 1;

		result = 31 * result * x;
		result = 31 * result * y;
		result = 31 * result * z;

		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof LocateMap) {
			return obj.hashCode() == this.hashCode();
		}
		return super.equals(obj);
	}

	@Override
	public String toString() {
		return x + "/" + y + "/" + z;
	}

}
