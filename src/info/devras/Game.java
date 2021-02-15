package info.devras;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import info.devras.helper.Wrout;
import info.devras.listener.BlockListener;
import info.devras.listener.CommonListener;
import info.devras.listener.DamageListener;
import info.devras.listener.JoinListener;
import info.devras.manager.GameManager;

public class Game extends JavaPlugin {

	/**
	 * Getting Game Instance
	 * @return self
	 */
	public static Game getInstance() {
		return Game.getPlugin(Game.class);
	}

	/*
	 * GamaManager
	 */
	private GameManager manager;
	/*
	 * Game Task
	 */
	private BukkitRunnable task;
	/*
	 * Store Manager (DB, File)
	 */

	/*
	 * Getter
	 */

	public GameManager getManager() {
		return manager;
	}

	@Override
	public void onDisable() {
		Wrout.Write("Disabling...");

		for (Player p : Bukkit.getOnlinePlayers()) {
			if (manager.ExtendPlayer(p)) {
				manager.RemovePlayer(p);
			}
		}

		task.cancel();

		try {
			manager.Reset();
			manager = null;
		} catch (Exception e) {
			Wrout.Write("Error while in manager#Reset");
			e.printStackTrace();
		}

		Wrout.Write("Disabled.");
	}

	@Override
	public void onEnable() {
		Wrout.Write("Enabling...");
		manager = new GameManager();

		Wrout.Write("Setup...");
		manager.Setup();

		for (Player p : Bukkit.getOnlinePlayers()) {
			manager.AddPlayer(p);
		}

		task = new BukkitRunnable() {
			@Override
			public void run() {
				// if need, rename this.
				if (manager.isInGame()) {
					manager.onTick();
				} else {
					// depend
					manager.onTick();
				}
			}
		};

		// 20L -> 1 second
		// 40L -> 2 seconds
		task.runTaskTimer(this, 20L, 20L);

		onRegister();

		Wrout.Write("Enabled.");

	}

	// Register EventListeners.
	public void onRegister() {
		Bukkit.getPluginManager().registerEvents(new JoinListener(), this);
		Bukkit.getPluginManager().registerEvents(new DamageListener(), this);
		Bukkit.getPluginManager().registerEvents(new BlockListener(), this);

		Bukkit.getPluginManager().registerEvents(new CommonListener(), this);
	}

}
