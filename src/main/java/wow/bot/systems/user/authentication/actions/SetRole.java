package wow.bot.systems.user.authentication.actions;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import wow.bot.actions.framework.actions.message.recieved.MessageAction;
import wow.bot.actions.framework.annotations.ActionDescription;
import wow.bot.actions.framework.enums.EventFilter;
import wow.bot.systems.user.authentication.UserAuthenticator;

@ActionDescription(value = "(.setRole) (<@(.*?)>) (\\w)+", filters = EventFilter.MESSAGE_RECIEVED)
public class SetRole extends MessageAction {

	private UserAuthenticator userAuthenticator = new UserAuthenticator();

	@Override
	public boolean handleAction(MessageReceivedEvent event) {
		String role = userAuthenticator.getUserRole(event.getAuthor().getAsMention());

		if(!"ADMIN".equals(role)){
			event.getChannel().sendMessage("You don't have those privileges.");
			return true;
		}


		if(userAuthenticator.setUserRole(event.getMessage().getMentionedUsers().get(0).getAsMention(), event.getMessage().getContentRaw().split(" ")[2])){
			event.getChannel().sendMessage("User's role updated.").queue();
		} else {
			event.getChannel().sendMessage("Huh, that's weird. I can't update that user's role.").queue();
		}

		return true;
	}

	public UserAuthenticator getUserAuthenticator() {
		return userAuthenticator;
	}

	public void setUserAuthenticator(UserAuthenticator userAuthenticator) {
		this.userAuthenticator = userAuthenticator;
	}
}
