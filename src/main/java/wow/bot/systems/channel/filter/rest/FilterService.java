package wow.bot.systems.channel.filter.rest;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class FilterService {
	private static FilterService ourInstance = new FilterService();

	public static FilterService getInstance() {
		return ourInstance;
	}

	private ChannelService channelService = new ChannelService();
	private Set<String> filteredChannels = new HashSet<>();

	private FilterService() {
		filteredChannels.addAll(channelService.getFilteredChannels());
	}

	public List<String> getFilteredChannels(){
		return filteredChannels.stream().collect(Collectors.toList());
	}

	public void addChannel(String channelName) throws Exception {
		if(!channelService.addChannel(channelName)){
			throw new Exception("Unable to add " + channelName + "to the server.");
		}
		filteredChannels.add(channelName);
	}

	public void removeChannel(String channelName) throws Exception {
		if(!channelService.removeChannel(channelName)){
			throw new Exception("Unable to remove " + channelName + "from the server.");
		}
		filteredChannels.remove(channelName);
	}

	public boolean isChannelInFilter(String channelName){
		return filteredChannels.contains(channelName);
	}
}
