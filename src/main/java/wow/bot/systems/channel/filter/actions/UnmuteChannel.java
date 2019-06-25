package wow.bot.systems.channel.filter.actions;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import wow.bot.actions.framework.actions.message.recieved.MessageAction;
import wow.bot.actions.framework.annotations.ActionDescription;
import wow.bot.actions.framework.enums.EventFilter;
import wow.bot.systems.channel.filter.rest.ChannelService;
import wow.bot.systems.user.authentication.UserAuthenticator;

import java.util.stream.Collectors;

@ActionDescription(value = ".unmute this", filters = EventFilter.MESSAGE_RECIEVED)
public class UnmuteChannel extends MessageAction {

	private ChannelService channelService = new ChannelService();
	private UserAuthenticator authenticator = new UserAuthenticator();

	@Override
	public boolean handleAction(MessageReceivedEvent event) {
		String role = authenticator.getUserRole(event.getAuthor().getAsMention());
		if(!role.equals("ADMIN")) {
			event.getChannel().sendMessage("Sorry, you aren't authorized to do this action.").queue();
			return true;
		}

		String channelName = event.getChannel().getName();
		if(!channelService.isChannelFiltered(channelName)){
			if(channelService.addChannelToFilter(channelName)){
				event.getChannel().sendMessage("Channel un-muted. I will now listed for commands here.").queue();
				return true;
			}

			event.getChannel().sendMessage("Huh. Something when wrong. I couldn't un-mute this channel.").queue();
		}

		return true;
	}
}
