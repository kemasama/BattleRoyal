package info.devras.helper;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.RenderType;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

/**
 *
 * @author crisdev333
 *
 */
public class ScoreHelper {

	private static HashMap<UUID, ScoreHelper> players = new HashMap<>();

	public static boolean hasScore(Player player) {
		return players.containsKey(player.getUniqueId());
	}

	public static ScoreHelper createScore(Player player) {
		return new ScoreHelper(player);
	}

	public static ScoreHelper getByPlayer(Player player) {
		return players.get(player.getUniqueId());
	}

	public static ScoreHelper removeScore(Player player) {
		return players.remove(player.getUniqueId());
	}

	private Scoreboard scoreboard;
	private Objective sidebar, playerList, belowName;
	private Team Admin, Moderator, Member, Ranker;
	private UUID key;

	private ScoreHelper(Player player) {
		scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
		sidebar = scoreboard.registerNewObjective("sidebar", "dummy", "sidebar", RenderType.INTEGER);
		sidebar.setDisplaySlot(DisplaySlot.SIDEBAR);

		playerList = scoreboard.registerNewObjective("player", "dummy", "player", RenderType.INTEGER);
		playerList.setDisplaySlot(DisplaySlot.PLAYER_LIST);

		belowName = scoreboard.registerNewObjective("hp", "dummy", "HP", RenderType.INTEGER);
		belowName.setDisplayName("HP");
		belowName.setDisplaySlot(DisplaySlot.BELOW_NAME);

		// Create Teams
		for (int i = 1; i <= 15; i++) {
			Team team = scoreboard.registerNewTeam("SLOT_" + i);
			team.addEntry(genEntry(i));
		}

		Admin = scoreboard.registerNewTeam("00_Admin");
		Admin.setDisplayName("Admin");
		Moderator = scoreboard.registerNewTeam("01_Mod");
		Moderator.setDisplayName("Moderator");
		Member = scoreboard.registerNewTeam("02_Mem");
		Member.setDisplayName("Member");
		Ranker = scoreboard.registerNewTeam("03_Rank");
		Ranker.setDisplayName("Ranker");

		setTeam(player);

		player.setScoreboard(scoreboard);
		this.key = player.getUniqueId();
		players.put(player.getUniqueId(), this);
	}

	public Player getOwn() {
		return Bukkit.getPlayer(key);
	}

	@SuppressWarnings("deprecation")
	public void setCustomTeam(Player player, String team, String prefix, String suffix) {
		try {
			Team t;
			if (scoreboard.getTeam(team) == null) {
				t = scoreboard.registerNewTeam(team);
			} else {
				t = scoreboard.getEntryTeam(team);
			}

			t.addPlayer(player);
			t.setPrefix(prefix);
			t.setSuffix(suffix);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("deprecation")
	public void setTeam(Player player) {
		if (player.isOp()) {
			if (!Moderator.hasPlayer(player)) {
				Moderator.addPlayer(player);
			}
		} else {
			if (!Member.hasPlayer(player)) {
				Member.addPlayer(player);
			}
		}
	}

	@SuppressWarnings("deprecation")
	public void unTeam(Player player) {
		if (Admin.hasPlayer(player)) {
			Admin.removePlayer(player);
		}
		if (Moderator.hasPlayer(player)) {
			Moderator.removePlayer(player);
		}
		if (Member.hasPlayer(player)) {
			Member.removePlayer(player);
		}
		if (Ranker.hasPlayer(player)) {
			Ranker.removePlayer(player);
		}
	}

	@SuppressWarnings("deprecation")
	public void setHealth(Player player) {
		playerList.getScore(player).setScore((int) player.getHealth());
		belowName.getScore(player).setScore((int) player.getHealth());
	}

	public void setTitle(String title) {
		title = ChatColor.translateAlternateColorCodes('&', title);
		sidebar.setDisplayName(title.length() > 32 ? title.substring(0, 32) : title);
	}

	public void setSlot(int slot, String text) {
		Team team = scoreboard.getTeam("SLOT_" + slot);
		String entry = genEntry(slot);
		if (!scoreboard.getEntries().contains(entry)) {
			sidebar.getScore(entry).setScore(slot);
		}

		text = ChatColor.translateAlternateColorCodes('&', text);
		String pre = getFirstSplit(text);
		String suf = getFirstSplit(ChatColor.getLastColors(pre) + getSecondSplit(text));
		team.setPrefix(pre);
		team.setSuffix(suf);
	}

	public void removeSlot(int slot) {
		String entry = genEntry(slot);
		if (scoreboard.getEntries().contains(entry)) {
			scoreboard.resetScores(entry);
		}
	}

	public void setSlotsFromList(List<String> list) {
		while (list.size() > 15) {
			list.remove(list.size() - 1);
		}

		int slot = list.size();

		if (slot < 15) {
			for (int i = (slot + 1); i <= 15; i++) {
				removeSlot(i);
			}
		}

		for (String line : list) {
			setSlot(slot, line);
			slot--;
		}
	}

	private String genEntry(int slot) {
		return ChatColor.values()[slot].toString();
	}

	private String getFirstSplit(String s) {
		return s.length() > 16 ? s.substring(0, 16) : s;
	}

	private String getSecondSplit(String s) {
		if (s.length() > 32) {
			s = s.substring(0, 32);
		}
		return s.length() > 16 ? s.substring(16) : "";
	}

}