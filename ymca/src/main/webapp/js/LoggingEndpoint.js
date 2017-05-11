var loggingEndpoint = {
		
		websocket : null,
		
		init : function()
		{
			var origin = window.location.origin.replace("http://", "ws://").replace("https://","wss://");
			websocket = new WebSocket(origin + "/ymca/LoggingEndpoint");
			websocket.onopen = function() 
			{
				//Sets up the logger instance with the correct session ID
				websocket.send('{"httpSessionId":"' + sessionId + '"}');
			};
			
			websocket.onmessage = function(message)
			{
				var jsonMessage = JSON.parse(message.data);			
				
				if (Array.isArray(jsonMessage))
				{
					for (i in jsonMessage)
					{
						loggingEndpoint.log(jsonMessage[i], true);
					}
				}
				else
				{
					loggingEndpoint.log(jsonMessage, true);
				}
			};
		},
		
		log : function(logRecord, localOnly)
		{
			logRecord.level = logRecord.level.toUpperCase();

			if (!logRecord.timestamp)
			{
				logRecord.timestamp = Date.now();
			}
			
			var func;
			if(typeof outputLog === "object")
			{
				outputLog.append(logRecord);
			}
			
			switch (logRecord.level) 
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
			
			if (!localOnly)
			{
				websocket.send(JSON.stringify(logRecord));
			}
			
			func(logRecord.level + " : " + logRecord.message);
		},
		
		closeConnect : function()
		{
			websocket.close();
		}
}

window.addEventListener('error', function(msg)
{
    loggingEndpoint.log({level:'ERROR', message:(msg.message || msg)});
});
document.addEventListener('DOMContentLoaded', loggingEndpoint.init, false);