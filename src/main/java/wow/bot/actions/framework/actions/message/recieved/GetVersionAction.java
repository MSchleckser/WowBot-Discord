package wow.bot.actions.framework.actions.message.recieved;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import wow.bot.WowBot;
import wow.bot.actions.framework.annotations.ActionDescription;

@ActionDescription(value = ".version", commandDescription = ".version")
public class GetVersionAction extends MessageReceivedAction{
	@Override
	public boolean handleAction(MessageReceivedEvent event) {
		String response = WowBot.getConfig().getProperty("version");
		((MessageReceivedEvent) event).getChannel().sendMessage(response).queue();
		return true;
	}
}
