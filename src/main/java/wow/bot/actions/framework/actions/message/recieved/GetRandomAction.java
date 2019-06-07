package wow.bot.actions.framework.actions.message.recieved;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import wow.bot.actions.QuoteGetter;
import wow.bot.actions.framework.annotations.ActionDescription;

@ActionDescription(value = "^(.getRandom) (<@(.*?)>)", commandDescription = ".getRandom @User")
public class GetRandomAction extends MessageReceivedAction{
	@Override
	public boolean handleAction(MessageReceivedEvent event) {
		String response = QuoteGetter.getRandomQuote(event);
		event.getChannel().sendMessage(response).queue();
		return true;
	}
}
