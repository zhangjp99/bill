/**
 * Copyright (c) 2015-2020 EOVA.CN. All rights reserved.
 * 
 * Licensed to: 上海蜜炬信息科技有限公司(1613965675@qq.com)
 * Licensed under the EPPL license: http://eova.cn/eppl.txt
 * Software copyright registration number:2020SR0109251
 * For authorization, please contact: admin@eova.cn
 */
package com.eova.common.utils.db;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.expr.SQLBinaryOperator;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.sql.ast.expr.SQLSequenceExpr;
import com.alibaba.druid.sql.ast.statement.SQLInsertStatement;
import com.alibaba.druid.sql.ast.statement.SQLInsertStatement.ValuesClause;
import com.alibaba.druid.sql.dialect.oracle.visitor.OracleSchemaStatVisitor;
import com.alibaba.druid.sql.parser.SQLParserUtils;
import com.alibaba.druid.sql.parser.SQLStatementParser;
import com.alibaba.druid.stat.TableStat;
import com.alibaba.druid.stat.TableStat.Name;
import com.eova.common.utils.xx;
import com.eova.config.EovaConfig;
import com.eova.config.EovaConst;

public class SqlUtil {

	/**
	 * 向SQL中插入条件
	 * @param sql
	 * @param condition
	 * @param dbType
	 * @return
	 */
	public static String addCondition(String sql, String condition) {
		if (xx.isEmpty(condition)) {
			return sql;
		}
		condition = condition.trim().toLowerCase();

		SQLBinaryOperator op = SQLBinaryOperator.BooleanAnd;

		if (condition.startsWith("and")) {
			op = SQLBinaryOperator.BooleanAnd;
			condition = condition.replace("and ", "");
		} else if (condition.startsWith("or")) {
			op = SQLBinaryOperator.BooleanOr;
			condition = condition.replace("or ", "");
		} else if (condition.startsWith("xor")) {
			op = SQLBinaryOperator.BooleanXor;
			condition = condition.replace("xor ", "");
		}

		return notNewLine(SQLUtils.addCondition(sql, condition, op, false, EovaConfig.EOVA_DBTYPE));
	}

	public static String addConditions(String sql, String condition) {
		if (xx.isEmpty(condition)) {
			return sql;
		}
		// condition = condition.trim().toLowerCase();

		return notNewLine(SQLUtils.addCondition(sql, condition, EovaConfig.EOVA_DBTYPE));
	}

	/**
	 * 追加Where条件
	 * @param where where子句
	 * @param condition where条件
	 * @return
	 */
	public static String appendWhereCondition(String where, String condition) {
		if (condition == null || condition.trim().equals(""))
			return where;

		// 格式化
		condition = condition.trim();
		where = where == null ? "" : where.trim();

		// where子句自动补头
		if (where.toLowerCase().indexOf("where") == -1) {
			where = " where 1=1 " + where;
		}
		// condition子句自动去头
		if (condition.toLowerCase().startsWith("where ")) {
			condition = condition.substring(6, condition.length());
		}

		// 自动补充and关键字
		if (!condition.toLowerCase().startsWith("and") && !condition.toLowerCase().startsWith("or")) {
			where += " and ";
		}

		return String.format(" %s %s", where, condition);
	}

	/**
	 * 智能构建where
	 * @param s
	 * @return
	 */
	public static String buildWhere(String s, String condition) {
		if (condition == null)
			condition = "";
		if (s == null)
			s = "";
		s = s.trim();
		condition = condition.trim();
		if (!s.toLowerCase().endsWith("where") && !condition.toLowerCase().startsWith("where")) {
			s += " where 1=1 ";
		}
		return s + " " + condition;
	}

	public static String buildWhere(String condition) {
		return buildWhere("", condition);
	}

	/**
	 * 格式化SQL去除各种拼接逻辑产生的无意义字符
	 * @param sql
	 */
	public static String formatSql(String sql) {
		// 多个空格替换
		sql = sql.trim();
		sql = sql.replaceAll("\\s+", " ");
		if (sql.endsWith(" where 1=1")) {
			sql = sql.replace(" where 1=1", "");
		}
		sql = sql.replace(" where 1=1 and ", " where ");
		sql = sql.replace(" where 1=1 order ", " order ");
		return sql;
	}

	/**
	 * SQL不换行格式化
	 * @param str
	 * @return
	 */
	public static String notNewLine(String str) {
		str = str.replaceAll("\t|\r|\n", " ");
		str = str.replaceAll("  ", " ");
		return str;
	}

	/**
	 * 获取SQL中的表名
	 * @param dbType DB类型
	 * @param sql SQL
	 * @return
	 */
	public static Set<Name> getTableNames(String dbType, String sql) {
		List<SQLStatement> stmtList = SQLUtils.parseStatements(sql, dbType);

		SQLStatement stmt = stmtList.get(0);
		OracleSchemaStatVisitor vt = new OracleSchemaStatVisitor();
		stmt.accept(vt);
		Map<Name, TableStat> tables = vt.getTables();
		return tables.keySet();
	}

	/**
	 * 获取EOVA规范的自增序列
	 * @param tableName 当前表名
	 * @return
	 */
	public static String getSequence(String tableName) {
		if (xx.isOracle()) {
			return String.format("%s%s.nextval", EovaConst.SEQ_, tableName);
		} else if (xx.isPgsql()) {
			return String.format("nextval('%s%s'::regclass)", EovaConst.SEQ_, tableName);
		}
		return null;
	}

	/**
	 * 获取插入语句表名
	 * @param dbType
	 * @param sql
	 * @return
	 */
	public static String getInsertTableName(String dbType, String sql) {
		SQLStatementParser parser = SQLParserUtils.createSQLStatementParser(sql, dbType);
		List<SQLStatement> list = parser.parseStatementList();
		SQLInsertStatement statement = (SQLInsertStatement) list.get(0);
		return statement.getTableName().toString();
	}

	/**
	 * 添加Oracle Seq 列
	 * @param dbType
	 * @param sql
	 * @param seqName
	 * @param columnName
	 * @return
	 */
	public static String addInsertSequenceColumn(String dbType, String sql, String seqName, String columnName) {
		SQLStatementParser parser = SQLParserUtils.createSQLStatementParser(sql, dbType);
		List<SQLStatement> list = parser.parseStatementList();
		SQLInsertStatement st = (SQLInsertStatement) list.get(0);
		List<ValuesClause> vcs = st.getValuesList();

		st.addColumn(new SQLIdentifierExpr(columnName));

		ValuesClause vc = vcs.get(0);
		vc.addValue(new SQLSequenceExpr(new SQLIdentifierExpr(seqName), SQLSequenceExpr.Function.NextVal));

		return formatSql(st.toString());
	}
}