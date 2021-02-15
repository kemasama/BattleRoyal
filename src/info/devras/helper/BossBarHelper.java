package info.devras.helper;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

public class BossBarHelper {
	private BossBar bar;

	public BossBarHelper() {
		bar = Bukkit.createBossBar("", BarColor.YELLOW, BarStyle.SOLID);
	}

	public void ShowAll() {
		for (Player p : Bukkit.getOnlinePlayers()) {
			if (!bar.getPlayers().contains(p)) {
				bar.addPlayer(p);
			}
		}

		bar.setVisible(true);
	}

	public void setTitle(String title) {
		bar.setTitle(title);
	}

	public void setColor(BarColor color) {
		bar.setColor(color);
	}

	/**
	 * set Progress percentage
	 * @param per Min 0D, Max 1D
	 */
	public void setProgress(double per) {
		bar.setProgress(per);
	}

	public BossBar getRaw() {
		return bar;
	}

}
