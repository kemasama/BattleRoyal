package info.devras.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import info.devras.Game;

public class DamageListener implements Listener {
	@EventHandler
	public void onDamage(EntityDamageEvent event) {
		if (event.getEntity() instanceof Player) {
			if (!Game.getInstance().getManager().isInGame()) {
				event.setCancelled(true);
			}
		}
	}
}
