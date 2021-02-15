package info.devras.players;

import java.util.UUID;

public class ScoreInfo {
	// SC_* Tier
	public static int SC_BRONZE = 0;
	public static int SC_SILVER = 300;
	public static int SC_GOLD = 700;
	public static int SC_DIAMOND = 1200;
	public static int SC_MASTER = 1800;

	// CT_* Cost
	public static int CT_PREPARE = 5;

	private int score;
	private String name;

	public ScoreInfo(UUID uuid) {
		GetScore();
		setName();
	}

	// Get player score from store (DB, File)
	private void GetScore() {
		this.score = 0;
	}

	public void Start() {
		// prepare cost
		if (!hasTierProtect(CT_PREPARE)) {
			score = score - CT_PREPARE;
		}
	}

	public boolean hasTierProtect(int volume) {
		int tmp = score + volume;
		// upper Master
		if (score >= SC_MASTER & tmp < SC_MASTER) {
			return true;
		} else if (score >= SC_DIAMOND & tmp < SC_DIAMOND) {
			return true;
		} else if (score >= SC_GOLD & tmp < SC_GOLD) {
			return true;
		} else if (score >= SC_SILVER & tmp < SC_SILVER) {
			return true;
		} else if (score < SC_SILVER) {
			// lower than silver, in bronze tier
			// always protect
			return true;
		}

		return false;
	}

	private void setName() {
		if (score <= SC_BRONZE) {
			name = "Bronze";
		}
		if (score <= SC_SILVER) {
			name = "Silver";
		}
		if (score <= SC_GOLD) {
			name = "Gold";
		}
		if (score <= SC_DIAMOND) {
			name = "Diamond";
		}
		if (score <= SC_MASTER) {
			name = "Master";
		}
	}

	public int getPoint() {
		return score;
	}

	public String getRankName() {
		return name;
	}
}
