var webcam = {
	videoElements : [],
	callbacks : [],
	init : function() {
		videoElements = Array.from(document.getElementsByClassName("videoElement"));

		navigator.getUserMedia = navigator.getUserMedia
				|| navigator.webkitGetUserMedia || navigator.mozGetUserMedia
				|| navigator.msGetUserMedia || navigator.oGetUserMedia;

		if (navigator.getUserMedia) {
			navigator.getUserMedia({
				video : {facingMode: "user"}
			}, webcam.handleVideo, webcam.videoError);
		}

		//xhttp.sendRequest(null, webcam.setId, 'IdServlet', 'GET');
		webcam.setId(sessionId);
	},

	handleVideo : function(stream)
	{
		var objectURL = window.URL.createObjectURL(stream);
		videoElements.forEach(function(v)
		{
			v.src = objectURL;
		});
	},

	videoError : function(error)
	{
		videoElements.forEach(function(v)
		{
			v.src = "resources/video_error.mp4";
		})
		loggingEndpoint.log('WARNING', 'Error loading Webcam');
	},

	setId : function(uuid) {
		id = uuid;
		xhttp.sendRequest('uuid=' + id, function() {
			webcam.notifyListeners(id)
		}, 'WebcamServlet');;
	},

	addListener : function(callback) {
		if (typeof callback === "function") {
			webcam.callbacks.push(callback);
		}
	},

	notifyListeners : function(id) {
		webcam.callbacks.forEach(function(callback) {
			callback(id);
		});
	},

	// FOR THE LOVE OF GOD DON'T TOUCH IT, IT WORKS
	captureImage : function(callback) {
		var canvas = document.createElement("canvas");
		var video = videoElements[0];
		canvas.height = video.videoHeight;
		canvas.width = video.videoWidth;
		var ctx = canvas.getContext("2d");
		// assign current frame of video to canvas
		ctx.drawImage(video, 10, 10);
		// get data URL
		var dataURL = canvas.toDataURL();
		// escape url, inc. manual stuff because escape() misses things
		dataURL = encodeURIComponent(dataURL);
		// Send to servlet
		xhttp.sendRequest('webcamImage=' + dataURL, function(responseText)
        {
            webcam.processResponse(responseText);
            callback();
        },
        "WebcamServlet");
	},

	processResponse : function (responseText)
    {
		"use strict";
        var responseJSON = JSON.parse(responseText);
        var captionElement = Array.from(document.getElementsByClassName('videoCaption'));
        captionElement.forEach(function (caption)
        {
            caption.innerHTML = responseJSON.description.captions[0].text;
        });
	}
}

document.addEventListener('DOMContentLoaded', webcam.init, false);