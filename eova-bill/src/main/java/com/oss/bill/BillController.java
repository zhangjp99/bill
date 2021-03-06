package com.oss.bill;

import java.io.BufferedReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.eova.core.IndexController;
import com.jfinal.aop.Before;
import com.jfinal.ext.interceptor.POST;
import com.jfinal.plugin.activerecord.Db;
import com.oss.beans.Tokens;
import com.oss.model.BorrowerRequest;
import com.oss.model.ClcInfo;
import com.oss.model.DigitalReading;
import com.oss.model.Location;
import com.oss.model.PaperReading;
import com.oss.model.UserLogin;
import com.oss.utils.DateUtils;
import com.oss.utils.HttpClient;
import com.oss.utils.JsonUtils;

public class BillController extends IndexController  {
	
	private static final Logger logger = LoggerFactory.getLogger(BillController.class);
	
	public static final String CHILD = "少儿";
	public static final String ADULT = "成人";
	
	public void index() {
		System.out.println("简单才是高科技，因为简单所以更快，降低70%开发成本");
		render("/paperbook/index.html");
	}
	
	/**
	 * 获取读者账单信息
	 */
	@Before(POST.class)
	public void getBorrowerInfo(){
		//返回对象 
		Map<String, Object> result = new HashMap<String, Object>();
	    try{
	    	//从requst中读取json字符串
	        StringBuilder json = new StringBuilder(); 
	        BufferedReader reader = this.getRequest().getReader();
	        String line = null;
	        while((line = reader.readLine()) != null){
	            json.append(line);
	        }
	        reader.close();
	        //调用fastjson解析出对象
	        System.out.println(json.toString());
	        BorrowerRequest request = JSONObject.parseObject(json.toString(), BorrowerRequest.class);
	        //用户信息
	        BorrowerRequest borrowerInfo =  getUserInfo(request.getBorrowerId());
	        //纸质阅读信息
	        PaperReading paReading = new PaperReading();
	      //电子阅读信息
	        DigitalReading dReading = new DigitalReading();
	        if(borrowerInfo!=null) {
	        	Map<String, ClcInfo> tMap =  getCategory();
		        Map<String, String> bookMap = getBooknames(request.getBorrowerId());
		        Map<String, Location> locMap = getLocation();
//	        	Map<String, String> bookMap = new HashMap<String, String>();
	        	//如果类型是少儿，获取少儿阅读信息表，否则检查成人阅读表
	        	paReading =  getPaperInfo(request.getBorrowerId(), tMap,bookMap,locMap);
	        	dReading = getDigitalInfo(request.getBorrowerId());
	        	//返回
	        	result.put("code", "0000");
	        	result.put("desc", "成功");
	        	result.put("BorrowerInfo", borrowerInfo);
//	        	result.put("PaperReading", paReading);
//	        	result.put("DigitalReading", dReading);
	        	if(paReading!=null) {
	        		result.put("PaperReading", paReading);
	        	}
	        	if(dReading!=null) {
	        		result.put("DigitalReading", dReading);
	        	}
	        	renderJson(result);
	        	return;
	        }
	        result.put("code", "1001");
        	result.put("desc", "用户信息未找到："+request.getBorrowerId());
        	renderJson(result);
        	return;
	    }
	    catch(Exception ex){
	    	ex.printStackTrace();
	    	//返回
	        result.put("code", "1002");
	        result.put("desc", "服务异常:"+ex.getMessage());
			renderJson(result);
	    }
	}
	
	/**
	 * 登录
	 */
	@Before(POST.class)
	public void login(){
		//返回对象 
		Map<String, Object> result = new HashMap<String, Object>();
	    try{
	    	//从requst中读取json字符串
	        StringBuilder json = new StringBuilder(); 
	        BufferedReader reader = this.getRequest().getReader();
	        String line = null;
	        while((line = reader.readLine()) != null){
	            json.append(line);
	        }
	        reader.close();
	        //调用fastjson解析出对象
	        System.out.println(json.toString());
	        UserLogin request = JSONObject.parseObject(json.toString(), UserLogin.class);
	        try {
	        	HttpClient http = new HttpClient();
	        	Tokens tokens = http.login(request.getName(), request.getPwd());
	        	System.out.println("登录接口,时间"+DateUtils.getCurrentFromtDate());
	        	System.out.println("登录接口，tokens={},Borrower={}"+tokens.getUat()+"---"+tokens.getShlibBorrower());
	        	if(tokens != null && !StringUtils.isEmpty(tokens.getShlibBorrower())) {
	        		result.put("code", "0000");
	        		result.put("desc", "登录成功");
	        		result.put("borrowerId", tokens.getShlibBorrower());
	        		renderJson(result);
	        		return;
	        	}
			} catch (Exception e) {
				e.printStackTrace();
			}
	        result.put("code", "1001");
	        result.put("desc", "登录失败，用户名或密码不正确");
	    	renderJson(result);
	        return;
	    }
	    catch(Exception ex){
	    	ex.printStackTrace();
	    	//返回
	        result.put("code", "1002");
	        result.put("desc", "服务异常:"+ex.getMessage());
			renderJson(result);
			return;
	    }
	}
	
