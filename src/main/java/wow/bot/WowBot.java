package wow.bot;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wow.bot.actions.framework.action.event.listenter.ActionDispatcher;
import wow.bot.systems.mig.hunter.feature.dispatcher.MigDispatcherScheduler;
import wow.bot.systems.mig.hunter.MigHunter;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.util.Properties;

public class WowBot {
	private static Properties config;

	private static JDA bot;
	private static Logger logger = LoggerFactory.getLogger(WowBot.class);

	public static void main(String[] args) {
		config = loadProperties("/config.properties");
		ActionDispatcher actionDispatcher = ActionDispatcher.getInstance("wow.bot");
		MigDispatcherScheduler.getInstance().setMigHunter(MigHunter.getInstance());
		MigDispatcherScheduler.getInstance().scheduleNewMig();
		String token = getToken(config);
		try {
			bot = new JDABuilder(token)
					.addEventListener(actionDispatcher).build();

			bot.awaitReady();
		} catch (LoginException e) {
			logger.error("Unable to login with the provided token. Token: " + token);
		} catch (InterruptedException e) {
			logger.error("Interrupted exception during startup.", e);
		}
	}

	private static Properties loadProperties(String path) {
		Properties properties = new Properties();

		try {
			properties.load(new WowBot().getClass().getResourceAsStream(path));
		} catch (IOException e) {
			logger.error("Error encountered when loading system properties.", e);
		}

		return properties;
	}

	public static void setBot(JDA bot){
		WowBot.bot = bot;
	}

	public static JDA getBot(){
		return bot;
	}

	public static Properties getConfig() {
		return config;
	}

	private static String getToken(Properties inputStream){
		String token = System.getenv("WOWBOT_TOKEN");

		if(token == null)
			token = inputStream.getProperty("WOWBOT_TOKEN").trim();

		return token;
	}
}
