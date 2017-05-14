/**
 * 
 */
var xhttp = 
{
	sendRequest : function(data, responseFuction, address, method) {
		method = method || 'POST'
		address = address || '';
		var xhttp;
		if (window.XMLHttpRequest)
        {
			xhttp = new XMLHttpRequest();
		}
        else
        {
			xhttp = new ActiveXObject('Microsoft.XMLHTTP');
		}
		xhttp.open(method, address, true);
		xhttp.setRequestHeader('Content-Type',
				'application/x-www-form-urlencoded');
		xhttp.onreadystatechange = function()
        {
            if (this.readyState == 4)
            {
				if (this.status == 200)
                {
                    if (typeof responseFuction === 'function')
                    {
                        responseFuction(this.responseText);
                    }
                }
                else
                {
                    var description = /<b>description<\/b>\s*(?:<.*?>)*(.*?)(?:<\/.*?>)*\s*<\/p>/gm.exec(this.responseText);
                    var errorMsg = this.responseText;
                    if(description != null)
                    {
                        errorMsg = description[1];
                    }   
                	loggingEndpoint.log({level:'WARNING', message:(address + ' responded with: ' + errorMsg)});
                }
			}
		}
        xhttp.send(data);
	}
}