package wow.bot.systems.welcomer.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kong.unirest.Unirest;
import wow.bot.systems.welcomer.models.WelcomeChannel;

public class WelcomeService {
	private static WelcomeService ourInstance = new WelcomeService();

	public static WelcomeService getInstance() {
		return ourInstance;
	}

	private String welcomeChannel;
	private ObjectMapper mapper = new ObjectMapper();

	private WelcomeService() {
		fetchWelcomeChannel();
	}

	public String getWelcomeChannel() {
		return welcomeChannel;
	}

	public void setWelcomeChannel(String welcomeChannel) {
		this.welcomeChannel = welcomeChannel;
		updateWelcomeChannel(welcomeChannel);
	}

	private final void fetchWelcomeChannel(){
			try {
				welcomeChannel = Unirest.get("http://localhost:8080/welcomechannel")
						.asString().getBody();
			} catch (Exception e) {
				e.printStackTrace();
				welcomeChannel = "";
			}
	}

	private final void updateWelcomeChannel(String channelName) {

		System.out.println("update " + channelName);
		WelcomeChannel welcomeChannel = new WelcomeChannel();
		welcomeChannel.setChannelName(channelName);


		try {
			String object = mapper.writeValueAsString(welcomeChannel);
			Unirest.post("http://localhost:8080/welcomechannel")
					.header("Content-Type", "application/json")
					.body(object).asString().getBody();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}
}
