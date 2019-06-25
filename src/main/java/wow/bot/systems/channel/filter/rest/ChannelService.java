package wow.bot.systems.channel.filter.rest;

import kong.unirest.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ChannelService {

	private String basePath = "http://localhost:8080/channels/";
	private GetRequest isChannelFiltered = Unirest.get("http://localhost:8080/channels/filtered/{channelName}");
	private HttpRequestWithBody addChannelToFilter = Unirest.post("http://localhost:8080/channels/filtered/add/{channelName}");

	public List<String> getFilteredChannels() {
		HttpResponse<JsonNode> response = Unirest.get(basePath + "filtered").asJson();
		return StreamSupport.stream(response.getBody().getArray().spliterator(), false)
				.map(Object::toString)
				.collect(Collectors.toList());
	}

	public boolean isChannelFiltered(String channelName) {
		return Boolean.valueOf(isChannelFiltered.routeParam("channelName", channelName).asString().getBody());
	}

	public boolean addChannelToFilter(String channelName){
		return addChannelToFilter.routeParam("channelName", channelName).asString().getBody().equals("Successful");
	}
}
