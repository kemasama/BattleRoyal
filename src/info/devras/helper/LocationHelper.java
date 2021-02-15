package info.devras.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class LocationHelper {
	public static double getRelativeCoord(int i) {
		double d = i;
		d = d < 0 ? d - 0.5 : d + 0.5;
		return d;
	}

	public static List<Location> circle(Location loc, int r, int h, boolean hollow, boolean sphere, int py) {
		List<Location> blocks = new ArrayList<>();

		int cx = loc.getBlockX();
		int cy = loc.getBlockY();
		int cz = loc.getBlockZ();
		for (int x = cx - r; x <= cx; x++) {
			for (int z = cz - r; z <= cz; z++) {
				for (int y = (sphere ? cy - r : cy); y < (sphere ? cy + r : cy + h); y++) {
					double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z)
							+ (sphere ? (cy - y) * (cy - y) : 0);

					if (dist < r * r && !(hollow && dist < (r - 1) * (r - 1))) {
						Location l = new Location(loc.getWorld(), x, y + py, z);
						blocks.add(l);
					}
				}
			}
		}

		return blocks;
	}

	public static Player getMostNear(Location from) {
		Player player = null;
		double diff = 0;

		for (Player pl : from.getWorld().getPlayers()) {
			if (player == null) {
				player = pl;
				diff = from.distanceSquared(pl.getLocation());
				continue;
			}

			double dis = from.distanceSquared(pl.getLocation());
			if (dis < diff) {
				player = pl;
				diff = dis;
			}
		}

		return player;
	}

	public static Player getMostNear(Location from, GameMode ignore) {
		Player player = null;
		double diff = 0;

		for (Player pl : from.getWorld().getPlayers()) {
			if (pl.getGameMode().equals(ignore)) {
				continue;
			}

			if (player == null) {
				player = pl;
				diff = from.distanceSquared(pl.getLocation());
				continue;
			}

			double dis = from.distanceSquared(pl.getLocation());
			if (dis < diff) {
				player = pl;
				diff = dis;
			}
		}

		return player;
	}

	public static Player getMostNear(Location from, GameMode ignore, Player... ignores) {
		Player player = null;
		double diff = 0;

		List<UUID> key = new ArrayList<>();
		for (Player p : ignores) {
			key.add(p.getUniqueId());
		}

		for (Player pl : from.getWorld().getPlayers()) {
			if (pl.getGameMode().equals(ignore)) {
				continue;
			}

			if (key.contains(pl.getUniqueId())) {
				continue;
			}

			if (player == null) {
				player = pl;
				diff = from.distanceSquared(pl.getLocation());
				continue;
			}

			double dis = from.distanceSquared(pl.getLocation());
			if (dis < diff) {
				player = pl;
				diff = dis;
			}
		}

		return player;
	}

	public static List<Player> getPlayersByLocation(Location from, double distance) {
		List<Player> pl = new ArrayList<>();
		try {
			for (Player p : from.getWorld().getPlayers()) {
				if (from.distanceSquared(p.getLocation()) <= distance) {
					pl.add(p);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return pl;
	}

	public static List<LivingEntity> getEntityByLocation(Location from, double distance) {
		List<LivingEntity> pl = new ArrayList<>();
		try {
			for (Entity l : from.getWorld().getEntities()) {
				if (l instanceof LivingEntity && from.distanceSquared(l.getLocation()) <= distance) {
					pl.add((LivingEntity) l);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return pl;
	}

	public static boolean equalsLocation(Location to, Location from) {
		return to.getWorld().getName().equals(from.getWorld().getName())
				&& to.getBlockX() == from.getBlockX()
				&& to.getBlockY() == from.getBlockY()
				&& to.getBlockZ() == from.getBlockZ();
	}

	public static Location lookAt(Location loc, Location look) {
		loc = loc.clone();
		double dx = look.getX() - loc.getX();
		double dy = look.getY() - loc.getY();
		double dz = look.getZ() - loc.getZ();

		if (dx != 0) {
			if (dx < 0) {
				loc.setYaw((float) (1.5 * Math.PI));
			} else {
				loc.setYaw((float) (0.5 * Math.PI));
			}
			loc.setYaw((float) loc.getYaw() - (float) Math.atan(dz / dx));
		} else if (dz < 0) {
			loc.setYaw((float) Math.PI);
		}

		double dxz = Math.sqrt(Math.pow(dx, 2) + Math.pow(dz, 2));

		loc.setPitch((float) -Math.atan(dy / dxz));

		loc.setYaw(-loc.getYaw() * 180f / (float) Math.PI);
		loc.setPitch(loc.getPitch() * 180f / (float) Math.PI);

		return loc;
	}

	public static void setLooking(Player p, Location target) {
		Vector direction = target.toVector().subtract(getVector(p)).normalize();
		double x = direction.getX(),
				y = direction.getY(),
				z = direction.getZ();

		Location changed = target.clone();
		changed.setYaw(180 - toDegree(Math.atan2(x, z)));
		changed.setPitch(90 - toDegree(Math.acos(y)));

		p.teleport(changed);
	}

	public static float toDegree(double angle) {
		return (float) Math.toDegrees(angle);
	}

	public static Vector getVector(Player p) {
		return p.getEyeLocation().toVector();
	}
}
