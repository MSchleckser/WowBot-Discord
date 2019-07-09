package wow.bot.systems.channel.filter.actions;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import wow.bot.actions.framework.actions.message.recieved.MessageAction;
import wow.bot.actions.framework.annotations.ActionDescription;
import wow.bot.actions.framework.enums.EventFilter;
import wow.bot.systems.channel.filter.rest.FilterService;

import java.util.stream.Collectors;

@ActionDescription(value = ".filteredChannels", filters = EventFilter.MESSAGE_RECIEVED)
public class FilteredChannels extends MessageAction {

	private FilterService filterService = FilterService.getInstance();

	@Override
	public boolean handleAction(MessageReceivedEvent event) {
		event.getChannel().sendMessage(filterService.getFilteredChannels().stream().collect(Collectors.joining(", "))).queue();
		return true;
	}
}
