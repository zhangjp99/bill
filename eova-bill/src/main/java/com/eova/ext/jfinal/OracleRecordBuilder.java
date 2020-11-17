/**
 * Copyright (c) 2015-2020 EOVA.CN. All rights reserved.
 * 
 * Licensed to: 上海蜜炬信息科技有限公司(1613965675@qq.com)
 * Licensed under the EPPL license: http://eova.cn/eppl.txt
 * Software copyright registration number:2020SR0109251
 * For authorization, please contact: admin@eova.cn
 */
package com.eova.ext.jfinal;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.eova.config.EovaConfig;
import com.jfinal.plugin.activerecord.CPI;
import com.jfinal.plugin.activerecord.Config;
import com.jfinal.plugin.activerecord.ModelBuilder;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.RecordBuilder;

/**
 * Oracle专用Record构建器
 */
public class OracleRecordBuilder extends RecordBuilder {
	
	public static final OracleRecordBuilder me = new OracleRecordBuilder();
	
	public List<Record> build(Config config, ResultSet rs) throws SQLException {
		List<Record> result = new ArrayList<Record>();
		ResultSetMetaData rsmd = rs.getMetaData();
		int columnCount = rsmd.getColumnCount();
		String[] labelNames = new String[columnCount + 1];
		int[] types = new int[columnCount + 1];
		buildLabelNamesAndTypes(rsmd, labelNames, types);
		while (rs.next()) {
			Record record = new Record();
			CPI.setColumnsMap(record, config.getContainerFactory().getColumnsMap());
			Map<String, Object> columns = record.getColumns();
			for (int i=1; i<=columnCount; i++) {
				Object value;

				if (types[i] == Types.NUMERIC) {
					int p = rsmd.getPrecision(i);
					int s = rsmd.getScale(i);
					if (s == 0) {
						if (p <= 10) {
							value = rs.getInt(i);
						} else if (p <= 18) {
							value = rs.getLong(i);
						} else {
							value = rs.getBigDecimal(i);
						}
					} else {
						if (p + s <= 8) {
							value = rs.getFloat(i);
						} else if (p + s <= 16) {
							value = rs.getDouble(i);
						} else {
							value = rs.getBigDecimal(i);
						}
					}
				} else {
					if (types[i] == Types.TIMESTAMP) {
						value = rs.getTimestamp(i);
					} else if (types[i] == Types.DATE) {
						value = rs.getDate(i);
					} else if (types[i] == Types.CLOB) {
						value = ModelBuilder.me.handleClob(rs.getClob(i));
					} else if (types[i] == Types.NCLOB) {
						value = ModelBuilder.me.handleClob(rs.getNClob(i));
					} else if (types[i] == Types.BLOB) {
						value = ModelBuilder.me.handleBlob(rs.getBlob(i));
					} else {
						// Eova Oracle 特殊处理
						value = EovaConfig.convertor.convertValue(rs.getObject(i), types[i]);
					}
				}
				
				columns.put(labelNames[i], value);
			}
			result.add(record);
		}
		return result;
	}
}