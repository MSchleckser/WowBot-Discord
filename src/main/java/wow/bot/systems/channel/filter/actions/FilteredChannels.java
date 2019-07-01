package wow.bot.systems.channel.filter.actions;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import wow.bot.actions.framework.actions.message.recieved.MessageAction;
import wow.bot.actions.framework.annotations.ActionDescription;
import wow.bot.actions.framework.enums.EventFilter;
import wow.bot.systems.channel.filter.rest.FilterService;
import wow.bot.systems.user.authentication.UserAuthenticator;

import java.util.stream.Collectors;

@ActionDescription(value = ".filteredChannels", filters = EventFilter.MESSAGE_RECIEVED)
public class FilteredChannels extends MessageAction {

	private FilterService filterService = FilterService.getInstance();
	private UserAuthenticator authenticator = new UserAuthenticator();

	@Override
	public boolean handleAction(MessageReceivedEvent event) {
		String role = authenticator.getUserRole(event.getAuthor().getAsMention());

		if(!role.equals("ADMIN")) {
			event.getChannel().sendMessage("Sorry, you aren't authorized to do this action.").queue();
			return true;
		}

		event.getChannel().sendMessage(filterService.getFilteredChannels().stream().collect(Collectors.joining(", "))).queue();
		return true;
	}
}
