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
                loggingEndpoint.log('FINE', 'Connection to server log opened');
			};
			
			websocket.onmessage = function(message) 
			{
				var jsonMessage = JSON.parse(message.data);				
				loggingEndpoint.log(jsonMessage.level, jsonMessage.message);
			};
		},
		
		log : function(level, message)
		{
			level = level.toUpperCase();
			var func;
			if(typeof outputLog === "object")
			{
				outputLog.append(level, message);
			}
			
			switch (level) 
			{
				case 'INFO':
					func = console.info;
					break;
				case 'WARN':
				case 'WARNING':
					func = console.warn;
					break;
				case 'ERROR':
				case 'SEVERE':
					func = console.error;
					break;
				default:
					func = console.log;
			}
			
			func(level + " : " + message);
		},
		
		closeConnect : function()
		{
			websocket.close();
		},
		
		sendId : function(id)
		{
			websocket.send(id);
		}
}

window.addEventListener('error', function(msg)
{
    loggingEndpoint.log('ERROR', msg.message || msg);
});
document.addEventListener('DOMContentLoaded', loggingEndpoint.init, false);