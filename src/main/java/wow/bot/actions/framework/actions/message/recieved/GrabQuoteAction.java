package wow.bot.actions.framework.actions.message.recieved;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import wow.bot.actions.QuoteGrabber;
import wow.bot.actions.framework.annotations.ActionDescription;
import wow.bot.actions.framework.enums.EventFilter;

@ActionDescription(value = "^(.grab) (<@(.*?)>)", commandDescription = ".grab @User", filters = {EventFilter.MESSAGE_RECIEVED})
public class GrabQuoteAction extends MessageAction {

	private QuoteGrabber quoteGrabber = new QuoteGrabber();

	@Override
	public boolean handleAction(MessageReceivedEvent event) {
		String response = quoteGrabber.getQuote(event);
		event.getChannel().sendMessage(response).queue();
		return true;
	}
}
