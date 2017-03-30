var exampleEndpoint = {

	webSocket : null,
		
	init : function()
	{
		webSocket = new WebSocket("ws://localhost:8080/ymca/ExampleListenerEndpoint");
		webSocket.onopen = function() 
		{
			webcam.addListener(exampleEndpoint.sendId);
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