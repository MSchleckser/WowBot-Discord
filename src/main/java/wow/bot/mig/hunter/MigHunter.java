package wow.bot.mig.hunter;

import kong.unirest.Unirest;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import wow.bot.mig.hunter.models.MissLog;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Random;

public class MigHunter {
	private static MigHunter ourInstance = new MigHunter();

	public static MigHunter getInstance() {
		return ourInstance;
	}

	private Scheduler scheduler;
	private HashMap<String, MissLog> misses = new HashMap<>();
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

	public String rollHit(FireTypes type, Message message){
		LocalDateTime shootTime = message.getCreationTime().toLocalDateTime();
		User user = message.getAuthor();

		if(!isMigInAir) {
			logMiss(user, shootTime, type);
			return "Skies are clear. Return to base for sensor maintenance.";
		}

		if(!validShooter(type, message))
			return getCountDown(misses.get(message.getAuthor().getName()).getType(), user, shootTime);

		if(!isHit(type))
			return registerMiss(type, message);

		return registerHit(type, message);
	}

	private float getDeltaSeconds(LocalDateTime messageTime, LocalDateTime testTime){
		return ChronoUnit.SECONDS.between(testTime, messageTime);
	}

	private String registerMiss(FireTypes type, Message message){
		logMiss(message.getAuthor(), message.getCreationTime().toLocalDateTime(), type);
		return type.getMissMessage();
	}

	private String registerHit(FireTypes type, Message message){
		isMigInAir = false;
		float delta = getDeltaSeconds(message.getCreationTime().toLocalDateTime(), timeMigDispatched);

		String kills = registerHitWithDb(message.getAuthor());

		return String.format(type.getHitMessage(), delta, message.getAuthor().getAsMention(), kills);
	}

	private String registerHitWithDb(User user){
		 return Unirest.post("http://localhost:8080/kills/add")
				 .queryString("user", user.getAsMention())
				 .asString().getBody();
	}

	private boolean isHit(FireTypes type){
		Random random = new Random();
		return type.getHitChance() > random.nextFloat() * 100.0f;
	}

	private void logMiss(User user, LocalDateTime shootTime, FireTypes type){
		misses.put(user.getName(), new MissLog(shootTime, type));
	}

	private boolean validShooter(FireTypes type, Message message){
		String authorName = message.getAuthor().getName();
		 if(!misses.containsKey(authorName))
		 	return true;

		 if(getDeltaSeconds(message.getCreationTime().toLocalDateTime(), misses.get(authorName).getMissTime()) > misses.get(authorName).getType().getCoolDown()) {
			 misses.remove(message.getAuthor().getName());
			 return true;
		 }


		 return false;
	}

	private String getCountDown(FireTypes type, User user, LocalDateTime shootTime){
		LocalDateTime missTime = misses.get(user.getName()).getMissTime();
		float delta = type.getCoolDown() - getDeltaSeconds(shootTime, missTime);
		return String.format(type.getCoolDownMessage(), delta);
	}

	public void schedule(JobDetail jobDetail, Trigger trigger){
		try {
			scheduler.clear();
			scheduler.scheduleJob(jobDetail, trigger);
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}

	public void dispatchMig(){
		timeMigDispatched = LocalDateTime.now().plusHours(5);
		isMigInAir = true;
	}

	public boolean getIsMigInAir(){
		return isMigInAir;
	}

	public LocalDateTime getTimeMigDispatched() {
		return timeMigDispatched;
	}

	public void setTimeMigDispatched(LocalDateTime timeMigDispatched) {
		this.timeMigDispatched = timeMigDispatched;
	}
}
