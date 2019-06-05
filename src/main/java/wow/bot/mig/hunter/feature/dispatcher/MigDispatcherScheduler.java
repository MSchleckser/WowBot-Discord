package wow.bot.mig.hunter.feature.dispatcher;

import org.quartz.*;
import wow.bot.WowBot;
import wow.bot.mig.hunter.MigHunter;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Random;

public class MigDispatcherScheduler {
	private static MigDispatcherScheduler ourInstance = new MigDispatcherScheduler();

	public static MigDispatcherScheduler getInstance() {
		return ourInstance;
	}

	private MigDispatcherScheduler() {
		min = Float.parseFloat(WowBot.getConfig().getProperty("migPeriodMinimum"));
		max = Float.parseFloat(WowBot.getConfig().getProperty("migPeriodMaximum"));
	}

	private MigHunter migHunter;

	private final float min;
	private final float max;

	public void scheduleNewMig(){
		migHunter.schedule(getJobDetail(), getTrigger());
	}

	public void setMigHunter(MigHunter migHunter){
		this.migHunter = migHunter;
	}

	private JobDetail getJobDetail(){
		JobDetail job = JobBuilder.newJob(MigDispatcher.class)
				.withIdentity("Mig Dispatcher", "Mig Hunter")
				.build();
		return job;
	}

	private Trigger getTrigger(){
		return ((SimpleTrigger) TriggerBuilder.newTrigger()
				.withIdentity("Mig Dispatcher Trigger", "Mig Hunter")
				.forJob(getJobDetail()).startAt(getRandomInstant()).build());
	}

	private Date getRandomInstant(){
		return Date.from(LocalDateTime.now().plusSeconds(getRandomDelay())
				.atZone(ZoneId.systemDefault()).toInstant());
	}

	private long getRandomDelay(){
		long delay = (long)(min + (new Random().nextFloat() * (max - min)));
		return delay;
	}
}
