package info.devras.helper;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class SrvUtil {
	public static String getServerVersion ( )
	{
	    return Bukkit.getServer ( ).getClass ( ).getPackage ( ).getName ( ).substring ( 23 );
	}

	public static void setPing(Player p, int newPing) {
		try{
			Class<?> CraftPlayerClass = Class.forName("org.bukkit.craftbukkit." + getServerVersion() + ".entity.CraftPlayer");
			Object CraftPlayer = CraftPlayerClass.cast(p);
			Method getHandle = CraftPlayer.getClass().getMethod("getHandle", new Class[0]);
			Object EntityPlayer = getHandle.invoke(CraftPlayer, new Object[0]);
			Field ping = EntityPlayer.getClass().getDeclaredField("ping");
			//ping.setAccessible(true);
			ping.setInt(EntityPlayer, newPing);
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	public static int getPing(Player p){
		if (!p.getClass().getName().equals("org.bukkit.craftbukkit." + getServerVersion() + ".entity.CraftPlayer")) {
			p = Bukkit.getPlayer(p.getUniqueId());
		}
		try{
			Class<?> CraftPlayerClass = Class.forName("org.bukkit.craftbukkit." + getServerVersion() + ".entity.CraftPlayer");
			Object CraftPlayer = CraftPlayerClass.cast(p);
			Method getHandle = CraftPlayer.getClass().getMethod("getHandle", new Class[0]);
			Object EntityPlayer = getHandle.invoke(CraftPlayer, new Object[0]);
			Field ping = EntityPlayer.getClass().getDeclaredField("ping");
			return ping.getInt(EntityPlayer);
		} catch (Exception e){
			e.printStackTrace();
		}
		return 0;
	}

	public static void sendPacket(Player player, Object packet) {
		try {
			Object handle = player.getClass().getMethod("getHandle", new Class[0]).invoke(player, new Object[0]);
			Object playerCon = handle.getClass().getField("playerConnection").get(handle);
			playerCon.getClass().getMethod("sendPacket", getNMSClass("Packet")).invoke(playerCon, packet);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void sendTitle(Player player, String title, String subtitle, int fadeInTime, int showTime, int fadeOutTime) {
	    try {
	        Object chatTitle = getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class)
	                .invoke(null, "{\"text\": \"" + title + "\"}");
	        Constructor<?> titleConstructor = getNMSClass("PacketPlayOutTitle").getConstructor(
	                getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0], getNMSClass("IChatBaseComponent"),
	                int.class, int.class, int.class);
	        Object packet = titleConstructor.newInstance(
	                getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("TITLE").get(null), chatTitle,
	                fadeInTime, showTime, fadeOutTime);

	        Object chatsTitle = getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class)
	                .invoke(null, "{\"text\": \"" + subtitle + "\"}");
	        Constructor<?> timingTitleConstructor = getNMSClass("PacketPlayOutTitle").getConstructor(
	                getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0], getNMSClass("IChatBaseComponent"),
	                int.class, int.class, int.class);
	        Object timingPacket = timingTitleConstructor.newInstance(
	                getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("SUBTITLE").get(null), chatsTitle,
	                fadeInTime, showTime, fadeOutTime);

	        sendPacket(player, packet);
	        sendPacket(player, timingPacket);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}

	public static Class<?> getNMSClass(String name) {

		try {
			return Class.forName("net.minecraft.server." + getServerVersion() + "." + name);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public static Object getCraftPlayer(Player p){
		if (!p.getClass().getName().equals("org.bukkit.craftbukkit." + getServerVersion() + ".entity.CraftPlayer")) {
			return null;
		}

		try{
			Class<?> CraftPlayerClass = Class.forName("org.bukkit.craftbukkit." + getServerVersion() + ".entity.CraftPlayer");
			Object CraftPlayer = CraftPlayerClass.cast(p);
			//Class<?> mcEPClass = Class.forName("net.minecraft.server." + v + ".EntityPlayer");

			return CraftPlayer;

			//EntityPlayer ep = ((CraftPlayer) p).getHandle();
		} catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}

	public static String healthFormat(double health) {
		return ( (health > 40.0) ? ChatColor.DARK_GREEN : (health > 35.0) ? ChatColor.GREEN :
			(health > 30.0 ? ChatColor.YELLOW : (health > 20.0) ? ChatColor.LIGHT_PURPLE : (health > 10.0) ? ChatColor.DARK_PURPLE : ChatColor.RED)).toString() + String.format("%.1f", health);
	}

	public static String pingFormat(int ping) {
		return ( (ping < 100) ? ChatColor.GREEN : (ping < 150) ? ChatColor.YELLOW : ChatColor.RED).toString()
				+ ( (ping < 50) ? "*" : "") + ping;
	}
	public static String format(double tps) {
		return ( (tps > 19.0) ? ChatColor.GREEN : (tps > 18.0) ? ChatColor.YELLOW : ChatColor.RED).toString()
				+ ( (tps > 20.0) ? "*" : "") + Math.min( Math.round(tps * 100.0) / 100.0, 20.0);
	}

	public static String getHeartIcon(int health, int maxHealth) {
		String str = "";
		if (maxHealth == health) {
		for (int i = 0; i < 10; i++) {
		str = String.valueOf(str) + ChatColor.DARK_RED.toString() + "♥";
		}
		return str;
		}
		if (health >= 0.9D * maxHealth && health <= maxHealth) {
		  for (int i = 0; i < 9; i++) {
		    str = String.valueOf(str) + ChatColor.DARK_RED.toString() + "♥";
		  }
		  return String.valueOf(str) + ChatColor.GRAY.toString() + "♥";
		}

		if (health >= 0.8D * maxHealth && health <= 0.9D * maxHealth) {
		  for (int i = 0; i < 8; i++) {
		    str = String.valueOf(str) + ChatColor.DARK_RED.toString() + "♥";
		  }
		  for (int i = 0; i < 2; i++) {
		    str = String.valueOf(str) + ChatColor.GRAY.toString() + "♥";
		  }
		  return str;
		}
		if (health >= 0.7D * maxHealth && health <= 0.8D * maxHealth) {
		  for (int i = 0; i < 7; i++) {
		    str = String.valueOf(str) + ChatColor.DARK_RED.toString() + "♥";
		  }
		  for (int i = 0; i < 3; i++) {
		    str = String.valueOf(str) + ChatColor.GRAY.toString() + "♥";
		  }
		  return str;
		}
		if (health >= 0.6D * maxHealth && health <= 0.7D * maxHealth) {
		  for (int i = 0; i < 6; i++) {
		    str = String.valueOf(str) + ChatColor.DARK_RED.toString() + "♥";
		  }
		  for (int i = 0; i < 4; i++) {
		    str = String.valueOf(str) + ChatColor.GRAY.toString() + "♥";
		  }
		  return str;
		}
		if (health >= 0.5D * maxHealth && health <= 0.6D * maxHealth) {
		  for (int i = 0; i < 5; i++) {
		    str = String.valueOf(str) + ChatColor.DARK_RED.toString() + "♥";
		  }
		  for (int i = 0; i < 5; i++) {
		    str = String.valueOf(str) + ChatColor.GRAY.toString() + "♥";
		  }
		  return str;
		}
		if (health >= 0.4D * maxHealth && health <= 0.5D * maxHealth) {
		  for (int i = 0; i < 4; i++) {
		    str = String.valueOf(str) + ChatColor.DARK_RED.toString() + "♥";
		  }
		  for (int i = 0; i < 6; i++) {
		    str = String.valueOf(str) + ChatColor.GRAY.toString() + "♥";
		  }
		  return str;
		}
		if (health >= 0.3D * maxHealth && health <= 0.4D * maxHealth) {
		  for (int i = 0; i < 3; i++) {
		    str = String.valueOf(str) + ChatColor.DARK_RED.toString() + "♥";
		  }
		  for (int i = 0; i < 7; i++) {
		    str = String.valueOf(str) + ChatColor.GRAY.toString() + "♥";
		  }
		  return str;
		}
		if (health >= 0.2D * maxHealth && health <= 0.3D * maxHealth) {
		  for (int i = 0; i < 2; i++) {
		    str = String.valueOf(str) + ChatColor.DARK_RED.toString() + "♥";
		  }
		  for (int i = 0; i < 8; i++) {
		    str = String.valueOf(str) + ChatColor.GRAY.toString() + "♥";
		  }
		  return str;
		}
		if (health >= 0.1D * maxHealth && health <= 0.2D * maxHealth) {
		  str = String.valueOf(str) + ChatColor.DARK_RED.toString() + "♥";
		  for (int i = 0; i < 9; i++) {
		    str = String.valueOf(str) + ChatColor.GRAY.toString() + "♥";
		  }
		  return str;
		}
		if (health > 0 && health <= 0.1D * maxHealth) {
		  str = String.valueOf(str) + ChatColor.DARK_RED.toString() + "♥";
		  for (int i = 0; i < 9; i++) {
		    str = String.valueOf(str) + ChatColor.GRAY.toString() + "♥";
		  }
		  return str;
		}
		if (health <= 0) {
		  for (int i = 0; i < 10; i++) {
		    str = String.valueOf(str) + ChatColor.GRAY.toString() + "♥";
		  }
		  for (int i = 0; i < 10; i++) {
		    str = String.valueOf(str) + ChatColor.DARK_RED.toString() + "♥";
		      }
		      return str;
		    }
		    if (health > maxHealth) {
		      health = maxHealth;
		    }
		    return str;
	}
}