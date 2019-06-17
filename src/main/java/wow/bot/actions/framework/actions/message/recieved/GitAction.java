package wow.bot.actions.framework.actions.message.recieved;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wow.bot.WowBot;
import wow.bot.actions.framework.annotations.ActionDescription;
import wow.bot.actions.framework.enums.EventFilter;

@ActionDescription(value = ".git", commandDescription = ".git", filters = {EventFilter.MESSAGE_RECIEVED})
public class GitAction extends MessageAction {

	private Logger logger = LoggerFactory.getLogger(GitAction.class);

	@Override
	public boolean handleAction(MessageReceivedEvent event) {
		try {
			String discord = WowBot.getConfig().getProperty("github.discord");
			String rest = WowBot.getConfig().getProperty("github.rest");
			event.getChannel().sendMessage("Discord Bot Github: " + discord).queue();
			event.getChannel().sendMessage("Rest Service Github: " + rest).queue();
			return true;
		} catch (Exception e){
			logger.error("Git action encountered an error.", e);
		}

		return false;
	}
}
