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
				video : true
			}, webcam.handleVideo, webcam.videoError);
		}

		xhttp.sendRequest(null, webcam.setId, 'IdServlet', 'GET');
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
		console.error("Error loading Webcam");
	},

	setId : function(uuid) {
		id = uuid;
		xhttp.sendRequest('uuid=' + id, function() {
			webcam.notifyListeners(id)
		}, 'WebcamServlet');
		console.info(id);
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
	captureImage : function() {
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
		xhttp.sendRequest('webcamImage=' + dataURL, webcam.processResponse,
				"WebcamServlet");
	},

	processResponse : function(responseText) {
		console.log(responseText);
	}
}

document.addEventListener('DOMContentLoaded', webcam.init, false);