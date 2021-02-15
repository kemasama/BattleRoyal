package info.devras.listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.PlayerInventory;

import info.devras.Game;
import info.devras.GameConfig;

public class JoinListener implements Listener {
	@EventHandler
	public void onLogin(PlayerLoginEvent event) {
		if (Game.getInstance().getManager().isInGame()) {
			event.disallow(Result.KICK_OTHER, "I'm sorry, game is already running! Please try again later.");
		} else {
			if (GameConfig.MAX_PLAYERS < (Bukkit.getOnlinePlayers().size() + 1)) {
				event.disallow(Result.KICK_FULL, "I'm sorry, game is already full! Please try again later.");
			}
		}
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Player p = event.getPlayer();
		//UUID key = p.getUniqueId();

		Game.getInstance().getManager().AddPlayer(p);

		// Clear equip
		PlayerInventory inv = p.getInventory();
		inv.clear();
		inv.setArmorContents(null);
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		Player p = event.getPlayer();
		//UUID key = p.getUniqueId();

		Game.getInstance().getManager().RemovePlayer(p);
	}
}
