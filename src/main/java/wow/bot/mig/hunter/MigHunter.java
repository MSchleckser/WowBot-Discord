package wow.bot.mig.hunter;

import kong.unirest.Unirest;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import wow.bot.mig.hunter.feature.miss.tracker.MissTracker;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Random;

public class MigHunter {
	private static MigHunter ourInstance = new MigHunter();

	public static MigHunter getInstance() {
		return ourInstance;
	}

	private Scheduler scheduler;
	private MissTracker missTracker = new MissTracker();
	private boolean isMigInAir = false;
	private LocalDateTime timeMigDispatched;

	private MigHunter() {
		try {
			scheduler = new StdSchedulerFactory().getScheduler();
			scheduler.start();
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}

	public String rollHit(FireTypes type, Message message) {
		LocalDateTime shootTime = message.getCreationTime().toLocalDateTime();
		User user = message.getAuthor();

		if (!isMigInAir) {
			missTracker.logMiss(message, type);
			return "Skies are clear. Return to base for sensor maintenance.";
		}

		if (!missTracker.isValidShooter(message))
			return getCountDown(missTracker.getMostCurrentMissEvent(message.getAuthor()).getType(), user, shootTime);

		if (!isHit(type))
			return registerMiss(type, message);

		return registerHit(type, message);
	}

	private float getDeltaSeconds(LocalDateTime messageTime, LocalDateTime testTime) {
		return ChronoUnit.SECONDS.between(testTime, messageTime);
	}

	private String registerMiss(FireTypes type, Message message) {
		missTracker.logMiss(message, type);
		return type.getMissMessage();
	}

	private String registerHit(FireTypes type, Message message) {
		isMigInAir = false;
		float delta = getDeltaSeconds(message.getCreationTime().toLocalDateTime(), timeMigDispatched);

		String kills = registerHitWithDb(message.getAuthor());

		return String.format(type.getHitMessage(),
				delta,
				missTracker.getMissCountForUser(message.getAuthor()),
				message.getAuthor().getAsMention(),
				kills);
	}

	private String registerHitWithDb(User user) {
		return Unirest.post("http://localhost:8080/kills/add")
				.queryString("user", user.getAsMention())
				.asString().getBody();
	}

	private boolean isHit(FireTypes type) {
		Random random = new Random();
		return type.getHitChance() > random.nextFloat() * 100.0f;
	}

	private String getCountDown(FireTypes type, User user, LocalDateTime shootTime) {
		LocalDateTime missTime = missTracker.getMostCurrentMissEvent(user).getMissTime();
		float delta = type.getCoolDown() - getDeltaSeconds(shootTime, missTime);
		return String.format(type.getCoolDownMessage(), delta);
	}

	public void schedule(JobDetail jobDetail, Trigger trigger) {
		try {
			scheduler.clear();
			scheduler.scheduleJob(jobDetail, trigger);
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}

	public void dispatchMig() {
		//Convert to CST.
		missTracker.clearMisses();
		timeMigDispatched = LocalDateTime.now().plusHours(5);
		isMigInAir = true;
	}

	public boolean getIsMigInAir() {
		return isMigInAir;
	}
}
