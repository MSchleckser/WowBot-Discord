package wow.bot.systems.mig.hunter.feature.miss.tracker;

import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;
import wow.bot.systems.mig.hunter.FireTypes;
import wow.bot.systems.mig.hunter.models.MissLog;
import wow.bot.systems.mig.hunter.utilities.DateUtilities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Utility class to encapsulate the logic for tracking the number and types of attempts made users.
 *
 * @author Michael Schleckser
 */
public class MissTracker {

	private Map<String, List<MissLog>> missLogs = new HashMap<>();

	/**
	 * Logs the missed attack to the users list of missed attacks. Logs should be cleared after the mig is shot down.
	 *
	 * @param message - The message as sent to the bot by Discord.
	 * @param type - The attack type sent by the user.
	 */
	public void logMiss(Message message, FireTypes type){
		LocalDateTime shootTime = message.getCreationTime().toLocalDateTime();
		User user = message.getAuthor();
		MissLog missLog = new MissLog(shootTime, type);

		if(!missLogs.containsKey(user.getName()))
			missLogs.put(user.getName(), new ArrayList<>());

		missLogs.get(user.getName()).add(missLog);
	}

	/**
	 *
	 * @param user - User to be looked up.
	 * @return The number of attempts the user has made.
	 */
	public int getMissCountForUser(User user){
		if(!missLogs.containsKey(user.getName()))
			return 0;

		return missLogs.get(user.getName()).size();
	}

	/**
	 * Returns the most recent attempt made by the specified user.
	 * @param user - User to look up.
	 * @return The most current attempt made by the user.
	 */
	public MissLog getMostCurrentMissEvent(User user){
		if(!missLogs.containsKey(user.getName()))
			return null;

		return missLogs.get(user.getName()).get(missLogs.get(user.getName()).size() - 1);
	}


	/**
	 *
	 *
	 * @param message - Message containing user and time information.
	 * @return Has the countdown passed.
	 */
	public boolean isValidShooter(Message message){
		User user = message.getAuthor();
		if(!missLogs.containsKey(user.getName()))
			return true;

		float deltaTime = DateUtilities.getDeltaSeconds(message.getCreationTime().toLocalDateTime(), getMostCurrentMissEvent(user).getMissTime());

		return deltaTime > getMostCurrentMissEvent(user).getType().getCoolDown();
	}

	/**
	 * Clears the logs of missed attacks.
	 */
	public void clearMisses(){
		missLogs.clear();
	}

	/**
	 *
	 * @return
	 */
	public Map<String, List<MissLog>> getMissLogs() {
		return missLogs;
	}

	/**
	 *
	 * @param missLogs
	 */
	public void setMissLogs(Map<String, List<MissLog>> missLogs) {
		this.missLogs = missLogs;
	}
}