	/**
	 * 获取分类信息
	 * @return
	 */
	public Map<String, ClcInfo> getCategory(){
		Map<String, ClcInfo> map = new HashMap<String, ClcInfo>();
		List<?> list = Db.use("bill").query("select clc,clcname,book1,book2,book3 from reading_account_2019_clc");
		if(list!=null &&list.size()>0) {
			for (int i = 0; i < list.size(); i++) {
				Object[] obj =  (Object[]) list.get(i);
				ClcInfo clc = new ClcInfo();
				if(obj[0]!=null ) {
					clc.setClc(obj[0].toString());
					clc.setClcname(obj[1]!=null ? obj[1].toString() : "");
					clc.setBook1(obj[2]!=null ? obj[2].toString() : "");
					clc.setBook2(obj[3]!=null ? obj[3].toString() : "");
					clc.setBook3(obj[4]!=null ? obj[4].toString() : "");
					map.put(obj[0].toString(),clc);
				}
			}
		}
		return map;
	}
	/**
	 * 获取借阅过的3本书
	 * @param borrowerId
	 * @return
	 */
	public Map<String, String> getBooknames(String borrowerId){
		Map<String, String> map = new HashMap<String, String>();
		List<?> list = Db.use("bill").query("select bookname from reading_account_2019_borrower_booknames where ShLibBorrower=? order by ckodate desc",borrowerId);
		if(list!=null &&list.size()>0) {
			for (int i = 0; i < list.size(); i++) {
				if( i == 0) {
					map.put("book1", list.get(i).toString());
				}else if( i == 1) {
					map.put("book2", list.get(i).toString());
				}else if( i == 2) {
					map.put("book3", list.get(i).toString());
				}
			}
		}
		return map;
	}
	
	/**
	 * 获取图书馆信息
	 * @param borrowerId
	 * @return
	 */
	public Map<String, Location> getLocation(){
		Map<String, Location> map = new HashMap<String, Location>();
		List<?> list = Db.use("bill").query("select location,lname,qx from reading_account_2019_loc");
		if(list!=null &&list.size()>0) {
			for (int i = 0; i < list.size(); i++) {
				Location loc = new Location();
				Object[] obj =  (Object[]) list.get(i);
				loc.setLocation(obj[0]!=null ? obj[0].toString():"");
				loc.setLname(obj[1]!=null ? obj[1].toString():"");
				loc.setQx(obj[2]!=null ? obj[2].toString():"");
				if(obj[0]!=null) {
					map.put(obj[0].toString(), loc);
				}
			}
		}
		return map;
	}
	
