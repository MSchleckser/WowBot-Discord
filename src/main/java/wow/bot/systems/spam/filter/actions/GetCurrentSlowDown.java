package wow.bot.systems.spam.filter.actions;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import wow.bot.actions.framework.actions.message.recieved.MessageAction;
import wow.bot.actions.framework.annotations.ActionDescription;
import wow.bot.actions.framework.enums.EventFilter;
import wow.bot.systems.spam.filter.services.SpamFilterService;

@ActionDescription(value = ".slowdown", filters = EventFilter.MESSAGE_RECIEVED, commandDescription = ".slowdown")
public class GetCurrentSlowDown extends MessageAction {

	private SpamFilterService spamFilterService = SpamFilterService.getInstance();

	@Override
	public boolean handleAction(MessageReceivedEvent event) {
		event.getChannel().sendMessage("The current slowdown time is " + spamFilterService.getSlowdown()).queue();

		return true;
	}
}
