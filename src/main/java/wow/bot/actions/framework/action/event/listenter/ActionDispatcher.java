package wow.bot.actions.framework.action.event.listenter;

import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wow.bot.actions.framework.Action;
import wow.bot.actions.framework.actions.message.recieved.MessageReceivedAction;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ActionDispatcher extends ListenerAdapter {

	private static ActionDispatcher INSTANCE = null;
	private Logger logger = LoggerFactory.getLogger(ActionDispatcher.class);
	private List<Action> actions;

	private ActionDispatcher(String path) {

		Reflections reflections = new Reflections(path, new SubTypesScanner(false));
		actions = reflections.getSubTypesOf(MessageReceivedAction.class).stream()
				.map(clazz -> (MessageReceivedAction) this.convertClassToAction(clazz))
				.filter(Objects::isNull)
				.collect(Collectors.toList());
	}

	public static ActionDispatcher getInstance() {
		if (INSTANCE == null)
			throw new NullPointerException("Action Dispatcher has not been initialized. Call ActionDispatcher.getInstance(String path) to initialize Action Dispatcher.");

		return INSTANCE;
	}

	public static ActionDispatcher getInstance(String path) {
		if (INSTANCE == null)
			INSTANCE = new ActionDispatcher(path);

		return INSTANCE;
	}

	@Override
	public void onReady(ReadyEvent event) {
		event.getJDA().getTextChannels()
				.stream().filter(textChannel -> textChannel.getName().contains("bot-sandbox"))
				.findFirst().get()
				.sendMessage("The wowinator is ready.").queue();
	}

	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		if (event.getAuthor().isBot())
			return;

		if (!event.getChannel().getName().equals("bot-sandbox"))
			return;

		MessageReceivedAction foundAction = actions.stream()
				.map(action -> (MessageReceivedAction) action)
				.filter(action -> action.getRegex().matcher(event.getMessage().getContentRaw()).matches())
				.findFirst()
				.orElse(null);

		if (foundAction == null)
			return;

		foundAction.handleAction(event);

	}

	public List<Action> getActions() {
		return new ArrayList<>(actions);
	}

	private Action convertClassToAction(Class<? extends Action> clazz) {
		try {
			return clazz.getConstructor().newInstance();
		} catch (Exception e) {
			logger.error("Exception when converting class to instance.", e);
		}

		return null;
	}
}
