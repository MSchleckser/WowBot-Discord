package wow.bot.systems.mig.hunter.actions;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import wow.bot.actions.framework.actions.message.recieved.MessageAction;
import wow.bot.actions.framework.annotations.ActionDescription;
import wow.bot.actions.framework.enums.EventFilter;
import wow.bot.systems.mig.hunter.FireTypes;
import wow.bot.systems.mig.hunter.MigHunter;

@ActionDescription(value = ".fox", commandDescription = ".fox", filters = {EventFilter.MESSAGE_RECIEVED})
public class FoxAction extends MessageAction {

	@Override
	public boolean handleAction(MessageReceivedEvent event) {
		String message = MigHunter.getInstance().rollHit(FireTypes.FOX, event.getMessage());
		event.getChannel().sendMessage(message).queue();
		return true;
	}


}