	/**
	 * 获取用户信息
	 * @return
	 */
	public BorrowerRequest getUserInfo(String borrowerId){
		 List<?> list = Db.use("bill").query("select * from reading_account_2019_borrower where ShLibBorrower=?",borrowerId);
		if(list!=null &&list.size()>0) {
			BorrowerRequest user = new BorrowerRequest();
			Object[] obj =  (Object[]) list.get(0);
			user.setBorrowerId(obj[0]!=null ? obj[0].toString():"");
			user.setSex(obj[1]!=null ? obj[1].toString():"");
			user.setBirth(obj[2]!=null ? obj[2].toString():"");
			user.setRtype(obj[3]!=null ? obj[3].toString():"");
			user.setHoroscope(obj[4]!=null ? obj[4].toString():"");
			return user;
		}
		return null;
	}
	/**
	 * 获取数字阅读信息
	 * @return
	 */
	public DigitalReading getDigitalInfo(String borrowerId){
		 List<?> list = Db.use("bill").query("select * from z_ereading_2019 where ShLibBorrower=?",borrowerId);
		if(list!=null &&list.size()>0) {
			DigitalReading digitalReading = new DigitalReading();
			Object[] obj =  (Object[]) list.get(0);
			digitalReading.setBorrowerId(obj[0]!=null ? obj[0].toString():"");
			digitalReading.setBookCount(obj[1]!=null ? Integer.parseInt(obj[1].toString()):0);
			digitalReading.setMagCount(obj[2]!=null ? Integer.parseInt(obj[2].toString()):0);
			digitalReading.setNewspaperCount(obj[3]!=null ? Integer.parseInt(obj[3].toString()):0);
			digitalReading.setOverPercent(obj[4]!=null ? Float.parseFloat(obj[4].toString()):0);
			digitalReading.setMostLoveReadBook(obj[5]!=null ? obj[5].toString():"");
			digitalReading.setReaderTime(obj[6]!=null ? obj[6].toString():"");
			digitalReading.setSaveCarbon(obj[7]!=null ? obj[7].toString():"");
			digitalReading.seteTag(obj[8]!=null ? obj[8].toString():"");
			return digitalReading;
		}
		return null;
	}
	/**
	 * 获取纸质阅读信息--成人
	 * @return
	 */
	public PaperReading getPaperInfo(String borrowerId,Map<String, ClcInfo> map,Map<String, String> bookmap, Map<String, Location>locMap){
		 List<?> list = Db.use("bill").query("select ShLibBorrower,cnt,location_cnt,month1,month2,month3,month4,month5,"
		 		+ "month6,month7,month8,month9,month10,month11,month12,per,tag,book_cnt,mag_cnt,readchar,readcar,loc,"
		 		+ "recom_loc,clc1,clc2,clc3 from reading_account_2019_adult  where ShLibBorrower=?",borrowerId);
		if(list!=null &&list.size()>0) {
			PaperReading paper = new PaperReading();
			Object[] obj =  (Object[]) list.get(0);
			paper.setBorrowerId(obj[0]!=null ? obj[0].toString():"");
			paper.setCnt(obj[1]!=null ? Integer.parseInt(obj[1].toString()):0);
			paper.setLocationCnt(obj[2]!=null ? Integer.parseInt(obj[2].toString()):0);
			paper.setMonth1(obj[3]!=null ? Integer.parseInt(obj[3].toString()):0);
			paper.setMonth2(obj[4]!=null ? Integer.parseInt(obj[4].toString()):0);
			paper.setMonth3(obj[5]!=null ? Integer.parseInt(obj[5].toString()):0);
			paper.setMonth4(obj[6]!=null ? Integer.parseInt(obj[6].toString()):0);
			paper.setMonth5(obj[7]!=null ? Integer.parseInt(obj[7].toString()):0);
			paper.setMonth6(obj[8]!=null ? Integer.parseInt(obj[8].toString()):0);
			paper.setMonth7(obj[9]!=null ? Integer.parseInt(obj[9].toString()):0);
			paper.setMonth8(obj[10]!=null ? Integer.parseInt(obj[10].toString()):0);
			paper.setMonth9(obj[11]!=null ? Integer.parseInt(obj[11].toString()):0);
			paper.setMonth10(obj[12]!=null ? Integer.parseInt(obj[12].toString()):0);
			paper.setMonth11(obj[13]!=null ? Integer.parseInt(obj[13].toString()):0);
			paper.setMonth12(obj[14]!=null ? Integer.parseInt(obj[14].toString()):0);
			paper.setPer(obj[15]!=null ? obj[15].toString():"");
			paper.setTag(obj[16]!=null ? obj[16].toString():"");
			paper.setBookCnt(obj[17]!=null ? Integer.parseInt(obj[17].toString()):0);
			paper.setMagCnt(obj[18]!=null ? Integer.parseInt(obj[18].toString()):0);
			paper.setReadchar(obj[19]!=null ? Integer.parseInt(obj[19].toString()):0);
			paper.setReadcar(obj[20]!=null ? Integer.parseInt(obj[20].toString()):0);
			JsonUtils.getLibraryMap();
			paper.setLoc(conversion(obj[21],locMap));
			paper.setEname(obj[21]!=null ? obj[21].toString().replaceAll(" ", ""):"");
			paper.setLocname(conversion2(obj[21], locMap));
			paper.setRecomLoc(conversion2(obj[22], locMap));
			paper.setClc1(obj[23]!=null ? map.get(obj[23].toString()).getClcname():"");
			paper.setClc2(obj[24]!=null ? map.get(obj[24].toString()).getClcname():"");
			paper.setClc3(obj[25]!=null ? map.get(obj[25].toString()).getClcname():"");
			paper.setClcBook1(bookmap.get("book1")!=null ? bookmap.get("book1"):"");
			paper.setClcBook2(bookmap.get("book2")!=null ? bookmap.get("book2"):"");
			paper.setClcBook3(bookmap.get("book3")!=null ? bookmap.get("book3"):"");
			return paper;
		}
		return null;
	}
	
	/**
	 * book转换
	 * @param clcInfo
	 * @return
	 */
	public String clctToString(ClcInfo clcInfo) {
		String book=clcInfo.getBook1();
		if(!StringUtils.isEmpty(clcInfo.getBook2())) {
			book += clcInfo.getBook2()+";";
		}
		if(!StringUtils.isEmpty(clcInfo.getBook3())) {
			book += clcInfo.getBook3()+";";
		}
		return book;
	}
	
