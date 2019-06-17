package wow.bot.actions.framework.action.event.listenter;

import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wow.bot.actions.framework.Action;
import wow.bot.actions.framework.actions.message.recieved.MessageAction;
import wow.bot.actions.framework.actions.privte.message.recieved.PrivateMessageAction;
import wow.bot.actions.framework.annotations.ActionDescription;
import wow.bot.actions.framework.enums.EventFilter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class ActionDispatcher extends ListenerAdapter {

	private static ActionDispatcher INSTANCE = null;
	private Logger logger = LoggerFactory.getLogger(ActionDispatcher.class);
	private List<MessageAction> messageReceivedActions;
	private List<PrivateMessageAction> privateMessageReceivedActions;

	private ActionDispatcher(String path) {

		Reflections reflections = new Reflections(path);
		List<Action> actions = reflections.getTypesAnnotatedWith(ActionDescription.class).stream()
				.map(clazz -> convertClassToAction(clazz))
				.filter(Objects::nonNull).collect(Collectors.toList());

		messageReceivedActions = actions.stream()
				.filter(action -> action.getFilters().contains(EventFilter.MESSAGE_RECIEVED))
				.map(action -> (MessageAction) action)
				.collect(Collectors.toList());

		privateMessageReceivedActions = actions.stream()
				.filter(action -> action.getFilters().contains(EventFilter.PRIVATE_MESSAGE_RECIEVED))
				.map(action -> (PrivateMessageAction) action)
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
		Optional<TextChannel> channelOptional =
				event.getJDA().getTextChannels()
				.stream().filter(textChannel -> textChannel.getName().contains("bot-sandbox"))
				.findFirst();

		if(!channelOptional.isPresent()){
			logger.error("Unable to locate the channel bot-sandbox for ");
			return;
		}

		channelOptional.get().sendMessage("The wowinator is ready.").queue();
	}

	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		if (event.getAuthor().isBot())
			return;

		if (!event.getChannel().getName().equals("bot-sandbox"))
			return;

		MessageAction foundAction = messageReceivedActions.stream()
				.filter(action -> action.getRegex().matcher(event.getMessage().getContentRaw()).matches())
				.findFirst()
				.orElse(null);

		if (foundAction == null)
			return;

		foundAction.handleAction(event);

	}

	@Override
	public void onPrivateMessageReceived(PrivateMessageReceivedEvent event) {
		if (event.getAuthor().isBot())
			return;

		PrivateMessageAction foundAction = privateMessageReceivedActions.stream()
				.filter(action -> action.getRegex().matcher(event.getMessage().getContentRaw()).matches())
				.findFirst()
				.orElse(null);

		if (foundAction == null)
			return;

		foundAction.handleAction(event);
	}

	public List<Action> getMessageReceivedActions() {
		return new ArrayList<>(messageReceivedActions);
	}

	private Action convertClassToAction(Class<?> clazz) {
		try {
			return (Action)clazz.getConstructor().newInstance();
		} catch (Exception e) {
			logger.error("Exception when converting class "+ clazz.getCanonicalName() +" to instance.", e);

		}

		return null;
	}
}
