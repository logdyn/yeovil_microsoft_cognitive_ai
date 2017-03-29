var webcam = {
	video : null,
	init: function() 
	{
		video = document.querySelector(".videoElement");
	
		navigator.getUserMedia = navigator.getUserMedia
				|| navigator.webkitGetUserMedia || navigator.mozGetUserMedia
				|| navigator.msGetUserMedia || navigator.oGetUserMedia;
	
		if (navigator.getUserMedia)
		{
			navigator.getUserMedia(
				{video : true},
				webcam.handleVideo,
				webcam.videoError);
		}
	},
	
	handleVideo: function(stream)
	{
		video.src = window.URL.createObjectURL(stream);
	},
	
	videoError: function(e)
	{
		// do something
	},
	
	captureImage: function() {
		  var canvas = document.createElement("canvas");
		  var context = canvas.getContext("2d");
//		  c.width = 160;
//		  c.height = 90;
		  context.drawImage(video, 0, 0, 160, 90);
		  xhttp.sendRequest(canvas.toDataURL(), null, "/WebcamServlet");
	}
}
document.addEventListener('DOMContentLoaded', webcam.init, false);