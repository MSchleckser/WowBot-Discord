package wow.bot.systems.channel.filter.actions;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wow.bot.actions.framework.actions.message.recieved.MessageAction;
import wow.bot.actions.framework.annotations.ActionDescription;
import wow.bot.actions.framework.enums.EventFilter;
import wow.bot.systems.channel.filter.rest.FilterService;
import wow.bot.systems.user.authentication.UserAuthenticator;

@ActionDescription(value = ".mute this", filters = EventFilter.MESSAGE_RECIEVED, adminCommand = true)
public class MuteChannel extends MessageAction {

	private Logger logger = LoggerFactory.getLogger(MuteChannel.class);
	private FilterService filterService = FilterService.getInstance();
	private UserAuthenticator authenticator = new UserAuthenticator();

	@Override
	public boolean handleAction(MessageReceivedEvent event) {
		String role = authenticator.getUserRole(event.getAuthor().getAsMention());
		if(!role.equals("ADMIN")) {
			event.getChannel().sendMessage("Sorry, you aren't authorized to do this action.").queue();
			return true;
		}

		String channelName = event.getChannel().getName();
		String message = "";
		if(filterService.isChannelInFilter(channelName)){

			try {
				filterService.removeChannel(channelName);
			} catch (Exception e) {
				logger.error("Error encountered while removing channel from filter.", e);
				message = "Huh. Something when wrong. I couldn't mute this channel.";
			}

			message = message.isEmpty() ? "Channel muted. I will now listed for commands here." : message;
		} else {
			message = "This channel is already muted.";
		}

		event.getChannel().sendMessage(message).queue();
		return true;
	}
}
