package wow.bot.mig.hunter.actions;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import wow.bot.actions.framework.actions.message.recieved.MessageReceivedAction;
import wow.bot.actions.framework.annotations.ActionDescription;
import wow.bot.mig.hunter.FireTypes;
import wow.bot.mig.hunter.MigHunter;

@ActionDescription(value = ".guns", commandDescription = ".guns")
public class GunsAction extends MessageReceivedAction {
	@Override
	public boolean handleAction(MessageReceivedEvent event) {
		String message = MigHunter.getInstance().rollHit(FireTypes.GUNS, event.getMessage());
		event.getChannel().sendMessage(message).queue();
		return true;
	}
}
