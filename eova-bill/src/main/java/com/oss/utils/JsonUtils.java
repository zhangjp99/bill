package com.oss.utils;



import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.kit.PathKit;
import com.oss.model.Qx;
import com.oss.model.QxList;
import com.oss.model.Tss;


public class JsonUtils {
	
	public static Map<String, String> map = new HashMap<String, String>();
	public static Map<String, String> emap = new HashMap<String, String>();
	
	 //读取json文件
    public static String readJsonFile(String fileName) {
        String jsonStr = "";
        try {
            File jsonFile = new File(fileName);
            FileReader fileReader = new FileReader(jsonFile);
            Reader reader = new InputStreamReader(new FileInputStream(jsonFile),"utf-8");
            int ch = 0;
            StringBuffer sb = new StringBuffer();
            while ((ch = reader.read()) != -1) {
                sb.append((char) ch);
            }
            fileReader.close();;
            reader.close();
            jsonStr = sb.toString();
            return jsonStr;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * 读取文件转换map
     * @return
     */
    public static void getLibraryMap() {
    	 String path = PathKit.getRootClassPath() + File.separator+"gcodeList.json";
         String json = readJsonFile(path);
         QxList request = JSONObject.parseObject(json.toString(), QxList.class);
         List<Qx> qx = request.getQx();
         for (Qx qx2 : qx) {
        	 map.put(qx2.getCode(), qx2.getName());
        	 emap.put(qx2.getCode(), qx2.getEname());
        	 String ename = "";
        	 String name = "";
        	 String locEname = "";
        	 for (Tss tss : qx2.getTss()) {
        		  if (!tss.getLevel().equals("基层服务点")) {
                      map.put(tss.getCode(), tss.getName());
                      emap.put(tss.getCode(), tss.getEname());
                      if(ename.isEmpty()) {
                    	  ename =  tss.getEname();
                    	  name = tss.getName();
                      }
                  }else {
                	    map.put(tss.getCode(),name);
                        emap.put(tss.getCode(),ename);
                  }
                
			}
        	 
		}
	}
    /**
     * 根据code 获取出name参数
     * @param code
     * @return
     */
    public static String findName(String code) {
    	if(map!=null) {
    		if(map.get(code)!=null) {
    			return  map.get(code);
    		}
    	}
    	return null;
    }
    
    /**
     * 根据code 获取出name参数
     * @param code
     * @return
     */
    public static String findEName(String code) {
    	if(emap!=null) {
    		if(emap.get(code)!=null) {
    			return  emap.get(code);
    		}
    	}
    	return null;
    }

    public static void main(String[] args) {
    	getLibraryMap();
    	getLibraryMap();
    	System.out.println(findName("J018"));
    	System.out.println(findEName("J018"));
    	System.out.println(findName("J134"));
    	System.out.println(findEName("J134"));
    	System.out.println(map.size());
    	System.out.println(emap.size());
    	
	}
}
