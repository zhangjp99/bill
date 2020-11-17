/**
 * Copyright (c) 2015-2020 EOVA.CN. All rights reserved.
 * 
 * Licensed to: 上海蜜炬信息科技有限公司(1613965675@qq.com)
 * Licensed under the EPPL license: http://eova.cn/eppl.txt
 * Software copyright registration number:2020SR0109251
 * For authorization, please contact: admin@eova.cn
 */
package com.eova.widget.upload;

import java.io.File;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.eova.aop.UploadIntercept;
import com.eova.common.base.BaseController;
import com.eova.common.utils.xx;
import com.eova.common.utils.io.FileUtil;
import com.eova.common.utils.io.ImageUtil;
import com.eova.common.utils.util.RandomUtil;
import com.eova.common.utils.util.RegexUtil;
import com.eova.config.EovaConfig;
import com.eova.model.MetaField;
import com.eova.model.MetaFieldConfig;
import com.jfinal.kit.LogKit;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.upload.UploadFile;

/**
 * 上传组件
 * 
 * @author Jieven
 * 
 */
public class UploadController extends BaseController {

	// 异步传图
	public void img() {
		Ret r = upload("img", getPara("name"), null);
		renderJson(r);
	}

	// 异步传文件
	public void file() {
		Ret rt = upload("file", getPara("name"), null);
		renderJson(rt);
	}

	// 异步传临时文件
	public void temp() {
		Ret rt = upload("file", getPara("name"), null);
		renderJson(rt);
	}

	// 编辑器上传图片(wangEditor)
	public void editor() {
		// 开始上传
		Ret rt = upload("img", null, "/editor");
		if (rt.isFail()) {
			renderText("error|" + rt.getStr("msg"));
			return;
		}
		// 获取最终上传目录
		String uploadDir = rt.getStr("uploadDir");
		// 获取图片服务域名
		String domain = EovaConfig.getProps().get("domain_img");
		if (xx.isEmpty(domain)) {
			throw new RuntimeException("图片上传异常,请先配置图片服务域名!配置项:domain.config domain_img=图片服务域名");
		}

		uploadDir = FileUtil.formatWebPath(uploadDir);

		String url = String.format("%s/%s/%s", xx.delEnd(domain, "/"), xx.delStartEnd(uploadDir, "/"), rt.getStr("fileName"));

		JSONObject o = new JSONObject();
		JSONArray urls = new JSONArray();
		urls.add(0, url);
		o.put("errno", 0);
		o.put("data", urls);

		renderJson(o);
	}

	// yyyy 按年, yyyyMM 按月 , yyyyMMdd 按天, 其它自己脑补
	private String parseTimePath(String path) {
		// String path = "/abc/{yyyy}/{MMdd}";
		for (String x : path.split("/")) {
			if (!xx.isEmpty(x) && x.startsWith("{")) {
				x = x.replaceAll("\\{", "").replaceAll("\\}", "");
				// String time = DateTime.now().toString(x);
				String time = xx.time.formatNow(x);
				path = RegexUtil.replaceAll(String.format("\\{%s\\}", x), time, path, -1);
			}
		}
		return path;
	}

	/**
	 * 构建上传目录
	 * @param fileDirConfig 自定义目录
	 * @param defaultConfigDir 缺省目录配置
	 * @return
	 */
	private String buildUploadDir(MetaFieldConfig config, String defaultDir) {

		String s = null;

		// 构建自定义上传目录
		if (config != null) {
			String fileDirConfig = config.getFiledir();
			// 自定义目录
			if (!xx.isEmpty(fileDirConfig)) {
				// 按时间自动分目录/
				if (fileDirConfig.indexOf("{") != -1) {
					s = parseTimePath(fileDirConfig);
				} else {
					// 固定目录
					s = fileDirConfig;
				}
			}
		}
		
		// 默认目录
		if (s == null && !xx.isEmpty(defaultDir)) {
			s = defaultDir;
		}

		// 兜底的临时目录(既没有自定义目录, 也没有缺省目录)
		if (s == null) {
			s = xx.getConfig("eova_upload_temp", "/");
		}

		// 格式化路径
		return FileUtil.formatPath(s);
	}

