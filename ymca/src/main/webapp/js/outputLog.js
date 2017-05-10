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
            Array.from(log.parentElement.getElementsByTagName("input")).forEach(function(input)
            {
               if(input.type === "checkbox")
               {
                   input.addEventListener("change", outputLog.toggleLevelVisible);
               }
            });
            Array.from(log.parentElement.getElementsByTagName("button")).forEach(function(button)
            {
                button.addEventListener("click", outputLog.clear, true);//currenty only button is for clearing
            });
		});
	},
	
	append : function(logRecord) 
	{
		var logElements = Array.from(document
				.getElementsByClassName("logElement"));
		logElements.forEach(function(log) 
		{
			var levelClass;
            var date = logRecord.timestamp ? new Date(logRecord.timestamp) : new Date();
            var timeHTML = '<span class="timestamp"><time datetime=' + date.toISOString() + ' title="' + date.toISOString() + '">' + date.toLocaleTimeString() + '</time> : </span>';
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
			var messageHTML = '<samp class="' + levelClass + '">' + iconHTML + timeHTML + logRecord.level + " : " +  logRecord.message + '<br/></samp>';
			log.innerHTML += messageHTML;
			
			if (!outputLog.isHover)
			{
				log.scrollTop = log.scrollHeight;
			}		
		});
	},
    
    toggleLevelVisible : function(event)
    {
        var target = event.target;
        target.parentElement.parentElement.nextElementSibling.classList.toggle(target.name, !target.checked);
    },
    
    clear: function(event)
    {
        event.target.parentElement.parentElement.parentElement.querySelector('.logElement').innerHTML='';
    }
}

document.addEventListener('DOMContentLoaded', outputLog.init, false);
