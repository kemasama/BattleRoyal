package info.devras.manager;

import org.bukkit.entity.Player;

public interface IManager {
	public void Setup();

	public void Reset();

	public void Run();

	public void AddPlayer(Player p);

	public void RemovePlayer(Player p);

	public boolean ExtendPlayer(Player p);

	public void onTick();
}
