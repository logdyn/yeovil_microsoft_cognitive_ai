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
	
	append : function(logRecord) 
	{
		var logElements = Array.from(document
				.getElementsByClassName("logElement"));
		logElements.forEach(function(log) 
		{
			var levelClass;
            var time = logRecord.time ? new Date(logRecord.time) : new Date();
            var timeHTML = '<time datetime=' + time.toISOString() + ' title="' + time.toISOString() + '">' + time.toLocaleTimeString() + '</time>';
			var iconHTML = '<span class="glyphicon';
			
			switch (logRecord.level) 
			{
				case 'FINE':
					levelClass = "text-success";
					break;
				case 'INFO':
					levelClass = "text-info";
					iconHTML += " glyphicon-info-sign";
					break;
				case 'WARN':
				case 'WARNING':
					iconHTML += " glyphicon-alert";
					levelClass = "text-warning";
					break;
				case 'ERROR':
				case 'SEVERE':
					iconHTML += " glyphicon-remove-sign";
					levelClass = "text-danger";
					break;
				default:
					levelClass = "text-" + logRecord.level.toLowerCase();
			}
			
			iconHTML += '"></span>';
			var messageHTML = '<samp class="' + levelClass + '">' + iconHTML + timeHTML + ' : ' +  logRecord.level + " : " +  logRecord.message + '</samp><br/>';
			log.innerHTML += messageHTML;
			
			if (!outputLog.isHover)
			{
				log.scrollTop = log.scrollHeight;
			}		
		});
	}
}

document.addEventListener('DOMContentLoaded', outputLog.init, false);
