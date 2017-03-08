/**
 * 
 */
var xhttp = {
	sendRequest: function(data, responseFuction, address)
	{
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
		xhttp.open('POST', address, true);
		xhttp.setRequestHeader('Content-Type','application/x-www-form-urlencoded');
		xhttp.onreadystatechange = function()
		{
			if (this.readyState == 4 && this.status == 200)
			{
				if (typeof responseFuction === 'function')
				{
					responseFuction(this.responseText);
				}
			}
		}
		xhttp.send(data);
	}
}