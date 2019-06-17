package wow.bot.actions.framework.actions.privte.message.recieved;

import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;
import wow.bot.actions.framework.Action;

public abstract class PrivateMessageAction extends Action {

	public PrivateMessageAction(){
		super();
	}

	@Override
	public boolean handleAction(Event event) {
		PrivateMessageReceivedEvent messageReceivedEvent = (PrivateMessageReceivedEvent) event;

		if(messageReceivedEvent.getAuthor().isBot())
			return true;

		return handleAction(messageReceivedEvent);
	}

	public abstract boolean handleAction(PrivateMessageReceivedEvent event);

}
