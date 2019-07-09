package wow.bot.systems.mig.hunter.actions.kills;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import wow.bot.actions.framework.actions.message.recieved.MessageAction;
import wow.bot.actions.framework.annotations.ActionDescription;
import wow.bot.actions.framework.enums.EventFilter;


@ActionDescription(value = "^((\\.kills)(?!(\\w|\\W)))", commandDescription = ".kills", filters = {EventFilter.MESSAGE_RECIEVED})
public class KillCountAction extends MessageAction {

	private String countMessage = "%s, you have killed %d migs.";
	private String zeroKillsMessage = "%s, you have not killed any migs. You are a disgrace to the country.";
	private MigHunterRestCallHandler migHunterRestCallHandler = new MigHunterRestCallHandler();

	@Override
	public boolean handleAction(MessageReceivedEvent event) {
		int count = migHunterRestCallHandler.getNumberofKills(event.getAuthor().getAsMention());

		String userMention = event.getAuthor().getAsMention();
		String message = count > 0 ? String.format(countMessage, userMention, count) : String.format(zeroKillsMessage, userMention);

		event.getChannel().sendMessage(message).queue();
		return true;
	}
}
