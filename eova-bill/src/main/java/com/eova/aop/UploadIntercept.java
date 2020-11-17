/**
 * Copyright (c) 2015-2020 EOVA.CN. All rights reserved.
 * 
 * Licensed to: 上海蜜炬信息科技有限公司(1613965675@qq.com)
 * Licensed under the EPPL license: http://eova.cn/eppl.txt
 * Software copyright registration number:2020SR0109251
 * For authorization, please contact: admin@eova.cn
 */
package com.eova.aop;

import com.eova.model.MetaFieldConfig;
import com.jfinal.kit.Ret;
import com.jfinal.upload.UploadFile;

/**
 * 上传拦截器
 * @author Jieven
 *
 */
public interface UploadIntercept {

	public Ret upload(String code, String en, MetaFieldConfig config, String newFileName, String uploadDir, UploadFile file);

}