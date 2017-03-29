var webcam = {
	video : null,
	init : function() {
		video = document.querySelector(".videoElement");

		navigator.getUserMedia = navigator.getUserMedia
				|| navigator.webkitGetUserMedia || navigator.mozGetUserMedia
				|| navigator.msGetUserMedia || navigator.oGetUserMedia;

		if (navigator.getUserMedia) {
			navigator.getUserMedia({
				video : true
			}, webcam.handleVideo, webcam.videoError);
		}
	},

	handleVideo : function(stream) {
		video.src = window.URL.createObjectURL(stream);
	},

	videoError : function(e) {
		// do something
	},

	//FOR THE LOVE OF GOD DON'T TOUCH IT, IT WORKS
	captureImage : function() {
		var canvas = document.createElement("canvas");
		video = document.querySelector(".videoElement");
		canvas.height = video.videoHeight;
		canvas.width = video.videoWidth;
		var ctx = canvas.getContext("2d");
		//assign current frame of video to canvas
		ctx.drawImage(video, 10, 10);
		//get data URL
		var dataURL = canvas.toDataURL();
		//escape url, inc. manual stuff because escape() misses things
		dataURL = escape(dataURL);
		dataURL=dataURL.replace("+", "%2B");
		dataURL=dataURL.replace("/", "%2F");
		//Send to servlet
		xhttp.sendRequest('webcamImage=' + dataURL, null,
				"WebcamServlet");
	}
}
document.addEventListener('DOMContentLoaded', webcam.init, false);