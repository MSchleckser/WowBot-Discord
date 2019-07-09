package wow.bot.systems.mig.hunter.actions.kills;

import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import wow.bot.actions.framework.actions.message.recieved.MessageAction;
import wow.bot.actions.framework.annotations.ActionDescription;
import wow.bot.actions.framework.enums.EventFilter;

import java.util.List;

@ActionDescription(value = "^(.kills) (<@(.*?)>)", commandDescription = ".kills @user", filters = {EventFilter.MESSAGE_RECIEVED})
public class KillCountForUserAction extends MessageAction {

	private String countMessage = "%s has killed %d migs.";
	private String zeroKillsMessage = "%s has not killed any migs. They are a disgrace to the country.";
	private MigHunterRestCallHandler migHunterRestCallHandler = new MigHunterRestCallHandler();

	@Override
	public boolean handleAction(MessageReceivedEvent event) {
		List<User> mentionedUsers = event.getMessage().getMentionedUsers();

		if(mentionedUsers.isEmpty())
			event.getChannel().sendMessage("You must mention somebody to get their kill count.");

		String user = mentionedUsers.get(0).getAsMention();
		int count = migHunterRestCallHandler.getNumberofKills(user);

		String userMention = event.getAuthor().getAsMention();
		String message = count > 0 ? String.format(countMessage, userMention, count) : String.format(zeroKillsMessage, userMention);

		event.getChannel().sendMessage(message).queue();
		return true;
	}
}
