package wow.bot.mig.hunter.models;

import wow.bot.mig.hunter.FireTypes;

import java.time.LocalDateTime;

public class MissLog {

	private LocalDateTime missTime;
	private FireTypes type;

	public MissLog(){}

	public MissLog(LocalDateTime missTime, FireTypes type) {
		this.missTime = missTime;
		this.type = type;
	}

	public LocalDateTime getMissTime() {
		return missTime;
	}

	public void setMissTime(LocalDateTime missTime) {
		this.missTime = missTime;
	}

	public FireTypes getType() {
		return type;
	}

	public void setType(FireTypes type) {
		this.type = type;
	}
}
