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
	}
}
document.addEventListener('DOMContentLoaded', webcam.init, false);