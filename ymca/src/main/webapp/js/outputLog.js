var outputLog = 
{
	append : function(jsonMessage) 
	{
		var logElements = Array.from(document
				.getElementsByClassName("logElement"));
		logElements.forEach(function(log) 
				{
			var levelClass;

			switch (jsonMessage.level) 
			{
				case 'FINE':
					levelClass = "text-success"
					break;
				case 'SEVERE':
					levelClass = "text-danger"
					break;
				default:
					levelClass = "text-" + jsonMessage.level.toLowerCase();
			}

			var message = '<samp class="' + levelClass + '">'
					+ jsonMessage.message + '</samp><br/>';
			log.innerHTML += message;
		});
	}
}