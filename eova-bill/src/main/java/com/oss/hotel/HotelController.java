/**
 * Copyright (c) 2019 EOVA.CN. All rights reserved.
 * 
 * Licensed under the EPPL license: http://eova.cn/eppl.txt
 * For authorization, please contact: admin@eova.cn
 */
package com.oss.hotel;

import java.util.List;

import com.eova.common.Easy;
import com.eova.common.base.BaseController;
import com.eova.common.utils.xx;
import com.eova.common.utils.jfinal.RecordUtil;
import com.eova.common.utils.time.TimestampUtil;
import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.jfinal.plugin.activerecord.tx.TxConfig;

/**
 * 酒店管理
 * 
 * @author Jieven
 * 
 */
public class HotelController extends BaseController {

	public void add() throws Exception {
		render("/hotel/add.html");
	}

	// 添加酒店和库存
	@Before(Tx.class)
	@TxConfig(xx.DS_MAIN)
	public void doAdd() throws Exception {

		// 获取酒店信息(Form)
		String name = get("name");
		String tel = get("tel");
		String state = get("state");
		String score = get("score");
		String address = get("address");
		String province = get("province");
		String city = get("city");
		String region = get("region");
		// 获取库存信息(Grid)
		String eova_data = get("eova_data");

		// 保存酒店信息
		Record hotel = new Record();
		hotel.set("name", name);
		hotel.set("tel", tel);
		hotel.set("state", state);
		hotel.set("score", score);
		hotel.set("create_time", TimestampUtil.getNow());
		hotel.set("province", province);
		hotel.set("city", city);
		hotel.set("region", region);
		hotel.set("address", address);
		Db.save("hotel", hotel);

		// 如果觉得如上写法太冗余,可以使用Eova内提供的简单方法(PS:仅限获取Model)
		// Hotel hotel = getModel(XXX.class, "name", "tel", "state", ...);

		int hotel_id = hotel.getInt("id");

		// 保存库存信息
		List<Record> es = RecordUtil.parseArray(eova_data);
		for (Record e : es) {
			Record t = new Record();
			// 需要手工获取值域字段
			t.set("hotel_id", hotel_id);
			t.set("category", e.get("category_val"));
			t.set("num", e.get("num"));
			Db.save("hotel_stock", t);
		}

		renderJson(Easy.sucess());
	}

}