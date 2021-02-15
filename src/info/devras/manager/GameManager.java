package info.devras.manager;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;

import info.devras.Game;
import info.devras.GameConfig;
import info.devras.helper.BossBarHelper;
import info.devras.helper.LocateMap;
import info.devras.helper.ScoreHelper;
import info.devras.helper.SoundHelper;
import info.devras.helper.Wrout;
import info.devras.players.PlayerInfo;

/*
 * GameManager
 *   extends Manager
 *
 */
public class GameManager extends Manager {

	public static final int GAME_PREPARE_TIMER = 20;
	public static final GameMode GAME_MODE = GameMode.SURVIVAL;

	protected BossBarHelper bossbar = new BossBarHelper();

	// All Materials are so Material.AIR
	protected World gameWorld;
	protected ArrayList<LocateMap> placeOrBreak = new ArrayList<>();

	protected int timer = 20;

	public boolean canBreak(LocateMap map) {
		Wrout.Write("CAN " + map.toString());
		if (placeOrBreak.contains(map)) {
			return true;
		}

		return false;
	}

	public boolean onPlace(LocateMap map) {
		Wrout.Write("PLACE " + map.toString());
		return placeOrBreak.add(map);
	}

	public boolean onBreak(LocateMap map) {
		Wrout.Write("BREAK " + map.toString());
		return placeOrBreak.remove(map);
	}

	@Override
	public void Setup() {
		super.Setup();

		// Setup Map
		World w = Bukkit.getWorld("game");
		if (w == null) {
			gameWorld = Bukkit.getWorlds().get(0);
		} else {
			gameWorld = w;
		}

		timer = GAME_PREPARE_TIMER;
	}

	@Override
	public void Reset() {
		super.Reset();

		Wrout.Write("Game Reset");

		timer = GAME_PREPARE_TIMER;
		inGame = false;

		// Reset Map
		// force replace
		Wrout.Write("Reset MAP, Start");

		for (LocateMap map : placeOrBreak) {
			Location loc = new Location(gameWorld, map.getX(), map.getY(), map.getZ());
			loc.getBlock().setType(Material.AIR);
		}

		Wrout.Write("Reset MAP, End");

		// ReJoin Handler
		for (Player p : Bukkit.getOnlinePlayers()) {
			RemovePlayer(p);
			AddPlayer(p);
		}

		bossbar.getRaw().removeAll();
	}

	public void Over() {
		// Champion
		Wrout.Write("Game Over");

		SoundHelper.Play(Sound.ENTITY_PLAYER_LEVELUP, 1f, 1f);
	}

	@Override
	public void Run() {
		super.Run();

		Wrout.Write("Game Start");

		// Locate to start point
		inGame = true;

		bossbar.ShowAll();
		bossbar.setTitle("§eRound 1");
	}

	@Override
	public void AddPlayer(Player p) {
		super.AddPlayer(p);
		// Locate to lobby

		Wrout.Write("Join " + p.getName());

		Location loc = Bukkit.getWorlds().get(0).getHighestBlockAt(1, 1).getLocation();
		p.teleport(loc);
	}

	@Override
	public void RemovePlayer(Player p) {
		super.RemovePlayer(p);

		if (ScoreHelper.hasScore(p)) {
			ScoreHelper.removeScore(p);
		}

		Wrout.Write("Quit " + p.getName());
	}

	@Override
	public void onTick() {
		super.onTick();

		GameManager manager = Game.getInstance().getManager();

		// ScoreHelper

		if (!inGame) {
			// didn't start game,
			// however can start numbers...

			if (Bukkit.getOnlinePlayers().size() >= GameConfig.MIN_PLAYERS) {
				timer--;
				if (timer <= 0) {
					Run();
				}
			} else {
				timer = GAME_PREPARE_TIMER;
			}
		}

		int alive = 0;
		int multiple = GameConfig.CT_MULTIPLE_1;

		for (Player p : Bukkit.getOnlinePlayers()) {
			if (p.getGameMode().equals(GAME_MODE)) {
				alive += 1;
			}
		}

		if (alive <= GameConfig.MULTIPLE_NEXT_3) {
			multiple = GameConfig.CT_MULTIPLE_4;
		} else if (alive <= GameConfig.MULTIPLE_NEXT_2) {
			multiple = GameConfig.CT_MULTIPLE_3;
		} else if (alive <= GameConfig.MULTIPLE_NEXT_1) {
			multiple = GameConfig.CT_MULTIPLE_2;
		} else if (alive <= GameConfig.MULTIPLE_START) {
			multiple = GameConfig.CT_MULTIPLE_2;
		}

		for (Player p : Bukkit.getOnlinePlayers()) {
			ScoreHelper helper;
			if (ScoreHelper.hasScore(p)) {
				helper = ScoreHelper.getByPlayer(p);
			} else {
				helper = ScoreHelper.createScore(p);
			}

			helper.setTitle("§e§lBattleBlock");

			PlayerInfo info = manager.getInfo(p.getUniqueId());

			if (!inGame) {
				int num = Math.abs(Bukkit.getOnlinePlayers().size() - GameConfig.MIN_PLAYERS);
				boolean prepare = (timer != GAME_PREPARE_TIMER);
				if (prepare) {
					helper.setSlot(5, "§f準備中・・・");
					helper.setSlot(4, "§f開始まで §a" + timer + "秒");
				} else {
					helper.setSlot(5, "§fあと" + num + "人で");
					helper.setSlot(4, " §fゲーム開始です");
				}
				helper.setSlot(3, "");
			} else {
				if (p.getGameMode().equals(GAME_MODE)) {
					helper.setSlot(5, "§f生存： §a" + alive);
					helper.setSlot(4, "§fキル： §a" + info.getKills());
					helper.setSlot(3, "§f倍率： §ex" + multiple);
					info.setMultiple(multiple);
				} else {
					info.Store();
				}
			}

			helper.setSlot(2, "");
			helper.setSlot(1, "§emc.devras.info");

		}

		if (alive == 1 && inGame) {
			// Over
			Over();
			Reset();
		}

	}

}
