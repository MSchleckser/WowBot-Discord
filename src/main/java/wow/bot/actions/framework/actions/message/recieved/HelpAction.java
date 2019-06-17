package wow.bot.actions.framework.actions.message.recieved;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import wow.bot.actions.framework.Action;
import wow.bot.actions.framework.action.event.listenter.ActionDispatcher;
import wow.bot.actions.framework.annotations.ActionDescription;
import wow.bot.actions.framework.enums.EventFilter;

import java.util.stream.Collectors;

@ActionDescription(value = ".help", commandDescription = ".help", filters = {EventFilter.MESSAGE_RECIEVED})
public class HelpAction extends MessageAction {

	@Override
	public boolean handleAction(MessageReceivedEvent event) {
		String commands = ActionDispatcher.getInstance().getMessageReceivedActions().stream().map(Action::getCommandDescription)
				.collect(Collectors.joining(", "));
		event.getChannel().sendMessage(commands).queue();
		return true;
	}
}
