var up6_app = {
    ins: null
    ,edgeApp: null
    ,Config:null
    , checkFF: function ()
    {
        var mimetype = navigator.mimeTypes;
        if (typeof mimetype == "object" && mimetype.length)
        {
            for (var i = 0; i < mimetype.length; i++)
            {
                var enabled = mimetype[i].type == this.Config.firefox.type;
                if (!enabled) enabled = mimetype[i].type == this.Config.firefox.type.toLowerCase();
                if (enabled) return mimetype[i].enabledPlugin;
            }
        }
        else
        {
            mimetype = [this.Config.firefox.type];
        }
        if (mimetype)
        {
            return mimetype.enabledPlugin;
        }
        return false;
    }
	, Setup: function ()
	{
		//文件夹选择控件
		acx += '<object id="objHttpPartition" classid="clsid:' + this.Config.ie.part.clsid + '"';
        acx += ' codebase="' + this.Config.ie.path + '" width="1" height="1" ></object>';

		$("body").append(acx);
	}
    , init: function ()
    {
        var param = { name: "init", config: this.Config };
        this.postMessage(param);
    }
    , initNat: function ()
    {
        if (!this.chrome45) return;
        this.exitEvent();
        document.addEventListener('Uploader6EventCallBack', function (evt)
        {
            this.recvMessage(JSON.stringify(evt.detail));
        });
    }
    , initEdge: function ()
    {
        this.edgeApp.run();
    }
    , exit: function ()
    {
        var par = { name: 'exit' };
        var evt = document.createEvent("CustomEvent");
        evt.initCustomEvent(this.entID, true, false, par);
        document.dispatchEvent(evt);
    }
    , exitEvent: function ()
    {
        var obj = this;
        $(window).bind("beforeunload", function () { obj.exit(); });
    }
    , addFile: function (json)
    {
        var param = { name: "add_file"};
        jQuery.extend(param, json);
        this.postMessage(param);
    }
    , addFolder: function (json) {
        var param = { name: "add_folder"};
        jQuery.extend(param, json);
        this.postMessage(param);
    }
    , openFiles: function ()
    {
        var param = { name: "open_files"};
        this.postMessage(param);
    }
    , openFolders: function ()
    {
        var param = { name: "open_folders"};
        this.postMessage(param);
    }
    , pasteFiles: function ()
    {
        var param = { name: "paste_files"};
        this.postMessage(param);
    }
    , checkFile: function (f)
    {
        var param = { name: "check_file"};
        jQuery.extend(param, f);
        this.postMessage(param);
    }
    , checkFolder: function (fd)
    {
        var param = { name: "check_folder"};
        jQuery.extend(param, fd);
        param.name = "check_folder";
        this.postMessage(param);
    }
    , scanFolder: function (fd) {
        var param = { name: "scan_folder"};
        jQuery.extend(param, fd);
        param.name = "scan_folder";
        this.postMessage(param);
    }
    , checkFolderNat: function (fd)
    {
        var param = { name: "check_folder", config: this.Config, folder: JSON.stringify(fd) };
        this.postMessage(param);
    }
    , postFile: function (f)
    {
        var param = { name: "post_file"};
        jQuery.extend(param, f);
        this.postMessage(param);
    }
    , postFolder: function (fd)
    {
        var param = { name: "post_folder"};
        jQuery.extend(param, fd);
        param.name = "post_folder";
        this.postMessage(param);
    }
    , stopFile: function (f)
    {
        var param = { name: "stop_file", idSign: f.idSign};
        this.postMessage(param);
    }
    , delFolder: function (f) {
        var param = { name: "del_folder", idSign: f.idSign };
        this.postMessage(param);
    }
    , postMessage:function(json)
    {
        try {
            this.ins.parter.postMessage(JSON.stringify(json));
        } catch (e) { }
    }
    , postMessageNat: function (par)
    {
        var evt = document.createEvent("CustomEvent");
        evt.initCustomEvent(this.entID, true, false, par);
        document.dispatchEvent(evt);
    }
    , postMessageEdge: function (par)
    {
        this.edgeApp.send(par);
    }
};
