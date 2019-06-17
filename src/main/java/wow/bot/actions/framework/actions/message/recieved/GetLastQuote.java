package wow.bot.actions.framework.actions.message.recieved;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import wow.bot.actions.QuoteGetter;
import wow.bot.actions.framework.annotations.ActionDescription;
import wow.bot.actions.framework.enums.EventFilter;

@ActionDescription(value = "(.getLast) (<@(.*?)>)", commandDescription = ".getLast @User", filters = {EventFilter.MESSAGE_RECIEVED})
public class GetLastQuote extends MessageAction {

	private QuoteGetter quoteGetter = new QuoteGetter();

	@Override
	public boolean handleAction(MessageReceivedEvent event) {
		String retMessage = quoteGetter.getQuote(event);
		event.getChannel().sendMessage(retMessage).queue();
		return true;
	}
}
