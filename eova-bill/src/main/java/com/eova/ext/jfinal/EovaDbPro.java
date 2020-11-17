/**
 * Copyright (c) 2015-2020 EOVA.CN. All rights reserved.
 * 
 * Licensed to: 上海蜜炬信息科技有限公司(1613965675@qq.com)
 * Licensed under the EPPL license: http://eova.cn/eppl.txt
 * Software copyright registration number:2020SR0109251
 * For authorization, please contact: admin@eova.cn
 */
package com.eova.ext.jfinal;

import com.eova.common.utils.xx;
import com.eova.common.utils.db.SqlUtil;
import com.jfinal.plugin.activerecord.DbPro;
import com.jfinal.plugin.activerecord.Record;

/**
 * 拓展默认Db操作<br>
 * 1.常用聚合API自动类型转换,使用更顺滑<br>
 * 2.Eova Oracle序列的自动指定
 * 
 * @author Jieven
 *
 */
public class EovaDbPro extends DbPro {

	public EovaDbPro(String configName) {
		super(configName);
	}

	@Override
	public boolean save(String tableName, String primaryKey, Record record) {
		// Oracle && 单主键 && 主键没值 -> 指定Sequence
		if (xx.isSequence() && !primaryKey.contains(",") && record.get(primaryKey) == null) {
			record.set(primaryKey, SqlUtil.getSequence(tableName));
		}
		return super.save(tableName, primaryKey, record);
	}

	@Override
	public Record findByIds(String tableName, String primaryKey, Object... idValues) {
		if (xx.isPgsql()) {
			// PostgreSql:operator does not exist: integer = character varying
			// PGSQL为严格强类型, 数值类型的主键传字符串值就会异常, 为了使用顺滑再次做自动类型强转
			// 特别约束: 主键类型为INT 则必须值为数字(Seq), 类型为VARCHAR 则必须为字符串(UUID)
			// 反之 例如数字10001 就不能使用VARCHAR
			for (int i = 0; i < idValues.length; i++) {
				try {
					idValues[i] = Integer.parseInt(idValues[i].toString());
				} catch (Exception e) {
				}
			}
		}
		return super.findByIds(tableName, primaryKey, idValues);
	}

}