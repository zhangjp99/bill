/**
 * Copyright (c) 2015-2020 EOVA.CN. All rights reserved.
 * 
 * Licensed to: 上海蜜炬信息科技有限公司(1613965675@qq.com)
 * Licensed under the EPPL license: http://eova.cn/eppl.txt
 * Software copyright registration number:2020SR0109251
 * For authorization, please contact: admin@eova.cn
 */
package com.eova.ext.jfinal;

import com.jfinal.plugin.activerecord.dialect.OracleDialect;

/**
 * 拓展Oracle方言:个性化识别Number类型和Boolean类型
 * 
 * @author Jieven
 *
 */
public class EovaOracleDialect extends OracleDialect {

	public EovaOracleDialect() {
		this.modelBuilder = OracleModelBuilder.me;
		this.recordBuilder = OracleRecordBuilder.me;
	}

}