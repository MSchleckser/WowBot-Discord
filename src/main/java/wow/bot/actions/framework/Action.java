package wow.bot.actions.framework;

import net.dv8tion.jda.core.events.Event;
import wow.bot.actions.framework.annotations.ActionDescription;
import wow.bot.actions.framework.enums.EventFilter;
import wow.bot.systems.user.authentication.enums.Role;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public abstract class Action {

	private static final RuntimeException noAnnotationException = new RuntimeException("Types extending Action require the ActionDescription annotation");

    private boolean isEnabled = true;
    private Pattern regex;
    private String helpDescription;
    private String commandDescription;
    private List<EventFilter> filters;
    private Role minimumPrivilegeLevel;

    public abstract boolean handleAction(Event event);

    public Action() {
    	ActionDescription description = getAnnotation();
    	this.regex = Pattern.compile(description.value(), description.regexFlags());
    	this.helpDescription = description.helpDescription();
    	this.commandDescription = description.commandDescription();
    	this.filters = Arrays.asList(description.filters());
    	this.minimumPrivilegeLevel = description.minimumPrivilegeLevel();
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

	public List<EventFilter> getFilters() {
		return filters;
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

	public Role getMinimumPrivilegeLevel() {
		return minimumPrivilegeLevel;
	}
}
