var loggingEndpoint = {
		
		websocket : null,
		
		init : function()
		{
			var origin = window.location.origin.replace("http://", "ws://");
			websocket = new WebSocket(origin + "/ymca/LoggingEndpoint");
			websocket.onopen = function() 
			{
				//Sets up the logger instance with the correct session ID
				loggingEndpoint.sendId(sessionId);
			};
			
			websocket.onmessage = function(message) 
			{
				var jsonMessage = JSON.parse(message.data);
				console.log(jsonMessage['level'] + ": " + jsonMessage['message']);
			};
		},
		
		closeConnect : function()
		{
			websocket.close();
		},
		
		sendId(id)
		{
			websocket.send(id);
		}
}

document.addEventListener('DOMContentLoaded', loggingEndpoint.init, false);