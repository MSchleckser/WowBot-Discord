package wow.bot.mig.hunter.utilities;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class DateUtilities {

	/**
	 * Returns the total number of seconds between the two specified times.
	 * @param messageTime
	 * @param testTime
	 * @return
	 */
	public static float getDeltaSeconds(LocalDateTime messageTime, LocalDateTime testTime){
		return ChronoUnit.SECONDS.between(testTime, messageTime);
	}

}
