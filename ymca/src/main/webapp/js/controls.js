var controls = {
	init: function()
	{
		var fullscreenBtns = Array.from(document.getElementsByClassName('videoFullscreen'));
		var capImageBtns = Array.from(document.getElementsByClassName('capImage'));
		
		fullscreenBtns.forEach(function(fullscreenBtn)
		{
			fullscreenBtn.addEventListener('click', controls.fullscreen);
		});
		capImageBtns.forEach(function(capImageBtn)
		{
			capImageBtn.addEventListener('click', controls.captureImage);
		});
	},
	fullscreen: function(event)
	{
		event = event || window.event;
		var videoElement = document.querySelector(".videoElement");
		if (typeof videoElement !== "undefined" && videoElement.requestFullscreen) 
		{
			videoElement.requestFullscreen();
		}
		else if(typeof videoElement !== "undefined" && videoElement.webkitRequestFullScreen)
		{
			videoElement.webkitRequestFullScreen();
		}
		else
		{
			event.target.className = event.target.className + " disabled";
			console.warn("failed to make element fullscreen.", videoElement);
		}
	},
	captureImage: function(event)
	{
		event = event || window.event;
		if (typeof webcam !== "undefined" && webcam.captureImage)
		{
			webcam.captureImage();
		}
		else
		{
			event.target.className = event.target.className + " disabled";
		}
	}
}
document.addEventListener('DOMContentLoaded', controls.init);