package wow.bot.mig.hunter;

import org.quartz.*;

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
	}

	private MigHunter migHunter;

	private final float min = 120;
	private final float max = 7200;

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
