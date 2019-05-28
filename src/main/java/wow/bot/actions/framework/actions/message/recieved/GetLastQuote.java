package wow.bot.actions.framework.actions.message.recieved;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import wow.bot.actions.QuoteGetter;
import wow.bot.actions.framework.annotations.ActionDescription;

@ActionDescription(value = "(.getLast) (<@(.*?)>)", commandDescription = ".getLast @User")
public class GetLastQuote extends MessageReceivedAction {
	@Override
	public boolean handleAction(MessageReceivedEvent event) {
		String retMessage = QuoteGetter.getQuote(event);
		event.getChannel().sendMessage(retMessage).queue();
		return true;
	}
}