	/**
	 * 获取纸质阅读信息--少儿
	 * @return
	 */
	/*
	 * public PaperReading getPaperChildInfo(String borrowerId,Map<String, String>
	 * map){ List<?> list = Db.use("bill").
	 * query("select ShLibBorrower,cnt,location_cnt,month1,month2,month3,month4,month5,"
	 * +
	 * "month6,month7,month8,month9,month10,month11,month12,per,tag,book_cnt,mag_cnt,readchar,loc,"
	 * +
	 * "recom_loc,clc1,clc2,clc3 from reading_account_2019_child  where ShLibBorrower=?"
	 * ,borrowerId); if(list!=null &&list.size()>0) { PaperReading paper = new
	 * PaperReading(); Object[] obj = (Object[]) list.get(0);
	 * paper.setBorrowerId(obj[0]!=null ? obj[0].toString():"");
	 * paper.setCnt(obj[1]!=null ? Integer.parseInt(obj[1].toString()):0);
	 * paper.setLocationCnt(obj[2]!=null ? Integer.parseInt(obj[2].toString()):0);
	 * paper.setMonth1(obj[3]!=null ? Integer.parseInt(obj[3].toString()):0);
	 * paper.setMonth2(obj[4]!=null ? Integer.parseInt(obj[4].toString()):0);
	 * paper.setMonth3(obj[5]!=null ? Integer.parseInt(obj[5].toString()):0);
	 * paper.setMonth4(obj[6]!=null ? Integer.parseInt(obj[6].toString()):0);
	 * paper.setMonth5(obj[7]!=null ? Integer.parseInt(obj[7].toString()):0);
	 * paper.setMonth6(obj[8]!=null ? Integer.parseInt(obj[8].toString()):0);
	 * paper.setMonth7(obj[9]!=null ? Integer.parseInt(obj[9].toString()):0);
	 * paper.setMonth8(obj[10]!=null ? Integer.parseInt(obj[10].toString()):0);
	 * paper.setMonth9(obj[11]!=null ? Integer.parseInt(obj[11].toString()):0);
	 * paper.setMonth10(obj[12]!=null ? Integer.parseInt(obj[12].toString()):0);
	 * paper.setMonth11(obj[13]!=null ? Integer.parseInt(obj[13].toString()):0);
	 * paper.setMonth12(obj[14]!=null ? Integer.parseInt(obj[14].toString()):0);
	 * paper.setPer(obj[15]!=null ? obj[15].toString():"");
	 * paper.setTag(obj[16]!=null ? obj[16].toString():"");
	 * paper.setBookCnt(obj[17]!=null ? Integer.parseInt(obj[17].toString()):0);
	 * paper.setMagCnt(obj[18]!=null ? Integer.parseInt(obj[18].toString()):0);
	 * paper.setReadchar(obj[19]!=null ? Integer.parseInt(obj[19].toString()):0);
	 * //少儿表缺少 //paper.setReadcar(obj[20]!=null ?
	 * Integer.parseInt(obj[20].toString()):0); paper.setLoc(conversion(obj[20]));
	 * paper.setRecomLoc(conversion(obj[21])); paper.setClc1(obj[22]!=null ?
	 * map.get(obj[22].toString()):""); paper.setClc2(obj[23]!=null ?
	 * map.get(obj[23].toString()):""); paper.setClc3(obj[24]!=null ?
	 * map.get(obj[24].toString()):""); return paper; } return null; }
	 */
	/**
	 * 转换到馆QX
	 * @param obj
	 * @return
	 */
	public String conversion(Object loc,Map<String, Location>locMap) {
		String qx ="";
//		Set<String> outputs = new HashSet<String>();
		if(loc!=null && !"".equals(loc.toString())) {
			String[] locArr = loc.toString().split(";");
			for (int i = 0; i < locArr.length; i++) {
				if(!StringUtils.isEmpty(locArr[i])) {
//					outputs.add(locMap.get(locArr[i].trim()).getQx());
					qx+= locMap.get(locArr[i].trim()).getQx() +";";
				}
			}
		}
//		if(outputs.size()>0) {
//			for (String str : outputs) {
//				qx+=str+";";
//			}
//		}
		return qx;
	}
	
	/**
	 * 转换图书馆名称
	 * @param obj
	 * @return
	 */
	public String conversion2(Object obj,Map<String, Location>locMap) {
		String loc ="";
		Set<String> outputs = new HashSet<String>();
		if(obj!=null && !"".equals(obj.toString())) {
			String[] locArr = obj.toString().split(";");
			for (int i = 0; i < locArr.length; i++) {
				if(!StringUtils.isEmpty(locArr[i])) {
					if(locMap.get(locArr[i].trim())!=null) {
						outputs.add(locMap.get(locArr[i].trim()).getLname());
					}
				}
			}
		}
		if(outputs.size()>0) {
			for (String str : outputs) {
				loc+=str+";";
			}
		}
		return loc;
	}
	
}
