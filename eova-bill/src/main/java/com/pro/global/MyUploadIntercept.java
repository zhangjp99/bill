/**
 * Copyright (c) 2019 EOVA.CN. All rights reserved.
 * 
 * Licensed under the EPPL license: http://eova.cn/eppl.txt
 * For authorization, please contact: admin@eova.cn
 */
package com.pro.global;

import com.eova.aop.UploadIntercept;
import com.eova.common.utils.xx;
import com.eova.model.MetaFieldConfig;
import com.jfinal.kit.Ret;
import com.jfinal.upload.UploadFile;

/**
 * 自定义上传拦截
 * 阿里OSS Java SDK 文件上传: https://help.aliyun.com/document_detail/84781.html?spm=a2c4g.11186623.6.787.4b2b7815JrpGQO
 * 七牛云 Java SDK
 * 
 * @author Jieven
 *
 */
public class MyUploadIntercept implements UploadIntercept {

	@Override
	public Ret upload(String code, String en, MetaFieldConfig config, String newFileName, String uploadDir, UploadFile uploadFile) {

		String endpoint = xx.getConfig("aliyun.oss.endpoint");
		String accessKeyId = xx.getConfig("aliyun.oss.accesskeyid");
		String accessKeySecret = xx.getConfig("aliyun.oss.accesskeysecret");
		String bucket = xx.getConfig("aliyun.oss.bucket");

		// 默认使用EOVA文件策略:http://www.eova.cn/doc/1-2-7
		// 1.默认随机文件名
		// 2.上传云后会自动删除本地文件

		//		// 获取上传文件
		//		File file = uploadFile.getFile();
		//
		//		// 创建OSSClient实例
		//		OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
		//
		//		// Object Key "/_avatar/158450125071618506.png" 无效。
		//		// Object名称不能以“/”或者“\”开头。
		//		uploadDir = xx.delStart(uploadDir, File.separator);
		//
		//		// 创建PutObjectRequest对象
		//		PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, uploadDir + "/" + newFileName, file);
		//
		//		// 上传文件
		//		ossClient.putObject(putObjectRequest);
		//		// 关闭OSSClient
		//		ossClient.shutdown();

		// 普通上传返回文件名, 富文本需要返回目录
		return Ret.ok("fileName", newFileName).set("uploadDir", uploadDir);
	}

	// 阿里云Maven依赖
	//	<dependency>
	//		<groupId>com.aliyun.oss</groupId>
	//		<artifactId>aliyun-sdk-oss</artifactId>
	//		<version>3.8.0</version>
	//	</dependency>

}