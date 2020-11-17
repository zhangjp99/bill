// 前端校验器自定义正则规则配置
var EOVA_DIY_RULES = {
	company_code : [ /^[A-Z]{2,4}$/, "企业编码只能用2-4位大写字母" ],
	style_code : [ /^[A-Z0-9]{3,5}$/, "款号只能用3-5位数字或大写字母" ],
};

// EOVA前端全局配置命名空间
var EOVA = {
	edit_emotions : [ {
		title : '默认',
		type : 'image',
		content : [ {
			alt : '[微笑]',
			src : 'http://img.t.sinajs.cn/t4/appstyle/expression/ext/normal/5c/huanglianwx_org.gif'
		}, {
			alt : '[坏笑]',
			src : 'http://img.t.sinajs.cn/t4/appstyle/expression/ext/normal/50/pcmoren_huaixiao_org.png'
		} ]
	}, {
		title : 'emoji',
		type : 'emoji',
		content : [ '😃', '😄', '😁', '😆' ]
	} ]
}