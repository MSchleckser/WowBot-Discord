package wow.bot.systems.mig.hunter.feature.dispatcher;

import net.dv8tion.jda.core.entities.TextChannel;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wow.bot.WowBot;
import wow.bot.systems.mig.hunter.MigHunter;

import java.util.Optional;

public class MigDispatcher implements Job {

	private Logger logger = LoggerFactory.getLogger(MigDispatcher.class);

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
		Optional<TextChannel> channelOptional = WowBot.getBot().getTextChannels().stream()
				.filter(textChannel -> textChannel.getName().contains("sandbox"))
				.findFirst();

		if(!channelOptional.isPresent()){
			logger.error("Unable to locate sandox channel.");
			return;
		}

		channelOptional.get().sendMessage("Mig! 12 O'Clock!").queue();

		migHunter.dispatchMig();
		scheduleNewMig();
	}



	private void scheduleNewMig(){
		MigDispatcherScheduler.getInstance().scheduleNewMig();
	}
}
