var outputLog = 
{
	
	isHover : false,
		
	init : function()
	{
		var logElements = Array.from(document
				.getElementsByClassName("logElement"));
		logElements.forEach(function(log) 
		{
			log.addEventListener("mouseover", function(){outputLog.isHover = true});
			log.addEventListener("mouseout", function(){outputLog.isHover = false});
		});
	},
	
	append : function(level, message) 
	{
		var logElements = Array.from(document
				.getElementsByClassName("logElement"));
		logElements.forEach(function(log) 
		{
			var levelClass;
			var iconHTML = '<span class="';
			switch (level) 
			{
				case 'FINE':
					levelClass = "text-success"
					break;;
				case 'WARNING':
					iconHTML += "glyphicon glyphicon-warning-sign"
					levelClass = "text-" + level.toLowerCase();
					break;
				case 'SEVERE':
					iconHTML += "glyphicon glyphicon-warning-sign"
					levelClass = "text-danger"
					break;
				default:
					levelClass = "text-" + level.toLowerCase();
			}
			iconHTML += '"></span>';
			var messageHTML = '<samp class="' + levelClass + '">' + iconHTML + level.toUpperCase() + " : " +  message + '</samp><br/>';
			log.innerHTML += messageHTML;
			
			if (!outputLog.isHover)
			{
				log.scrollTop = log.scrollHeight;
			}		
		});
	}
}

document.addEventListener('DOMContentLoaded', outputLog.init, false);