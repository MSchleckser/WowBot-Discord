package wow.bot.systems.mig.hunter.actions;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import wow.bot.actions.framework.actions.message.recieved.MessageAction;
import wow.bot.actions.framework.annotations.ActionDescription;
import wow.bot.actions.framework.enums.EventFilter;
import wow.bot.systems.mig.hunter.MigHunter;

@ActionDescription(value = ".picture", commandDescription = ".picture", filters = {EventFilter.MESSAGE_RECIEVED})
public class PictureAction extends MessageAction {
	@Override
	public boolean handleAction(MessageReceivedEvent event) {
		String picture = "Clean";

		if(MigHunter.getInstance().getIsMigInAir())
			picture = "One on the scope";

		event.getChannel().sendMessage(picture).queue();

		return true;
	}
}
