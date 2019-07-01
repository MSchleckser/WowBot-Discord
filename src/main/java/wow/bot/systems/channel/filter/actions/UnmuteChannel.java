package wow.bot.systems.channel.filter.actions;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wow.bot.actions.framework.actions.message.recieved.MessageAction;
import wow.bot.actions.framework.annotations.ActionDescription;
import wow.bot.actions.framework.enums.EventFilter;
import wow.bot.systems.channel.filter.rest.FilterService;
import wow.bot.systems.user.authentication.UserAuthenticator;

@ActionDescription(value = ".unmute this", filters = EventFilter.MESSAGE_RECIEVED, adminCommand = true)
public class UnmuteChannel extends MessageAction {

	private Logger logger = LoggerFactory.getLogger(UnmuteChannel.class);
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
		if(!filterService.isChannelInFilter(channelName)){

			try {
				filterService.addChannel(channelName);
			} catch (Exception e) {
				logger.error("Error encountered while attempting to un-mute this channel.", e);
				message = "Huh. Something when wrong. I couldn't un-mute this channel.";
			}

			message = message.isEmpty() ? "Channel un-muted. I will now listed for commands here." : message;
		} else {
			message = "This channel is already un-muted.";
		}

		event.getChannel().sendMessage(message).queue();
		return true;
	}
}
