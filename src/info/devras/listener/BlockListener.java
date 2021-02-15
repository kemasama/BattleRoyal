package info.devras.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import info.devras.Game;
import info.devras.helper.LocateMap;
import info.devras.manager.GameManager;

public class BlockListener implements Listener {
	// Prepend, force cancel If didn't start game.
	// And, store the block location using LocateMap and ArrayList
	// That list using Manager Reset Event

	@EventHandler
	public void onBreak(BlockBreakEvent event) {
		if (!Game.getInstance().getManager().isInGame()) {
			event.setCancelled(true);
		} else {
			// game starting
			GameManager manager = Game.getInstance().getManager();
			LocateMap map = new LocateMap(event.getBlock().getLocation());
			if (manager.canBreak(map)) {
				manager.onBreak(map);
			} else {
				event.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onPlace(BlockPlaceEvent event) {
		if (!Game.getInstance().getManager().isInGame()) {
			event.setCancelled(true);
		} else {
			// game starting
			GameManager manager = Game.getInstance().getManager();
			LocateMap map = new LocateMap(event.getBlock().getLocation());
			manager.onPlace(map);
		}
	}
}
