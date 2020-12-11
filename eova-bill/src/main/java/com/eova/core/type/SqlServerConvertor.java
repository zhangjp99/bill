package com.eova.core.type;


import java.util.HashMap;
import java.util.Map;

import com.eova.model.MetaField;

public class SqlServerConvertor extends Convertor {
	
	
	@SuppressWarnings("serial")
	private final static Map<String, Class> map = new HashMap<String, Class>() {
		{
			put("INT IDENTITY", Integer.class);
		}
	};

	@Override
	public Map<String, Class> mapping() {
		return map;
	}

	@Override
	public Object convert(MetaField field, Object o) {
		if (o == null) {
			return null;
		}

		return rule(o, getJavaType(field));
	}

	@Override
	public Object convertValue(Object o, int type) {
		// TODO Auto-generated method stub
		return null;
	}

}
