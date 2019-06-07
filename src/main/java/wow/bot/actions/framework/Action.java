package wow.bot.actions.framework;

import net.dv8tion.jda.core.events.Event;
import wow.bot.actions.framework.annotations.ActionDescription;

import java.util.Arrays;
import java.util.regex.Pattern;

public abstract class Action {

	private static final RuntimeException noAnnotationException = new RuntimeException("Types extending Action require the ActionDescription annotation");

    private boolean isEnabled = true;
    private Pattern regex;
    private String helpDescription;
    private String commandDescription;
    private Class<? extends Event> eventType;

    public abstract boolean handleAction(Event event);

    public Action() {
    	ActionDescription description = getAnnotation();
    	this.regex = Pattern.compile(description.value(), description.regexFlags());
    	this.helpDescription = description.helpDescription();
    	this.commandDescription = description.commandDescription();
	}

	public boolean isEnabled() {
        return isEnabled;
    }

    public Pattern getRegex() {
        return regex;
    }

    public String getHelpDescription() {
        return helpDescription;
    }

    public String getCommandDescription() {
        return commandDescription;
    }

    public Class<? extends Event> getEventType() {
        return eventType;
    }

    private ActionDescription getAnnotation(){
		ActionDescription description = Arrays.stream(this.getClass().getAnnotations())
				.filter(annotation -> annotation instanceof ActionDescription)
				.map(annotation -> (ActionDescription) annotation)
				.findFirst().orElse(null);

		if(description == null){
			throw noAnnotationException;
		}

		return description;
	}
}
