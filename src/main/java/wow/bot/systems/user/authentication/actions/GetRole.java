package wow.bot.systems.user.authentication.actions;

import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;
import wow.bot.actions.framework.actions.privte.message.recieved.PrivateMessageAction;
import wow.bot.actions.framework.annotations.ActionDescription;
import wow.bot.actions.framework.enums.EventFilter;
import wow.bot.systems.user.authentication.UserAuthenticator;

@ActionDescription(value = ".role", filters = {EventFilter.PRIVATE_MESSAGE_RECIEVED})
public class GetRole extends PrivateMessageAction {

	private UserAuthenticator userAuthenticator = new UserAuthenticator();

	@Override
	public boolean handleAction(PrivateMessageReceivedEvent event) {
		System.out.println(event.getAuthor().getAsMention());
		event.getChannel().sendMessage(userAuthenticator.getUserRole(event.getAuthor().getAsMention())).queue();
		return true;
	}
}
