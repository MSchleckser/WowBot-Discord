package wow.bot.mig.hunter.feature.dispatcher;

import org.quartz.*;
import wow.bot.WowBot;
import wow.bot.mig.hunter.MigHunter;

public class MigDispatcher implements Job {

	@Override
	public void execute(JobExecutionContext context) {
		MigHunter migHunter = MigHunter.getInstance();

		if(migHunter.getIsMigInAir()) {
			scheduleNewMig();
			return;
		}

		dispatchMig(migHunter);
	}

	private void dispatchMig(MigHunter migHunter){
		WowBot.getBot().getTextChannels().stream()
				.filter(textChannel -> textChannel.getName().contains("sandbox"))
				.findFirst().get()
				.sendMessage("Mig! 12 O'Clock!").queue();

		migHunter.dispatchMig();
		scheduleNewMig();
	}



	private void scheduleNewMig(){
		MigDispatcherScheduler.getInstance().scheduleNewMig();
	}
}
