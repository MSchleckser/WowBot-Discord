package wow.bot.brother;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import wow.bot.actions.framework.actions.message.recieved.MessageReceivedAction;
import wow.bot.actions.framework.annotations.ActionDescription;
import wow.bot.brother.fetcher.BrotherFetcher;

import java.util.regex.Pattern;

@ActionDescription(value = "brother",regexFlags = Pattern.CASE_INSENSITIVE,  commandDescription = "brother")
public class BrotherAction extends MessageReceivedAction {
	@Override
	public boolean handleAction(MessageReceivedEvent event) {
		BrotherFetcher fetcher = new BrotherFetcher();
		event.getChannel().sendMessage(fetcher.fetchBrother()).queue();

		return true;
	}
}
