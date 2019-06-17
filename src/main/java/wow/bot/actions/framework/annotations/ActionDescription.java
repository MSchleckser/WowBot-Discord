package wow.bot.actions.framework.annotations;

import wow.bot.actions.framework.enums.EventFilter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ActionDescription {
    String value() default "";
    boolean enabled() default true;
    String commandDescription() default "";
    String helpDescription() default "";
    int regexFlags() default 0;
    EventFilter[] filters() default {};
}
