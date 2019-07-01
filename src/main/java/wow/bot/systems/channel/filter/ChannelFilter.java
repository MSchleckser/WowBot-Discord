package wow.bot.systems.channel.filter;

import java.util.HashSet;
import java.util.Set;

public class ChannelFilter {
	private static ChannelFilter ourInstance = new ChannelFilter();

	public static ChannelFilter getInstance() {
		return ourInstance;
	}

	private Set<String> filteredChannels = new HashSet<>();

	private ChannelFilter() {
	}

	public void addChannel(String channelName){
		filteredChannels.add(channelName);
	}

	public void removeChannel(String channelName){
		filteredChannels.remove(channelName);
	}

	public boolean containsChannel(String channelName){
		return filteredChannels.contains(channelName);
	}

}
