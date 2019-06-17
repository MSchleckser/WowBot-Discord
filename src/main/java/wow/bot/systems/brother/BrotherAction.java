package wow.bot.systems.brother;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import wow.bot.actions.framework.actions.message.recieved.MessageAction;
import wow.bot.actions.framework.annotations.ActionDescription;
import wow.bot.actions.framework.enums.EventFilter;
import wow.bot.systems.brother.fetcher.BrotherFetcher;

import java.util.regex.Pattern;

@ActionDescription(value = "brother",regexFlags = Pattern.CASE_INSENSITIVE,  commandDescription = "brother", filters = {EventFilter.MESSAGE_RECIEVED})
public class BrotherAction extends MessageAction {
	@Override
	public boolean handleAction(MessageReceivedEvent event) {
		BrotherFetcher fetcher = new BrotherFetcher();
		event.getChannel().sendMessage(fetcher.fetchBrother()).queue();

		return true;
	}
}
