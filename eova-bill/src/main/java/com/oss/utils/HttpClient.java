package com.oss.utils;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.log4j.Logger;

import com.oss.Manager.DebugManager;
import com.oss.Manager.DevManager;
import com.oss.Manager.ServerManager;
import com.oss.Manager.TestManager;
import com.oss.beans.Devs;
import com.oss.beans.Servers;
import com.oss.beans.Tokens;
import com.oss.enums.ServerGroups;
/**
 * the class HttpClient.
 * @author Administrator
 *
 */
public class HttpClient {
	private static Logger log = Logger.getLogger(HttpClient.class);
	public static String msg;
	/**
	 * 
	 * @param serverUrl
	 * @param requestContent
	 * @return
	 */
	public static String http2inner(String serverUrl,Object requestContent,String method){
		HttpURLConnection httpcon =null;
		OutputStream ops = null;
		String result = null;
		try {
			URL url = new URL(serverUrl);
			httpcon = (HttpURLConnection) url.openConnection();
			httpcon.setRequestMethod(method);
			httpcon.setDoOutput(true);
			httpcon.setDoInput(true);
			ops = httpcon.getOutputStream();
			//ops.write(requestContent.toString().getBytes("UTF-8"));
			ops.close();
			httpcon.connect();
			result = processServerResponse(httpcon);
			log.info("result:" + result);
			if (result != null) {
				return result;
			} else {
				log.error("服务器返回的响应码为：" + httpcon.getResponseCode());
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(httpcon!=null){
				httpcon.disconnect();
			}
		}
		return result;
	} 
	
	/**
	 * 
	 * @param http
	 * @return
	 * @throws IOException
	 */
	public static String processServerResponse(HttpURLConnection http)
			throws IOException {
		String str = null;
		if (http.getResponseCode() == 200) {
			InputStream iStrm = http.getInputStream();
			// read data
			ByteArrayOutputStream out = new ByteArrayOutputStream(4096);
			byte[] buf = new byte[1024];
			for (int len = iStrm.read(buf); len != -1; len = iStrm.read(buf)) {
				out.write(buf, 0, len);
			}
			// close input stream
			iStrm.close();
			str = new String(out.toByteArray(), "UTF-8");
			return str;
		}
		return null;
	}
	/**
	 * 登录获取Borrower信息
	 * @param name
	 * @param pwd
	 * @return
	 */
	public static Tokens login(String name,String pwd) {
		try {
			/*1. 获取一个Server 服务器参数对象*/
			Servers servers=new ServerManager().getServer(ServerGroups.ws0a);
			/*2. 生成一个开发者账号对象	,选择是否输出接口调用时间,选择是否输出所有的接口请求与返回样例
			 * 默认输出调试时间，默认开启调用结果输出，默认不开启随机设备id*/
			Devs devs=new DevManager().getDev("shlib.ydzd.web.v1","07CA20E0ED626CA7EDBE2153395E572354C3C176","TestDevice",true,true,false);
			/*3. 根据开发者账号对象生成一个令牌对象*/
			TestManager testManager = new TestManager();
			testManager.setCardno(name);
			testManager.setPasswd(pwd);
			Tokens tokens = testManager.getTokens(devs,servers);
			return tokens;
		} catch (Exception e) {
			log.info("登录失败",e);
		}
		return null;
	}
	
	public static void main(String[] args) {
//		/*1. 获取一个Server 服务器参数对象*/
//		Servers servers=new ServerManager().getServer(ServerGroups.ws11);
//		/*2. 生成一个开发者账号对象	,选择是否输出接口调用时间,选择是否输出所有的接口请求与返回样例
//		* 默认输出调试时间，默认开启调用结果输出，默认不开启随机设备id*/
//		Devs devs=new DevManager().getDev("shlib.ydzd.web.v1","07CA20E0ED626CA7EDBE2153395E572354C3C176","TestDevice",true,true,false);
//		/*3. 根据开发者账号对象生成一个令牌对象*/
//		TestManager testManager = new TestManager();
//		testManager.setCardno("00947826");
//		testManager.setPasswd("1234561");
//		testManager.setIbarcode("54122101262999");
//		testManager.setSid("04335537");
//		testManager.setQrcode("96b292e43785b8878635c5f53f953ca608795e50d9ebddc0e95055df30b7548406e39bb54b7ab23f7a4b57961dc0a076");
//		Tokens tokens = testManager.getTokens(devs,servers);
//		/*4. 选择需要调用的测试接口并生成一组接口参数对象,包括对象的测试参数*/
////		ParmManager.addAPI("RFID用已借图书查询", APIConst.API_BOOKINFO_RFID,"",tokens,testManager);
////		ParmManager.addAPI("RFID用图书续借",APIConst.API_BOOKRENEW2_RFID,"",tokens,testManager);
////		ParmManager.addAPI("RFID用已借可借册数查询",APIConst.API_BOOKINFO_CNT_RFID,"",tokens,testManager);
////		ParmManager.addAPI("RFID图书馆藏查询",APIConst.API_BookCollection_RFID,"",tokens,testManager);
////		ParmManager.addAPI("门禁相关：查询卡状态",APIConst.API_IC_GETCARDSTATUS,"",tokens,testManager);
////		ParmManager.addAPI("门禁相关：查询用户信息",APIConst.API_IC_GETREADERINFO,"",tokens,testManager);
////		ParmManager.addAPI("门禁相关：查询卡用户姓名+卡状态",APIConst.API_IC_GETREADERNAME_STATUS,"",tokens,testManager);
////		ParmManager.addAPI("二维码相关：解码二维码",APIConst.API_QR_DECODE,"",tokens,testManager);
//		testManager.doAPIS(servers);
//		/*5. 执行调用并根据配置输出*/
//		DebugManager.getAll(devs);
		Tokens tokens = login("jyang", "jyang1");
		System.out.println(tokens.getShlibBorrower());

	}
}
