
(function() {
	
	var fileinput = function(options, success, error) {
		var defaults = {
            auto : true,
            pick : {
                id : '#upload-pick',
                multiple : false
            },
            accept : {
                title : '选择文件',
                extensions : 'gif,jpg,jpeg,bmp,png',
                mimeTypes : 'image/gif,image/jpg,image/jpeg,image/bmp,image/png'
            },
            swf : Feng.ctxPath + '/static/js/plugins/webuploader/Uploader.swf',
            disableGlobalDnd : true,
            duplicate : true,
            server : "/upload/img",
            fileSingleSizeLimit : 1024 * 1024 * 1024
        };

        this.settings = $.extend({}, defaults, options);
        this.success = success;
        this.error = error;

	};

    fileinput.prototype = {
		/**
		 * 初始化webUploader
		 */
		init : function() {
            this.webUploader = WebUploader.create(this.options);
			this.bindEvent();
			return this;
		},

		/**
		 * 绑定事件
		 */
		bindEvent : function() {
			console.log(this)
			var that =  this;

            that.webUploader.on('fileQueued', function(file) {
            	console.log(file)
				var $li = $('<div><img width="100px" height="100px"></div>');
				var $img = $li.find('img');

				$(that.settings.pick.id + '-preview').html($li);

				// 生成缩略图
                that.webUploader.makeThumb(file, function(error, src) {
					if (error) {
						$img.replaceWith('<span>不能预览</span>');
						return;
					}
					$img.attr('src', src);
				}, that.picWidth, that.picHeight);
			});

			// 文件上传过程中创建进度条实时显示。
            that.webUploader.on('uploadProgress', function(file, percentage) {
                $(that.settings.pick.id + '-progress').css("width",percentage * 100 + "%");
			});

			// 文件上传成功，给item添加成功class, 用样式标记上传成功。
            that.webUploader.on('uploadSuccess', function(file,response) {
                that.success(file,response)
			});

			// 文件上传失败，显示上传出错。
            that.webUploader.on('uploadError', function(file) {
                that.success(file)
			});

			// 其他错误
            that.webUploader.on('error', function(type) {
				if ("Q_EXCEED_SIZE_LIMIT" == type) {
					Feng.error("文件大小超出了限制");
				} else if ("Q_TYPE_DENIED" == type) {
					Feng.error("文件类型不满足");
				} else if ("Q_EXCEED_NUM_LIMIT" == type) {
					Feng.error("上传数量超过限制");
				} else if ("F_DUPLICATE" == type) {
					Feng.error("图片选择重复");
				} else {
					Feng.error("上传过程中出错");
				}
			});

			// 完成上传完了，成功或者失败
            that.webUploader.on('uploadComplete', function(file) {
			});
		}
	};

	window.fileinput = fileinput;

}());