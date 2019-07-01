package wow.bot.systems.channel.filter.rest;

import kong.unirest.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ChannelService {

	private final String basePath = "http://localhost:8080/channels/";
	private final String addChannel = "http://localhost:8080/channels/filtered/add/{channelName}";
	private final String removeChannel = "http://localhost:8080/channels/filtered/remove/{channelName}";

	public List<String> getFilteredChannels() {
		HttpResponse<JsonNode> response = Unirest.get(basePath + "filtered").asJson();
		return StreamSupport.stream(response.getBody().getArray().spliterator(), false)
				.map(Object::toString)
				.collect(Collectors.toList());
	}

	public boolean addChannel(String channelName){
		return Unirest.post(addChannel).routeParam("channelName", channelName).asString().getBody().equals("Successful");
	}

	public boolean removeChannel(String channelName){
		return Unirest.post(removeChannel).routeParam("channelName", channelName).asString().getBody().equals("Successful");
	}
}
