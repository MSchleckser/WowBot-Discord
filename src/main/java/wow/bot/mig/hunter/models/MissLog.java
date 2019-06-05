package wow.bot.mig.hunter.models;

import wow.bot.mig.hunter.FireTypes;

import java.time.LocalDateTime;
import java.util.Objects;

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

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		MissLog missLog = (MissLog) o;
		return Objects.equals(missTime, missLog.missTime) &&
				type == missLog.type;
	}
}
