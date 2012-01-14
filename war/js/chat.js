function Chat(channelToken, user) {
	this.channelToken = channelToken;
	console.log(channelToken);
	this.user = user;
	this.onMessage = function (user, message) {}
	this.onStatusMessage = function (message) {}
	
	// Open a channel
	this.channel = new goog.appengine.Channel(channelToken);
	this.socket = this.channel.open();
	this.socket.onopen = function() {
    	console.log('channel open');
    };
    this.socket.onmessage = function(m) {
    	console.log('channel message:', m);
    };
    this.socket.onerror = function(error) {
    	console.log('channel error:', error);
    };
    this.socket.onclose = function(a) {
    	console.log('channel close:', a);
    };
    
    
}

Chat.prototype.sendMessage = function (message) {
	this.onMessage(user, message);
}