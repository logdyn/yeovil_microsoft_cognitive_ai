var outputLog = 
{
		append : function(jsonMessage)
		{
			var logElements = Array.from(document.getElementsByClassName("logElement"));
			logElements.forEach(
					function(log)
					{
						var levelClass;
						
						switch(jsonMessage.level) 
						{
						    case 'INFO':
						    	levelClass = "bg-info"
						        break;
						    case 'FINE':
						    	levelClass = "bg-success"
							        break;
						    case 'WARNING':
						    	levelClass = "bg-warning"
						    case 'SEVERE':
						    	levelClass = "bg-danger"
				    		default:
				    			levelClass = "";
						}
						
						var message = '<p><samp class="' + levelClass + '">' + jsonMessage.message + '</samp></p><br/>';
						log.innerHTML = message;
					});
		}
}