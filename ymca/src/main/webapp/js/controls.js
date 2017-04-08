var controls = {
    interval: null,
	init: function ()
    {
        "use strict";
		var fullscreenBtns = Array.from(document.getElementsByClassName('videoFullscreen'));
		var capImageBtns = Array.from(document.getElementsByClassName('capImage'));
        var intervalCapBtns = Array.from(document.getElementsByClassName('intervalCapturing'));
		
		fullscreenBtns.forEach(function (fullscreenBtn)
        {
			fullscreenBtn.addEventListener('click', controls.fullscreen);
		});
		capImageBtns.forEach(function (capImageBtn)
        {
			capImageBtn.addEventListener('click', controls.captureImage);
		});
        intervalCapBtns.forEach(function (intervalCapBtn)
        {
            intervalCapBtn.addEventListener('click', controls.intervalCap);
        });
	},
	fullscreen: function (event)
    {
        "use strict";
		event = event || window.event;
		var videoElement = document.querySelector(".videoElement").parentElement;
		if (typeof videoElement !== "undefined" && videoElement.requestFullscreen)
        {
			videoElement.requestFullscreen();
		}
        else if (typeof videoElement !== "undefined" && videoElement.webkitRequestFullScreen)
        {
			videoElement.webkitRequestFullScreen();
		}
        else
		{
			event.target.className = event.target.className + " disabled";
			loggingEndpoint.log("WARNING", "Fullscreen not avalible");
		}
	},
	captureImage: function (event)
    {
        "use strict";
		event = event || window.event;
		if (typeof webcam !== "undefined" && webcam.captureImage)
		{
			webcam.captureImage();
		}
		else
		{
			event.target.className = event.target.className + " disabled";
		}
	},
    intervalCap: function (event)
    {
        "use strict";
        event = event || window.event;
        var target = event.target || event.srcElement;
        var active = target.innerHTML.includes("Stop");
        if (active)
        {
            target.innerHTML = target.innerHTML.replace("Stop","Start");
            target.className = target.className.replace("btn-danger", "btn-success");
            clearInterval(this.interval);
        }
        else
        {
            target.innerHTML = target.innerHTML.replace("Start","Stop");
            target.className = target.className.replace("btn-success", "btn-danger");
            this.interval = setInterval(webcam.captureImage, 5000);
        }
    }
}
document.addEventListener('DOMContentLoaded', controls.init);