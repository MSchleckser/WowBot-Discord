package wow.bot.actions.framework.actions.message.recieved;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import wow.bot.WowBot;
import wow.bot.actions.framework.annotations.ActionDescription;

@ActionDescription(value = ".git", commandDescription = ".git")
public class GitAction extends MessageReceivedAction {
	@Override
	public boolean handleAction(MessageReceivedEvent event) {
		try {
			String discord = WowBot.getConfig().getProperty("github.discord");
			String rest = WowBot.getConfig().getProperty("github.rest");
			((MessageReceivedEvent) event).getChannel().sendMessage("Discord Bot Github: " + discord).queue();
			((MessageReceivedEvent) event).getChannel().sendMessage("Rest Service Github: " + rest).queue();
			return true;
		} catch (Exception e){

		}

		return false;
	}
}
