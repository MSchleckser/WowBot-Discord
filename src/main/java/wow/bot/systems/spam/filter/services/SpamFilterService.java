package wow.bot.systems.spam.filter.services;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;

public class SpamFilterService {

	private static SpamFilterService INSTANCE = new SpamFilterService();

	public static final SpamFilterService getInstance() { return INSTANCE; }

	private SpamFilterService(){}

	private HashMap<String, LocalDateTime> lastMessageMap = new HashMap<>();
	private float slowdown = 0;

	public void log(String userMention){
		lastMessageMap.put(userMention, LocalDateTime.now());
	}

	public boolean isUserSlowed(String userMention){
		return getTimeSinceLastMessage(userMention) < slowdown;
	}

	public void setSlowdown(float slowdown){
		this.slowdown = slowdown > 0 ? slowdown : 0;
	}

	public float getSlowdown(){
		return slowdown;
	}

	private float getTimeSinceLastMessage(String userMention){
		LocalDateTime lastMessage = lastMessageMap.get(userMention);

		if(lastMessage == null) return 0;

		return lastMessage.until(LocalDateTime.now(), ChronoUnit.SECONDS);
	}
}
