var exampleEndpoint = {

	webSocket : null,
		
	init : function()
	{
		var origin = window.location.origin.replace("http://", "ws://").replace("https://","wss://");
		webSocket = new WebSocket(origin + "/ymca/ExampleListenerEndpoint");
		webSocket.onopen = function() 
		{
			//webcam.addListener(exampleEndpoint.sendId);
			exampleEndpoint.sendId(sessionId);
		};

		webSocket.onmessage = function(message) {
			console.log(message.data)
		};
	},
	
	closeConnect : function()
	{
		webSocket.close();
	},
	
	sendId : function(id)
	{
		webSocket.send(id);
	}
}

document.addEventListener('DOMContentLoaded', exampleEndpoint.init, false);