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
                    loggingEndpoint.log('WARNING', address + ' responded with: ' + this.responseText);
                }
			}
		}
        xhttp.send(data);
	}
}