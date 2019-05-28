package wow.bot.actions.framework.action.event.listenter;

import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import wow.bot.actions.framework.Action;
import wow.bot.actions.framework.actions.message.recieved.MessageReceivedAction;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ActionDispatcher extends ListenerAdapter {

	private static ActionDispatcher INSTANCE = null;

	public static ActionDispatcher getInstance(){
		if(INSTANCE == null)
			throw new NullPointerException("Action Dispatcher has not been initialized. Call ActionDispatcher.getInstance(String path) to initialize Action Dispatcher.");

		return INSTANCE;
	}

	public static ActionDispatcher getInstance(String path){
		if(INSTANCE == null)
			INSTANCE = new ActionDispatcher(path);

		return INSTANCE;
	}

	private List<? extends Action> actions;

	private ActionDispatcher(String path) {

		Reflections reflections = new Reflections(path, new SubTypesScanner(false));
		actions = reflections.getSubTypesOf(MessageReceivedAction.class).stream()
				.map(clazz -> (MessageReceivedAction)this.convertClassToAction(clazz))
				.filter(action -> action != null)
				.collect(Collectors.toList());
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

		if(!event.getChannel().getName().equals("bot-sandbox"))
			return;

		MessageReceivedAction foundAction = actions.stream()
				.map(action -> (MessageReceivedAction) action)
				.filter(action -> action.getRegex().matcher(event.getMessage().getContentRaw()).matches())
				.findFirst()
				.orElse(null);

		if(foundAction == null)
			return;

		foundAction.handleAction(event);

	}

	public List<? extends Action> getActions(){
		return new ArrayList<>(actions);
	}

	private Action convertClassToAction(Class<? extends Action> clazz){
		try {
			return  clazz.getConstructor().newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}

		return null;
	}
}
