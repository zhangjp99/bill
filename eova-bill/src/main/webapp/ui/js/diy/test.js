$(function() {
	setTimeout(function() {
		$.msg('我是从 /ui/js/test.js,产生的弹窗事件，仅用于测试菜单的自定义JS依赖');
	}, 100);
	
	// 自定义逻辑的DOM渲染, 延迟+去重
	var flag = false;
	$('.eova-layout')[0].addEventListener('DOMSubtreeModified', function() {
		if (flag) {return;}flag = true;
		setTimeout(function() {
			console.log("eova diy js render ing...");
			// 第1和3行加黄色
			$('.layui-table tr[data-index="0"]').css('background-color', '#eada91');
			$('.layui-table tr[data-index="2"]').css('background-color', '#eada91');

			// 经验值为0的加红色
			$('.layui-table td[data-field="exp"]').each(function(i, o) {
				if ($(o).text() == '0') {
					$(o).parent().css('background-color', '#FFCCCC');
				}
			});
			flag = false;
		}, 200)
	}, false);
});
