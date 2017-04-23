var modules = {
	init: function(event)
	{
		var closeBtns = Array.from(document.getElementsByClassName('closeBtn'));
		var fullscreenBtns = Array.from(document.getElementsByClassName('fullscreenBtn'));
		var addModuleBtns = Array.from(document.getElementsByClassName('addModuleBtn'));
		
		closeBtns.forEach(function(closeBtn)
		{
			closeBtn.addEventListener('click', modules.close);
		});
		
		fullscreenBtns.forEach(function(fullscreenBtn)
		{
			fullscreenBtn.addEventListener('click', modules.fullscreen);
		});
        addModuleBtns.forEach(function (addModuleBtn)
        {
            addModuleBtn.addEventListener('click', function (event)
            {
                modules.addModules([addModuleBtn.textContent.trim()]);
            });
        });
	},
	close: function(event)
	{
		event = event || window.event;
		event.target = event.target || event.srcElement;
        var targetModule = event.target.parentElement.parentElement.parentElement;
        targetModule.parentElement.removeChild(targetModule);
        modules.addModules();
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
	},
    addModules: function (moduleNames)
    {
        moduleNames = moduleNames || [];
        var modules = Array.from(document.getElementsByClassName('module'));
        var query = 'mods=';
        modules.forEach(function(module, index)
        {
            var textNode = module.getElementsByClassName('panel-heading')[0].firstChild;
            query += textNode.textContent.trim();
            query += ',';
        });
        moduleNames.forEach(function(moduleName)
        {
            query += encodeURIComponent(moduleName);
            query += ',';
        });
        query = query.substr(0, query.length-1); // remove last comma
        const modsRegex = /(mods=?[\w, %\d-]*)&?/;
        if (modsRegex.test(location.search))
        {
            location.search = location.search.replace(modsRegex, query);
        }
        else
        {
            if (location.search.length > 0)
            {
                query = '&' + query;
                location.search = location.search + query;
            }
            else
            {
                query = '?' + query;
                location.search = query;
            }
        } 
    }
}
document.addEventListener('DOMContentLoaded', modules.init);
