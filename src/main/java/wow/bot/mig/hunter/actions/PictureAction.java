package wow.bot.mig.hunter.actions;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import wow.bot.actions.framework.actions.message.recieved.MessageReceivedAction;
import wow.bot.actions.framework.annotations.ActionDescription;
import wow.bot.mig.hunter.MigHunter;

@ActionDescription(value = ".picture", commandDescription = ".picture")
public class PictureAction extends MessageReceivedAction {
	@Override
	public boolean handleAction(MessageReceivedEvent event) {
		String picture = "Clean";

		if(MigHunter.getInstance().getIsMigInAir())
			picture = "One on the scope";

		event.getChannel().sendMessage(picture).queue();

		return true;
	}
}
