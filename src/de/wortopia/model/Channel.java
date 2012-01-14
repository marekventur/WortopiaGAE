package de.wortopia.model;

import com.google.appengine.api.channel.ChannelMessage;
import com.google.appengine.api.channel.ChannelService;
import com.google.appengine.api.channel.ChannelServiceFactory;

public class Channel {
	private String token;
	private ChannelService channelService;
	private int size;
	
	public Channel(int size) {
		this.size = size;
		channelService = ChannelServiceFactory.getChannelService();
	}

	public String getToken() {
		if (token == null) 
			token = channelService.createChannel(String.valueOf(size));
		return token;
	}
	
	public void sendMessage(String message) {
	    channelService.sendMessage(new ChannelMessage(String.valueOf(size), message));
	}
}
