package wow.bot.systems.user.authentication.actions;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import wow.bot.actions.framework.actions.message.recieved.MessageAction;
import wow.bot.actions.framework.annotations.ActionDescription;
import wow.bot.actions.framework.enums.EventFilter;
import wow.bot.systems.user.authentication.UserAuthenticator;
import wow.bot.systems.user.authentication.enums.Role;

@ActionDescription(value = "(.setRole) (<@(.*?)>) (\\w)+", filters = EventFilter.MESSAGE_RECIEVED, minimumPrivilegeLevel = Role.ADMIN, commandDescription = ".setRole UserMention Role (Admin only)")
public class SetRole extends MessageAction {

	private UserAuthenticator userAuthenticator = new UserAuthenticator();

	@Override
	public boolean handleAction(MessageReceivedEvent event) {
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
