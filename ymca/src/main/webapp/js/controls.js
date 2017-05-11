var controls = {
    capturing: false,
	init: function ()
    {
        "use strict";
		var fullscreenBtns = Array.from(document.getElementsByClassName('videoFullscreen'));
		var capImageBtns = Array.from(document.getElementsByClassName('capImage'));
        var intervalCapBtns = Array.from(document.getElementsByClassName('intervalCapturing'));
        var logBtns = Array.from(document.getElementsByClassName('logTest'));
		
		fullscreenBtns.forEach(function (fullscreenBtn)
        {
			fullscreenBtn.addEventListener('click', controls.fullscreen);
		});
		capImageBtns.forEach(function (capImageBtn)
        {
			capImageBtn.addEventListener('click', controls.captureSingleImage);
		});
        intervalCapBtns.forEach(function (intervalCapBtn)
        {
            intervalCapBtn.addEventListener('click', controls.intervalCap);
        });
        logBtns.forEach(function(logBtn)
        {
             logBtn.addEventListener('click', controls.fakeLog);
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
	captureSingleImage: function (event)
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
        controls.capturing = target.innerHTML.includes("Start");
        if (controls.capturing)
        {
            controls.captureImage();
            target.innerHTML = target.innerHTML.replace("Start","Stop");
            target.className = target.className.replace("btn-success", "btn-danger");
        }
        else
        {
            target.innerHTML = target.innerHTML.replace("Stop","Start");
            target.className = target.className.replace("btn-danger", "btn-success");
        }
    },
    captureImage: function()
    {
        "use strict";
        if (controls.capturing)
        {
            webcam.captureImage(controls.captureImage);
        }
    },
    fakeLog : function(event)
    {
        loggingEndpoint.log({level: event.target.parentElement.parentElement.querySelector('.level-select').value, message:"example message"});
    }
}
document.addEventListener('DOMContentLoaded', controls.init);