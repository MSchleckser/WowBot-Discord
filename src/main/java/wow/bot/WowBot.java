package wow.bot;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wow.bot.actions.framework.action.event.listenter.ActionDispatcher;
import wow.bot.mig.hunter.feature.dispatcher.MigDispatcherScheduler;
import wow.bot.mig.hunter.MigHunter;

import javax.security.auth.login.LoginException;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class WowBot {
	private static Properties config;

	private static JDA bot;
	private static Logger logger = LoggerFactory.getLogger(WowBot.class);

	public static void main(String[] args) {
		config = loadProperties("src/main/resources/config.properties");
		ActionDispatcher actionDispatcher = ActionDispatcher.getInstance("wow.bot");
		MigDispatcherScheduler.getInstance().setMigHunter(MigHunter.getInstance());
		MigDispatcherScheduler.getInstance().scheduleNewMig();
		try {
			bot = new JDABuilder(System.getenv("WOWBOT_TOKEN"))
					.addEventListener(actionDispatcher).build();

			bot.awaitReady();
		} catch (LoginException e) {
			logger.error("Unable to login with the provided token.");
		} catch (InterruptedException e) {
			logger.error("Interrupted exception during startup.", e);
		}
	}

	private static Properties loadProperties(String path) {
		Properties properties = new Properties();

		try {
			properties.load(new FileInputStream(path));
		} catch (IOException e) {
			e.printStackTrace();
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
}