	/**
	 * <pre>
	 * 构建保存的文件名
	 * 1.固定文件名
	 * 2.保持原文件名(慎用)
	 * 3.按时间格式策略分目录
	 * 4.随机文件名(默认)
	 * </pre>
	 * @param config 元字段配置
	 * @param fileName 上传文件的名称
	 * @return
	 */
	private String buildFileName(MetaFieldConfig config, UploadFile file) {
		if (config != null) {
			// 文件名配置
			String fileNameConfig = config.getFilename();
			if (!xx.isEmpty(fileNameConfig)) {
				// 原文件名_时间戳(用于保持原文件+不重名)
				if (fileNameConfig.equals("ORIGINAL_TIME")) {
					return System.currentTimeMillis() + "@" + file.getOriginalFileName();
				}
				// 保持原文件名(保持原文件名, 重名的覆盖)
				if (fileNameConfig.equals("ORIGINAL")) {
					return file.getOriginalFileName();
				}
				// 固定文件名
				return fileNameConfig;
			}
		}

		// 默认随机文件名(增量随机数防止并发重名异常)
		return System.currentTimeMillis() + RandomUtil.nextIntAsStringByLength(5) + FileUtil.getFileType(file.getFileName());
	}

	/**
	 * 上传文件
	 * @param uploadType 上传类型:file/img
	 * @param name 文件控件name
	 * @param defaultDir 默认目录
	 * @return
	 */
	private Ret upload(String uploadType, String name, String defaultDir) {
		// 编码
		String code = getPara("code");
		String en = getPara("en");

		// 保持文件名
		boolean isOriginal = false;

		MetaFieldConfig config = null;
		// 获取特定源配置
		if (code.startsWith("EOVA_FLOW_WIDGET")) {
			String[] ss = code.split(",");
			Record e = Db.use(xx.DS_EOVA).findById("eova_flow_widget", ss[1]);
			String json = e.getStr("config");// 限定字段,缩小注入范围
			config = MetaField.parseConfig(json);
		}
		// 获取元字段配置
		else if (code != null && en != null) {
			MetaField field = MetaField.dao.getByObjectCodeAndEn(code, en);
			config = field.getConfig();
		}
		
		if (config != null && config.getFilename() != null && config.getFilename().equals("ORIGINAL")) {
			isOriginal = true;
		}

		// 文件大小限制(KB) 默认50M
		int maxKB = xx.getConfigInt("upload_size", 50 * 1024);
		if (uploadType.equals("img")) {
			// 图片10M
			maxKB = xx.getConfigInt("upload_img_size", 10 * 1024);
		}

		String uploadDir = null;

		// 构建上传目录
		uploadDir = buildUploadDir(config, defaultDir);

		String newFileName = null;
		UploadFile file = null;
		try {

			// 预创建上传目录, 避免多文件上传并发, 导致异常:not exists and can not create directory.
			String baseDir = xx.getConfig("static_root");
			jodd.io.FileUtil.mkdir(baseDir + uploadDir);

			if (name != null) {
				// 按参数名获取上传文件
				file = getFile(name, uploadDir);
			} else {
				// 获取第一个上传文件
				List<UploadFile> files = getFiles(uploadDir);
				if (xx.isEmpty(files)) {
					return Ret.fail("msg", "请选择一个文件");
				}
				file = files.get(0);
			}
			if (xx.isEmpty(file)) {
				return Ret.fail("msg", "请选择一个文件");
			}
			if (FileUtil.checkFileSize(file.getFile(), maxKB)) {
				return Ret.fail("msg", String.format("文件大小不能超过%sKB", maxKB));
			}
			// 图片合法性严格检查(图片后缀+图片头)
			if (uploadType.equalsIgnoreCase("img") && !ImageUtil.isImage(file.getFile().getPath())) {
				return Ret.fail("msg", "该文件不是标准的图片文件格式，请勿手工修改文件格式");
			}

			// 获取新文件名
			newFileName = buildFileName(config, file);

			// 上传文件拦截
			UploadIntercept intercept = EovaConfig.getUploadIntercept();
			if (intercept != null) {
				return intercept.upload(code, en, config, newFileName, uploadDir, file);
			}

			// 新文件 Path
			String path = file.getUploadPath() + File.separator + newFileName;

			// 保留原文件, 无需改名
			if (!isOriginal) {
				/*
				 * 如果文件存在,先删除.
				 * 1.指定文件名,一般为覆盖文件,否则不应指定文件名!如不能覆盖应自定义上传逻辑
				 * 2.未指定文件名,随机文件名,理论上不会存在重名,如巧合,应删之
				 */
				if (FileUtil.isExists(path)) {
					FileUtil.delete(path);
				}

				FileUtil.rename(file.getFile().getPath(), path);
				LogKit.info(file.getFile().getPath() + " -> " + newFileName);
			}

		} catch (Exception e) {
			e.printStackTrace();
			return Ret.fail("msg", "系统异常：文件上传失败,请稍后再试");
		} finally {
			// 上传异常, 垃圾回收, 保留原文件禁止回收
			if (!isOriginal) {
				FileUtil.delete(file.getFile());
			}
		}
		return Ret.ok("fileName", newFileName).set("uploadDir", uploadDir);
	}

}