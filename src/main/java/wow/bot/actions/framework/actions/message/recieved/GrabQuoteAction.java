package wow.bot.actions.framework.actions.message.recieved;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import wow.bot.actions.QuoteGrabber;
import wow.bot.actions.framework.annotations.ActionDescription;

@ActionDescription(value = "^(.grab) (<@(.*?)>)", commandDescription = ".grab @User")
public class GrabQuoteAction extends MessageReceivedAction{

	@Override
	public boolean handleAction(MessageReceivedEvent event) {
		String response = QuoteGrabber.getQuote(event);
		event.getChannel().sendMessage(response).queue();
		return true;
	}
}
