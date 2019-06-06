package wow.bot.mig.hunter.actions.kills;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import wow.bot.actions.framework.actions.message.recieved.MessageReceivedAction;
import wow.bot.actions.framework.annotations.ActionDescription;


@ActionDescription(value = "^((\\.kills)(?!(\\w|\\W)))", commandDescription = ".kills")
public class KillCountAction extends MessageReceivedAction {

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

	public String getCountMessage() {
		return countMessage;
	}

	public void setCountMessage(String countMessage) {
		this.countMessage = countMessage;
	}

	public MigHunterRestCallHandler getMigHunterRestCallHandler() {
		return migHunterRestCallHandler;
	}

	public void setMigHunterRestCallHandler(MigHunterRestCallHandler migHunterRestCallHandler) {
		this.migHunterRestCallHandler = migHunterRestCallHandler;
	}
}
