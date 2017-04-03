var modules = {
	init: function(event)
	{
		var closeBtns = Array.from(document.getElementsByClassName('closeBtn'));
		var fullscreenBtns = Array.from(document.getElementsByClassName('fullscreenBtn'));
		
		closeBtns.forEach(function(closeBtn)
		{
			closeBtn.addEventListener('click', modules.close);
		});
		
		fullscreenBtns.forEach(function(fullscreenBtn)
		{
			fullscreenBtn.addEventListener('click', modules.fullscreen);
		});
	},
	close: function(event)
	{
		event = event || window.event;
		var target = event.target.parentElement.parentElement.parentElement;
		target.parentElement.removeChild(target);
	},
	fullscreen: function(event)
	{
		event = event || window.event;
		var target = event.target.parentElement.parentElement.nextElementSibling;
		if (target.requestFullscreen)
		{
			target.requestFullscreen();
		}
		else if(target.webkitRequestFullScreen)
		{
			target.webkitRequestFullScreen();
		}
		else
		{
			console.warn("failed to make element fullscreen.", target);
		}
	}
}
document.addEventListener('DOMContentLoaded', modules.init);