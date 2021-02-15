package info.devras.players;

import java.util.UUID;

/*
 * Manager Player Attachment
 *
 * UUID As Primary Key
 * ScoreInfo As Score
 */
public class PlayerInfo {
	private UUID uuid;
	private ScoreInfo score;
	private int kills = 0;
	private int multiple = 1;

	public PlayerInfo(UUID uuid) {
		this.uuid = uuid;
		this.score = new ScoreInfo(uuid);
	}

	// Call at Start timing
	public void Start() {
		// Prepare
		score.Start();
	}

	// Save
	public void Store() {

	}

	public void onKill() {
		kills++;
	}

	public void setMultiple(int multi) {
		multiple = multi;
	}

	public int getKills() {
		return kills;
	}

	public int getKillPoints() {
		return multiple * kills;
	}

	public UUID getUUID() {
		return uuid;
	}

	public ScoreInfo getScore() {
		return score;
	}
}
