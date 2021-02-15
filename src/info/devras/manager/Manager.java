package info.devras.manager;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.entity.Player;

import info.devras.players.PlayerInfo;

public class Manager implements IManager {
	/*
	 * Required: Access modifier protected
	 */

	// store playerinfo primary uuid key
	protected HashMap<UUID, PlayerInfo> info = new HashMap<>();
	protected boolean inGame = false;

	public boolean isInGame() {
		return inGame;
	}

	@Override
	public void Setup() {
	}

	@Override
	public void Reset() {
		// overwrite all variables
		inGame = false;
	}

	@Override
	public void Run() {
		inGame = true;
	}

	@Override
	public void AddPlayer(Player p) {
		UUID key = p.getUniqueId();
		info.put(key, new PlayerInfo(key));
	}

	@Override
	public void RemovePlayer(Player p) {
		UUID key = p.getUniqueId();
		info.remove(key);
	}

	@Override
	public boolean ExtendPlayer(Player p) {
		UUID key = p.getUniqueId();
		return info.containsKey(key);
	}

	@Override
	public void onTick() {
	}

	public boolean hasInfo(UUID key) {
		return info.containsKey(key);
	}

	public PlayerInfo getInfo(UUID key) {
		return info.get(key);
	}

	public PlayerInfo unInfo(UUID key) {
		return info.remove(key);
	}

}
