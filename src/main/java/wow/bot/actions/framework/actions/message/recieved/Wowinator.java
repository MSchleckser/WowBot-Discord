package wow.bot.actions.framework.actions.message.recieved;

import kong.unirest.Unirest;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import wow.bot.actions.framework.annotations.ActionDescription;

import java.util.regex.Pattern;

@ActionDescription(value = "Wow", regexFlags = Pattern.CASE_INSENSITIVE, commandDescription = "wow")
public class Wowinator extends MessageReceivedAction {

	public Wowinator() {
		super();
	}

	public static String getWow() {
		String wow = "Unable to wow. I am very saddened.";

		try {
			String response = Unirest.get("http://localhost:8080/wow").asString().getBody();
			wow = response.length() < 200 ? response : wow;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return wow;
	}

	@Override
	public boolean handleAction(MessageReceivedEvent event) {
		try {
			event.getChannel().sendMessage(getWow()).queue();
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
