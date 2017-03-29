var video
document.addEventListener('DOMContentLoaded', function() {
	video = document.querySelector(".videoElement");

	navigator.getUserMedia = navigator.getUserMedia
			|| navigator.webkitGetUserMedia || navigator.mozGetUserMedia
			|| navigator.msGetUserMedia || navigator.oGetUserMedia;

	if (navigator.getUserMedia) {
		navigator.getUserMedia({
			video : true
		}, handleVideo, videoError);
	}
}, false);

function handleVideo(stream) {
	video.src = window.URL.createObjectURL(stream);
}

function videoError(e) {
	// do something
}