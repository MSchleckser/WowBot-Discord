package wow.bot.actions.framework.actions.message.recieved;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import wow.bot.WowBot;
import wow.bot.actions.framework.annotations.ActionDescription;
import wow.bot.actions.framework.enums.EventFilter;

@ActionDescription(value = ".version", commandDescription = ".version", filters = {EventFilter.MESSAGE_RECIEVED})
public class GetVersionAction extends MessageAction {
	@Override
	public boolean handleAction(MessageReceivedEvent event) {
		String response = WowBot.getConfig().getProperty("version");
		event.getChannel().sendMessage(response).queue();
		return true;
	}
}
