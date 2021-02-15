package info.devras.helper;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class SoundHelper {
	public static void Play(Sound sound, float volume, float pitch) {
		for (Player p : Bukkit.getOnlinePlayers()) {
			p.playSound(p.getLocation(), sound, volume, pitch);
		}
	}
}
