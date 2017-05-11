<%@ page import="models.modules.Module"%>
<%@ page import="java.util.Collection"%>
<%
	final Collection<Module> modules = (Collection<Module>) request.getAttribute("modules");
	final boolean containsWebcam = modules.contains(Module.WEBCAM);
%>
<div class="col-sm-12">
	<button class="capImage btn btn-success btn-lg btn-block<%=containsWebcam ? "" : " disabled"%>">
		Capture Single Image <span class="glyphicon glyphicon-cloud-upload"></span>
	</button>
    <button class="intervalCapturing btn btn-success btn-lg btn-block<%=containsWebcam ? "" : " disabled"%>">
        Start Capturing <span class="glyphicon glyphicon-cloud-upload"></span>
    </button>
	<button class="videoFullscreen btn btn-default btn-lg btn-block<%=containsWebcam ? "" : " disabled"%>">
		FullScreen <span class="glyphicon glyphicon-fullscreen"></span>
	</button>
	<div class="row">
		<div class="col-sm-6">
			<select class="level-select form-control" style="height:44px">
                <option>Error</option>
                <option>Warning</option>
                <option>Info</option>   
                <option>Config</option>
                <option selected="selected">Fine</option>
                <option>Finer</option>
                <option>Finest</option>
			</select>
		</div>
		<div class="col-sm-6">
			<button class="logTest btn btn-warning btn-lg btn-block">
				Log message
			</button>
		</div>
	</div>
</div>