function Wortopia(data) {
	
	// Create user from data
	this.user = createUserFromJson(data.user);
	
	// Create chat instance
	this.chat = new Chat(data.channelToken, this.user);
	
	this.chat.onMessage = function(user, message) {
		$('#chatMessages').append('<div class="message"><span class="sender">'+user.username+':</span><span class="message">'+message+'</span></div>');
	}
	this.chat.onStatusMessage = function(message) {
		$('#chatMessages').append('<div class="message"><span class="sender status_sender">Status:</span><span class="message">'+message+'</span></div>');
	}
	
	$('#chatInput').keypress(function(e) {
	    if(e.keyCode == 13) {
	        chat.sendMessage($('#chatInput').val());
	        $('#chatInput').val('');
	    }
	});
	
	
	
}
