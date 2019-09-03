package wow.bot.systems.welcomer.actions;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import wow.bot.actions.framework.actions.message.recieved.MessageAction;
import wow.bot.actions.framework.annotations.ActionDescription;
import wow.bot.actions.framework.enums.EventFilter;
import wow.bot.systems.welcomer.service.WelcomeService;

@ActionDescription(value = ".welcome", commandDescription = ".welcome (admin only)",
		helpDescription = ".welcome (admin only)", filters = EventFilter.MESSAGE_RECIEVED)
public class SetWelcomeChannelAction extends MessageAction {

	private WelcomeService welcomeService = WelcomeService.getInstance();

	@Override
	public boolean handleAction(MessageReceivedEvent event) {
		welcomeService.setWelcomeChannel(event.getChannel().getName());
		event.getChannel().sendMessage(String.format("Using channel %s to welcome new users.", welcomeService.getWelcomeChannel())).queue();
		return true;
	}
}
