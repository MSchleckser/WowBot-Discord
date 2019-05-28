package wow.bot.actions.framework.actions.message.recieved;

import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import wow.bot.actions.framework.Action;

public abstract class MessageReceivedAction extends Action {

	public MessageReceivedAction(){
		super();
	}

	@Override
	public boolean handleAction(Event event) {
		MessageReceivedEvent messageReceivedEvent = (MessageReceivedEvent) event;

		if(messageReceivedEvent.getAuthor().isBot())
			return true;

		return handleAction(messageReceivedEvent);
	}

	public abstract boolean handleAction(MessageReceivedEvent event);

}
