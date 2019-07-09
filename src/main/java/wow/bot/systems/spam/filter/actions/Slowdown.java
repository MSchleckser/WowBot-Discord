package wow.bot.systems.spam.filter.actions;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import wow.bot.actions.framework.actions.message.recieved.MessageAction;
import wow.bot.actions.framework.annotations.ActionDescription;
import wow.bot.actions.framework.enums.EventFilter;
import wow.bot.systems.spam.filter.services.SpamFilterService;
import wow.bot.systems.user.authentication.enums.Role;

@ActionDescription(value = ".slowdown \\d+", filters = EventFilter.MESSAGE_RECIEVED, minimumPrivilegeLevel = Role.ADMIN, commandDescription = ".slowdown duration (Admin only)")
public class Slowdown extends MessageAction {

	private SpamFilterService spamFilterService = SpamFilterService.getInstance();

	@Override
	public boolean handleAction(MessageReceivedEvent event) {
		float slowdown = Float.parseFloat(event.getMessage().getContentRaw().split(" ")[1]);
		spamFilterService.setSlowdown(slowdown);
		event.getChannel().sendMessage("Slowdown timer set to " + slowdown + ".").queue();
		return true;
	}
}
